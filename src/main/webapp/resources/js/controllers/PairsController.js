angular.module('myApp.PairsController',[]).
controller('PairsController', function($scope,$http){

    $scope.pairsMessage = "This is the pairs page";
    $scope.pairs = {};

    $scope.init = function(){
        $http.get('http://localhost:8080/api/pairs')
            .success(function (data, status) {
                if(status = 200){

                    //data will be equal to the arraylist returned by the UserRestController
                    console.log(data, "This is angalar pairs");
                    $scope.pairs = data;

                }
            }).error(function (error) {
            alert("something went wrong!!");

        });//end http.get
    }//end function

    $scope.init();

});//end controller