angular.module('myApp.ChallengeController',[]).
controller('ChallengeController', function($scope,$cookieStore,$http,$state,$stateParams){



    $scope.askedUserID = $stateParams.param;
    console.log("got into challengeController", $scope.askedUserID);

});
