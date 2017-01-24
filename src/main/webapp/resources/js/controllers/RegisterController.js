angular.module('myApp.RegisterController',[]).
controller('RegisterController',function($scope,$http,$state){
    $scope.RegisterText = "This is the Register!!";

    // alert("hell");
    $scope.register ={};

    $scope.submit=function(){


        console.log($scope.register);

        $http.post('http://localhost:8080/api/register', JSON.stringify($scope.register))
            .success(function (data, status) {
                if(status = 200){

                    console.log(data.username, "This is angalar register Controller");

                    //go to new state carrying "one of" the values the user entered as a  param
                    //go to the home page
                    $state.go('home',{varX:data.username});

                }
            }).error(function (error) {
            alert("something went wrong!!");
        });
    }
});



