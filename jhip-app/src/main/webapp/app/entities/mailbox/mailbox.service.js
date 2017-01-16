(function() {
    'use strict';
    angular
        .module('sgApp')
        .factory('Mailbox', Mailbox);

    Mailbox.$inject = ['$resource'];

    function Mailbox ($resource) {
        var resourceUrl =  'api/mailboxes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
