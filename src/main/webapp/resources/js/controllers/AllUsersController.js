angular.module('myApp.AllUsersController',[]).
controller('AllUsersController', function($scope,$http){

     $scope.init = function(){
            $http.get('/api/user/allUsers')
                .success(function (data, status) {
                    if(status = 200){
                        //data will be equal to the arraylist returned by the UserRestController
                        $scope.allUsers = data;
                    }
                }).error(function (error) {
                console.log("something went wrong!!");
            });//end http.get
    }//end function
    $scope.init();

});//end controller
