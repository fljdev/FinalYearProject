angular.module('myApp.ChallengeController',[]).
controller('ChallengeController', function($scope,$cookieStore,$http,$state,$stateParams,$timeout){



    $scope.currUser = $cookieStore.get('userCookie');
    $scope.askedUserID = $stateParams.param;

    $scope.init = function(){
        $http.post('/api/user/findById', $scope.askedUserID)
            .success(function (data, status) {
                if(status = 200){
                    $scope.askedUser = data;

                    $scope.createChallengeObject();

                }
            }).error(function (error) {
            console.log("something went wrong!!");


        });
    }
    $scope.init();


    $scope.createChallengeObject = function(){

        $scope.idArray = {};
        $scope.idArray[0]=$scope.currUser.id;
        $scope.idArray[1]=$scope.askedUser.id;

        $http.post('/api/challenge/saveChallenge',JSON.stringify($scope.idArray))
            .success(function (data, status) {
                if(status = 200){
                    console.log($scope.currUser.username,"vs",$scope.askedUser.username,"Challenge Saved");
                }
            }).error(function (error) {
            console.log("something went wrong in saveChallenge!!");
        });//end http.get


    }





});
