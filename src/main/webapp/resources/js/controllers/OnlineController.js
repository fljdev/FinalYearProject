angular.module('myApp.OnlineController',[]).
    controller('OnlineController', function($scope,$cookieStore,$http){

    $scope.online = {};

    $scope.challenge = function(x){

        var challenged = x.username;
        var challenger = $cookieStore.get('userCookie').username;
        alert(challenger + " has challenged "+challenged+" to a game!!");
    }

    $scope.init = function(){
        $http.post('http://localhost:8080/api/onlineUsers',$cookieStore.get('userCookie').password)
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
