angular.module('myApp.WhatIsForexController',[]).
controller('WhatIsForexController',function($scope, $stateParams, $cookieStore, $http,$rootScope){



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
                console.log("something went wrong in findById -> WhatIsFOrexController!!");
            });
        }
    };
    $scope.setUser();





});