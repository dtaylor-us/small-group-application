(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('MediaFeatureController', MediaFeatureController);

    MediaFeatureController.$inject = ['$scope', '$state', 'MediaFeature'];

    function MediaFeatureController ($scope, $state, MediaFeature) {
        var vm = this;

        vm.mediaFeatures = [];

        loadAll();

        function loadAll() {
            MediaFeature.query(function(result) {
                vm.mediaFeatures = result;
                vm.searchQuery = null;
            });
        }
    }
})();
