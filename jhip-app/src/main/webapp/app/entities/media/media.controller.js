(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('MediaController', MediaController);

    MediaController.$inject = ['$scope', '$state', 'Media'];

    function MediaController ($scope, $state, Media) {
        var vm = this;

        vm.media = [];

        loadAll();

        function loadAll() {
            Media.query(function(result) {
                vm.media = result;
                vm.searchQuery = null;
            });
        }
    }
})();
