angular.module('myApp.RegisterController',[]).
controller('RegisterController',function($scope,$http){
    $scope.RegisterText = "This is the Register!!";

    // alert("hell");
    $scope.register ={};

    $scope.submit=function(){


        console.log($scope.register);

        $http.post('http://localhost:8080/api/register', JSON.stringify($scope.register))
            .success(function (data, status) {
                if(status = 200){

                    console.log(data.username, "This is angalar register Controller");
                }
            }).error(function (error) {
            alert("something went wrong!!");
        });
    }
});



