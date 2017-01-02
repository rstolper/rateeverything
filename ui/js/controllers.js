(function() {

    var app = angular.module('rateeverything.controllers', ['rateeverything.services']);

    app.controller('dashboardCtrl', function ($scope, restDao, cloneItem, lunrService) {

        $scope.dao = restDao;

        $scope.newItem = {};
        $scope.allItems = {}; // static maintainer of all items the user has
        $scope.itemsByCategory = {};
        $scope.newItemSearchByCategory = {};
        // TODO: custom watch expression on allItems to refresh this list via refreshActiveSearch()
        $scope.searchResultItems = {}; // the active listing of items to display
        $scope.userId = "";
        $scope.searchString = "";

        $scope.isSearchActive = false;
        $scope.isItemAddOpen = false;
        //$scope.activeSearchString = "";
        $scope.addItemSearchString = "";
        $scope.loggedInUser = "";

        $scope.navBar = {};

        $scope.hasAtLeastOneItem = function () {
            return Object.keys($scope.allItems).length > 0;
        };

        $scope.hasAtLeastOneSearchResult = function () {
            return Object.keys($scope.searchResultItems).length > 0;
        };

        $scope.isLoggedIn = function () {
            return ($scope.loggedInUser && $scope.loggedInUser.trim().length > 0)
        };

        function onGoogleSignIn(googleUser) {
            var profile = googleUser.getBasicProfile();
            // after signed in
            console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
            console.log('Name: ' + profile.getName());
            console.log('Image URL: ' + profile.getImageUrl());
            console.log('Email: ' + profile.getEmail());

            var googleIdToken = googleUser.getAuthResponse().id_token;
            $scope.googleIdToken = googleIdToken;
            $scope.authProvider = 'Google';

            var userIdFromServer = "";

            $scope.$digest;

            // get user
            $scope.dao.getUserViaGoogle(googleIdToken, function (response) {
                console.log('user found: ' + JSON.stringify(response.data));
                userIdFromServer = response.data.userId.value;
                $scope.completeLogin(userIdFromServer);
            },
            function() {
                console.log('user not found with id token: ' + profile.getId());
                // create new user
                $scope.dao.createUserViaGoogle(googleIdToken, function(response) {
                    console.log("Created user: " + JSON.stringify(response.data));
                    userIdFromServer = response.data.userId.value;
                    $scope.completeLogin(userIdFromServer);
                });
            });
        }
        window.onGoogleSignIn = onGoogleSignIn;

        $scope.loginDebug = function () {
            if ($scope.userId && $scope.userId.trim().length > 0) {
                $scope.authProvider = 'Debug';
                $scope.completeLogin($scope.userId);
            }
        };

        $scope.completeLogin = function (userId) {
            $scope.loggedInUser = userId;

            console.log("Scope.loggedInUser: " + $scope.loggedInUser);

            // find all items by username
            $scope.dao.getAllItems($scope.googleIdToken, function (response) {
                var receivedItems = response.data;
                $scope.allItems = {};
                $scope.searchResultItems = {};
                lunrService.dropItemsIndex();
                for (var key in receivedItems) {
                    newItemHandler(receivedItems[key]);
                }
                refreshActiveSearch();
                $scope.$broadcast('focusSearch');
            });
        }

        $scope.logout = function () {
            $scope.loggedInUser = "";
            $scope.authProvider = "";
            lunrService.dropItemsIndex();
            $scope.allItems = {};
            $scope.searchResultItems = {};
            $scope.newItemSearchByCategory = {};
            $scope.itemsByCategory = {};
            $scope.newItem = {};
            $scope.searchString = "";
            $scope.isSearchActive = false;
            $scope.$broadcast('loggedOut');
        }

        $scope.googleSignOut = function () {
            var auth2 = gapi.auth2.getAuthInstance();
            auth2.signOut().then(function () {
                console.log('User signed out.');
            });

            $scope.logout();
        }

        $scope.addNewItem = function () {
            // exit if anything is empty
            if (!$scope.loggedInUser || !$scope.newItem.category || !$scope.newItem.name || !$scope.newItem.rating)
                return;

            $scope.dao.insertItem($scope.googleIdToken, $scope.newItem, function (response) {
                var item = response.data;
                newItemHandler(item);
                $scope.newItem.name = "";
                $scope.newItem.rating = "";
                //$scope.search('searchbar')
                refreshActiveSearch();
                // TODO: be more intelligent about knowing if addItem modal is open
                $scope.searchWhileAdding();
                $scope.$broadcast('addItemToCategoryRequest');
            });
        };

        $scope.addItemToCategoryRequest = function(category) {
            $scope.newItem.category = category;
            $scope.newItem.name = "";
            $scope.newItem.rating = "";
            $scope.searchWhileAdding();
            $scope.$broadcast('addItemToCategoryRequest');
        }

        $scope.deleteItem = function (item) {
            var itemId = item.itemId;
            $scope.dao.deleteItem($scope.googleIdToken, itemId, function (response) {
                delete $scope.allItems[itemId];
                lunrService.removeItemFromLunr({itemId: itemId});
                //$scope.newItem = item; // for ease of readding
                refreshActiveSearch();
                // TODO: be more intelligent about knowing if addItem modal is open
                $scope.searchWhileAdding();
            });
        };

        $scope.search = function () {
            if ($scope.searchString && $scope.searchString.trim().length > 0)
            {
                $scope.isSearchActive = true;
                //$scope.activeSearchString = searchString;
            }
            else
            {
                $scope.isSearchActive = false;
            }
            refreshActiveSearch()
        }

        $scope.searchWhileAdding = function () {
            $scope.addItemSearchString = ($scope.newItem.category || '') + " " + ($scope.newItem.name || '');
            if ($scope.addItemSearchString && $scope.addItemSearchString.trim().length > 0)
            {
                var results = {};
                lunrService.findItems($scope.addItemSearchString, $scope.allItems).forEach(function(item) {
                    results[item.itemId] = item;
                });
                $scope.newItemSearchByCategory = _.groupBy(results, 'category');
            }
            else
            {
                $scope.newItemSearchByCategory = {};
            }
        }

        $scope.clearSearchString = function()
        {
            $scope.searchString = "";
            $scope.newItem = {};
            $scope.search('searchbar');
            $scope.$broadcast("focusSearch");
        }

        function newItemHandler(item) {
            $scope.allItems[item.itemId] = item
            lunrService.addItemToLunr(item)
        }

        function refreshActiveSearch()
        {
            if ($scope.isSearchActive)
            {
                $scope.searchResultItems = {};
                lunrService.findItems($scope.searchString, $scope.allItems).forEach(function(item) {
                    $scope.searchResultItems[item.itemId] = item;
                });
            }
            else
            {
                $scope.searchResultItems = $scope.allItems;
            }

            $scope.itemsByCategory = _.groupBy($scope.searchResultItems, 'category');
        }
    });
})();