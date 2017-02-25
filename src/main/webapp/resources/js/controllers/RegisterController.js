angular.module('myApp.RegisterController',[]).
controller('RegisterController',function($scope,$http,$state,$cookieStore){
    $scope.RegisterText = "This is the Register!!";

    $scope.submit=function(){

        $http.post('/api/user/register', JSON.stringify($scope.register))
            .success(function (data, status) {
                if(status = 200){

                    $scope.register = data;
                    $state.go('home');
                    $cookieStore.put('userCookie',$scope.register);
                }
            }).error(function (error) {
            console.log("something went wrong!!");
        });
    }
});



