angular.module('myApp.PairsController',[]).
controller('PairsController', function($scope,$http,$interval){

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
    $interval( function(){ $scope.init(); }, 3500);

});