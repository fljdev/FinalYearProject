angular.module('myApp.LoginController',[]).
controller('LoginController',function($scope, $http, $state){
        $scope.LoginText = "This is the login!!";
        $scope.login ={};




    $scope.submit=function(){
        console.log($scope.login);

        $http.post('http://localhost:8080/api/login', JSON.stringify($scope.login))
            .then(function (response) {
                if(response.status = 200){


                    console.log(response.data, "This is angalar login Controller");
                    alert(response.data.username);


                    $state.go('home',{varX:response.data.username});
                    // $state.go('home');



                }
            }).catch(function (error) {
                alert("something went wrong!!");
            });


    }

});




