angular.module('myApp.SoloTradeController',[]).
controller('SoloTradeController',function($scope,$http,$state,$cookieStore,$interval,$mdSidenav){
    $scope.currUser = $cookieStore.get('userCookie');
    $scope.stakes = ["100", "250","500","1000","2500","5000","10000"];
    $scope.available = $scope.currUser.account.balance;
    $scope.leverage = 300;
    $scope.mMargin=0;

    $scope.openBuyRate=0;
    $scope.openSellRate=0;


    /**
     * init is called initially to fill the rows with currency data
     * then it is called again every four seconds to update
     * THIS SECTION WORKS 1000%!!!
     */
    $scope.init = function(){
        $http.get('/api/fight/pairs')
            .success(function (data, status) {
                if(status = 200){

                    //data will be equal to the arraylist returned by the UserRestController
                    $scope.pairs = data;

                }
            }).error(function (error) {
            console.log("something went wrong in the pairs controller init function!!");
        });//end http.get
    }//end function
    $interval( function(){ $scope.init(); }, 4000);


    /**
     *
     * SideNav Section
     */
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

        /**
         * reduce amount available each time trader opens a position
         */
        $scope.available = $scope.available - $scope.currUserStake;


        /**
         * Maintanance Margin is always half the original margin
         * Account equiry must exceed the mMargin level, or position is automatically closed
         * increased every time a trader opens a position
         */
        $scope.mMargin += (($scope.currUserStake / 2));


        /**
         * THIS SECTION, WORKS 100% for Direct Quotes (USD the counter currency)
         */

        if($scope.pairChosenSym.match("/USD")){

            if($scope.direction=='buy'){

                $scope.openBuyRate = $scope.pairChosen.ask;

            }else if ($scope.direction == 'sell'){

                $scope.openSellRate = $scope.pairChosen.bid;
            }

            $scope.watch();



        }else if($scope.pairChosenSym.match("USD/")){
            console.log($scope.pairChosenSym, " is an InDirect Quote");
        }else{
            console.log($scope.pairChosenSym, " is a Cross");
        }


    }


    /**
     * The watch and watchMarkets will update the bid/ask for the chosen currency pair
     */
    $scope.watch = function(){
        $scope.watchMarkets = function(){
            console.log("watching");

            $http.post('/api/fight/getThisPair',$scope.pairChosenSym)
                .success(function (data, status) {
                    if(status = 200){
                        $scope.pairChosen = data;
                    }
                }).error(function (error) {
                alert("something went wrong in pairs call inside watch markets!!");
            });//end http.get

            $scope.directQuoteCalc($scope.pairChosen,$scope.direction);



        }
        $interval( function(){ $scope.watchMarkets(); }, 4000);
    }




    $scope.directQuoteCalc = function(pair,direction){


        /**
         * curr ask and curr bid are used for calculating the P&L, whether (buy or sell)
         */
        $scope.currAsk = pair.ask;
        $scope.currBid = pair.bid;

        $scope.closeSellRate = $scope.currBid;

        $scope.profitAndLoss = ($scope.closeSellRate - $scope.openBuyRate) * ($scope.currUserStake * $scope.leverage);
        $scope.profitAndLossView = $scope.profitAndLoss.toFixed(2);

    }


});



