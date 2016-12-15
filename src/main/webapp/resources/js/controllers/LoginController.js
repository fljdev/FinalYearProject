angular.module('myApp.LoginController',[]).
controller('LoginController',function($scope, $http){
        $scope.LoginText = "This is the login!!";
        $scope.login ={};

    // var user = $scope.login.username;
    // var pass = $scope.login.password;

    $scope.submit=function(){


        console.log($scope.login);


    }

});




