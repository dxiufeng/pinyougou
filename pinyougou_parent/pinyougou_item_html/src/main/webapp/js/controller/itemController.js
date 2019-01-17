app.controller('itemController', function ($scope) {
   /* $scope.number = 1;*/
    //商品数量的加减
    $scope.changeNum = function (num) {


        if ($scope.number < 1) {
            $scope.number = 1;//防止num小于1
        }
        //转成数字在进行运算(防止字符串+字符串发生)
        $scope.number = parseInt($scope.number );

        $scope.number=  $scope.number+num;
    };

    $scope.alt=function () {
        alert("aaaa");
    }


    /**
     * 规格选择和取消,规格绑定 $scope.specificationItems={};
     * @type {{}}
     */
    $scope.specificationItems={};
    $scope.selectSpecification=function (key,value) {
        $scope.specificationItems[key]=value;
    }

    $scope.isSelected=function (key,value) {
        if ($scope.specificationItems[key]==value){
            return true;//说明已经选中了
        }else {
            return false;//说明没选中

        }

    }
});