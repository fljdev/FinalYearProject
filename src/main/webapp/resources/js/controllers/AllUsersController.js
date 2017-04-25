angular.module('myApp.AllUsersController',[]).
controller('AllUsersController', function($scope,$http,$rootScope,$cookieStore,$state){

    /**
     * User user object from the Database, instead of the browser cookie (No Problems)
     */
    $scope.setUser = function(){
        $rootScope.currentUser = $cookieStore.get('userCookie');
        if($rootScope.currentUser){
            $http.post('/api/user/findById', JSON.stringify($rootScope.currentUser.id))
                .success(function (data, status) {
                    if(status = 200){
                        $cookieStore.put('userCookie', data);
                        $rootScope.currentUser = data;
                    }
                }).error(function (error) {
                console.log("something went wrong in findById -> AllUsersController!!");
            });
        }
    };
    $scope.setUser();

     $scope.init = function(){
            $http.get('/api/user/allUsers')
                .success(function (data, status) {
                    if(status = 200){
                        $scope.allUsers = data;
                        $scope.setGraph1(data);
                        $scope.getUserTrades()
                    }
                }).error(function (error) {
                console.log("something went wrong!!");
            });
    };
    $scope.init();



    $scope.getUserTrades = function(){
        $scope.trades = [];
        $scope.users=[];


        for(i=0;i<$scope.allUsers.length;i++){

            $http.post('/api/trade/findTradesByUser',JSON.stringify($scope.allUsers[i].id))
                .success(function (data, status) {
                    if(status = 200){
                        $scope.trades.push(data.length);
                    }
                }).error(function (error) {
                console.log("something went wrong!!");
            });
        }

        for(i=0;i<$scope.allUsers.length;i++){
            $scope.users.push($scope.allUsers[i].username);
        }
    };


    $scope.setGraph1 = function(data){
        $scope.labels=[];
        $scope.data=[];
        for(i = 0; i < data.length; i++){
            for( i = 0; i < data.length; i++){
                $scope.labels.push(data[i].username);
                $scope.data.push(data[i].account.balance);

            }
        }


    };

    $scope.getStats = function(){
        console.log("into method");
        $state.go('stats');
    }

});
