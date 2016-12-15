angular.module('myApp.RegisterController',[]).
controller('RegisterController',function($scope,$http){
    $scope.RegisterText = "This is the Register!!";

    alert("hell");
    $scope.register ={};

    $scope.submit=function(){


        console.log($scope.register);

        $http.post('http://localhost:8080/api/register', JSON.stringify($scope.register))
            .then(function (response) {
                if(response.status = 200){

                    console.log(response.data, "This is angalar register Controller");
                    alert($scope.register.username+ " Registered");
                }
            }).catch(function (error) {
            alert("something went wrong!!");
        });
    }
});



