//登录相关
app.controller('loginController',function ($scope,$controller,loginService) {

    $controller('baseController',{$scope:$scope});//继承



    //请求登录用户名
    $scope.loginName=function () {

        loginService.loginName().success(
            function (response) {
                $scope.username=response;

            }
        )
    }


})