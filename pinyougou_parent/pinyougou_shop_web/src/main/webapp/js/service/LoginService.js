app.service('loginService',function ($http) {

    this.loginName=function () {
        return $http.get('../login/getLoginName.do');
    }


});