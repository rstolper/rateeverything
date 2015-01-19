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
        $scope.userName = "";
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

        $scope.login = function () {
            if ($scope.isLoggedIn()) {
                // logout
                $scope.loggedInUser = "";
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
            else {
                // login
                if ($scope.userName && $scope.userName.trim().length > 0) {
                    $scope.loggedInUser = $scope.userName;
                }

                // find all items by username
                $scope.dao.getAllItems($scope.loggedInUser, function (receivedItems) {
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
        };

        $scope.addNewItem = function () {
            // exit if anything is empty
            if (!$scope.loggedInUser || !$scope.newItem.category || !$scope.newItem.name || !$scope.newItem.rating)
                return;

            $scope.newItem.owner = $scope.loggedInUser
            $scope.dao.insertItem($scope.newItem, function (item) {
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
            var itemId = item.id;
            $scope.dao.deleteItem(itemId, function () {
                delete $scope.allItems[itemId];
                lunrService.removeItemFromLunr({id: itemId});
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
                    results[item.id] = item;
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
            $scope.allItems[item.id] = item
            lunrService.addItemToLunr(item)
        }

        function refreshActiveSearch()
        {
            if ($scope.isSearchActive)
            {
                $scope.searchResultItems = {};
                lunrService.findItems($scope.searchString, $scope.allItems).forEach(function(item) {
                    $scope.searchResultItems[item.id] = item;
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