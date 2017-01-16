(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('MediaFeatureDialogController', MediaFeatureDialogController);

    MediaFeatureDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'MediaFeature', 'Media', 'File'];

    function MediaFeatureDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, MediaFeature, Media, File) {
        var vm = this;

        vm.mediaFeature = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.media = Media.query();
        vm.mediafeaturefiles = File.query({filter: 'mediafeature-is-null'});
        $q.all([vm.mediaFeature.$promise, vm.mediafeaturefiles.$promise]).then(function() {
            if (!vm.mediaFeature.mediaFeatureFile || !vm.mediaFeature.mediaFeatureFile.id) {
                return $q.reject();
            }
            return File.get({id : vm.mediaFeature.mediaFeatureFile.id}).$promise;
        }).then(function(mediaFeatureFile) {
            vm.mediafeaturefiles.push(mediaFeatureFile);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.mediaFeature.id !== null) {
                MediaFeature.update(vm.mediaFeature, onSaveSuccess, onSaveError);
            } else {
                MediaFeature.save(vm.mediaFeature, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sgApp:mediaFeatureUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createDate = false;
        vm.datePickerOpenStatus.dateModified = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
