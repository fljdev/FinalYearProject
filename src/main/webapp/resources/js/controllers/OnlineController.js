angular.module('myApp.OnlineController',[]).
    controller('OnlineController', function($scope,$cookieStore,$http,$state){

    $scope.gameTimes = [15,30,60];
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
        $http.post('/api/user/onlineUsers',name)
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
