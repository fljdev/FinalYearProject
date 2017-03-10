angular.module('myApp.AllUsersController',[]).
controller('AllUsersController', function($scope,$http){

     $scope.init = function(){
            $http.get('/api/user/allUsers')
                .success(function (data, status) {
                    if(status = 200){
                        $scope.allUsers = data;
                    }
                }).error(function (error) {
                console.log("something went wrong!!");
            });
    };
    $scope.init();

});
