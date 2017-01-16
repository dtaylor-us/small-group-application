(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('CalendarController', CalendarController);

    CalendarController.$inject = ['$scope', '$state', 'Calendar'];

    function CalendarController ($scope, $state, Calendar) {
        var vm = this;

        vm.calendars = [];

        loadAll();

        function loadAll() {
            Calendar.query(function(result) {
                vm.calendars = result;
                vm.searchQuery = null;
            });
        }
    }
})();
