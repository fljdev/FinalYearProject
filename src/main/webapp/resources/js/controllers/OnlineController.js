angular.module('myApp.OnlineController',[]).
    controller('OnlineController', function($scope,$cookieStore,$http,$state){

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

    $scope.challenge = function(x){
        var obj=x
        $state.go('challenge',{param:obj.id});
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
