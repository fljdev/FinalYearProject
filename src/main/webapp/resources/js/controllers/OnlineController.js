angular.module('myApp.OnlineController',[]).
    controller('OnlineController', function($scope,$cookieStore,$http,$state,$interval,$rootScope,$timeout){

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

    $scope.gameTimes = [1,2,5];
    $scope.gameStakes = [250,500,1000,2500,5000];
    $scope.selectedTime = 1;
    $scope.selectedStake = 1000;


    $scope.challengeSent = false;



    $scope.challenge = function(opponent,t,s){
        console.log(opponent,t,s);

        $rootScope.iAmLive = true;
        var challengeParams = {};
        challengeParams.currUserID = $rootScope.currentUser.id+"";
        challengeParams.opponentID = opponent.id+"";
        challengeParams.duration = t+"";
        challengeParams.stake = s+"";

        $http.post('/api/challenge/saveChallenge',JSON.stringify(challengeParams))
            .success(function (data, status) {
                if(status = 200){
                    console.log($rootScope.currentUser.username,"vs",opponent.username,"Challenge Saved");
                    console.log(data);
                    $scope.challID = data.id;
                    // $scope.waitForReply($scope.challID);
                    $scope.waitForReplyInterval($scope.challID);
                    // $scope.startChallengeValidTimer();

                    $scope.startTimerInterval();
                    $scope.challengeSent = true;

                    var msg = "Your Challenge to "+opponent.username+" was sent";
                    swal(msg,"Wait for their response","success");
                    // $state.go('openChallenges');

                }
            }).error(function (error) {
            console.log("something went wrong in saveChallenge!!");
        });
    };





 


    $http.post('/api/user/onlineUsers',$rootScope.currentUser)
        .success(function (data, status) {
            if(status = 200){
                $scope.online = data;
            }
        }).error(function (error) {
        console.log("something went wrong!!");
    });
    /**
     * Challenger game timer area
     */


        $scope.waitForReply = function(id){
            $http.post('/api/challenge/waitForReply',id)
                .success(function (data, status) {
                    if(status = 200){

                        if(data.accepted){

                            $scope.startGameTimerInterval(data.duration);
                            $state.go('trade',{challengeID: data.id});
                            $interval.cancel($scope.promise);
                            $interval.cancel($scope.prom);
                        }
                    }
                }).error(function (error) {
                console.log("something went wrong in /api/challenge/waitForReply'!!");
            });
        };
        // var promise = $interval( function(){ $scope.waitForReply($scope.challID); }, 5000);


    $scope.waitForReplyInterval = function(id){
        $scope.promise = $interval(function () {

            if($scope.challengeSent==true ){
                $scope.waitForReply(id);
            }else{
                $interval.cancel($scope.promise);
            }
        }, 5000);
    };




    $rootScope.gameTimeRemaining = 60;
    $scope.startGameTimerInterval = function(duration){
        var gameTimerPromise = $interval (function(){

            if($scope.gameTimeRemaining>=0){
                $http.post('/api/challenge/gameTimeRemainingForChallenger',$scope.challID)
                    .success(function (data, status) {
                        if(status = 200){

                            $rootScope.gameTimeRemaining=data;
                        }
                    }).error(function (error) {
                    console.log("something went wrong in onlines api/challenge/getChallengeRequestValidTime''!!");
                });
            }else{
                $interval.cancel(gameTimerPromise);
                console.log("got in to $interval.cancel(gameTimerPromise)");

                $http.post('/api/challenge/completeChallenge',JSON.stringify($scope.challID))
                    .success(function (data, status) {
                        if(status = 200){


                            msg = "Winner Winner "+data.firstName+" gets Dinner!!";
                            swal(msg,"congratulations","success");
                            $state.go('home');

                        }
                    }).error(function (error) {
                    console.log("something went wrong in waitForReply!!");
                });
                $state.go('home');

                var msg = "This Game has ended "+$rootScope.currentUser.firstName;
                swal({
                    title: msg,
                    text: 'Thanks for playing',
                    timer: 4000
                });

            }
        },5000);
    };



    $scope.challengeValidTime=60;
    $scope.startTimerInterval = function(){
        $scope.prom = $interval(function(){

            if($scope.challengeValidTime >0){

                $http.post('/api/challenge/getChallengeRequestValidTimeForChallenger',$scope.challID)
                    .success(function (data, status) {
                        if(status = 200){
                            $scope.challengeValidTime=data;
                            console.log("$scope.challengeValidTime ",$scope.challengeValidTime);

                        }
                    }).error(function (error) {
                    console.log("something went wrong in onlines api/challenge/getChallengeRequestValidTime''!!");
                });
            }else{
                $interval.cancel($scope.prom);

                $http.post('/api/challenge/withdrawChallenge',$scope.challID)
                    .success(function (data, status) {
                        if(status = 200){
                            $rootScope.iAmLive = false;
                            console.log("/withdrawChallenge returned ",data);
                        }
                    }).error(function (error) {
                    console.log("something went wrong in withdrawChallenge!!");
                });


                swal("Challenge not accepted","They Chickened Out!","error");
                $state.go('home');
            }
        },5000);
    };





    // $scope.startGameTimer = function(duration){
    //     $rootScope.gameCounter = duration * 30;
    //     $scope.onTimeout = function(){
    //         $rootScope.gameCounter--;
    //         mytimeout = $timeout($scope.onTimeout,1000);
    //
    //         if($rootScope.gameCounter==0){
    //             $scope.stop();
    //         }
    //     };
    //     var mytimeout = $timeout($scope.onTimeout,1000);
    //     $scope.stop = function(){
    //         $timeout.cancel(mytimeout);
    //         $http.post('/api/challenge/completeChallenge',JSON.stringify($scope.challID))
    //             .success(function (data, status) {
    //                 if(status = 200){
    //                     console.log("got back from completeChallenge");
    //                     msg = "Winner Winner "+data.firstName+" gets Dinner!!";
    //                     swal(msg,"congratulations","success");
    //                 }
    //             }).error(function (error) {
    //             console.log("something went wrong in waitForReply!!");
    //         });
    //         var msg = "This Game has ended "+$rootScope.currentUser.firstName;
    //         swal({
    //             title: msg,
    //             text: 'Thanks for playing',
    //             timer: 4000
    //         });
    //     }
    // };








    // $scope.challengeValidTime=35;
    // $scope.startChallengeValidTimer = function(){
    //
    //     $scope.onTimeout = function(){
    //         mytimeout = $timeout($scope.onTimeout,5000);
    //
    //         $http.post('/api/challenge/getChallengeRequestValidTime',$scope.challID)
    //             .success(function (data, status) {
    //                 if(status = 200){
    //                     $scope.challengeValidTime=data;
    //                     console.log($scope.challengeValidTime);
    //                     if($scope.challengeValidTime<=0){
    //                         $scope.stop();
    //                     }
    //                 }
    //             }).error(function (error) {
    //             console.log("something went wrong in onlines api/challenge/getChallengeRequestValidTime''!!");
    //         });
    //     };
    //     var mytimeout = $timeout($scope.onTimeout,5000);
    //     $scope.stop = function(){
    //         $timeout.cancel(mytimeout);
    //         swal("Challenge not accepted","They Chickened Out!","error");
    //         $state.go('home');
    //
    //         $http.post('/api/challenge/withdrawMyChallenge',$rootScope.currentUser.id)
    //                 .success(function (data, status) {
    //                     if(status = 200){
    //                         $rootScope.iAmLive = false;
    //                     }
    //                 }).error(function (error) {
    //                 console.log("something went wrong in withdrawMyChallenge!!");
    //         });
    //     }
    // };













})
    .filter('toMinSec', function(){
        return function(input){
            var minutes = parseInt(input/60, 10);
            var seconds = input%60;
            return minutes+' mins'+(seconds ? ' and '+seconds+' seconds' : '');
        }
    });

