angular.module('myApp.RegisterController',[]).
controller('RegisterController',function($scope,$http,$state,$cookieStore){
    $scope.RegisterText = "This is the Register!!";

    // alert("hell");
    $scope.register ={};

    $scope.submit=function(){


        console.log($scope.register);

        $http.post('http://localhost:8080/api/register', JSON.stringify($scope.register))
            .success(function (data, status) {
                if(status = 200){

                    $scope.register = data;
                    console.log($scope.register);
                    // console.log(data.username, "This is angalar register Controller");

                    $state.go('home');
                    $cookieStore.put('user',$scope.register);


                }
            }).error(function (error) {
            alert("something went wrong!!");
        });
    }
});



