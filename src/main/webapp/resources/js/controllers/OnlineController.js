angular.module('myApp.OnlineController',[]).
    controller('OnlineController', function($scope,$cookieStore,$http,$state){

    $scope.online = {};

    $scope.challenge = function(x){

        // var challengingPlayer = x.username;
        // var challenged = $cookieStore.get('userCookie').username;

        var obj=x.username
        $state.go('challenged',{param:obj});
    }

    var name="";
    if(!$cookieStore.get('userCookie')){
        name = "xyz";

    }else{
        name = $cookieStore.get('userCookie').username

    }

    $scope.init = function(){
        $http.post('http://localhost:8080/api/onlineUsers',name)
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
