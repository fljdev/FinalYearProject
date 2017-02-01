angular.module('myApp.ChallengedController',[]).
controller('ChallengedController', function($scope,$cookieStore,$http,$state,$stateParams){

    $scope.challenged = {};

    $scope.currentUser = $cookieStore.get('userCookie').username;

    $scope.object = $stateParams.param;
    $scope.askedUser = $scope.object;



    // $scope.challenge = function(x){
    //
    //     var challenged = x.username;
    //     var challenger = $cookieStore.get('userCookie').username;
    //     alert(challenger + " has challenged "+challenged+" to a game!!");
    // }

    // var name="";
    // if(!$cookieStore.get('userCookie')){
    //     name = "xyz";
    //
    // }else{
    //     name = $cookieStore.get('userCookie').username
    //
    // }

    // $scope.init = function(){
    //     $http.post('http://localhost:8080/api/onlineUsers',name)
    //         .success(function (data, status) {
    //             if(status = 200){
    //
    //
    //
    //             }
    //         }).error(function (error) {
    //         alert("something went wrong!!");
    //
    //     });//end http.get
    // }//end function
    //
    // $scope.init();


});
