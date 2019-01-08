//控制层
app.controller('goodsController', function ($scope, $controller, goodsService, uploadService, itemCatService, typeTemplateService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = goodsService.update($scope.entity); //修改
        } else {
            serviceObject = goodsService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    };
    //添加数据
    $scope.add = function () {
        $scope.entity.goodsDesc.introduction = editor.html();

        goodsService.add($scope.entity).success(
            function (response) {
                if (response.flag) {
                    //重新查询
                    alert(response.msg);
                    $scope.entity = {};
                    editer.html("");
                } else {
                    alert(response.msg);
                }
            }
        );
    };


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //上传文件
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(
            function (response) {
                if (response.flag) {
                    $scope.img_entity.url = response.msg;
                } else {
                    alert(response.msg)
                }
            }
        )
    }

    //添加定义方法,把图片添加到entity.goodsDesc.imagesItem中
    $scope.entity = {goods: {}, goodsDesc: {itemImages: []}};
    $scope.add_image_entity = function () {
        $scope.entity.goodsDesc.itemImages.push($scope.img_entity);

    }
    //删除图片
    $scope.dele_image_entity = function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index, 1);
    }


    //进行分目录选择一级
    $scope.selectItemCat1List = function (parentId) {
        itemCatService.findByParentId(parentId).success(
            function (response) {
                $scope.ItemCat1List = response;
            }
        )
    }

    //进行分目录选择二级newValue,和oldValue都是该属性itemCat的id值
    $scope.$watch('entity.goods.category1Id', function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.ItemCat2List = response;
            }
        )
    });

    //进行分目录选择三级
    $scope.$watch('entity.goods.category2Id', function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.ItemCat3List = response;

                /* $scope.getTypeId($scope.ItemCat3List);*/
            }
        )
    })

    //读取三级目录下的typeId
    /*$scope.getTypeId=function (itemCat3List) {
        $scope.typeId=itemCat3List[0].typeId;
    }*/
    //查询模板id
    $scope.$watch('entity.goods.category3Id', function (newValue, oldValue) {
        itemCatService.findOne(newValue).success(
            function (response) {
                $scope.entity.goods.typeTemplateId = response.typeId;

                /* $scope.getTypeId($scope.ItemCat3List);*/
            }
        )
    });


    $scope.$watch('entity.goods.typeTemplateId', function (newValue, oldValue) {
        typeTemplateService.findOne(newValue).success(
            function (response) {
                $scope.tbTypeTemplate = response;//返回一个tbTypeTemplate对象

                $scope.tbTypeTemplate.brandIds = JSON.parse($scope.tbTypeTemplate.brandIds);

                //扩展属性
                $scope.tbTypeTemplate.customAttributeItems = JSON.parse($scope.tbTypeTemplate.customAttributeItems)
            }
        )
    })


});
