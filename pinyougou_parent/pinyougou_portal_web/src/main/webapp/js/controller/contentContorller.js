app.controller('contentController', function ($scope, contentService) {

    $scope.findByCategoryId = function (categoryId) {
        contentService.findByCategoryId(categoryId).success(
            function (response) {

                    $scope.contentList=response;
            })
    }
});