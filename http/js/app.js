(function() {

    var app = angular.module('rateeverythingApp', []);

    app.controller('RateEverythingCtrl', function () {
        this.items = sampleItems;
    });
    
    var sampleItems = [
        {
            userName: "roman",
            category: "boots",
            itemName: "timberland",
            rating: "Yes",
            esItemId: "123"
        },
        {
            userName: "manveen",
            category: "boots",
            itemName: "aldo",
            rating: "Yes",
            esItemId: "123"
        }
    ];
    
})();