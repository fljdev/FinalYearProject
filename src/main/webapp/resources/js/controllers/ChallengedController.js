angular.module('myApp.ChallengedController',[]).
controller('ChallengedController', function($scope,$cookieStore,$http,$state,$stateParams,$timeout){

    $scope.direction = ["long","short"];
    $scope.stakes = ["100", "250","500","1000","2500","5000","10000"]
    $scope.leverages=["20","33","50","100","200","400"];

    $scope.currUser = $cookieStore.get('userCookie');
    $scope.askedUserID = $stateParams.param;

    $scope.pairs;
    $scope.initPairs = function(){
        $http.get('http://localhost:8080/api/fight/pairs')
            .success(function (data, status) {
                if(status = 200){
                    $scope.pairs = data;
                }
            }).error(function (error) {
            console.log("something went wrong in the pairs controller init function!!");
        });//end http.get
    }
    $scope.initPairs()

    $http.post('http://localhost:8080/api/user/findById', $scope.askedUserID)
        .success(function (data, status) {
            if(status = 200){
                $scope.askedUser = data;
            }
        }).error(function (error) {
        alert("something went wrong!!");
    });




    $scope.fight = function() {

        $scope.gameParamsObj.cId  = $scope.currUser.id +"";
        $scope.gameParamsObj.oId = $scope.askedUser.id +"";


        $scope.gameParamsObj.cPair=$scope.currUserPair.symbols;
        $scope.gameParamsObj.oPair=$scope.askedUserPair.symbols;

        $scope.gameParamsObj.cBalance = $scope.currUser.account.balance+"";
        $scope.gameParamsObj.oBalance = $scope.askedUser.account.balance+"";

        /**
         * Now I send the lev, long,stake and pair etc to the controller
         * an object will be make
         * the id of that object will be returned
         * then i will state.go to a new controller, taking the ID of the FightStart object with me
         */
        $http.post('http://localhost:8080/api/fight/makeFightStartObjAndReturnID', JSON.stringify($scope.gameParamsObj))
            .success(function (data, status) {
                if(status = 200){
                    console.log("makeFightStartObjAndReturnID method worked, the ID of FightStart obj is : ", data);
                    $state.go('fightStart',{paramm:data});
                }
            }).error(function (error) {
            alert("something went wrong!!");
        });
    }

});//end controller
