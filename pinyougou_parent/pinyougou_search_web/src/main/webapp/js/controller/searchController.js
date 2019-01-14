app.controller('searchController',function ($scope,searchService) {

    //查询方法,通过solr进行查询,其中品牌和分类分别在redis中储存
    $scope.search=function () {

        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap=response;
            }
        )
    }


    //创建一个方法,用于操作面包屑导航栏
    $scope.searchMap={'keywords':'','category':'','brand':'','spec':{}}; //初始化数据格式

    //添加搜索条件
    $scope.addSearchItem=function (key,value) {
        if('category'==key || 'brand' == key){
            //说明传入的数据是商品分类或者品牌分类
            $scope.searchMap[key]=value;
        }else {
            //说明传入的数据是别的,即规格分类
            $scope.searchMap.spec[key]=value;
        }
        //当进行修改后,调用search方法对后端进行访问
        $scope.search()
    }

    //删除搜索条件
    $scope.deleSearchItem=function (key) {
        if('category'==key || 'brand'==key){
            $scope.searchMap[key]='';
        }else {
            delete $scope.searchMap.spec[key];//移除此属性
        }
        //当进行修改后,调用search方法对后端进行访问
        $scope.search();
    }

});