angular.module('myApp.PairsController',[]).
controller('PairsController', function($scope,$http,$interval){

    $scope.pairsMessage = "This is the pairs page";
    $scope.pairs = {};


    $scope.init = function(){
        $http.get('http://localhost:8080/api/user/pairs')
            .success(function (data, status) {
                if(status = 200){

                    //data will be equal to the arraylist returned by the UserRestController
                    console.log(data, "This is angalar pairs");
                    $scope.pairs = data;

                }
            }).error(function (error) {
            console.log("something went wrong in the pairs controller init function!!");



        });//end http.get
    }//end function

    $interval( function(){ $scope.init(); }, 4000);

    // $scope.init();

});//end controller