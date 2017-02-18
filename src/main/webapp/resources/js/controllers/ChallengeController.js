angular.module('myApp.ChallengeController',[]).
controller('ChallengeController', function($scope,$cookieStore,$http,$state,$stateParams){



    $scope.currUser = $cookieStore.get('userCookie');

    $scope.init = function(){
        $scope.askedUserID = $stateParams.param;
        $http.post('http://localhost:8080/api/user/findById', $scope.askedUserID)
            .success(function (data, status) {
                if(status = 200){

                    $scope.askedUserX = data;
                    console.log("1",status,$scope.askedUserX);

                    $scope.pp();

                }
            }).error(function (error) {
            alert("something went wrong!!");


        });
    }
    $scope.init();


    $scope.pp = function(){
        console.log("2 ",$scope.askedUserX);


        console.log("3 ",$scope.currUser);
    }




});
