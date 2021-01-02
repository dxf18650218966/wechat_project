package com.wechat.minio.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.wechat.minio.service.MinioService;
import com.wechat.model.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dxf
 * @date 2020/12/23 22:49
 * @version 1.0
 */
@RestController
@CrossOrigin
@RequestMapping("minio")
public class MinioController {
    @Autowired
    private MinioService minioService;

    /**
     * 上传文件到 minio
     * @param files
     * @return
     */
    @PostMapping("upload_file")
    public Object uploadFile(@RequestParam("file") MultipartFile[] files){
        List<JSONObject> list = new ArrayList<>(16);
        if(files.length > 0) {
            for (MultipartFile multipartFile : files) {
                // 通过InputStream上传对象
                String path = minioService.putObject(multipartFile);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("fileName", multipartFile.getOriginalFilename());
                if(StrUtil.isNotBlank(path)) {
                    jsonObject.put("state", "successful");
                    jsonObject.put("path", path);
                }else {
                    jsonObject.put("state", "failure");
                }
                list.add(jsonObject);
            }
        }
        return ResponseUtil.resultSuccess(list);
    }
}
