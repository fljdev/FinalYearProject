angular.module('myApp.HomeController',[]).
    controller('HomeController',function($scope, $stateParams, $cookieStore){

    $scope.currentUser = $cookieStore.get('userCookie').username;


});

