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

        $scope.nativeAuth = {
            action: "Login",
            usernameIsEmail: false
        };

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

            $scope.authToken = googleUser.getAuthResponse().id_token;
            $scope.authProvider = 'Google';

            var userIdFromServer = "";

            $scope.$digest;

            // get user
            $scope.dao.getUser($scope.authToken, $scope.authProvider, function (response) {
                console.log('user found: ' + JSON.stringify(response.data));
                userIdFromServer = response.data.userId.value;
                $scope.completeLogin(userIdFromServer);
            },
            function() {
                console.log('user not found with id token: ' + profile.getId());
                // create new user
                $scope.dao.createUserViaGoogle($scope.authToken, function(response) {
                    console.log("Created user: " + JSON.stringify(response.data));
                    userIdFromServer = response.data.userId.value;
                    $scope.completeLogin(userIdFromServer);
                });
            });
        }
        window.onGoogleSignIn = onGoogleSignIn;

//        $scope.loginDebug = function () {
//            if ($scope.userId && $scope.userId.trim().length > 0) {
//                $scope.authProvider = 'Debug';
//                $scope.completeLogin($scope.userId);
//            }
//        };

        $scope.loginNativeAuth = function() {
            if ($scope.nativeAuth.action == "Login") {
                console.log("Logging in with username: " + $scope.nativeAuth.username);
                $scope.dao.getNativeAuthToken($scope.nativeAuth.username, $scope.nativeAuth.password, function (response) {
                    console.log("Got native auth token: " + JSON.stringify(response.data));
                }, function (response) {
                    console.log("Error logging in:" + JSON.stringify(response.data));
                });
            } else {
                console.log("Creating new user with username: " + $scope.nativeAuth.username);
                $scope.dao.createUserViaNative($scope.nativeAuth.username, $scope.nativeAuth.password, function (response) {
                    console.log("Created user via native auth: " + JSON.stringify(response.data));
                }, function (response) {
                    console.log("Error creating user:" + JSON.stringify(response.data));
                });
            }
        }

        $scope.completeLogin = function (userId) {
            $scope.loggedInUser = userId;

            console.log("Scope.loggedInUser: " + $scope.loggedInUser);

            // find all items by username
            $scope.dao.getAllItems($scope.authToken, $scope.authProvider, function (response) {
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

            $scope.dao.insertItem($scope.authToken, $scope.authProvider, $scope.newItem, function (response) {
                var item = response.data;
                newItemHandler(item);
                $scope.newItem.name = "";
                $scope.newItem.rating = "";
                $scope.newItem.notes = "";
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
            $scope.dao.deleteItem($scope.authToken, $scope.authProvider, itemId, function (response) {
                delete $scope.allItems[itemId];
                lunrService.removeItemFromLunr({itemId: itemId});
                //$scope.newItem = item; // for ease of readding
                $scope.newItem.name = item.name;
                $scope.newItem.category = item.category;
                $scope.newItem.rating = item.rating;
                $scope.newItem.notes = item.notes;
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

//        function compareItems(a,b) {
//          if (a.category != b.category) {
//            if (a.category > b.category) {
//                return -1;
//            } else {
//                return 1;
//            }
//          } else if (a.rating != b.rating) {
//            if (a.rating == 'Yes') {
//                return -1;
//            } else if (a.rating == 'Maybe' && b.rating == 'No') {
//                return -1;
//            } else {
//                return 1;
//            }
//          } else {
//            if (a.name > b.name) {
//                return 1;
//            } else {
//                return -1;
//            }
//          }
//        }

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

//            Object.keys($scope.itemsByCategory).forEach(function(key) {
//                $scope.itemsByCategory[key].sort(compareItems);
//            });
        }
    });
})();