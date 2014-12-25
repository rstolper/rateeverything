(function() {

    var app = angular.module('rateeverything.services', []);

    app.factory('esDao', function(cloneItem) {
        return {
            //esclient: elasticsearch.Client({hosts: ['192.168.1.6:9200']}),
            esclient: elasticsearch.Client({hosts: ['127.0.0.1:9200']}),
            //esclient: elasticsearch.Client({hosts: ['http://dwalin-us-east-1.searchly.com/api-key/27d4807c8d63c036b5f1e2efbacaa01f']}),

            /*
             * Store a new item in ElasticSearch
             */
            insertItem: function(item, addedItemCallback)
            {
                // set the creation date
                item.creationDateMsec = Date.now();

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
    });

    app.factory('dummyDao', function(cloneItem) {
        return dummyDao = {
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
            },
            deleteItem: function (esItemId, itemDeletedCallback) {
                delete this.items[esItemId];
                itemDeletedCallback(esItemId);
            }
        };
    });

    app.factory('cloneItem', function() {
        return function (item) {
            var clonedItem = {
                userName: item.userName,
                category: item.category,
                itemName: item.itemName,
                rating: item.rating,
                creationDateMsec: item.creationDateMsec,
                esItemId: this.itemIndex
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
                this.field('itemName')
                this.ref('esItemId')
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
            findItems : function (searchString) {
                if (!lunrItemsIndex) createItemsIndex();

                // search the items index
                var resultRefs = lunrItemsIndex.search(searchString);

                // get the items from internal list
                var resultItems = [];
                resultRefs.forEach(function(entry) {
                    resultItems.push($scope.allItems[entry.ref])
                });

                return resultItems;
            },
            dropItemsIndex : function () {
                lunrItemsIndex = null;
            }
        }
    })
})();