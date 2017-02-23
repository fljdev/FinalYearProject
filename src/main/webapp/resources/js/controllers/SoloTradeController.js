angular.module('myApp.SoloTradeController',[]).
controller('SoloTradeController',function($scope,$http,$state,$cookieStore,$interval,$mdSidenav){
    $scope.currUser = $cookieStore.get('userCookie');
    $scope.stakes = ["100", "250","500","1000","2500","5000","10000"];
    $scope.available = $scope.currUser.account.balance;
    $scope.leverage = 300;
    $scope.mMargin=0;

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

            console.log("sym : ",x, "dir : ", $scope.direction);
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


        $scope.currAsk = $scope.pairChosen.ask;
        console.log("ask is ",$scope.currAsk);
        $scope.currBid = $scope.pairChosen.bid;
        console.log("bid is ",$scope.currBid);


    }
});



