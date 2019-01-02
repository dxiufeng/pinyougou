app.controller("brandController", function ($scope, $http, brandService,$controller) {

    //引入父类的js文件
    $controller('baseController',{$scope:$scope});

    //查询所有
    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                $scope.pageResult = response;
            }
        )
    };



    //刷新页面
    $scope.reloadPage = function () {
        $scope.searchBrand($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage)
    };


    //分页
    $scope.findPage = function (page, size) {
        brandService.findPage(page, size).success(
            function (response) {
                $scope.list = response.rows;//显示当前的数据
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        )
    };

    //保存品牌
    $scope.saveBrand = function () {

        if ($scope.entity.id == null) {
            //若果id值是null,说明是想数据库中添加数据
            brandService.saveBrand($scope.entity).success(
                function (response) {
                    $scope.Result = response;
                    if ($scope.Result.flag) {
                        $scope.reloadPage();
                    } else {
                        alert($scope.Result.msg);
                    }

                }
            )
        } else {
            //如果id不是null,说明是想数据库中修改数据
            brandService.updateBrand($scope.entity).success(
                function (response) {
                    $scope.Result = response;
                    if ($scope.Result.flag) {
                        $scope.reloadPage();
                    } else {
                        alert($scope.Result.msg);
                    }
                }
            )

        }


    };

    //修改品牌
    $scope.changeBrand = function (brand) {
        //把原来的数据赋值到表格中,并发送ajax请求
        $scope.entity = brand;
    };

    //删除品牌:

    //3调用删除方法,把ids传入到后台
    $scope.deleteBrand = function () {
        brandService.deleteBrand($scope.ids).success(
            function (response) {
                $scope.Result = response;
                if ($scope.Result.flag) {
                    $scope.reloadPage();
                } else {
                    alert($scope.Result.msg);
                }
            }
        )
    };


    $scope.searchEntity = {};//初始化searchEntity,保证接收的不为null;
    //条件查询:
    $scope.searchBrand = function (page, size) {
        brandService.searchBrand(page, size,$scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;//显示当前的数据
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        )
    };




});