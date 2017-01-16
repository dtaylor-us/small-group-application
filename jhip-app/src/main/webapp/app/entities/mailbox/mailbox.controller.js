(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('MailboxController', MailboxController);

    MailboxController.$inject = ['$scope', '$state', 'Mailbox'];

    function MailboxController ($scope, $state, Mailbox) {
        var vm = this;

        vm.mailboxes = [];

        loadAll();

        function loadAll() {
            Mailbox.query(function(result) {
                vm.mailboxes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
