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



    // /**
    //  * User user object from the Database, instead of the browser cookie (No Problems)
    //  */
    // $scope.setUser = function(){
    //     $rootScope.currentUser = $cookieStore.get('userCookie');
    //     if($rootScope.currentUser){
    //         $http.post('/api/user/findById', JSON.stringify($rootScope.currentUser.id))
    //             .success(function (data, status) {
    //                 if(status = 200){
    //                     $cookieStore.put('userCookie', data);
    //                     $rootScope.currentUser = data;
    //                 }
    //             }).error(function (error) {
    //             console.log("something went wrong in findById -> TradeController!!");
    //         });
    //     }
    // };

    $scope.submitRegistration =function(){

        $http.post('/api/user/register', JSON.stringify($scope.register))
            .success(function (data, status) {
                if(status = 200){

                    if(data==""){
                        console.log("api register call got back null");
                        swal("Your Form Contains errors","username or email not unique or passwords don't match","error");
                    }else{
                        console.log("api register call got back",data);

                        $rootScope.loggedIn = true;
                        $rootScope.currentUser = data;
                        $cookieStore.put('userCookie',$rootScope.currentUser);

                        var welcomeString = "Welcome to Forex Fighter "+$rootScope.currentUser.username;
                        swal(welcomeString," thanks for joining us ","success");

                        $state.go('home');
                    }

                }
            }).error(function (error) {
            console.log("something went wrong!!");
        });
    };


    $scope.submitLogin=function(){

        $http.post('/api/user/login', JSON.stringify($scope.login))
            .success(function (data, status) {
                if(status = 200){

                        $rootScope.loggedIn = true;

                        $rootScope.currentUser= data;
                        $cookieStore.put('userCookie',$rootScope.currentUser);

                        var welcomeString = "Welcome Back "+$rootScope.currentUser.username;
                        swal(welcomeString," we've missed you ","success");

                        $state.go('home');



                }
            }).error(function (error) {
            console.log("something went wrong!!");
        });
    };
});

