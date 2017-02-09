angular.module('myApp.RegisterController',[]).
controller('RegisterController',function($scope,$http,$state,$cookieStore){
    $scope.RegisterText = "This is the Register!!";

    // alert("hell");
    $scope.register ={};

    $scope.submit=function(){


        console.log($scope.register);

        $http.post('http://localhost:8080/api/user/register', JSON.stringify($scope.register))
            .success(function (data, status) {
                if(status = 200){

                    $scope.register = data;
                    console.log($scope.register);

                    $state.go('home');
                    $cookieStore.put('userCookie',$scope.register);


                }
            }).error(function (error) {
            alert("something went wrong!!");
        });
    }
});



