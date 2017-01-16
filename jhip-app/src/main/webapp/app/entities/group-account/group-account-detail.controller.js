(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('GroupAccountDetailController', GroupAccountDetailController);

    GroupAccountDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'GroupAccount', 'Calendar', 'Mailbox', 'Media'];

    function GroupAccountDetailController($scope, $rootScope, $stateParams, previousState, entity, GroupAccount, Calendar, Mailbox, Media) {
        var vm = this;

        vm.groupAccount = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sgApp:groupAccountUpdate', function(event, result) {
            vm.groupAccount = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
