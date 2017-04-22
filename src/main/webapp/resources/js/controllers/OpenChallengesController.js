angular.module('myApp.OpenChallengesController',[]).
controller('OpenChallengesController', function($scope,$cookieStore,$http,$state,$rootScope){

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
        $http.post('/api/challenge/challengesSent',$rootScope.currentUser)
            .success(function (data, status) {
                if(status = 200){
                    $scope.sentChallenges = data;
                }
            }).error(function (error) {
            console.log("something went wrong in challengesSent !!");
        });

        $http.post('/api/challenge/challengesRecieved',$rootScope.currentUser)
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
    };

    $scope.decline = function(x){

        var obj = x;


        $http.post('/api/challenge/declineChallenge',obj.id)
            .success(function (data, status) {
            if(status = 200){

                console.log("x.id was no ",obj.id);
                console.log("withdraw challenge worked");
            }
        }).error(function (error) {
            console.log("something went wrong in withdraw challenge!!");
        });
    };


    $scope.accept = function(x){


        console.log("challenge that was accepted : ",x);
        $http.post('/api/challenge/acceptChallenge',x.id)
            .success(function (data, status) {
                if(status = 200){
                    var name = x.challengerName + " Vs " + x.opponentName;
                    console.log("x.id was no ",x.id);
                    swal("Let's Trade",name,"success");
                    $rootScope.openChallengesCount--;

                    $state.go('trade',{challengeID: x.id});


                }
            }).error(function (error) {
            console.log("something went wrong in accept challenge!!");
        });




        $http.post('/api/challenge/updateGameAccount',x.id)
            .success(function (data, status) {
                if(status = 200){
                    console.log("Updating GameAccount worked , x.id was no ",x.id);
                }
            }).error(function (error) {
            console.log("something went wrong in updateGameAccount!!");
        });
    };

    $scope.findMore = function(){
        $state.go('onlineUsers');
    };

    $scope.playNow = function(x){
        $state.go('trade',{challengeID: x.id});
    }
});
