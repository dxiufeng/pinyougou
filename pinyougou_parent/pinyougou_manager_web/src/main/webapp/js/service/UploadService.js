app.service('uploadService',function ($http) {

    //上传图片
    this.uploadFile=function () {

        var formdata=new FormData();
        formdata.append('file',file.files[0]);//代码文件上传框的name,里面有很多图片


        //文件上传
        return $http({
            url:'../upload.do',
            method:'post',
            data:formdata,
            headers:{'Content-Type':undefined},//默认格式是json格式,写未指定,则就是指定上传格式是MultipartFile
            transformRequest: angular.identity

        })
    }

});