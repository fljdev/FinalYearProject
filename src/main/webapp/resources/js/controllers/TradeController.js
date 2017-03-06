angular.module('myApp.TradeController',[]).
controller('TradeController',function($scope,$http,$state,$cookieStore,$interval,$mdSidenav){

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



    $scope.position = 75000;
    var max = 5000000;
    var min = 2500;

    $scope.reqSym="";

    $scope.increment = function() {
        if ($scope.position >= max) { return; }
        $scope.position +=2500;
        $scope.marginRequired = $scope.position/$scope.leverage;
        $scope.marginRequiredView = $scope.marginRequired.toFixed(2);
        $scope.convertRequiredMarginUSD($scope.pairChosenSym);

    };
    $scope.decrement = function() {
        if ($scope.position <= min) { return; }
        $scope.position -=2500;
        $scope.marginRequired = $scope.position/$scope.leverage;
        $scope.marginRequiredView = $scope.marginRequired.toFixed(2);
        $scope.convertRequiredMarginUSD($scope.pairChosenSym);

    };









    // $scope.stakes = ["100", "250","500","1000","2500","5000","10000"];
    $scope.available = $scope.currUser.account.balance;
    $scope.availableView = $scope.available.toFixed(2);
    $scope.equity = $scope.available;
    $scope.equityView = ($scope.equity.toFixed(2));
    $scope.leverage = 300;
    $scope.mMargin=0;
    $scope.marginRequired = $scope.position/$scope.leverage;
    $scope.marginRequiredView = $scope.marginRequired.toFixed(2);





    $scope.convertRequiredMarginUSD = function(sym){
        console.log("sym came in as ",sym);
        if(sym.match("EUR/")){
            console.log("i neeed ask of EUR/USD");
            var symParam = "EUR/USD";
            $scope.getThisConversionPair(symParam);
        }else if(sym.match("GBP/")){
            console.log("i neeed ask of GBP/USD");
            var symParam = "GBP/USD";
            $scope.getThisConversionPair(symParam);
        }else if(sym.match("AUD/")){
            console.log("i neeed ask of AUD/USD");
            var symParam = "AUD/USD";
            $scope.getThisConversionPair(symParam);
        }else if(sym.match("NZD/")){
            console.log("i neeed ask of NZD/USD");
            var symParam = "NZD/USD";
            $scope.getThisConversionPair(symParam);
        }else if(sym.match("USD/")){
            $scope.marginRequiredAccount = $scope.marginRequired;
            $scope.marginRequiredAccountView = $scope.marginRequiredAccount.toFixed(2);
        }


    };

    $scope.getThisConversionPair = function(conversionPair){
        $http.post('/api/trade/getThisPair',conversionPair)
            .success(function (data, status) {
                if(status = 200){
                    $scope.marginConversionPairAsk = data.ask;
                    console.log("got this from post ",$scope.marginConversionPairAsk);
                    $scope.marginRequiredAccount = $scope.marginRequired *  $scope.marginConversionPairAsk;
                    $scope.marginRequiredAccountView = $scope.marginRequiredAccount.toFixed(2);
                }
            }).error(function (error) {
            console.log("something went wrong in $scope.getThisConversionPair!!");
        });
    }

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



    $scope.init();
    $interval( function(){ $scope.init(); }, 4000);

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



    $scope.toggleLeft = buildToggler('left');
    $scope.toggleRight = buildToggler('right');
    function buildToggler(componentId) {
        return function(x,y) {

            $scope.direction = y;
            $scope.pairChosen =x;
            $scope.pairChosenSym = $scope.pairChosen.symbols;

            $scope.reqSym = $scope.pairChosenSym.charAt(0)+$scope.pairChosenSym.charAt(1)+$scope.pairChosenSym.charAt(2);
            $scope.convertRequiredMarginUSD($scope.pairChosenSym);

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
        var tradeObject = {};

        tradeObject.playerID = $scope.currUser.id+"";
        tradeObject.pairSymbols = $scope.pairChosen.symbols;
        tradeObject.stake = $scope.currUserStake;
        tradeObject.action = $scope.direction;



        $http.post('/api/trade/saveTrade',JSON.stringify(tradeObject))
            .success(function (data, status) {
                if(status = 200){
                    console.log("save trade http.post worked");
                }
            }).error(function (error) {
            console.log("something went wrong in pairs call inside watch markets!!");
        });


        $scope.available = $scope.available - $scope.marginRequiredAccount;
        $scope.availableView = $scope.available.toFixed(2);

        $scope.mMargin += ($scope.marginRequiredAccount/2);
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
    }






    $scope.closeTrade = function(x){

        console.log("closing : ", x);
        var closeParams = {};
        closeParams.id = $scope.currUser.id+"";
        closeParams.sym = x.symbols;

        $http.post('/api/trade/closeTrade',JSON.stringify(closeParams))
            .success(function (data, status) {
                if(status = 200){
                    // $scope.pairChosen = data;
                    console.log("yey");
                }
            }).error(function (error) {
            console.log("something went wrong in closeTrade!!");
        });
    }



    $scope.watch = function(param){
        $scope.watchMarkets = function(){

            $http.post('/api/trade/getThisPair',$scope.pairChosenSym)
                .success(function (data, status) {
                    if(status = 200){
                        $scope.pairChosen = data;
                        console.log("again");
                    }
                }).error(function (error) {
                console.log("something went wrong in pairs call inside watch markets !!");
            });
            $scope.calculatePositions(param);
        }
        $interval( function(){ $scope.watchMarkets(); }, 4000);
        $scope.calculatePositions(param);
    }





    $scope.calculatePositions = function(param){
        if(param.match("/USD")){
            $scope.directQuoteCalc($scope.pairChosen,$scope.direction);
        }else if(param.match("USD/")){
            $scope.indirectQuoteCalc($scope.pairChosen,$scope.direction);
        }else if(param.match("cross")){
            $scope.crossQuoteCalc($scope.pairChosen,$scope.direction);
        }
    }



    $scope.directQuoteCalc = function(pair,direction){

        $scope.closeSellRate = pair.bid;
        $scope.closeAskRate = pair.ask;

        $scope.longPipDifference = $scope.closeSellRate - $scope.openBuyRate;
        $scope.shortPipDifference = $scope.openSellRate - $scope.closeAskRate;

        // $scope.lotSize = $scope.currUserStake * $scope.leverage;

        if(direction=='buy'){
            $scope.profitAndLoss = ( $scope.longPipDifference * $scope.position);
            $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
            $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.marginRequiredAccount));
            $scope.equityView = ($scope.equity.toFixed(2));


        }else if(direction=='sell'){
            $scope.profitAndLoss = ($scope.shortPipDifference * $scope.position);
            $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
            $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.marginRequiredAccount));
            $scope.equityView = $scope.equity.toFixed(2);
        }
    }

    $scope.indirectQuoteCalc = function(pair,direction){

        $scope.closeSellRate = pair.bid;
        $scope.closeAskRate = pair.ask;


        $scope.longPipDifference = $scope.closeSellRate - $scope.openBuyRate;
        $scope.shortPipDifference = $scope.openSellRate - $scope.closeAskRate;

        // $scope.lotSize = $scope.currUserStake * $scope.leverage;

        if(direction=='buy'){
            $scope.profitAndLoss = ( ($scope.longPipDifference *  $scope.position) / $scope.closeSellRate);
            $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
            $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.marginRequiredAccount));
            $scope.equityView = $scope.equity.toFixed(2);
        }else if(direction=='sell'){
            $scope.profitAndLoss = (($scope.shortPipDifference * $scope.position)/$scope.closeAskRate);
            $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
            $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.marginRequiredAccount));
            $scope.equityView = $scope.equity.toFixed(2);
        }
    }

    $scope.crossQuoteCalc = function(pair,direction){

        if(pair.symbols=='EUR/GBP'){
            $scope.param = 'GBP/USD';
        }else if(pair.symbols=='EUR/AUD'){
            $scope.param='AUD/USD';
        }else if(pair.symbols=='EUR/NZD') {
            $scope.param = 'NZD/USD';
        }else if(pair.symbols=='AUD/NZD') {
            $scope.param = 'NZD/USD';
        }

        $scope.closeSellRate = pair.bid;
        $scope.closeAskRate = pair.ask;

        $scope.longPipDifference = $scope.closeSellRate - $scope.openBuyRate;
        $scope.shortPipDifference = $scope.openSellRate - $scope.closeAskRate;
        // $scope.lotSize = $scope.currUserStake * $scope.leverage;


        if(direction=='buy'){
            $scope.getBase();

            $scope.setCrossProfit = function(){
                $scope.profitAndLoss = ($scope.longPipDifference * $scope.position *$scope.baseAsk);
                $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
                $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.marginRequiredAccount));
                $scope.equityView = $scope.equity.toFixed(2);
            }
        }else if(direction=='sell'){
            $scope.getBase();

            $scope.setCrossProfit = function() {
                $scope.profitAndLoss = ($scope.shortPipDifference * $scope.position * $scope.baseBid);
                $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
                $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.marginRequiredAccount));
                $scope.equityView = $scope.equity.toFixed(2);
            }
        }
    }

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
}


});



