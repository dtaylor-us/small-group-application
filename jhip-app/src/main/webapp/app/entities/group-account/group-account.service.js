(function() {
    'use strict';
    angular
        .module('sgApp')
        .factory('GroupAccount', GroupAccount);

    GroupAccount.$inject = ['$resource', 'DateUtils'];

    function GroupAccount ($resource, DateUtils) {
        var resourceUrl =  'api/group-accounts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                        data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
