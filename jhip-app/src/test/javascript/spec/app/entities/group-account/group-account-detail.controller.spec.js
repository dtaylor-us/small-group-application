'use strict';

describe('Controller Tests', function() {

    describe('GroupAccount Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockGroupAccount, MockCalendar, MockMailbox, MockMedia;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockGroupAccount = jasmine.createSpy('MockGroupAccount');
            MockCalendar = jasmine.createSpy('MockCalendar');
            MockMailbox = jasmine.createSpy('MockMailbox');
            MockMedia = jasmine.createSpy('MockMedia');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'GroupAccount': MockGroupAccount,
                'Calendar': MockCalendar,
                'Mailbox': MockMailbox,
                'Media': MockMedia
            };
            createController = function() {
                $injector.get('$controller')("GroupAccountDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'sgApp:groupAccountUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
