(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('MediaDialogController', MediaDialogController);

    MediaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Media', 'MediaFeature'];

    function MediaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Media, MediaFeature) {
        var vm = this;

        vm.media = entity;
        vm.clear = clear;
        vm.save = save;
        vm.mediafeatures = MediaFeature.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.media.id !== null) {
                Media.update(vm.media, onSaveSuccess, onSaveError);
            } else {
                Media.save(vm.media, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sgApp:mediaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
