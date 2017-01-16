(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('MessageDetailController', MessageDetailController);

    MessageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Message', 'Mailbox'];

    function MessageDetailController($scope, $rootScope, $stateParams, previousState, entity, Message, Mailbox) {
        var vm = this;

        vm.message = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sgApp:messageUpdate', function(event, result) {
            vm.message = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
