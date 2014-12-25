(function() {

    var app = angular.module('rateeverything.directives', []);
    
    app.directive('focusOn', function() {
        return function(scope, elem, attr) {
            scope.$on(attr.focusOn, function(e) {
                elem[0].focus();
                elem[0].select();
            });
        };
    });

})();