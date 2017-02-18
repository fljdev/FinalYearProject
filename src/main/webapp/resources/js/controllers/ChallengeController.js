angular.module('myApp.ChallengeController',[]).
controller('ChallengeController', function($scope,$cookieStore,$http,$state,$stateParams){



    $scope.currUser = $cookieStore.get('userCookie');
    $scope.askedUserID = $stateParams.param;

    $scope.init = function(){
        $http.post('http://localhost:8080/api/user/findById', $scope.askedUserID)
            .success(function (data, status) {
                if(status = 200){

                    $scope.askedUser = data;
                    console.log("1",status,$scope.askedUser);

                    $scope.pp();

                }
            }).error(function (error) {
            alert("something went wrong!!");


        });
    }
    $scope.init();


    $scope.pp = function(){
        console.log("2 ",$scope.askedUser);


        console.log("3 ",$scope.currUser);
    }




});
