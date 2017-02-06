angular.module('myApp.ChallengedController',[]).
controller('ChallengedController', function($scope,$cookieStore,$http,$state,$stateParams,$timeout){

    $scope.challenged = {};

    $scope.direction = ["long","short"];
    $scope.stakes = ["100", "250","500","1000","2500","5000","10000"]
    $scope.leverages=[10,50,100,200,500];

    $scope.currUser = $cookieStore.get('userCookie');
    $scope.askedUserID = $stateParams.param;
    $scope.askedUser;




    $scope.pairs;
    $scope.initPairs = function(){
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
    }
    $scope.initPairs()



    $http.post('http://localhost:8080/api/findById', $scope.askedUserID)
        .success(function (data, status) {
            if(status = 200){
                $scope.askedUser = data;
            }
        }).error(function (error) {
        alert("something went wrong!!");
    });



    $scope.p1 = $scope.currUser.symbols;

    $scope.fight = function(){

        $scope.currentUserFinalPair = $scope.currUserPair ;
        $scope.askedUserFinalPair = $scope.askedUserPair ;


        /**
         * Get the players stakes in numeric format for calculations
         */
        $scope.currUserStakeFloat = parseFloat($scope.currUserStake)
        $scope.askedUserStakeFloat = parseFloat($scope.askedUserStake)

        /**
         * Going long, they pay the ask, so get the ask for both pairs
         */
        $scope.currUserPairFloatAsk = parseFloat($scope.currUserPair.ask)
        $scope.askedUserPairFloatAsk = parseFloat($scope.askedUserPair.ask)

        /**
         * get the leverage each player want to fight with
         */
        $scope.currUserLev = $scope.currUserLeverage
        $scope.askedUserLev = $scope.askedUserLeverage

        /**
         * get the direction (long/short) for each player
         */
        $scope.currUserDir = $scope.currUserDirection
        $scope.askedUserDir = $scope.askedUserDirection


        $scope.currUserPosSize = $scope.currUserStakeFloat * $scope.currUserPairFloatAsk * $scope.currUserLev;
        $scope.askedUserPosSize = $scope.askedUserStakeFloat * $scope.askedUserPairFloatAsk * $scope.askedUserLev;

        $scope.currUser.account.balance = $scope.currUser.account.balance - $scope.currUserStakeFloat
        $scope.askedUser.account.balance = $scope.askedUser.account.balance - $scope.askedUserStakeFloat


        $scope.updatePriceChanges();
    }

    $scope.updatePriceChanges = function(){

        $scope.counter = 10;

        $scope.onTimeout = function(){
            $scope.counter--;
            mytimeout = $timeout($scope.onTimeout,1000);

            $scope.initPairs();

            function findLatestQuote(currentCurrency) {
                return currentCurrency.symbols === $scope.currentUserFinalPair.symbols;
            }//end findLatestPosition


            $scope.currUserLatestPositon =  $scope.pairs.find(findLatestQuote);
            $scope.askedUserLatestPositon =  $scope.pairs.find(findLatestQuote);

            $scope.currUserPosSize = $scope.currUserStakeFloat * parseFloat($scope.currUserLatestPositon.ask) * $scope.currUserLev;
            $scope.askedUserPosSize = $scope.askedUserStakeFloat * parseFloat($scope.askedUserLatestPositon.ask) * $scope.askedUserLev;


            if($scope.counter==0){

                $timeout.cancel(mytimeout);
            }//end if
        }//end onTimeout
        var mytimeout = $timeout($scope.onTimeout,1000);

    }//end updatePriceChanges

});//end controller
