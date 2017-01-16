(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('MailboxDialogController', MailboxDialogController);

    MailboxDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Mailbox', 'Message'];

    function MailboxDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Mailbox, Message) {
        var vm = this;

        vm.mailbox = entity;
        vm.clear = clear;
        vm.save = save;
        vm.messages = Message.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.mailbox.id !== null) {
                Mailbox.update(vm.mailbox, onSaveSuccess, onSaveError);
            } else {
                Mailbox.save(vm.mailbox, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sgApp:mailboxUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
