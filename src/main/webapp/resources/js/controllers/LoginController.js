angular.module('myApp.LoginController',[]).
controller('LoginController',function($scope, $http, $state,$cookieStore){

    $scope.submit=function(){
        console.log($scope.login);

        $http.post('/api/user/login', JSON.stringify($scope.login))
            .success(function (data, status) {
                if(status = 200){

                    $scope.login = data;
                    $state.go('home');

                    $cookieStore.put('userCookie',$scope.login);
                }
            }).error(function (error) {
            console.log("something went wrong!!");
        });
    }
});




