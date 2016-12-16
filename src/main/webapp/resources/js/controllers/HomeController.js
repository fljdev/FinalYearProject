angular.module('myApp.HomeController',[]).
    controller('HomeController',function($scope, $stateParams){

    $scope.currentUser = $stateParams.varX;

});

