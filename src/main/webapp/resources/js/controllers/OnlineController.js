angular.module('myApp.OnlineController',[]).
    controller('OnlineController', function($scope,$cookieStore,$http,$state,$interval){

    $scope.gameTimes = [2,15,30,60];
    $scope.gameStakes = [250,500,1000,2500,5000];
    $scope.selectedTime = 15;
    $scope.selectedStake = 1000;

    $scope.currUser = $cookieStore.get('userCookie');
    if($scope.currUser){
        $http.post('/api/user/findById', JSON.stringify($scope.currUser.id))
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

        var challengeParams = {};
        challengeParams.currUserID = $scope.currUser.id+"";
        challengeParams.opponentID = opponent.id+"";
        challengeParams.duration = t+"";
        challengeParams.stake = s+"";

        $http.post('/api/challenge/saveChallenge',JSON.stringify(challengeParams))
            .success(function (data, status) {
                if(status = 200){
                    console.log($scope.currUser.username,"vs",opponent.username,"Challenge Saved");
                    console.log(data);
                    $scope.challID = data.id;
                    $scope.waitForReply($scope.challID);

                }
            }).error(function (error) {
            console.log("something went wrong in saveChallenge!!");
        });

    };

    $scope.waitForReply = function(id){
        console.log("inside waitForReply ",id);
        $http.post('/api/challenge/waitForReply',id)
            .success(function (data, status) {
                if(status = 200){

                    console.log("challenge accepted  ",data.accepted)
                    if(data.accepted){
                        $state.go('trade',{challengeID: data.id});
                    }

                }
            }).error(function (error) {
            console.log("something went wrong in waitForReply!!");
        });

    };
    $interval( function(){ $scope.waitForReply($scope.challID); }, 3000);


    var name="";
    if(!$cookieStore.get('userCookie')){
        name = "xyz";
    }else{
        name = $cookieStore.get('userCookie').username
    }

    $scope.init = function(){
        $http.post('/api/user/onlineUsers',$scope.currUser)
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
});
