angular.module('myApp.FightStartController',[]).
controller('FightStartController', function($scope,$cookieStore,$http,$state,$stateParams,$timeout){

    $scope.thisFight = {};
    $scope.currUser = $cookieStore.get('userCookie');
    $scope.opponent = {};

    /**
     * this is the id of the FightStart object that will be retrieved from the DB
     */
    var fightStartID = $stateParams.paramm;

    $scope.init = function(){
        $http.post('http://localhost:8080/api/fight/findFightObjectById',fightStartID)
            .success(function (data, status) {
                if(status = 200){
                    $scope.thisFight = data;
                    console.log($scope.thisFight.opponentID);
                    $scope.play();
                }
            }).error(function (error) {
            alert("something went wrong!!");
        });//end http.get
    }//end function
    $scope.init();


    $scope.getPercentLeverage = function(challLev,oppLev){
                if(challLev==400){
                    $scope.currMarginPercent = .25;
                }else if(challLev==200){
                    $scope.currMarginPercent = .50;
                }else if(challLev==100){
                    $scope.currMarginPercent = 1.0;
                }else if(challLev==50){
                    $scope.currMarginPercent = 2.0;
                }else if(challLev==33){
                    $scope.currMarginPercent = 3.0;
                }else if(challLev==20){
                    $scope.currMarginPercent = 5.0;
                }

                if(oppLev==400){
                    $scope.askedMarginPercent = .25;
                }else if(oppLev==200){
                    $scope.askedMarginPercent = .50;
                }else if(oppLev==100){
                    $scope.askedMarginPercent = 1.0;
                }else if(oppLev==50){
                    $scope.askedMarginPercent = 2.0;
                }else if(oppLev==33){
                    $scope.askedMarginPercent = 3.0;
                }else if(oppLev==20){
                    $scope.askedMarginPercent = 5.0;
                }
    }

    $scope.play = function(){
        console.log($scope.thisFight);
        $scope.currUsername = $scope.currUser.username;

        $http.post('http://localhost:8080/api/user/findById',$scope.thisFight.opponentID)
            .success(function (data, status) {
                if(status = 200){
                    console.log(data.username);
                    $scope.oppUsername = data.username;
                }
            }).error(function (error) {
            alert("something went wrong in find by id!!");
        });//end http.get


        /**
         * This section is the static variables, they will not change as the game progresses
         */
        $scope.mMargin = $scope.thisFight.challengerStake/2;
        $scope.currPair = $scope.thisFight.pairs[0].symbols;
        $scope.currDir = $scope.thisFight.challengerDirection;
        $scope.oppPair = $scope.thisFight.pairs[1].symbols;
        $scope.oppDir = $scope.thisFight.opponentDirection;

        /**
         * both players chosen leverages will be passed to a function which will return the % leverage of their position
         */
        $scope.getPercentLeverage($scope.thisFight.challengerLeverage, $scope.thisFight.opponentLeverage);

        /**
         * Stake is shared between both players (they must play for the same amount
         */
        $scope.stake = $scope.thisFight.challengerStake;

        /**
         * Challenger Variables
         */
        $scope.currUserCurrentPos = $scope.stake * $scope.thisFight.challengerLeverage * $scope.thisFight.pairs[0].ask;
        $scope.currUserSellValue =  $scope.stake * $scope.thisFight.challengerLeverage * $scope.thisFight.pairs[0].bid;
        $scope.currPL =  ($scope.currUserSellValue-$scope.currUserCurrentPos);
        $scope.currBalMinusStake = ($scope.thisFight.challengerBalance - $scope.thisFight.challengerStake);
        $scope.currAvailable = ($scope.currBalMinusStake + $scope.currPL);
        $scope.currEquity = ($scope.currPL + $scope.currAvailable + $scope.thisFight.challengerStake);

        $scope.currUserCurrentPosView = $scope.currUserCurrentPos.toFixed(2);
        $scope.currPLView = $scope.currPL.toFixed(2);
        $scope.currAvailableView =  $scope.currAvailable.toFixed(2);
        $scope.currEquityView = $scope.currEquity.toFixed(2);

        /**
         * Opponend Variables
         */
        $scope.opponentCurrentPos = $scope.stake * $scope.thisFight.opponentLeverage * $scope.thisFight.pairs[1].ask;
        $scope.opponentSellValue =  $scope.stake * $scope.thisFight.opponentLeverage * $scope.thisFight.pairs[1].bid;
        $scope.oppPL = ($scope.opponentSellValue - $scope.opponentCurrentPos);
        $scope.oppBalMinusStake = $scope.thisFight.opponentBalance - $scope.thisFight.challengerStake ;
        $scope.oppAvailable = ($scope.oppBalMinusStake + $scope.oppPL);
        $scope.oppEquity = ($scope.oppPL + $scope.oppAvailable + $scope.thisFight.challengerStake);

        $scope.opponentCurrentPosView = $scope.opponentCurrentPos.toFixed(2);
        $scope.oppPLView = $scope.oppPL.toFixed(2);
        $scope.oppAvailableView = $scope.oppAvailable.toFixed(2);
        $scope.oppEquityView = $scope.oppEquity.toFixed(2);

    }//end play







    // // $scope.updatePriceChanges = function(){
    // //
    // //     $scope.counter = 10;
    // //
    // //     $scope.onTimeout = function(){
    // //         $scope.counter--;
    // //         mytimeout = $timeout($scope.onTimeout,1000);
    // //
    // //         $scope.initPairs();
    // //
    // //         function findLatestQuote(currentCurrency) {
    // //             return currentCurrency.symbols === $scope.currentUserFinalPair.symbols;
    // //         }//end findLatestPosition
    // //
    // //
    // //         $scope.currUserLatestPositon =  $scope.pairs.find(findLatestQuote);
    // //         $scope.askedUserLatestPositon =  $scope.pairs.find(findLatestQuote);
    // //
    // //         $scope.currUserPosSize = $scope.currUserStakeFloat * parseFloat($scope.currUserLatestPositon.ask) * $scope.currUserLev;
    // //         var x = $scope.currUserPosSize
    // //         var x4 = x.toFixed(4);
    // //         $scope.currUserPosSize = x4;
    // //         $scope.askedUserPosSize = $scope.askedUserStakeFloat * parseFloat($scope.askedUserLatestPositon.ask) * $scope.askedUserLev;
    // //         var y = $scope.askedUserPosSize
    // //         var y4 = y.toFixed(4);
    // //         $scope.askedUserPosSize = y4;
    // //
    // //

    // //
    // //         if($scope.counter==0){
    // //
    // //             $timeout.cancel(mytimeout);
    // //         }//end if
    // //     }//end onTimeout
    // //     var mytimeout = $timeout($scope.onTimeout,1000);
    // //
    // // }//end updatePriceChanges
    //


});//end controller
