(function() {
    'use strict';

    angular
        .module('sgApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('mailbox', {
            parent: 'entity',
            url: '/mailbox',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Mailboxes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mailbox/mailboxes.html',
                    controller: 'MailboxController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('mailbox-detail', {
            parent: 'entity',
            url: '/mailbox/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Mailbox'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mailbox/mailbox-detail.html',
                    controller: 'MailboxDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Mailbox', function($stateParams, Mailbox) {
                    return Mailbox.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'mailbox',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('mailbox-detail.edit', {
            parent: 'mailbox-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mailbox/mailbox-dialog.html',
                    controller: 'MailboxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Mailbox', function(Mailbox) {
                            return Mailbox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mailbox.new', {
            parent: 'mailbox',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mailbox/mailbox-dialog.html',
                    controller: 'MailboxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                mailboxName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('mailbox', null, { reload: 'mailbox' });
                }, function() {
                    $state.go('mailbox');
                });
            }]
        })
        .state('mailbox.edit', {
            parent: 'mailbox',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mailbox/mailbox-dialog.html',
                    controller: 'MailboxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Mailbox', function(Mailbox) {
                            return Mailbox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mailbox', null, { reload: 'mailbox' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mailbox.delete', {
            parent: 'mailbox',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mailbox/mailbox-delete-dialog.html',
                    controller: 'MailboxDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Mailbox', function(Mailbox) {
                            return Mailbox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mailbox', null, { reload: 'mailbox' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
