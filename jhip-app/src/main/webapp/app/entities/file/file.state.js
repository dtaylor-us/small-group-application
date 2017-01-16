(function() {
    'use strict';

    angular
        .module('sgApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('file', {
            parent: 'entity',
            url: '/file',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Files'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/file/files.html',
                    controller: 'FileController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('file-detail', {
            parent: 'entity',
            url: '/file/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'File'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/file/file-detail.html',
                    controller: 'FileDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'File', function($stateParams, File) {
                    return File.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'file',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('file-detail.edit', {
            parent: 'file-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/file/file-dialog.html',
                    controller: 'FileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['File', function(File) {
                            return File.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('file.new', {
            parent: 'file',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/file/file-dialog.html',
                    controller: 'FileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fileType: null,
                                file: null,
                                fileContentType: null,
                                fileSize: null,
                                fileName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('file', null, { reload: 'file' });
                }, function() {
                    $state.go('file');
                });
            }]
        })
        .state('file.edit', {
            parent: 'file',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/file/file-dialog.html',
                    controller: 'FileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['File', function(File) {
                            return File.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('file', null, { reload: 'file' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('file.delete', {
            parent: 'file',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/file/file-delete-dialog.html',
                    controller: 'FileDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['File', function(File) {
                            return File.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('file', null, { reload: 'file' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
