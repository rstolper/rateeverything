(function() {

    var app = angular.module('rateeverything.services', []);

    app.factory('restDao', function(cloneItem, $http) {
        return {
            insertItem: function(authToken, authProvider, item, addedItemCallback)
            {
                var req = {
                    method: 'POST',
                    url: '/app/api/v1/user/items',
                    headers: {
                        'AuthToken': authToken,
                        'AuthProvider': authProvider
                    },
                    data: item
                }
                $http(req).then(addedItemCallback, function(response) {
                    alert("item post failed :(");
                });
            },

            updateItem: function(authToken, authProvider, itemId, item, updateItemCallback)
            {
                var req = {
                    method: 'PUT',
                    url: '/app/api/v1/user/items/' + itemId,
                    headers: {
                        'AuthToken': authToken,
                        'AuthProvider': authProvider
                    },
                    data: item
                }
                $http(req).then(updateItemCallback, function(response) {
                    alert("item update failed :(");
                });
            },

            getAllItems: function (authToken, authProvider, itemsReceivedCallback)
            {
                var req = {
                    method: 'GET',
                    url: '/app/api/v1/user/items',
                    headers: {
                        'AuthToken': authToken,
                        'AuthProvider': authProvider
                    }
                }
                $http(req).then(itemsReceivedCallback, function(response) {
                    alert("get all items failed :(");
                })
            },

            deleteItem: function (authToken, authProvider, itemId, itemDeletedCallback)
            {
                var req = {
                    method: 'DELETE',
                    url: '/app/api/v1/user/items/' + itemId,
                    headers: {
                        'AuthToken': authToken,
                        'AuthProvider': authProvider
                    }
                }
                $http(req).then(itemDeletedCallback, function(response) {
                    alert("delete item failed :(");
                });
            },

            getUser: function (authToken, authProvider, userFoundCallback, userNotFoundCallback)
            {
                var req = {
                    method: 'GET',
                    url: '/app/api/v1/user',
                    headers: {
                        'AuthToken': authToken,
                        'AuthProvider': authProvider
                    }
                }
                $http(req).then(userFoundCallback, userNotFoundCallback);
            },

            createUserViaGoogle: function (googleIdToken, successCallback, errorCallback) {
                var req = {
                    method: 'POST',
                    url: '/app/api/v1/users/viaGoogleAuthToken',
                    headers: {
                        'AuthToken': googleIdToken
                    }
                }
                $http(req).then(successCallback, errorCallback);
            },

            createUserViaNative: function (username, password, successCallback, errorCallback) {
                var req = {
                    method: 'POST',
                    url: '/app/api/v1/users/viaNativeAuth',
                    data: {
                        username: username,
                        password: password
                    }
                }
                $http(req).then(successCallback, errorCallback);
            },

            getNativeAuthToken: function (username, password, successCallback, errorCallback) {
                var req = {
                    method: 'POST',
                    url: '/app/api/v1/auth',
                    data: {
                        username: username,
                        password: password
                    }
                }
                $http(req).then(successCallback, errorCallback);
            }
        }
    });

    app.factory('dummyDao', function(cloneItem) {
        return dummyDao = {
            itemIndex: 1,
            items: {"0": {category:"test", name:"test", userId:"test", rating:"Yes", itemId:"0"}},
            insertItem: function (item, addedItemCallback) {
                // simulate a new item object..
                var addedItem = cloneItem(item);
                addedItem.itemId = this.itemIndex++;

                this.items[addedItem.itemid] = addedItem;
                addedItemCallback(addedItem);
            },
            getAllItems: function (userId, itemsReceivedCallback) {
                var dao = this;
                var promise = new Promise(function(resolve, reject) {
                    var receivedItems = [];
                    for (var key in dao.items) {
                        receivedItems.push(dao.items[key]);
                    }
                    resolve(receivedItems);
                }).then(itemsReceivedCallback);
            },
            deleteItem: function (itemId, itemDeletedCallback) {
                delete this.items[itemId];
                itemDeletedCallback(itemId);
            }
        };
    });

    app.factory('cloneItem', function() {
        return function (item) {
            var clonedItem = {
                category: item.category,
                name: item.name,
                rating: item.rating,
                creationDate: item.creationDate,
                userId: item.userId,
                itemId: this.itemIndex
            };

            return clonedItem;
        }
    });

    app.factory('lunrService', function() {
        var lunrItemsIndex;
        var interface = {}

        function createItemsIndex()
        {
            lunrItemsIndex = lunr(function () {
                this.field('category')
                this.field('name')
                this.ref('itemId')
            })
        }

        return {
            addItemToLunr : function (item) {
                if (!lunrItemsIndex) createItemsIndex();
                lunrItemsIndex.add(item);
            },
            updateItemInLunr : function (item) {
                if (!lunrItemsIndex) createItemsIndex();
                lunrItemsIndex.update(item);
            },
            removeItemFromLunr : function (item) {
                if (!lunrItemsIndex) createItemsIndex();
                lunrItemsIndex.remove(item);
            },
            findItems : function (searchString, allItems) {
                if (!lunrItemsIndex) createItemsIndex();

                // search the items index
                var resultRefs = lunrItemsIndex.search(searchString);

                // get the items from internal list
                var resultItems = [];
                resultRefs.forEach(function(entry) {
                    resultItems.push(allItems[entry.ref])
                });

                return resultItems;
            },
            dropItemsIndex : function () {
                lunrItemsIndex = null;
            }
        }
    })
})();