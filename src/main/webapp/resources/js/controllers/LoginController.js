angular.module('myApp.LoginController',[]).
controller('LoginController',function($scope, $http, $state, $stateParams){
        $scope.LoginText = "This is the login!!";
        $scope.login ={};

    $scope.submit=function(){
        console.log($scope.login);

        $http.post('http://localhost:8080/api/login', JSON.stringify($scope.login))
            .success(function (data, status) {
                if(status = 200){

                    //display values the user entered
                    console.log(data, "This is angalar login Controller");

                    //go to new state carrying "one of" the values the user entered as a  param
                    //go to the home page
                    $state.go('home',{varX:data.username});

                }
            }).error(function (error) {
                alert("something went wrong!!");

            });


    }

});




