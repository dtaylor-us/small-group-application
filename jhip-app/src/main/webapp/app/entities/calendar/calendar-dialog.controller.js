(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('CalendarDialogController', CalendarDialogController);

    CalendarDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Calendar', 'Event'];

    function CalendarDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Calendar, Event) {
        var vm = this;

        vm.calendar = entity;
        vm.clear = clear;
        vm.save = save;
        vm.events = Event.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.calendar.id !== null) {
                Calendar.update(vm.calendar, onSaveSuccess, onSaveError);
            } else {
                Calendar.save(vm.calendar, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sgApp:calendarUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
