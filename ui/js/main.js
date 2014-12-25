(function() {

    var app = angular.module('rateeverything', ['rateeverything.directives', 'rateeverything.controllers']);

    // Need to manually "init" the bootstrap tooltips
    $("body").tooltip({ selector: '[data-toggle=tooltip]' });

})();