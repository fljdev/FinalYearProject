angular.module('myApp.OnlineController',[]).
    controller('OnlineController', function($scope,$cookieStore,$http,$state){

    $scope.challenge = function(x){
        var obj=x
        $state.go('challenge',{param:obj.id});
    }



    /**
     * The below section deals solely with displaying the users who are currently online
     * except the user who is logged into this browser session
     * @type {string}
     */

    var name="";
    if(!$cookieStore.get('userCookie')){
        name = "xyz";
    }else{
        name = $cookieStore.get('userCookie').username
    }

    $scope.init = function(){
        $http.post('http://localhost:8080/api/user/onlineUsers',name)
            .success(function (data, status) {
                if(status = 200){
                    //data will be equal to the arraylist returned by the UserRestController onlineUsers method
                    $scope.online = data;
                }
            }).error(function (error) {
            alert("something went wrong!!");
        });//end http.get
    }//end function
    $scope.init();
});
