(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('CalendarDetailController', CalendarDetailController);

    CalendarDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Calendar', 'Event'];

    function CalendarDetailController($scope, $rootScope, $stateParams, previousState, entity, Calendar, Event) {
        var vm = this;

        vm.calendar = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sgApp:calendarUpdate', function(event, result) {
            vm.calendar = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
