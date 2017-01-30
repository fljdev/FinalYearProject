angular.module('myApp.OnlineController',[]).
    controller('OnlineController', function($scope,$cookieStore,$http){

    $scope.online = {};

    $scope.challenge = function(){
        alert("challenged");
    }

    $scope.init = function(){
        $http.get('http://localhost:8080/api/onlineUsers')
            .success(function (data, status) {
                if(status = 200){

                    //data will be equal to the arraylist returned by the UserRestController onlineUsers method
                    console.log(data, "This is angalar OnlineController");
                    $scope.online = data;

                }
            }).error(function (error) {
            alert("something went wrong!!");

        });//end http.get
    }//end function

    $scope.init();


});
