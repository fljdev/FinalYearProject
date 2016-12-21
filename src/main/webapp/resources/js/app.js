angular.module('myApp',[
    'ngRoute',
    'ngSanitize',
    'chart.js',
    'ui.router',
    'myApp.HomeController',
    'myApp.LoginController',
    'myApp.RegisterController',
    'myApp.WelcomeController'
]).
    config(function ($stateProvider,$urlRouterProvider) {

    $urlRouterProvider.otherwise("/welcome");

    $stateProvider
        .state('welcome',{
            url:"/welcome",
            templateUrl: "resources/js/views/Welcome.html",
            controller:"WelcomeController"
        })
        .state('home',{
            url:"/home/:varX",
            templateUrl: "resources/js/views/Home.html",
            controller:"HomeController"
        })
        .state('login',{
            url:"/login",
            templateUrl: "resources/js/views/Login.html",
            controller:"LoginController"
        })
        .state('register',{
            url:"/register",
            templateUrl: "resources/js/views/Register.html",
            controller:"RegisterController"
    });
});

