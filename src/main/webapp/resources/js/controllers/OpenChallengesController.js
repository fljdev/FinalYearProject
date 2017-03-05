angular.module('myApp.OpenChallengesController',[]).
controller('OpenChallengesController', function($scope,$cookieStore,$http,$state){

    $scope.currUser = $cookieStore.get('userCookie');

    $scope.init = function(){
        $http.post('/api/challenge/challengesSent',$scope.currUser.id)
            .success(function (data, status) {
                if(status = 200){
                    $scope.sentChallenges = data;
                }
            }).error(function (error) {
            console.log("something went wrong in challengesSent !!");
        });

        $http.post('/api/challenge/challengesRecieved',$scope.currUser.id)
            .success(function (data, status) {
                if(status = 200){
                    $scope.challengesRecieved = data;
                }
            }).error(function (error) {
            console.log("something went wrong in recievedChallenged !!");
        });
    }//end function
    $scope.init();

    $scope.withdraw = function(x){

        var obj = x;

        console.log("x came in as ",x);

        $http.post('/api/challenge/withdrawChallenge',obj.id)
            .success(function (data, status) {
                if(status = 200){

                    console.log("x.id was no ",obj.id);
                    console.log("withdraw challenge worked");
                }
            }).error(function (error) {
            console.log("something went wrong in withdraw challenge!!");
        });
    }

    $scope.accept = function(x){
        console.log("challenge that was accepted : ",x);

    }

});
