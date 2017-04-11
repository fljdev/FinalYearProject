angular.module('myApp.TradeController',[]).
controller('TradeController',function($scope,$http,$state,$cookieStore,$interval,$mdSidenav,$stateParams){


    /**
     * User user object from the Database, instead of the browser cookie (No Problems)
     */
    $scope.setUser = function(){
        $scope.currUser = $cookieStore.get('userCookie');
        if($scope.currUser){
            $http.post('/api/user/findById', JSON.stringify($scope.currUser.id))
                .success(function (data, status) {
                    if(status = 200){
                        $cookieStore.put('userCookie', data);

                        $scope.thisUser = data;
                    }
                }).error(function (error) {
                console.log("something went wrong in findById -> TradeController!!");
            });
        }
    };
    $scope.setUser();

    /**
     * Find open trades to alter the TradeTable view (Working, possible bug)
     */
    $scope.getPairs = function(){
        $http.get('/api/trade/pairs')
            .success(function (data, status) {
                if(status = 200){
                    $scope.pairs = data;
                }
            }).error(function (error) {
            console.log("something went wrong in the pairs controller init function!!");
        });
    };

    $scope.showOpenTrades = function(){
        $scope.getPairs();
        $http.post('/api/trade/getOpenTrades',$scope.currUser.id)
            .success(function (data, status) {
                if(status = 200){
                    $scope.openTrades = data;
                }
            }).error(function (error) {
            console.log("something went wrong in getOpenTrades call!!");
        });
    };

    $scope.checkForOpenTrades= function (pair) {
        function findPair(currentPair) {
            return currentPair.currencyPairOpen.symbols === pair.symbols;
        }
        var trade = $scope.openTrades.find(findPair);
        if(trade){
            return trade;
        }
        return false;
    };

    $scope.showOpenTrades();
    $interval( function(){ $scope.showOpenTrades(); }, 3000);



    /**
     * Increment/Decrement function (No Problems with these)
     */
    var max = 5000000;
    var min = 2500;
    $scope.positionUnits = 2500;
    $scope.leverage = 100;
    $scope.mMargin=0;
    $scope.preTradeMarginRequiredTradedCurrency = $scope.positionUnits/$scope.leverage;
    $scope.preTradeMarginRequiredTradedCurrencyView = $scope.preTradeMarginRequiredTradedCurrency.toFixed(2);


    $scope.increment = function() {

        if ($scope.positionUnits >= max) { return; }
        $scope.positionUnits +=2500;

        $scope.preTradeMarginRequiredTradedCurrency = $scope.positionUnits/$scope.leverage;
        $scope.preTradeMarginRequiredTradedCurrencyView = $scope.preTradeMarginRequiredTradedCurrency.toFixed(2);
        /**
         * I will now convert the margin from above to USD (account currency)
         */
        $scope.convertRequiredMarginToUSD($scope.preTradePairChosen.symbols);

    };
    $scope.decrement = function() {

        if ($scope.positionUnits <= min) { return; }
        $scope.positionUnits -=2500;

        /**
         * Based on users chosen positionUnits size, I will calc the amount of margin required
         * in the chosen currency base pair
         */
        $scope.preTradeMarginRequiredTradedCurrency = $scope.positionUnits/$scope.leverage;
        $scope.preTradeMarginRequiredTradedCurrencyView = $scope.preTradeMarginRequiredTradedCurrency.toFixed(2);

        /**
         * I will now convert the margin from above to USD (account currency)
         */
        $scope.convertRequiredMarginToUSD($scope.preTradePairChosen.symbols);
    };

    $scope.convertRequiredMarginToUSD = function(sym){
        if(sym.match("EUR/")){
            var symParam = "EUR/USD";
            $scope.getThisConversionPair(symParam);
        }else if(sym.match("GBP/")){
            var symParam = "GBP/USD";
            $scope.getThisConversionPair(symParam);
        }else if(sym.match("AUD/")){
            var symParam = "AUD/USD";
            $scope.getThisConversionPair(symParam);
        }else if(sym.match("NZD/")){
            var symParam = "NZD/USD";
            $scope.getThisConversionPair(symParam);
        }else if(sym.match("USD/")){
            console.log("got USD/");
            $scope.preTradeMarginRequiredUSD = $scope.preTradeMarginRequiredTradedCurrency;
            $scope.preTradeMarginRequiredUSDView = $scope.preTradeMarginRequiredUSD.toFixed(2);
        }
    };

    $scope.getThisConversionPair = function(conversionPair){
        $http.post('/api/trade/getThisPair',conversionPair)
            .success(function (data, status) {
                if(status = 200){
                    $scope.preTradeMarginConversionPairAsk = data.ask;
                    $scope.preTradeMarginRequiredUSD = $scope.preTradeMarginRequiredTradedCurrency *  $scope.preTradeMarginConversionPairAsk;
                    $scope.preTradeMarginRequiredUSDView = $scope.preTradeMarginRequiredUSD.toFixed(2);
                }
            }).error(function (error) {
            console.log("something went wrong in $scope.getThisConversionPair!!");
        });
    };


    /**
     * Worry about this section (challenge) as soon as multiple trades allowed in soo trade works!!
     */
    /**
     * Check if the game is a solo trade or part of a challenge
     * if part of a challenge, change the balance to the stake
     */
    var theVar = $stateParams.challengeID;
    $scope.currentChallenge={};
    $http.post('/api/challenge/findChallengeById',theVar)
        .success(function (data, status) {
            if(status = 200){
                $scope.currentChallenge = data;
                console.log("current challenge is : ",$scope.currentChallenge);

                theStake = $scope.currentChallenge.stake;

                $scope.setTradeVariables();

            }
        }).error(function (error) {
        console.log("something went wrong in  findChallengeByID!!");
    });
    /**
     * INITIAL Top table values ""**BEFORE TRADE**"" (No Problems with these)
     */
    $scope.check =false;


    $scope.setTradeVariables = function(){

        if(theVar>0){
            $scope.available = theStake;
            $scope.check = true;
        }else{
            $scope.available = $scope.currUser.account.balance;
            $scope.check=false;

        }

        $scope.availableView = $scope.available.toFixed(2);
        $scope.equity = $scope.available;
        $scope.equityView = ($scope.equity.toFixed(2));

    };




    /**
     * All the toggle section is concerned with it the button pressed (buy/sell) and the pair chosen (100% working)
     */

    $scope.toggleLeft = buildToggler('left');
    $scope.toggleRight = buildToggler('right');
    function buildToggler(componentId) {
        return function(x,y) {

            $scope.preTradeAction = y;
            $scope.preTradePairChosen = x;
            $scope.preTradeBasePairChosenSymbols = x.symbols.charAt(0)+x.symbols.charAt(1)+x.symbols.charAt(2);

            $scope.convertRequiredMarginToUSD($scope.preTradePairChosen.symbols);

            $mdSidenav(componentId).toggle();
        };
    }
    $scope.closeToggleLeft = buildTogglerClose('left');
    function buildTogglerClose(componentId) {
        return function() {
            $mdSidenav(componentId).toggle();
        };
    }

























    $scope.trade = function(){

        $scope.closeToggleLeft();

        var tradeObject = {};

        tradeObject.playerID = $scope.currUser.id+"";
        tradeObject.pairSymbols=$scope.preTradePairChosen.symbols;
        tradeObject.margin = $scope.preTradeMarginRequiredUSD+"";
        tradeObject.action = $scope.preTradeAction;
        tradeObject.positionUnits = $scope.positionUnits;
        console.log("pos",tradeObject.positionUnits);


        $http.post('/api/trade/saveTrade',JSON.stringify(tradeObject))
            .success(function (data, status) {
                if(status = 200){
                    console.log("trade created and persisted ",data);
                    $scope.tradeObject = data;
                    $scope.thisUser=$scope.tradeObject.user;
                    $cookieStore.put('userCookie', $scope.thisUser);



                    $scope.updateTradeScreenHeader($scope.tradeObject);

                }
            }).error(function (error) {
            console.log("something went wrong in saveTrade");
        });

    };

    $scope.updateTradeScreenHeader = function(tradeObj){



        $scope.available = tradeObj.user.account.balance;
        $scope.availableView = $scope.available.toFixed(2);

        $scope.mMargin += (tradeObj.margin/2);
        $scope.mMarginView  = $scope.mMargin.toFixed(2);



        $scope.updateEachTrade();

    };

    $scope.updateEachTrade = function(){

        $http.post('/api/trade/updateEachTrade',JSON.stringify($scope.thisUser))
            .success(function (data, status) {
                if(status = 200){
                    console.log("lala ",data);
                    // for(i=0; i < data.length;i++){
                    //     console.log(data[i].action);
                    //     if(data[i].currencyPairOpen.symbols == $scope.sy ){
                    //
                    //         $scope.pl = data[i].closingProfitLoss;
                    //         console.log($scope.sy);
                    //     }
                    // }
                    $scope.showOpenTrades();

                }
            }).error(function (error) {
            console.log("something went wrong in updateEachTrade");
        });

    };



        //
        // if($scope.pairChosenSym.match("/USD")){
        //
        //     $scope.watch("/USD");
        //
        // }else if($scope.pairChosenSym.match("USD/")){
        //
        //     $scope.watch("USD/");
        //
        // }else{
        //     $scope.watch("cross")
        // }
    // };




    $scope.closeLiveTrade = function(x){
        var closeParams = {};
        closeParams.id = $scope.thisUser.id+"";
        closeParams.sym = x.symbols;

        $http.post('/api/trade/closeLiveTrade',JSON.stringify(closeParams))

                .success(function (data, status) {
                    if(status = 200){
                        console.log("trade is ",data);

                        // $scope.tradeOn = false;
                    }
                }).error(function (error) {
                console.log("something went wrong in closeTrade!!");
            });

    };


    // $scope.closeLiveTrade = function(x){
    //
    //     var closeParams = {};
    //     closeParams.id = $scope.currUser.id+"";
    //     closeParams.sym = x.symbols;
    //     closeParams.profitAndLoss = $scope.profitAndLoss+"";
    //
    //     $http.post('/api/trade/closeLiveTrade',JSON.stringify(closeParams))
    //
    //         .success(function (data, status) {
    //             if(status = 200){
    //                 console.log("trade is ",data);
    //
    //                 $scope.tradeOn = false;
    //             }
    //         }).error(function (error) {
    //         console.log("something went wrong in closeTrade!!");
    //     });
    //
    //
    //     $scope.mMargin -= ($scope.marginRequiredUSD/2);
    //     $scope.mMarginView  = $scope.mMargin.toFixed(2);
    //     $scope.profitAndLoss=0;
    //     $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
    //
    //
    // };
//
//     $scope.closeGameTrade = function(x){
//
//         var closeParams = {};
//         closeParams.id = $scope.currUser.id+"";
//         closeParams.sym = x.symbols;
//         closeParams.profitAndLoss = $scope.profitAndLoss+"";
//         $http.post('/api/trade/closeGameTrade',JSON.stringify(closeParams));
//     };
//
//
//

















//     $scope.watch = function(param){
//         $scope.watchMarkets = function(){
//
//
//             /**
//              * $scope.pairChosenSym comes in when 'buy' or 'sell' is pressed
//              */
//             $http.post('/api/trade/getThisPair',$scope.pairChosenSym)
//                 .success(function (data, status) {
//                     if(status = 200){
//
//
//                         $scope.pairChosen = data;
//
//                         $scope.positionValue = $scope.position + $scope.profitAndLoss;
//                         $scope.positionValueView = $scope.positionValue.toFixed(2);
//
//                         console.log("positionValueView : ",$scope.positionValueView);
//                         $scope.convertPositionValueToAccountCurrency();
//
//
//                     }
//                 }).error(function (error) {
//                 console.log("something went wrong in pairs call inside watch markets !!");
//             });
//             $scope.calculatePositions(param);
//         };
//         $interval( function(){ $scope.watchMarkets(); }, 3000);
//         $scope.calculatePositions(param);
//     };
//
//
//     $scope.convertPositionValueToAccountCurrency = function(){
//
//         var conversionpair = "";
//
//         if($scope.pairChosenSym.match("EUR/")){
//             conversionPair ="EUR/USD";
//
//         }else if($scope.pairChosenSym.match("GBP")){
//             conversionPair ="GBP/USD";
//         }else if($scope.pairChosenSym.match("AUD")){
//             conversionPair ="AUD/USD";
//         }else if($scope.pairChosenSym.match("NZD")){
//             conversionPair ="NZD/USD";
//         }else if($scope.pairChosenSym.match("USD/")){
//             conversionpair = "EUR/USD"
//             $scope.accountCurrency = true;
//         }
//
//         $http.post('/api/trade/getThisPair',conversionPair)
//             .success(function (data, status) {
//                 if(status = 200){
//
//                     $scope.conversionPairResult = data;
//
//                     if($scope.accountCurrency==true){
//                         $scope.valueInAccountCurrency = $scope.positionValue * 1;
//                         $scope.valueInAccountCurrencyView = $scope.valueInAccountCurrency.toFixed(2);
//                     }else{
//                         $scope.valueInAccountCurrency = $scope.positionValue * $scope.conversionPairResult.ask;
//                         $scope.valueInAccountCurrencyView = $scope.valueInAccountCurrency.toFixed(2);
//                     }
//                 }
//             }).error(function (error) {
//             console.log("something went wrong in pairs call inside watch markets !!");
//         });
//     };
//
//     $scope.calculatePositions = function(param){
//
//         $scope.closeSellRate = $scope.pairChosen.bid;
//         $scope.closeAskRate = $scope.pairChosen.ask;
//
//         if(param.match("/USD")){
//             $scope.directQuoteCalc($scope.action);
//         }else if(param.match("USD/")){
//             $scope.indirectQuoteCalc($scope.action);
//         }else if(param.match("cross")){
//             $scope.crossQuoteCalc($scope.pairChosen,$scope.action);
//         }
//     };
//
//
//
//     $scope.directQuoteCalc = function(action){
//
//         $scope.longPipDifference = $scope.closeSellRate - $scope.openBuyRate;
//         $scope.shortPipDifference = $scope.openSellRate - $scope.closeAskRate;
//
//         if(action=='buy'){
//             $scope.profitAndLoss = ( $scope.longPipDifference * $scope.position);
//             $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
//             $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.marginRequiredUSD));
//             $scope.equityView = ($scope.equity.toFixed(2));
//             $scope.available +=$scope.profitAndLoss;
//             $scope.availableView=$scope.available.toFixed(2);
//
//         }else if(action=='sell'){
//             $scope.profitAndLoss = ($scope.shortPipDifference * $scope.position);
//             $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
//             $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.marginRequiredUSD));
//             $scope.equityView = $scope.equity.toFixed(2);
//             $scope.available +=$scope.profitAndLoss;
//             $scope.availableView=$scope.available.toFixed(2);
//         }
//     };
//
//     $scope.indirectQuoteCalc = function(action){
//
//         $scope.longPipDifference = $scope.closeSellRate - $scope.openBuyRate;
//         $scope.shortPipDifference = $scope.openSellRate - $scope.closeAskRate;
//
//         if(action=='buy'){
//             $scope.profitAndLoss = ( ($scope.longPipDifference *  $scope.position) / $scope.closeSellRate);
//             $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
//             $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.marginRequiredUSD));
//             $scope.equityView = $scope.equity.toFixed(2);
//             $scope.available +=$scope.profitAndLoss;
//             $scope.availableView=$scope.available.toFixed(2);
//         }else if(action=='sell'){
//             $scope.profitAndLoss = (($scope.shortPipDifference * $scope.position)/$scope.closeAskRate);
//             $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
//             $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.marginRequiredUSD));
//             $scope.equityView = $scope.equity.toFixed(2);
//             $scope.available +=$scope.profitAndLoss;
//             $scope.availableView=$scope.available.toFixed(2);
//         }
//     };
//
//
//
//     $scope.crossQuoteCalc = function(pair,action){
//
//         $scope.getParamForCrossQuoteCalc(pair.symbols);
//
//         $scope.longPipDifference = $scope.closeSellRate - $scope.openBuyRate;
//         $scope.shortPipDifference = $scope.openSellRate - $scope.closeAskRate;
//
//
//         if(action=='buy'){
//             $scope.getBase();
//
//             $scope.setCrossProfit = function(){
//                 $scope.profitAndLoss = ($scope.longPipDifference * $scope.position *$scope.baseAsk);
//                 $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
//                 $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.marginRequiredUSD));
//                 $scope.equityView = $scope.equity.toFixed(2);
//                 $scope.available +=$scope.profitAndLoss;
//                 $scope.availableView=$scope.available.toFixed(2);
//             }
//         }else if(action=='sell'){
//
//             $scope.getBase();
//
//             $scope.setCrossProfit = function() {
//                 $scope.profitAndLoss = ($scope.shortPipDifference * $scope.position * $scope.baseBid);
//                 $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
//                 $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.marginRequiredUSD));
//                 $scope.equityView = $scope.equity.toFixed(2);
//                 $scope.available +=$scope.profitAndLoss;
//                 $scope.availableView=$scope.available.toFixed(2);
//             }
//         }
//     };
//
//
//     /**
//      * Helper Methods for crossQuoteCalculations
//      * getParamForCrossQuoteCalc
//      * getBase
//      */
//     $scope.getBase = function(){
//     $http.post('/api/trade/getThisPair',$scope.param)
//         .success(function (data, status) {
//             if(status = 200){
//
//                 $scope.baseAsk = data.ask;
//                 $scope.baseBid = data.bid;
//                 $scope.setCrossProfit();
//             }
//         }).error(function (error) {
//     });
// };
//
//
//     $scope.getParamForCrossQuoteCalc = function(symbols){
//         if(symbols=='EUR/GBP'){
//             $scope.param = 'GBP/USD';
//         }else if(symbols=='EUR/AUD'){
//             $scope.param='AUD/USD';
//         }else if(symbols=='EUR/NZD') {
//             $scope.param = 'NZD/USD';
//         }else if(symbols=='AUD/NZD') {
//             $scope.param = 'NZD/USD';
//         }
//     };

});



