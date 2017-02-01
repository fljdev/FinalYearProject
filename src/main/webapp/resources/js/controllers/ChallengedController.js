angular.module('myApp.ChallengedController',[]).
controller('ChallengedController', function($scope,$cookieStore,$http,$state,$stateParams){

    $scope.challenged = {};

    $scope.currentUser = $cookieStore.get('userCookie');

    $scope.askedUserID = $stateParams.param;
    $scope.askedUserObject;


    $http.post('http://localhost:8080/api/findById', $scope.askedUserID)
        .success(function (data, status) {
            if(status = 200){
                $scope.askedUserObject = data;
            }
        }).error(function (error) {
        alert("something went wrong!!");
    });

    $scope.pairs;
    $scope.direction = ["long","short"];
    $scope.stakes = ["100", "250","500","1000","2500","5000","10000"]

    $http.get('http://localhost:8080/api/pairs')
        .success(function (data, status) {
            if(status = 200){

                //data will be equal to the arraylist returned by the UserRestController
                console.log(data, "This is angalar pairs");
                $scope.pairs = data;

            }
        }).error(function (error) {
        console.log("something went wrong in the pairs controller init function!!");



    });//end http.get



    // $scope.askedUser = $scope.object;



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
