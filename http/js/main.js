(function() {

    var app = angular.module('rateeverythingApp', []);
    
    app.directive('focusOn', function() {
        return function(scope, elem, attr) {
            scope.$on(attr.focusOn, function(e) {
                elem[0].focus();
                elem[0].select();
            });
        };
    });

    app.controller('RateEverythingCtrl', ['$scope', function ($scope) {
        
        $scope.dao = esDao;
        
        $scope.newItem = {};
        $scope.allItems = {}; // static maintainer of all items the user has
        $scope.itemsByCategory = {};
        // TODO: custom watch expression on allItems to refresh this list via refreshActiveSearch()
        $scope.searchResultItems = {}; // the active listing of items to display
        $scope.userName = "";
        $scope.searchString = "";
        
        $scope.isSearchActive = false;
        $scope.activeSearchString = "";
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
                dropItemsIndex();
                $scope.allItems = {};
                $scope.searchResultItems = {};
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
                    dropItemsIndex();
                    for (var key in receivedItems) {
                        newItemHandler(receivedItems[key]);
                    }
                    refreshActiveSearch();
                    $scope.$apply();
                    $scope.$broadcast('focusSearch');
                });
            }
        };
        
        $scope.addNewItem = function () {
            // exit if anything is empty
            if (!$scope.loggedInUser || !$scope.newItem.category || !$scope.newItem.itemName || !$scope.newItem.rating)
                return;
            
            $scope.newItem.userName = $scope.loggedInUser
            $scope.dao.insertItem($scope.newItem, function (item) {
                newItemHandler(item);
                //refreshActiveSearch();
                $scope.newItem.itemName = "";
                $scope.newItem.rating = "";
                $scope.search('additem')
                //refreshActiveSearch();
                $scope.$apply();
                $scope.$broadcast('addItemToCategoryRequest');
            });
        };
        
        $scope.addItemToCategoryRequest = function(category) {
            $scope.newItem.category = category;
            $scope.newItem.itemName = "";
            $scope.newItem.rating = "";
            $scope.$broadcast('addItemToCategoryRequest');
        }
        
        $scope.deleteItem = function (item) {
            var itemId = item.esItemId;
            $scope.dao.deleteItem(itemId, function () {
                delete $scope.allItems[itemId];
                removeItemFromLunr({esItemId: itemId});
                $scope.newItem = item; // for ease of readding
                refreshActiveSearch();
                $scope.$apply();
            });
        };
        
        $scope.search = function (searchBy) {
            var searchString = "";
            if (searchBy == 'searchbar')
                searchString = $scope.searchString;
            else if (searchBy == 'additem')
                //searchString = ($scope.newItem.category || '') + " " + ($scope.newItem.itemName || '');
                searchString = ($scope.newItem.category || '');
            
            if (searchString && searchString.trim().length > 0)
            {
                $scope.isSearchActive = true;
                $scope.activeSearchString = searchString;
            }
            else
            {
                $scope.isSearchActive = false;
            }
            refreshActiveSearch()
        }
        
        $scope.clearSearchString = function()
        {
            $scope.searchString = "";
            $scope.newItem = {};
            $scope.search('searchbar');
            $scope.$broadcast("focusSearch");
        }
        
        function newItemHandler(item) {
            $scope.allItems[item.esItemId] = item
            addItemToLunr(item)
        }
        
        function refreshActiveSearch()
        {
            if ($scope.isSearchActive)
            {
                $scope.searchResultItems = {};
                findItems($scope.activeSearchString).forEach(function(item) {
                    $scope.searchResultItems[item.esItemId] = item;
                });
            }
            else
            {
                $scope.searchResultItems = $scope.allItems;
            }
            
            $scope.itemsByCategory = _.groupBy($scope.searchResultItems, 'category');
        }
        
        /***** LUNR ******/

        var lunrItemsIndex;

        function createItemsIndex()
        {
            lunrItemsIndex = lunr(function () {
                this.field('category')
                this.field('itemName')
                this.ref('esItemId')
            })
        }

        function addItemToLunr(item) {
            if (!lunrItemsIndex) createItemsIndex();
            
            lunrItemsIndex.add(item); 
        }

        function removeItemFromLunr(item) {
            if (!lunrItemsIndex) createItemsIndex();
            
            lunrItemsIndex.remove(item);
        }

        function findItems(searchString) {
            if (!lunrItemsIndex) createItemsIndex();
            
            // search the items index
            var resultRefs = lunrItemsIndex.search(searchString);

            // get the items from internal list
            var resultItems = [];
            resultRefs.forEach(function(entry) {
                resultItems.push($scope.allItems[entry.ref])
            });

            return resultItems;
        }

        function dropItemsIndex() {
            lunrItemsIndex = null;
        }
    }]);
    
    var dummyDao = {
        itemIndex: 1,
        items: {"0": {category:"test", itemName:"test", userName:"test", rating:"Yes", esItemId:"0"}},
        insertItem: function (item, addedItemCallback) {
            // simulate a new item object..
            var addedItem = cloneItem(item); 
            addedItem.esItemId = this.itemIndex++;
            
            this.items[addedItem.esItemId] = addedItem;
            addedItemCallback(addedItem);
        },
        getAllItems: function (userName, itemsReceivedCallback) {
            var dao = this;
            var promise = new Promise(function(resolve, reject) {
                var receivedItems = [];
                for (var key in dao.items) {
                    receivedItems.push(dao.items[key]);
                }
                resolve(receivedItems);
            }).then(itemsReceivedCallback);
            
//            var receivedItems = [];
//            for (var key in this.items) {
//                receivedItems.push(this.items[key]);
//            }
//            itemsReceivedCallback(receivedItems);
            
            //return promise;
        },
        deleteItem: function (esItemId, itemDeletedCallback) {
            delete this.items[esItemId];
            itemDeletedCallback(esItemId);
        }
    };

    var esDao = {
        esclient: elasticsearch.Client({hosts: ['192.168.1.6:9200']}),

        /*
         * Store a new item in ElasticSearch
         */
        insertItem: function(item, addedItemCallback)
        {
            // call ES indexer
            this.esclient.index({
              index: 'items',
              type: 'item',
              body: item
            }, function (err, resp) {
                if (err) {
                    alert(err);
                } else {
                    var newItem = cloneItem(item);
                    newItem.esItemId = resp._id;
                    addedItemCallback(newItem);
                }
            });
        },

        /*
         * Get the list of items for a user from ElasticSearch
         */
        getAllItems: function (userName, itemsReceivedCallback)
        {
            this.esclient.search({
              index: 'items',
              size: 50,
              body: {
                query: {
                    filtered: {
                        query: {
                            match_all: {
                            }
                        },
                        filter: {
                            term: { "userName": userName }
                        }
                    }
                }
              }
            }).then(function (resp) {
                // for each item
                var items = []
                var hits = resp.hits.hits;
                hits.forEach(function(resEntry) {
                    var item = resEntry._source;
                    item.esItemId = resEntry._id;
                    items.push(item);
                })
                itemsReceivedCallback(items);
            }, function(err) {
                alert(err);   
            });
        },

        deleteItem: function (esItemId, itemDeletedCallback)
        {
            this.esclient.delete({
                index: 'items',
                type: 'item',
                id: esItemId
            }, function (err, resp) {
                if (err) {
                    alert(err);
                } else {
                    itemDeletedCallback(esItemId);
                }
            });
        }
    }
    
    // TODO: This is NOT maintainable. Strategic solution?
    function cloneItem(item)
    {
        var clonedItem = {
            userName: item.userName,
            category: item.category,
            itemName: item.itemName,
            rating: item.rating,
            esItemId: this.itemIndex
        };
        
        return clonedItem;
    }
})();