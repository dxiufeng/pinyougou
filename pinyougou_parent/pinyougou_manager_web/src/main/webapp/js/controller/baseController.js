//品牌控制层
app.controller('baseController', function ($scope) {
    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            $scope.reloadList();//重新加载
        }
    };

    //重新加载列表 数据
    $scope.reloadList = function () {
        //切换页码
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };


    $scope.selectIds = [];//选中的ID集合
    //更新复选
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {//如果是被选中,则增加到数组
            $scope.selectIds.push(id);
        } else {
            var idx = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx, 1);//删除 
        }
    };


    //定义一个可以去任意json中数据的方法
    $scope.jsonToString = function (jsonString, key) {
        var value = '';
        var json = JSON.parse(jsonString);
        for (i = 0; i < json.length; i++) {
            if (i == 0) {
                value += json[i][key];
            } else {
                value += "," + json[i][key];
            }

        }

        return value;
    }





});