package cn.itcast.core.controller;

import cn.itcast.common.utils.FastDFSClient;
import entity.Result;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片 管理
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/upload")
public class UploadController {


    //图片服务器ＵＲＬ
    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;

    //上传商品图片
    @RequestMapping("/uploadFile")
    public Result uploadFile(MultipartFile file){ //配置一个实现类


        //接收图片
        try {
            System.out.println(file.getOriginalFilename());

            //上传分布式文件系统 FastDFS
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fastDFS/fdfs_client.conf");

            //扩展名
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());

            //上传图片
            // group1/M00/00/01/wKjIgFWOYc6APpjAAAD-qk29i78248.jpg
            String path = fastDFSClient.uploadFile(file.getBytes(), ext, null);

            return new Result(true,FILE_SERVER_URL + path);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传失败");
        }

    }

}
