(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('MailboxDetailController', MailboxDetailController);

    MailboxDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Mailbox', 'Message'];

    function MailboxDetailController($scope, $rootScope, $stateParams, previousState, entity, Mailbox, Message) {
        var vm = this;

        vm.mailbox = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sgApp:mailboxUpdate', function(event, result) {
            vm.mailbox = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
