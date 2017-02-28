angular.module('myApp.TradeController',[]).
controller('TradeController',function($scope,$http,$state,$cookieStore,$interval,$mdSidenav){

    $scope.currUser = $cookieStore.get('userCookie');
    $scope.stakes = ["100", "250","500","1000","2500","5000","10000"];
    $scope.available = $scope.currUser.account.balance;
    $scope.leverage = 300;
    $scope.mMargin=0;



    $scope.init = function(){
        $http.get('/api/trade/pairs')
            .success(function (data, status) {
                if(status = 200){
                    $scope.pairs = data;
                }
            }).error(function (error) {
            console.log("something went wrong in the pairs controller init function!!");
        });
    }
    $scope.init();
    $interval( function(){ $scope.init(); }, 4000);





    $scope.toggleLeft = buildToggler('left');
    $scope.toggleRight = buildToggler('right');
    function buildToggler(componentId) {
        return function(x,y) {

            $scope.direction = y;
            $scope.pairChosen =x;
            $scope.pairChosenSym = $scope.pairChosen.symbols;

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


        $scope.available = $scope.available - $scope.currUserStake;

        $scope.mMargin += (($scope.currUserStake / 2));

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

        $scope.p = x;
        console.log("closing trade : ",$scope.p);
    }






    $scope.watch = function(param){
        $scope.watchMarkets = function(){

            $http.post('/api/trade/getThisPair',$scope.pairChosenSym)
                .success(function (data, status) {
                    if(status = 200){
                        $scope.pairChosen = data;
                    }
                }).error(function (error) {
                console.log("something went wrong in pairs call inside watch markets!!");
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

        $scope.lotSize = $scope.currUserStake * $scope.leverage;

        if(direction=='buy'){
            $scope.profitAndLoss = ( $scope.longPipDifference * $scope.lotSize);
            $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
            $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.currUserStake));
            $scope.equityView = ($scope.equity.toFixed(2));


        }else if(direction=='sell'){
            $scope.profitAndLoss = ($scope.shortPipDifference * $scope.lotSize);
            $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
            $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.currUserStake));
            $scope.equityView = $scope.equity.toFixed(2);
        }
    }

    $scope.indirectQuoteCalc = function(pair,direction){

        $scope.closeSellRate = pair.bid;
        $scope.closeAskRate = pair.ask;


        $scope.longPipDifference = $scope.closeSellRate - $scope.openBuyRate;
        $scope.shortPipDifference = $scope.openSellRate - $scope.closeAskRate;

        $scope.lotSize = $scope.currUserStake * $scope.leverage;

        if(direction=='buy'){
            $scope.profitAndLoss = ( ($scope.longPipDifference *  $scope.lotSize) / $scope.closeSellRate);
            $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
            $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.currUserStake));
            $scope.equityView = $scope.equity.toFixed(2);
        }else if(direction=='sell'){
            $scope.profitAndLoss = (($scope.shortPipDifference * $scope.lotSize)/$scope.closeAskRate);
            $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
            $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.currUserStake));
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
        $scope.lotSize = $scope.currUserStake * $scope.leverage;


        if(direction=='buy'){
            $scope.getBase();

            $scope.setCrossProfit = function(){
                $scope.profitAndLoss = ($scope.longPipDifference * $scope.lotSize *$scope.baseAsk);
                $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
                $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.currUserStake));
                $scope.equityView = $scope.equity.toFixed(2);
            }
        }else if(direction=='sell'){
            $scope.getBase();

            $scope.setCrossProfit = function() {
                $scope.profitAndLoss = ($scope.shortPipDifference * $scope.lotSize * $scope.baseBid);
                $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);
                $scope.equity = ($scope.available + $scope.profitAndLoss + parseFloat($scope.currUserStake));
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


