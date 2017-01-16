(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('GroupAccountDialogController', GroupAccountDialogController);

    GroupAccountDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'GroupAccount', 'Calendar', 'Mailbox', 'Media'];

    function GroupAccountDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, GroupAccount, Calendar, Mailbox, Media) {
        var vm = this;

        vm.groupAccount = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.groupcalendars = Calendar.query({filter: 'groupaccount-is-null'});
        $q.all([vm.groupAccount.$promise, vm.groupcalendars.$promise]).then(function() {
            if (!vm.groupAccount.groupCalendar || !vm.groupAccount.groupCalendar.id) {
                return $q.reject();
            }
            return Calendar.get({id : vm.groupAccount.groupCalendar.id}).$promise;
        }).then(function(groupCalendar) {
            vm.groupcalendars.push(groupCalendar);
        });
        vm.groupmailboxes = Mailbox.query({filter: 'groupaccount-is-null'});
        $q.all([vm.groupAccount.$promise, vm.groupmailboxes.$promise]).then(function() {
            if (!vm.groupAccount.groupMailbox || !vm.groupAccount.groupMailbox.id) {
                return $q.reject();
            }
            return Mailbox.get({id : vm.groupAccount.groupMailbox.id}).$promise;
        }).then(function(groupMailbox) {
            vm.groupmailboxes.push(groupMailbox);
        });
        vm.groupmedias = Media.query({filter: 'groupaccount-is-null'});
        $q.all([vm.groupAccount.$promise, vm.groupmedias.$promise]).then(function() {
            if (!vm.groupAccount.groupMedia || !vm.groupAccount.groupMedia.id) {
                return $q.reject();
            }
            return Media.get({id : vm.groupAccount.groupMedia.id}).$promise;
        }).then(function(groupMedia) {
            vm.groupmedias.push(groupMedia);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.groupAccount.id !== null) {
                GroupAccount.update(vm.groupAccount, onSaveSuccess, onSaveError);
            } else {
                GroupAccount.save(vm.groupAccount, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sgApp:groupAccountUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
