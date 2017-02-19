angular.module('myApp.OpenChallengesController',[]).
controller('OpenChallengesController', function($scope,$cookieStore,$http,$state){

    $scope.currUser = $cookieStore.get('userCookie');

    $scope.init = function(){
        $http.post('http://localhost:8080/api/challenge/challengesSent',$scope.currUser.id)
            .success(function (data, status) {
                if(status = 200){
                    $scope.sentChallenges = data;
                }
            }).error(function (error) {
            alert("something went wrong in challengesSent !!");
        });//end http.get

        $http.post('http://localhost:8080/api/challenge/challengesRecieved',$scope.currUser.id)
            .success(function (data, status) {
                if(status = 200){
                    $scope.challengesRecieved = data;
                }
            }).error(function (error) {
            alert("something went wrong in recievedChallenged !!");
        });//end http.get
    }//end function
    $scope.init();



    $scope.withdraw = function(){
        alert("Challenge WIthdrawn");
    }

    $scope.accept = function(){
        alert("Challenge Accepted");
    }



});
