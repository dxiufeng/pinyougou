app.controller('searchController', function ($scope,$location,searchService) {

    //查询方法,通过solr进行查询,其中品牌和分类分别在redis中储存
    $scope.search = function () {
        $scope.searchMap.currentPage = parseInt($scope.searchMap.currentPage);

        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;
                $scope.buildPageLabel()//构建分页栏
            }
        )
    }


    //创建一个方法,用于操作面包屑导航栏
    $scope.searchMap = {
        'keywords': '',
        'category': '',
        'brand': '',
        'spec': {},
        'price': '',
        'currentPage': 1,
        'pageSize': 20,
        'sort': '',
        'sortField': ''

    }; //初始化数据格式

    //添加搜索条件
    $scope.addSearchItem = function (key, value) {
        if ('category' == key || 'brand' == key || 'price' == key) {
            //说明传入的数据是商品分类或者品牌分类
            $scope.searchMap[key] = value;
        } else {
            //说明传入的数据是别的,即规格分类
            $scope.searchMap.spec[key] = value;
        }
        //当进行修改后,调用search方法对后端进行访问
        $scope.search()
    }

    //删除搜索条件
    $scope.deleSearchItem = function (key) {
        if ('category' == key || 'brand' == key || 'price' == key) {
            $scope.searchMap[key] = '';
        } else {
            delete $scope.searchMap.spec[key];//移除此属性
        }
        //当进行修改后,调用search方法对后端进行访问
        $scope.search();
    };

    //分页
    //先定义一个数组,在更加返回值确定数组的长度,在ng-repeat 数据

    $scope.buildPageLabel = function () {
        $scope.pageLabel = [];//分页栏
        //定义页码显示:要求显示5页
        var firstPage = 1;
        var lastPage = $scope.resultMap.totalPage;
        $scope.firstDot = true;//导航栏前面有点
        $scope.lastDot = true;//有点

        if ($scope.resultMap.totalPage > 5) {
            //需要隐藏部分页码
            if ($scope.searchMap.currentPage <= 3) {//当前页小于5时,后面的分页条要隐藏
                firstPage = 1;
                lastPage = 5;
                $scope.firstDot = false;//前面无点
            } else if ($scope.searchMap.currentPage > ($scope.resultMap.totalPage - 2)) { //当前页接近最后一页是,前面的分页条要隐藏
                firstPage = $scope.resultMap.totalPage - 4;
                lastPage = $scope.resultMap.totalPage;
                $scope.lastDot = false;//无点
            } else {
                firstPage = $scope.searchMap.currentPage - 2;
                lastPage = $scope.searchMap.currentPage + 2;
            }
        } else {
            $scope.firstDot = false;//前面无点
            $scope.lastDot = false;//无点
        }

        for (var i = firstPage; i <= lastPage; i++) {
            $scope.pageLabel.push(i);
        }
    };

    //切换当前页
    $scope.queryByPage = function (currentPage) {
        //防止切换过头
        if (currentPage < 1 || currentPage > $scope.resultMap.totalPage) {
            return;
        }

        //切换当前页
        $scope.searchMap.currentPage = currentPage;
        //进行查询
        $scope.search();
    }

    //不可用样式,上一页
    $scope.isTopPage = function () {
        if ($scope.searchMap.currentPage == 1) {
            //上一页显示不可用
            return false
        }
        return true
    }
    //不可用样式,下一页
    $scope.isEndPage = function () {
        if ($scope.searchMap.currentPage == $scope.resultMap.totalPage) {
            //下一页显示不可用
            return false
        }
        return true
    }


    //排序
    $scope.sortSearch = function (sort, sortField) {
        $scope.searchMap.sort = sort;
        $scope.searchMap.sortField = sortField;
        $scope.search();
    }

    //根据关键字判断隐藏品牌列表
    //关键字包含品牌时,隐藏品牌信息
    $scope.keywordsIsBrand = function () {
        for (var i = 0; i < $scope.resultMap.brandList.length; i++) {
            if ($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text) >= 0) {
                return true; //即输入的关键字是品牌,要隐藏
            }

        }
        return false;
    }


    //接收从pinyougou_portal_web传过来的数据,并进行搜索查询
    $scope.loadkeywords=function () {
        $scope.searchMap.keywords = $location.search()['keywords'];
        $scope.search();
    }


});