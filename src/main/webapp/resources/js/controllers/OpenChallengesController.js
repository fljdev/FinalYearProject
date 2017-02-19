angular.module('myApp.OpenChallengesController',[]).
controller('OpenChallengesController', function($scope,$cookieStore,$http,$state){

    $scope.currUser = $cookieStore.get('userCookie');

    $scope.openView = "this is openChallengesView";

    $scope.init = function(){
        $http.get('http://localhost:8080/api/challenge/challengesSent')
            .success(function (data, status) {
                if(status = 200){
                    $scope.challenges = data;
                }
            }).error(function (error) {
            alert("something went wrong in challengesSent !!");
        });//end http.get
    }//end function
    $scope.init();



});
