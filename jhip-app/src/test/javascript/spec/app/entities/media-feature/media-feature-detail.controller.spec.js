'use strict';

describe('Controller Tests', function() {

    describe('MediaFeature Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMediaFeature, MockMedia, MockFile;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMediaFeature = jasmine.createSpy('MockMediaFeature');
            MockMedia = jasmine.createSpy('MockMedia');
            MockFile = jasmine.createSpy('MockFile');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'MediaFeature': MockMediaFeature,
                'Media': MockMedia,
                'File': MockFile
            };
            createController = function() {
                $injector.get('$controller')("MediaFeatureDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'sgApp:mediaFeatureUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
