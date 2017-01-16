(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('GroupAccountDeleteController',GroupAccountDeleteController);

    GroupAccountDeleteController.$inject = ['$uibModalInstance', 'entity', 'GroupAccount'];

    function GroupAccountDeleteController($uibModalInstance, entity, GroupAccount) {
        var vm = this;

        vm.groupAccount = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            GroupAccount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
