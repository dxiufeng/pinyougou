app.controller("brandController", function ($scope, $http, brandService) {

    //查询所有
    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                $scope.pageResult = response;
            }
        )
    };

    /*分页配置信息*/
    $scope.paginationConf = {
        currentPage: 1, //当前页码
        totalItems: 10, //总记录数
        itemsPerPage: 10, //每页显示条数
        perPageOptions: [10, 20, 30, 40, 50], //分页选项
        onChange: function () {
            //当页码触发后自动变更的方法
            $scope.reloadPage();
        }

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
    //1在$scope中建立变量ids
    $scope.ids = [];
    //2创建方法,当选中是后,id值放入到ids中
    $scope.addId = function ($event, id) {
        if ($event.target.checked) {
            //如果选中了就向ids中添加数据
            $scope.ids.push(id);
        } else {
            //反之移除数据
            var index = $scope.ids.indexOf(id);
            $scope.ids.splice(index, 1);

        }


    };

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




})