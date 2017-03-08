angular.module('myApp.LogRegController',[]).
controller('LogRegController',function($scope,$http,$state,$cookieStore,$rootScope) {
    $(function () {

        $('#login-form-link').click(function (e) {
            $("#login-form").delay(100).fadeIn(100);
            $("#register-form").fadeOut(100);
            $('#register-form-link').removeClass('active');
            $(this).addClass('active');
            e.preventDefault();
        });
        $('#register-form-link').click(function (e) {
            $("#register-form").delay(100).fadeIn(100);
            $("#login-form").fadeOut(100);
            $('#login-form-link').removeClass('active');
            $(this).addClass('active');
            e.preventDefault();
        });

    });

    $scope.submitRegistration =function(){

        $http.post('/api/user/register', JSON.stringify($scope.register))
            .success(function (data, status) {
                if(status = 200){

                    $scope.register = data;
                    $state.go('home');
                    $cookieStore.put('userCookie',$scope.register);
                }
            }).error(function (error) {
            console.log("something went wrong!!");
        });
    };


    $scope.submitLogin=function(){

        $http.post('/api/user/login', JSON.stringify($scope.login))
            .success(function (data, status) {
                if(status = 200){

                    $scope.login = data;
                    $rootScope.loggedIn = true;

                    $state.go('home');

                    $cookieStore.put('userCookie',$scope.login);
                }
            }).error(function (error) {
            console.log("something went wrong!!");
        });
    };
});

