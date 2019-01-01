
//服务层
app.service("brandService", function ($http) {
    //查询所有
    this.findAll = function () {
        return $http.get("../brand/findAll.do");
    };

    //分页
    this.findPage = function (page, size) {
        return $http.get('../brand/findPage.do?page=' + page + '&size=' + size + '');
    }


    //保存brand
    this.saveBrand=function (entity) {
        return $http.post('../brand/saveBrand.do?', entity);
    }

    //修改数据
    this.updateBrand=function (entity) {
        return $http.post('../brand/updateBrand.do', entity)
    }

    //删除数据
    this.deleteBrand=function (ids) {
        return $http.post('../brand/deleteBrand.do?ids=' + ids + '')
    }

    //条件查询
    this.searchBrand=function (page,size,searchEntity) {
        return $http.post('../brand/searchPage.do?page=' + page + '&size=' + size + '', searchEntity)
    }






});