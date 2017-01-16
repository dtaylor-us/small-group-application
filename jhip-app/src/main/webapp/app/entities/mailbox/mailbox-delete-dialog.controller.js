(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('MailboxDeleteController',MailboxDeleteController);

    MailboxDeleteController.$inject = ['$uibModalInstance', 'entity', 'Mailbox'];

    function MailboxDeleteController($uibModalInstance, entity, Mailbox) {
        var vm = this;

        vm.mailbox = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Mailbox.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
