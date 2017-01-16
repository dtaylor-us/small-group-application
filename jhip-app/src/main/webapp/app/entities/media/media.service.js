(function() {
    'use strict';
    angular
        .module('sgApp')
        .factory('Media', Media);

    Media.$inject = ['$resource'];

    function Media ($resource) {
        var resourceUrl =  'api/media/:id';

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
