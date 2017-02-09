angular.module('myApp.navheader',[]).
directive('navheader', function($cookieStore, $state, $http){
    return{
        restrict:'E',
        scope : {},
        controller: function ($scope) {

            $scope.logOut = function () {
                var obj = $cookieStore.get('userCookie');
                $cookieStore.remove('userCookie');

                $http.post('http://localhost:8080/api/user/logout', JSON.stringify(obj))
                    .success(function (data, status) {

                        if(status = 200){
                            $state.go('welcome');
                        }
                    }).error(function (error) {
                    alert("something went wrong!!");
                });
            };
        },
        templateUrl:'resources/js/views/NavHeader.html',
        replace:true
    }
});