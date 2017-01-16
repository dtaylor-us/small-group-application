(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('MediaFeatureDetailController', MediaFeatureDetailController);

    MediaFeatureDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MediaFeature', 'Media', 'File'];

    function MediaFeatureDetailController($scope, $rootScope, $stateParams, previousState, entity, MediaFeature, Media, File) {
        var vm = this;

        vm.mediaFeature = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sgApp:mediaFeatureUpdate', function(event, result) {
            vm.mediaFeature = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
