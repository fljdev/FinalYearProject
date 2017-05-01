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
                    $scope.startChallengeValidTimer();

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

                            $scope.startTimer(data.duration);
                            $state.go('trade',{challengeID: data.id});
                            $interval.cancel(promise);
                        }
                    }
                }).error(function (error) {
                console.log("something went wrong in /api/challenge/waitForReply'!!");
            });

        };
        // var promise = $interval( function(){ $scope.waitForReply($scope.challID); }, 5000);


    $scope.waitForReplyInterval = function(id){
        var promise = $interval(function () {

            if($scope.challengeSent==true ){
                $scope.waitForReply(id);
            }else{
                $interval.cancel(promise);
            }
        }, 5000);
    };




    $scope.startTimer = function(duration){
        $rootScope.gameCounter = duration * 30;
        $scope.onTimeout = function(){
            $rootScope.gameCounter--;
            mytimeout = $timeout($scope.onTimeout,1000);

            if($rootScope.gameCounter==0){
                $scope.stop();
            }
        };
        var mytimeout = $timeout($scope.onTimeout,1000);
        $scope.stop = function(){
            $timeout.cancel(mytimeout);
            $http.post('/api/challenge/completeChallenge',JSON.stringify($scope.challID))
                .success(function (data, status) {
                    if(status = 200){
                        console.log("got back from completeChallenge");
                        msg = "Winner Winner "+data.firstName+" gets Dinner!!";
                        swal(msg,"congratulations","success");
                    }
                }).error(function (error) {
                console.log("something went wrong in waitForReply!!");
            });
            var msg = "This Game has ended "+$rootScope.currentUser.firstName;
            swal({
                title: msg,
                text: 'Thanks for playing',
                timer: 4000
            });
        }
    };



    $scope.challengeValidTime=35;
    $scope.startChallengeValidTimer = function(){

        $scope.onTimeout = function(){
            mytimeout = $timeout($scope.onTimeout,5000);

            $http.post('/api/challenge/getChallengeRequestValidTime',$scope.challID)
                .success(function (data, status) {
                    if(status = 200){
                        $scope.challengeValidTime=data;
                        console.log($scope.challengeValidTime);
                        if($scope.challengeValidTime<=0){
                            $scope.stop();
                        }
                    }
                }).error(function (error) {
                console.log("something went wrong in onlines api/challenge/getChallengeRequestValidTime''!!");
            });
        };


        var mytimeout = $timeout($scope.onTimeout,5000);
        $scope.stop = function(){
            $timeout.cancel(mytimeout);
            swal("Challenge not accepted","They Chickened Out!","error");
            $state.go('home');

            $http.post('/api/challenge/withdrawMyChallenge',$rootScope.currentUser.id)
                    .success(function (data, status) {
                        if(status = 200){
                            $rootScope.iAmLive = false;
                                // $scope.challengeValidTime=10;
                        }
                    }).error(function (error) {
                    console.log("something went wrong in withdrawMyChallenge!!");
                });
        }
    };
})





    .filter('toMinSec', function(){
        return function(input){
            var minutes = parseInt(input/60, 10);
            var seconds = input%60;
            return minutes+' mins'+(seconds ? ' and '+seconds+' seconds' : '');
        }
    });

