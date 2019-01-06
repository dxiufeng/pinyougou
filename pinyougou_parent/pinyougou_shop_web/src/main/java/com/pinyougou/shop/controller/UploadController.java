package com.pinyougou.shop.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import util.FastDFSClient;

/**
 * 上传图片,上传到需要专用的Storage服务器
 */

@RestController
public class UploadController {

    @Value("${FIlE_SERVICE_URL}")
    private String file_service_url;


    @RequestMapping("/upload")
    public Result upload( MultipartFile file){
        try {

            //获取文件的整个名称
            String originalFilename = file.getOriginalFilename();

            String[] split = originalFilename.split("\\.");
            String extName=split[split.length-1];


            //获取上传到图片服务器的客户端,需要加入配置文件,主要是要写明tracker_server=192.168.25.133:22122的ip地址
            FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");


            //把需要的文件上传,返回图片所在位置路径
            String fileId = client.uploadFile(file.getBytes(), extName);

            //拼接字符串形成url图片路径
            String url=file_service_url+fileId;

            return new Result(true,url);


        } catch (Exception e) {
            e.printStackTrace();

            return new Result(false,"上传失败");
        }

    }
}
