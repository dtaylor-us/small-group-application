(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('MediaDetailController', MediaDetailController);

    MediaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Media', 'MediaFeature'];

    function MediaDetailController($scope, $rootScope, $stateParams, previousState, entity, Media, MediaFeature) {
        var vm = this;

        vm.media = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sgApp:mediaUpdate', function(event, result) {
            vm.media = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
