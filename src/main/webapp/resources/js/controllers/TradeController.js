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
                    }
                }).error(function (error) {
                console.log("something went wrong in findById -> TradeController!!");
            });
        }
    };
    $scope.setUser();


    /**
     * Position size, margin and leverage section
     */

    /**
     * Increment/Decrement function (No Problems with these)
     */
    $scope.position = 2500;
    var max = 5000000;
    var min = 2500;
    $scope.reqSym="";

    $scope.increment = function() {
        if ($scope.position >= max) { return; }
        $scope.position +=2500;

        $scope.marginRequiredTradedCurrency = $scope.position/$scope.leverage;
        $scope.marginRequiredTradedCurrencyView = $scope.marginRequiredTradedCurrency.toFixed(2);

        $scope.convertRequiredMarginToUSD($scope.pairChosenSym);

    };
    $scope.decrement = function() {
        if ($scope.position <= min) { return; }
        $scope.position -=2500;

        /**
         * Based on users chosen position size, I will calc the amount of margin required
         * in the chosen currency base pair
         */
        $scope.marginRequiredTradedCurrency = $scope.position/$scope.leverage;
        $scope.marginRequiredTradedCurrencyView = $scope.marginRequiredTradedCurrency.toFixed(2);

        /**
         * I will now convert the margin from above to USD (account currency)
         */
        $scope.convertRequiredMarginToUSD($scope.pairChosenSym);

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
            $scope.marginRequiredUSD = $scope.marginRequiredTradedCurrency;
            $scope.marginRequiredUSDView = $scope.marginRequiredUSD.toFixed(2);
        }


    };

    $scope.getThisConversionPair = function(conversionPair){
        $http.post('/api/trade/getThisPair',conversionPair)
            .success(function (data, status) {
                if(status = 200){
                    $scope.marginConversionPairAsk = data.ask;
                    $scope.marginRequiredUSD = $scope.marginRequiredTradedCurrency *  $scope.marginConversionPairAsk;
                    $scope.marginRequiredUSDView = $scope.marginRequiredUSD.toFixed(2);
                }
            }).error(function (error) {
            console.log("something went wrong in $scope.getThisConversionPair!!");
        });
    };


    /**
     * Check if the game is a solo trade or part of a challenge
     * if part of a challenge, change the balance to the stake
     */
    var theVar = $stateParams.challengeID;
    console.log("got into trade ",theVar);
    $scope.currentChallenge={}

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

        console.log("inside setTrade , stake is ",theStake);


        console.log("theVar is : ",theVar);

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

        $scope.leverage = 300;
    };







    /**
     * Values get initialized now
     */
    $scope.mMargin=0;
    $scope.marginRequiredTradedCurrency = $scope.position/$scope.leverage;
    $scope.marginRequiredTradedCurrencyView = $scope.marginRequiredTradedCurrency.toFixed(2);




    /**
     * Find open trades to alter the TradeTable view (Working, possible bug)
     */
    $scope.init = function(){
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

    $scope.checkForOpenTrades= function (pair) {
        function findPair(currentPair) {
            return currentPair.currencyPairOpen.symbols === pair.symbols;
        }
        var found = $scope.openTrades.find(findPair);
        if(found){
            return true;
        }
        return false;
    };

    $scope.init();
    $interval( function(){ $scope.init(); }, 3000);



    /**
     * All the toggle section is concerned with it the button pressed (buy/sell) and the pair chosen (100% working)
     */

    $scope.toggleLeft = buildToggler('left');
    $scope.toggleRight = buildToggler('right');
    function buildToggler(componentId) {
        return function(x,y) {

            $scope.action = y;
            $scope.pairChosen =x;
            $scope.pairChosenSym = $scope.pairChosen.symbols;

            $scope.reqSym = $scope.pairChosenSym.charAt(0)+$scope.pairChosenSym.charAt(1)+$scope.pairChosenSym.charAt(2);
            $scope.convertRequiredMarginToUSD($scope.pairChosenSym);

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
        tradeObject.pairSymbols = $scope.pairChosen.symbols;
        tradeObject.margin = $scope.marginRequiredUSD+"";
        tradeObject.action = $scope.action;
        $http.post('/api/trade/saveTrade',JSON.stringify(tradeObject));


        /**
         * 1st POST TRADE UPDATE OF AVAILABLE
         * @type {number}
         */

        $scope.available = $scope.available - $scope.marginRequiredUSD;
        $scope.availableView = $scope.available.toFixed(2);

        $scope.mMargin += ($scope.marginRequiredUSD/2);
        $scope.mMarginView  = $scope.mMargin.toFixed(2);


        $scope.openBuyRate = $scope.pairChosen.ask;
        $scope.openSellRate = $scope.pairChosen.bid;


        if($scope.pairChosenSym.match("/USD")){

            $scope.watch("/USD");

        }else if($scope.pairChosenSym.match("USD/")){

            $scope.watch("USD/");

        }else{
            $scope.watch("cross")
        }
    };






    $scope.closeLiveTrade = function(x){

        var closeParams = {};
        closeParams.id = $scope.currUser.id+"";
        closeParams.sym = x.symbols;
        closeParams.profitAndLoss = $scope.profitAndLoss+"";
        $http.post('/api/trade/closeLiveTrade',JSON.stringify(closeParams));
    };

    $scope.closeGameTrade = function(x){

        var closeParams = {};
        closeParams.id = $scope.currUser.id+"";
        closeParams.sym = x.symbols;
        closeParams.profitAndLoss = $scope.profitAndLoss+"";
        $http.post('/api/trade/closeGameTrade',JSON.stringify(closeParams));
    };



    $scope.watch = function(param){
        $scope.watchMarkets = function(){


            /**
             * $scope.pairChosenSym comes in when 'buy' or 'sell' is pressed
             */
            $http.post('/api/trade/getThisPair',$scope.pairChosenSym)
                .success(function (data, status) {
                    if(status = 200){


                        $scope.pairChosen = data;

                        $scope.positionValue = $scope.position + $scope.profitAndLoss;
                        $scope.positionValueView = $scope.positionValue.toFixed(2);

                        console.log("positionValueView : ",$scope.positionValueView);
                        $scope.convertPositionValueToAccountCurrency();


                    }
                }).error(function (error) {
                console.log("something went wrong in pairs call inside watch markets !!");
            });
            $scope.calculatePositions(param);
        };
        $interval( function(){ $scope.watchMarkets(); }, 3000);
        $scope.calculatePositions(param);
    };


    $scope.convertPositionValueToAccountCurrency = function(){

        var conversionpair = "";

        if($scope.pairChosenSym.match("EUR/")){
            conversionPair ="EUR/USD";

        }else if($scope.pairChosenSym.match("GBP")){
            conversionPair ="GBP/USD";
        }else if($scope.pairChosenSym.match("AUD")){
            conversionPair ="AUD/USD";
        }else if($scope.pairChosenSym.match("NZD")){
            conversionPair ="NZD/USD";
        }else if($scope.pairChosenSym.match("USD/")){
            conversionpair = "EUR/USD"
            $scope.accountCurrency = true;
        }

        $http.post('/api/trade/getThisPair',conversionPair)
            .success(function (data, status) {
                if(status = 200){

                    $scope.conversionPairResult = data;

                    if($scope.accountCurrency==true){
                        $scope.valueInAccountCurrency = $scope.positionValue * 1;
                        $scope.valueInAccountCurrencyView = $scope.valueInAccountCurrency.toFixed(2);
                    }else{
                        $scope.valueInAccountCurrency = $scope.positionValue * $scope.conversionPairResult.ask;
                        $scope.valueInAccountCurrencyView = $scope.valueInAccountCurrency.toFixed(2);
                    }
                }
            }).error(function (error) {
            console.log("something went wrong in pairs call inside watch markets !!");
        });
    };

    $scope.calculatePositions = function(param){

        $scope.closeSellRate = $scope.pairChosen.bid;
        $scope.closeAskRate = $scope.pairChosen.ask;

        if(param.match("/USD")){
            $scope.directQuoteCalc($scope.action);
        }else if(param.match("USD/")){
            $scope.indirectQuoteCalc($scope.action);
        }else if(param.match("cross")){
            $scope.crossQuoteCalc($scope.pairChosen,$scope.action);
        }
    };



    $scope.directQuoteCalc = function(action){

        $scope.longPipDifference = $scope.closeSellRate - $scope.openBuyRate;
        $scope.shortPipDifference = $scope.openSellRate - $scope.closeAskRate;

        if(action=='buy'){
            $scope.profitAndLoss = ( $scope.longPipDifference * $scope.position);
            $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
            $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.marginRequiredUSD));
            $scope.equityView = ($scope.equity.toFixed(2));

        }else if(action=='sell'){
            $scope.profitAndLoss = ($scope.shortPipDifference * $scope.position);
            $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
            $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.marginRequiredUSD));
            $scope.equityView = $scope.equity.toFixed(2);
        }
    };

    $scope.indirectQuoteCalc = function(action){

        $scope.longPipDifference = $scope.closeSellRate - $scope.openBuyRate;
        $scope.shortPipDifference = $scope.openSellRate - $scope.closeAskRate;

        if(action=='buy'){
            $scope.profitAndLoss = ( ($scope.longPipDifference *  $scope.position) / $scope.closeSellRate);
            $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
            $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.marginRequiredUSD));
            $scope.equityView = $scope.equity.toFixed(2);
        }else if(action=='sell'){
            $scope.profitAndLoss = (($scope.shortPipDifference * $scope.position)/$scope.closeAskRate);
            $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
            $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.marginRequiredUSD));
            $scope.equityView = $scope.equity.toFixed(2);
        }
    };



    $scope.crossQuoteCalc = function(pair,action){

        $scope.getParamForCrossQuoteCalc(pair.symbols);

        $scope.longPipDifference = $scope.closeSellRate - $scope.openBuyRate;
        $scope.shortPipDifference = $scope.openSellRate - $scope.closeAskRate;


        if(action=='buy'){
            $scope.getBase();

            $scope.setCrossProfit = function(){
                $scope.profitAndLoss = ($scope.longPipDifference * $scope.position *$scope.baseAsk);
                $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
                $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.marginRequiredUSD));
                $scope.equityView = $scope.equity.toFixed(2);
            }
        }else if(action=='sell'){

            $scope.getBase();

            $scope.setCrossProfit = function() {
                $scope.profitAndLoss = ($scope.shortPipDifference * $scope.position * $scope.baseBid);
                $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
                $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.marginRequiredUSD));
                $scope.equityView = $scope.equity.toFixed(2);
            }
        }
    };


    /**
     * Helper Methods for crossQuoteCalculations
     * getParamForCrossQuoteCalc
     * getBase
     */
    $scope.getBase = function(){
    $http.post('/api/trade/getThisPair',$scope.param)
        .success(function (data, status) {
            if(status = 200){

                $scope.baseAsk = data.ask;
                $scope.baseBid = data.bid;
                $scope.setCrossProfit();
            }
        }).error(function (error) {
    });
};


    $scope.getParamForCrossQuoteCalc = function(symbols){
        if(symbols=='EUR/GBP'){
            $scope.param = 'GBP/USD';
        }else if(symbols=='EUR/AUD'){
            $scope.param='AUD/USD';
        }else if(symbols=='EUR/NZD') {
            $scope.param = 'NZD/USD';
        }else if(symbols=='AUD/NZD') {
            $scope.param = 'NZD/USD';
        }
    };

});



