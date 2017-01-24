angular.module('myApp.HomeController',[]).
    controller('HomeController',function($scope, $stateParams, $cookieStore){

    $scope.currentUser = $cookieStore.get('user').username;


});

