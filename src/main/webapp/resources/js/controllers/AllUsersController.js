angular.module('myApp.AllUsersController',[]).
controller('AllUsersController', function($scope,$http){

     $scope.init = function(){
            $http.get('http://localhost:8080/api/user/allUsers')
                .success(function (data, status) {
                    if(status = 200){
                        //data will be equal to the arraylist returned by the UserRestController
                        $scope.allUsers = data;
                    }
                }).error(function (error) {
                alert("something went wrong!!");
            });//end http.get
    }//end function
    $scope.init();

});//end controller
