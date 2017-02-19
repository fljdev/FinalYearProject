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



    $scope.withdraw = function(x){

        var obj = x;

        console.log("x came in as ",x);

        $http.post('http://localhost:8080/api/challenge/withdrawChallenge',obj.id)
            .success(function (data, status) {
                if(status = 200){

                    console.log("withdraw challenge worked");
                    // $scope.challengesRecieved = data;
                }
            }).error(function (error) {
            alert("something went wrong in withdraw challenge!!");
        });//end http.get
    }

    $scope.accept = function(){
        alert("Challenge Accepted");
    }



});
