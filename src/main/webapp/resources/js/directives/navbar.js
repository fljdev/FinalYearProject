angular.module('myApp.navheader',[]).
directive('navheader', function($cookieStore, $state, $http,$rootScope,$interval){
    return{
        restrict:'E',
        scope : {},
        controller: function ($scope, $rootScope) {

            $rootScope.loggedIn = false;


            if($cookieStore.get('userCookie')){
                $rootScope.loggedIn = true;
                $rootScope.currentUser = $cookieStore.get('userCookie');
            }


            $scope.findChallenges = function(){
                // $http.post('/api/challenge/challengesSent',$rootScope.currentUser)
                //     .success(function (data, status) {
                //         if(status = 200){
                //             $scope.sentChallenges = data;
                //         }
                //     }).error(function (error) {
                //     console.log("something went wrong in challengesSent !!");
                // });

                $http.post('/api/challenge/challengesRecieved',$rootScope.currentUser)
                    .success(function (data, status) {
                        if(status = 200){
                            $scope.challengesRecieved = data;
                            console.log("in challengesReceived with ",$scope.challengesRecieved);

                            var openChallenges = 0;
                            for (i = 0; i < $scope.challengesRecieved.length; i++) {
                                if ($scope.challengesRecieved[i].open) {
                                    openChallenges++;
                                }
                            }

                            if(openChallenges>0){
                                $rootScope.liveChallenge = true;
                                $rootScope.openChallengesCount = openChallenges;
                                $.snackbar({content: $rootScope.openChallengesCount + " open challenges"});
                            }else{
                                $rootScope.liveChallenge=false;
                            }

                        }
                    }).error(function (error) {
                    console.log("something went wrong in recievedChallenged !!");
                });
            }//end function
            $interval( function(){ $scope.findChallenges(); }, 5000);



            $scope.logOutUser = function () {
                var obj = $cookieStore.get('userCookie');
                $cookieStore.remove('userCookie');

                $rootScope.loggedIn = false;


                $http.post('/api/user/logout', JSON.stringify(obj))
                    .success(function (data, status) {

                        if(status = 200){
                            console.log("logged out ",data);

                            $state.go('welcome');
                        }
                    }).error(function (error) {
                    console.log("something went wrong!!");
                });
            };
        },
        templateUrl:'resources/js/directives/NavHeader.html',
        replace:true
    }
});