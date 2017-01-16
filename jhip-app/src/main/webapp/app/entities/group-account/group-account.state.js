(function() {
    'use strict';

    angular
        .module('sgApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('group-account', {
            parent: 'entity',
            url: '/group-account',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'GroupAccounts'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/group-account/group-accounts.html',
                    controller: 'GroupAccountController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('group-account-detail', {
            parent: 'entity',
            url: '/group-account/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'GroupAccount'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/group-account/group-account-detail.html',
                    controller: 'GroupAccountDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'GroupAccount', function($stateParams, GroupAccount) {
                    return GroupAccount.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'group-account',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('group-account-detail.edit', {
            parent: 'group-account-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/group-account/group-account-dialog.html',
                    controller: 'GroupAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GroupAccount', function(GroupAccount) {
                            return GroupAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('group-account.new', {
            parent: 'group-account',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/group-account/group-account-dialog.html',
                    controller: 'GroupAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                groupName: null,
                                groupDesc: null,
                                groupRules: null,
                                groupEmail: null,
                                startDate: null,
                                endDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('group-account', null, { reload: 'group-account' });
                }, function() {
                    $state.go('group-account');
                });
            }]
        })
        .state('group-account.edit', {
            parent: 'group-account',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/group-account/group-account-dialog.html',
                    controller: 'GroupAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GroupAccount', function(GroupAccount) {
                            return GroupAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('group-account', null, { reload: 'group-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('group-account.delete', {
            parent: 'group-account',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/group-account/group-account-delete-dialog.html',
                    controller: 'GroupAccountDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GroupAccount', function(GroupAccount) {
                            return GroupAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('group-account', null, { reload: 'group-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
