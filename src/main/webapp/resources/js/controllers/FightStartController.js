angular.module('myApp.FightStartController',[]).
controller('FightStartController', function($scope,$cookieStore,$http,$state,$stateParams,$timeout){

    $scope.thisFight = {};
    $scope.currUser = $cookieStore.get('userCookie');
    $scope.opponent = {};
    $scope.counter = 60;

    /**
     * this is the id of the FightStart object that will be retrieved from the DB
     */
    var fightStartID = $stateParams.paramm;

    $scope.init = function(){
        $http.post('http://localhost:8080/api/fight/findFightObjectById',fightStartID)
            .success(function (data, status) {
                if(status = 200){
                    $scope.thisFight = data;
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

        $scope.currUsername = $scope.currUser.username;

        $http.post('http://localhost:8080/api/user/findById',$scope.thisFight.opponentID)
            .success(function (data, status) {
                if(status = 200){
                    $scope.oppUsername = data.username;
                }
            }).error(function (error) {
            alert("something went wrong in find by id!!");
        });//end http.get


        $scope.onTimeout = function(){
            $scope.counter--;
            mytimeout = $timeout($scope.onTimeout,1000);
            if($scope.counter==0){
                $timeout.cancel(mytimeout);
            }

            if($scope.counter % 5 == 0){
                $scope.updatePairs();

            }
        }//end onTimeout
        var mytimeout = $timeout($scope.onTimeout,1000);

        console.log("edit Values called inside play function");
        $scope.editValues();
    }//end play

    $scope.editValues = function(){
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

        $scope.currAsk = $scope.thisFight.pairs[0].ask;
        console.log("curr ask is now ",$scope.currAsk);
        $scope.currBid = $scope.thisFight.pairs[0].bid;
        console.log("curr bid is now ",$scope.currBid);

        $scope.currUserCurrentPos = $scope.stake * $scope.thisFight.challengerLeverage * $scope.currAsk;
        $scope.currUserSellValue =  $scope.stake * $scope.thisFight.challengerLeverage * $scope.currBid;
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
    }


    $scope.updatePairs = function(){
        $http.post('http://localhost:8080/api/fight/getThisPair',$scope.thisFight.pairs[0].symbols)
            .success(function (data, status) {
                if(status = 200){
                    $scope.thisFight.pairs[0] = data;
                    console.log("editValues called inside updatePairs function by  currentUser");
                    $scope.editValues();
                }
            }).error(function (error) {
            alert("something went wrong in pairs!!");
        });//end http.get

        /**
         * millisecondsToWait ensures no concurrency error
         * @type {number}
         */
        var millisecondsToWait = 500;
        setTimeout(function() {

            $http.post('http://localhost:8080/api/fight/getThisPair',$scope.thisFight.pairs[1].symbols)
                .success(function (data, status) {
                    if(status = 200){
                        $scope.thisFight.pairs[1] = data;
                        console.log("editValues called inside updatePairs function by opponent");
                    }
                }).error(function (error) {
                alert("something went wrong in pairs!!");
            });//end http.get

        }, millisecondsToWait);
    }

});//end controller
