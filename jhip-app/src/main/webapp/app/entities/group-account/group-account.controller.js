(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('GroupAccountController', GroupAccountController);

    GroupAccountController.$inject = ['$scope', '$state', 'GroupAccount'];

    function GroupAccountController ($scope, $state, GroupAccount) {
        var vm = this;

        vm.groupAccounts = [];

        loadAll();

        function loadAll() {
            GroupAccount.query(function(result) {
                vm.groupAccounts = result;
                vm.searchQuery = null;
            });
        }
    }
})();
