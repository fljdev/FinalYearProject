angular.module('myApp.OnlineController',[]).
    controller('OnlineController', function($scope,$cookieStore){

    $scope.onlineMessage = "Who's online";


    $scope.online = {};
    $scope.online = $cookieStore.get('userCookie').username;




});
