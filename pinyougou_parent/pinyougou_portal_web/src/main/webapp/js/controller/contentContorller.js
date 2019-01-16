app.controller('contentController', function ($scope, contentService) {

    $scope.findByCategoryId = function (categoryId) {
        contentService.findByCategoryId(categoryId).success(
            function (response) {

                    $scope.contentList=response;
            })
    }

    //跳转到搜索模块pinyougou_search_web
    $scope.search=function () {
        location.href='http://localhost:9104/search.html#?keywords='+$scope.keywords;
    }
});