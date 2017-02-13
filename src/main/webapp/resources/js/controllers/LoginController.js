angular.module('myApp.LoginController',[]).
controller('LoginController',function($scope, $http, $state,$cookieStore, $rootScope){
        $scope.login ={};

    $scope.submit=function(){
        console.log($scope.login);

        $http.post('http://localhost:8080/api/user/login', JSON.stringify($scope.login))
            .success(function (data, status) {
                if(status = 200){



                    $scope.login = data;
                    console.log($scope.login);

                    $state.go('home');

                    $cookieStore.put('userCookie',$scope.login);



                }
            }).error(function (error) {
                alert("something went wrong!!");
        });
    }
});




