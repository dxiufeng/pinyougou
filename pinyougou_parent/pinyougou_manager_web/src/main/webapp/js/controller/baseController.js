app.controller('baseController',function ($scope) {


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
});