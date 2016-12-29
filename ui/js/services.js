(function() {

    var app = angular.module('rateeverything.services', []);

    app.factory('restDao', function(cloneItem, $http) {
        return {
            insertItem: function(userId, item, addedItemCallback)
            {
                $http.post('/app/api/v1/users/'+userId+'/items', item).
                    success(function(data, status, headers, config) {
                        addedItemCallback(data);
                    }).
                    error(function(data, status, headers, config) {
                        alert("item post failed :(");
                    });
            },

            getAllItems: function (userId, itemsReceivedCallback)
            {
                $http.get('/app/api/v1/users/'+userId+'/items').
                    success(function(data, status, headers, config) {
                        itemsReceivedCallback(data);
                    }).
                    error(function(data, status, headers, config) {
                        alert("get all items failed :(");
                    });
            },

            deleteItem: function (userId, itemId, itemDeletedCallback)
            {
                $http.delete('/app/api/v1/users/'+userId+'/items/'+itemId).
                    success(function(data, status, headers, config) {
                        itemDeletedCallback();
                    }).
                    error(function(data, status, headers, config) {
                        alert("delete item failed :(");
                    });
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