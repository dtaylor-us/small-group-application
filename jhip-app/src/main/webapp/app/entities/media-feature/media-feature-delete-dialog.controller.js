(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('MediaFeatureDeleteController',MediaFeatureDeleteController);

    MediaFeatureDeleteController.$inject = ['$uibModalInstance', 'entity', 'MediaFeature'];

    function MediaFeatureDeleteController($uibModalInstance, entity, MediaFeature) {
        var vm = this;

        vm.mediaFeature = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MediaFeature.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
