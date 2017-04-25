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



    if($rootScope.currentUser){
        $http.post('/api/user/findById', JSON.stringify($rootScope.currentUser.id))
            .success(function (data, status) {
                if(status = 200){
                    $cookieStore.put('userCookie', data);
                }
            }).error(function (error) {
            console.log("something went wrong in findById -> OnlineController!!");
        });
    }

    $scope.challenge = function(opponent,t,s){
        console.log(opponent,t,s);

        $scope.iAmLive = true;
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
                    $scope.waitForReply($scope.challID);
                    $scope.startChallengeValidTimer();

                    var msg = "Your Challenge to "+opponent.username+" was sent";
                    swal(msg,"Wait for their response","success");
                    // $state.go('openChallenges');

                }
            }).error(function (error) {
            console.log("something went wrong in saveChallenge!!");
        });

    };




    var name="";
    if(!$cookieStore.get('userCookie')){
        name = "xyz";
    }else{
        name = $cookieStore.get('userCookie').username
    }

    $scope.init = function(){
        $http.post('/api/user/onlineUsers',$rootScope.currentUser)
            .success(function (data, status) {
                if(status = 200){
                    //data will be equal to the arraylist returned by the UserRestController onlineUsers method
                    $scope.online = data;
                }
            }).error(function (error) {
            console.log("something went wrong!!");
        });
    };
    $scope.init();


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
            console.log("something went wrong in waitForReply!!");
        });

    };
    var promise = $interval( function(){ $scope.waitForReply($scope.challID); }, 3000);


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
                timer: 2000
            });
        }
    };



    $rootScope.challengeValidTime=10;
    var x = 10;
    $scope.startChallengeValidTimer = function(){
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
                $http.post('/api/challenge/withdrawMyChallenge',$rootScope.currentUser.id)
                    .success(function (data, status) {
                        if(status = 200){
                            $scope.iAmLive = false;
                            $rootScope.challengeValidTime=10;
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

