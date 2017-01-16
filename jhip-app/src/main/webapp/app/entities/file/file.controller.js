(function() {
    'use strict';

    angular
        .module('sgApp')
        .controller('FileController', FileController);

    FileController.$inject = ['$scope', '$state', 'DataUtils', 'File'];

    function FileController ($scope, $state, DataUtils, File) {
        var vm = this;

        vm.files = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            File.query(function(result) {
                vm.files = result;
                vm.searchQuery = null;
            });
        }
    }
})();
