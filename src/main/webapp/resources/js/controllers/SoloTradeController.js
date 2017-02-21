angular.module('myApp.SoloTradeController',[]).
controller('SoloTradeController',function($scope,$http,$state,$cookieStore,$interval,$mdSidenav){
    $scope.currUser = $cookieStore.get('userCookie');

    $scope.d = $scope.dir;

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
        return function(x) {
            $scope.p =x;
            $mdSidenav(componentId).toggle();
        };
    }
});



