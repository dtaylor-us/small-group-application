(function() {
    'use strict';

    angular
        .module('sgApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('media-feature', {
            parent: 'entity',
            url: '/media-feature',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MediaFeatures'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/media-feature/media-features.html',
                    controller: 'MediaFeatureController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('media-feature-detail', {
            parent: 'entity',
            url: '/media-feature/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MediaFeature'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/media-feature/media-feature-detail.html',
                    controller: 'MediaFeatureDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MediaFeature', function($stateParams, MediaFeature) {
                    return MediaFeature.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'media-feature',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('media-feature-detail.edit', {
            parent: 'media-feature-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/media-feature/media-feature-dialog.html',
                    controller: 'MediaFeatureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MediaFeature', function(MediaFeature) {
                            return MediaFeature.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('media-feature.new', {
            parent: 'media-feature',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/media-feature/media-feature-dialog.html',
                    controller: 'MediaFeatureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                mediaType: null,
                                mediaDesc: null,
                                createDate: null,
                                dateModified: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('media-feature', null, { reload: 'media-feature' });
                }, function() {
                    $state.go('media-feature');
                });
            }]
        })
        .state('media-feature.edit', {
            parent: 'media-feature',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/media-feature/media-feature-dialog.html',
                    controller: 'MediaFeatureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MediaFeature', function(MediaFeature) {
                            return MediaFeature.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('media-feature', null, { reload: 'media-feature' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('media-feature.delete', {
            parent: 'media-feature',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/media-feature/media-feature-delete-dialog.html',
                    controller: 'MediaFeatureDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MediaFeature', function(MediaFeature) {
                            return MediaFeature.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('media-feature', null, { reload: 'media-feature' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
