angular.module('myApp.ChallengeController',[]).
controller('ChallengeController', function($scope,$cookieStore,$http,$state,$stateParams,$timeout){



    $scope.currUser = $cookieStore.get('userCookie');
    $scope.askedUserID = $stateParams.param;

    $scope.init = function(){
        $http.post('http://localhost:8080/api/user/findById', $scope.askedUserID)
            .success(function (data, status) {
                if(status = 200){
                    $scope.askedUser = data;

                    $scope.createChallengeObject();

                }
            }).error(function (error) {
            alert("something went wrong!!");


        });
    }
    $scope.init();


    $scope.createChallengeObject = function(){
        // console.log("3 curr Id : ",$scope.currUser.id);
        // console.log("4 asked ID :  ",$scope.askedUser.id);

        $scope.idArray = {};
        $scope.idArray[0]=$scope.currUser.id;
        $scope.idArray[1]=$scope.askedUser.id;

        $http.post('http://localhost:8080/api/challenge/saveChallenge',JSON.stringify($scope.idArray))
            .success(function (data, status) {
                if(status = 200){
                    console.log($scope.currUser.username,"vs",$scope.askedUser.username,"Challenge Saved");
                }
            }).error(function (error) {
            alert("something went wrong in saveChallenge!!");
        });//end http.get


    }





});
