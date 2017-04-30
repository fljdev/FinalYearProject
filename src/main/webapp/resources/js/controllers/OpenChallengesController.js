angular.module('myApp.OpenChallengesController',[]).
controller('OpenChallengesController', function($scope,$cookieStore,$http,$state,$rootScope,$timeout){

    /**
     * User user object from the Database, instead of the browser cookie (No Problems)
     */
    $scope.setUser = function(){
        $rootScope.currentUser = $cookieStore.get('userCookie');
        if($rootScope.currentUser){
            $http.post('/api/user/findById', JSON.stringify($rootScope.currentUser.id))
                .success(function (data, status) {
                    if(status = 200){
                        $cookieStore.put('userCookie', data);
                        $rootScope.currentUser = data;
                    }
                }).error(function (error) {
                console.log("something went wrong in findById -> AllUsersController!!");
            });
        }
    };
    $scope.setUser();


    $scope.init = function(){
        // $http.post('/api/challenge/challengesSent',$rootScope.currentUser)
        //     .success(function (data, status) {
        //         if(status = 200){
        //             $scope.sentChallenges = data;
        //         }
        //     }).error(function (error) {
        //     console.log("something went wrong in challengesSent !!");
        // });

        // $http.post('/api/challenge/challengesRecieved',$rootScope.currentUser)
        //     .success(function (data, status) {
        //         if(status = 200){
        //             $scope.challengesRecieved = data;
        //         }
        //     }).error(function (error) {
        //     console.log("something went wrong in recievedChallenged !!");
        // });

        $http.post('/api/challenge/liveChallenge',$rootScope.currentUser)
            .success(function (data, status) {
                if(status = 200){
                    $scope.liveChallenge = data;
                }
            }).error(function (error) {
            console.log("something went wrong in liveChallenge !!");
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
    };

    $scope.decline = function(x){

        var obj = x;


        $http.post('/api/challenge/declineChallenge',obj.id)
            .success(function (data, status) {
            if(status = 200){

                console.log("x.id was no ",obj.id);
                console.log("decline challenge worked");
            }
        }).error(function (error) {
            console.log("something went wrong in withdraw challenge!!");
        });
    };


    $scope.accept = function(x){


        $http.post('/api/challenge/acceptChallenge',x.id)
            .success(function (data, status) {
                if(status = 200){
                    var name = x.challenger.firstName + " Vs " + x.opponent.firstName;
                    swal("Let's Trade",name,"success");
                    $rootScope.openChallengesCount--;

                    $scope.startTimer(x.duration);

                    $state.go('trade',{challengeID: x.id});


                }
            }).error(function (error) {
            console.log("something went wrong in accept challenge!!");
        });



        $scope.startTimer = function(duration){
            $rootScope.gameCounter = duration * 30;
            $scope.onTimeout = function(){
                $rootScope.gameCounter--;
                console.log("game time left ",$rootScope.gameCounter);
                mytimeout = $timeout($scope.onTimeout,1000);

                if($rootScope.gameCounter==0){
                    $scope.stop();
                }
            };
            var mytimeout = $timeout($scope.onTimeout,1000);

            $scope.stop = function(){
                $timeout.cancel(mytimeout);
                var msg = "This Game has ended "+$rootScope.currentUser.firstName
                swal({
                    title: msg,
                    text: 'Thanks for playing',
                    timer: 4000
                });
            };
        };



        $http.post('/api/challenge/updateGameAccount',x.id)
            .success(function (data, status) {
                if(status = 200){
                    console.log("Updating GameAccount worked , x.id was no ",x.id);
                }
            }).error(function (error) {
            console.log("something went wrong in updateGameAccount!!");
        });
    };

    // $scope.findMore = function(){
    //     $state.go('onlineUsers');
    // };

    // $scope.playNow = function(x){
    //     $state.go('trade',{challengeID: x.id});
    // }


    $scope.liveIncoming = true;

    $rootScope.challengeValidTime=10;
    var x = 10;
    $scope.startChallengeValidTimer = function(){
        console.log("in here");
        $scope.onTimeout = function(){
            $rootScope.challengeValidTime--;
            mytimeout = $timeout($scope.onTimeout,1000);


            if($rootScope.challengeValidTime==0){
                $scope.stop();
            }
        };
        var mytimeout = $timeout($scope.onTimeout,1000);
        $scope.stop = function(){
            $timeout.cancel(mytimeout);
            swal("Challenge not accepted","They Chickened Out!","error");
            $http.post('/api/challenge/declineChallenge',$rootScope.currentUser.id)
                .success(function (data, status) {
                    if(status = 200){
                        $scope.liveIncoming = false;
                        $rootScope.challengeValidTime=10;
                        swal("You refused this challenge","your loss!","error");
                        $state.go('home');

                    }
                }).error(function (error) {
                console.log("something went wrong in withdrawMyChallenge!!");
            });
        }
    };

    $scope.startChallengeValidTimer();

})  .filter('toMinSec', function(){
    return function(input){
        var minutes = parseInt(input/60, 10);
        var seconds = input%60;
        return minutes+' mins'+(seconds ? ' and '+seconds+' seconds' : '');
    }
});
