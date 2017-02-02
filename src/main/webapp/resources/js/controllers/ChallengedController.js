angular.module('myApp.ChallengedController',[]).
controller('ChallengedController', function($scope,$cookieStore,$http,$state,$stateParams,$timeout){

    $scope.challenged = {};
    $scope.counter = 60;


    // $scope.challengerPosSize

    $scope.currentUser = $cookieStore.get('userCookie');

    $scope.askedUserID = $stateParams.param;
    $scope.askedUserObject;

    $scope.pairs;
    $scope.direction = ["long","short"];
    $scope.stakes = ["100", "250","500","1000","2500","5000","10000"]
    $scope.leverages=[10,50,100,200,500];


    $http.post('http://localhost:8080/api/findById', $scope.askedUserID)
        .success(function (data, status) {
            if(status = 200){
                $scope.askedUserObject = data;
            }
        }).error(function (error) {
        alert("something went wrong!!");
    });



    $http.get('http://localhost:8080/api/pairs')
        .success(function (data, status) {
            if(status = 200){

                //data will be equal to the arraylist returned by the UserRestController
                console.log(data, "This is angalar pairs");
                $scope.pairs = data;

            }
        }).error(function (error) {
        console.log("something went wrong in the pairs controller init function!!");
    });//end http.get


    $scope.fight = function(){
        $scope.onTimeout = function(){
            $scope.counter--;
            mytimeout = $timeout($scope.onTimeout,1000);
            if($scope.counter==0){
                $timeout.cancel(mytimeout);
            }
        }
        var mytimeout = $timeout($scope.onTimeout,1000);

        // alert($scope.currentUser.username)
        // alert($challengedPair.symbols + " " + $scope.challengedPair.bid+ " "+$scope.challengedPair.ask )

        // $scope.challengedSym = $challengedPair.symbols
        /**
         * Get the players stakes in numeric format for calculations
         */
        var challengerStakeSelection = $scope.challengerStake
        var challengerStakeFloat = parseFloat(challengerStakeSelection)
        var challengedStakeSelection = $scope.challengedStake
        var challengedStakeFloat = parseFloat(challengedStakeSelection)

        /**
         * Going long, they pay the ask, so get the ask for both pairs
         */
        var challengerPairSelectionAsk = $scope.challengerPair.ask
        var challengerPairFloatAsk = parseFloat(challengerPairSelectionAsk)
        var challengedPairSelectionAsk = $scope.challengedPair.ask
        var challengedPairFloatAsk = parseFloat(challengedPairSelectionAsk)

        /**
         * get the leverage each player want to fight with
         */
        var challengerLev = $scope.challengerLeverage
        var challengedLev = $scope.challengedLeverage

        /**
         * get the direction (long/short) for each player
         */
        var challengerDir = $scope.challengerDirection
        var challengedDir = $scope.challengedDirection


        $scope.challengerPosSize = challengerStakeFloat * challengerPairFloatAsk * challengerLev;
        $scope.challengedPosSize = challengedStakeFloat * challengedPairFloatAsk * challengedLev;

        $scope.currentUser.account.balance = $scope.currentUser.account.balance - challengerStakeFloat
        $scope.askedUserObject.account.balance = $scope.askedUserObject.account.balance - challengedStakeFloat

    }

});
