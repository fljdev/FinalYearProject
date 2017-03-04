angular.module('myApp.OnlineController',[]).
    controller('OnlineController', function($scope,$cookieStore,$http,$state){

    $scope.stakes = ["100", "250","500","1000","2500","5000","10000"];


    $scope.currUser = $cookieStore.get('userCookie').username;
    // if($scope.currUser){
    //     $http.post('/api/user/findById', JSON.stringify($scope.currUser.id))
    //         .success(function (data, status) {
    //             if(status = 200){
    //                 $cookieStore.put('userCookie', data);
    //             }
    //         }).error(function (error) {
    //         console.log("something went wrong in findById -> OnlineController!!");
    //     });
    // }

    $scope.challenge = function(user,stake){
        console.log("stake is ",stake);
        console.log(" is ",user);

        // var obj=x
        // $state.go('challenge',{param:obj.id});
    }

    var name="";
    if(!$cookieStore.get('userCookie')){
        name = "xyz";
    }else{
        name = $cookieStore.get('userCookie').username
    }

    $scope.init = function(){
        $http.post('/api/user/onlineUsers',name)
            .success(function (data, status) {
                if(status = 200){
                    //data will be equal to the arraylist returned by the UserRestController onlineUsers method
                    $scope.online = data;
                }
            }).error(function (error) {
            console.log("something went wrong!!");
        });
    }
    $scope.init();
});
