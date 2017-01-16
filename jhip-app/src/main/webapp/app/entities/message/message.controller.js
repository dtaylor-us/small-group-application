(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('MessageController', MessageController);

    MessageController.$inject = ['$scope', '$state', 'Message'];

    function MessageController ($scope, $state, Message) {
        var vm = this;

        vm.messages = [];

        loadAll();

        function loadAll() {
            Message.query(function(result) {
                vm.messages = result;
                vm.searchQuery = null;
            });
        }
    }
})();
