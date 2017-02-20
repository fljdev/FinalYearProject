angular.module('myApp.PairsController',[]).
controller('PairsController', function($scope,$http,$interval){

    $scope.pairsMessage = "This is the pairs page";
    $scope.pairs = {};


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

    // $scope.init();

});//end controller