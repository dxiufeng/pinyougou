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



    /**
     * 规格选择和取消,规格绑定 $scope.specificationItems={};
     * @type {{}}
     */
    $scope.specificationItems={};//用户选择的数据
    $scope.selectSpecification=function (key,value) {
        $scope.specificationItems[key]=value;

        //每次点击规格触发查找方法
        $scope.searchSku();
}

    $scope.isSelected=function (key,value) {
        if ($scope.specificationItems[key]==value){
            return true;//说明已经选中了
        }else {
            return false;//说明没选中

        }

    }

    /**
     * 把sku商品中默认的展示信息,赋值给一个变量,
     * 当刷新时,可以初始化该变量,获取绑定的值
     *
     */

    $scope.sku={}//把skuList中 默认的数据赋值给$scope.sku
    $scope.loadSku=function () {
        $scope.sku = skuList[0];//设置默认值
     /*   var skuList = [
            {
                "id":1369282,
                "title": '精品半身裙（秋款打折） 移动4G 16G',
                "price":0.03,
                "spec":{"网络":"移动4G","机身内存":"16G"}
            },*/

        //把$scope.sku 中spec所对应的数据赋值给 $scope.specificationItems,用来绑定规格

        $scope.specificationItems=JSON.parse(JSON.stringify( $scope.sku.spec)) ;//克隆


    }

    /**
     * 检测两个对象是否相等
     */
    $scope.matchObject=function (m1,m2) {
        for (var k in m1){
            if(m1[k]!=m2[k]){
                return false;  //两个对象不等
            }
        }

        for (var k in m2 ){
            if (m2[k]!=m1[k]){
                return false
            }
        }

        //两个对象相等
        return true;
    };



    /**
     * 对skuList进行遍历,查询skuList中某个与specificationItems相等
     */

    $scope.searchSku=function () {
        for (var i=0; i<skuList.length;i++){
            if ($scope.matchObject(skuList[i].spec, $scope.specificationItems)){
                //说明用户选择的规格数据和数据库中查询出来的匹配上了
                //把该数据赋值给 $scope.sku={} 进行显示
                $scope.sku=skuList[i];
                break;
            }else {
                $scope.sku={id:0,title:'------',price:0}//没有匹配则显示0
            }

        }


    }

    /**
     * 加入购物车
     */
    
    $scope.addToCart=function () {
        alert($scope.sku.id);
    }

});