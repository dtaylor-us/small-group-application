(function() {
    'use strict';
    angular
        .module('sgApp')
        .factory('MediaFeature', MediaFeature);

    MediaFeature.$inject = ['$resource', 'DateUtils'];

    function MediaFeature ($resource, DateUtils) {
        var resourceUrl =  'api/media-features/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createDate = DateUtils.convertDateTimeFromServer(data.createDate);
                        data.dateModified = DateUtils.convertDateTimeFromServer(data.dateModified);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
