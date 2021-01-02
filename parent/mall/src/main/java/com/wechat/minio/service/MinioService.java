package com.wechat.minio.service;

import cn.hutool.core.util.RandomUtil;
import com.wechat.constant.SystemConst;
import com.wechat.tool.DateUtil;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

/**
 * @Author dai
 * @Date 2020/12/22 
 */
@Service
public class MinioService {
    private final static Logger logger = LoggerFactory.getLogger(MinioService.class);

    @Autowired
    private MinioClient minioClient;

     /**
     * 将本地文件上传到桶
     * @param filePath 文件路径
     * @return 成功true
     */
    public String putObject(String filePath) {
        try {
            // 获取路径后缀
            String suffixFileName = filePath.substring(filePath.lastIndexOf("."));
            // 存储桶里的对象名称（13位时间戳 + 13位随机数 + 后缀）
            String fileName = DateUtil.getTimeStamp13() + RandomUtil.randomNumbers(13) + suffixFileName;

            // 存储桶名称.
            String bucketName = createBucketName();
            minioClient.putObject(bucketName, fileName, filePath, new PutObjectOptions(2048,-1));
            return bucketName + "/" + fileName;
        } catch (Exception e) {
            logger.error("minio通过文件上传到对象中，失败原因：{}", ExceptionUtils.getStackTrace(e));
            return "";
        }
    }

    /**
     * 通过InputStream上传对象
     * @param file 待上传的文件
     * @return 访问路径
     */
    public String putObject(MultipartFile file) {
        try {
            // 原文件名
            String originalFileName = file.getOriginalFilename();
            // 原文件名后缀
            String suffixFileName = originalFileName.substring(originalFileName.lastIndexOf("."));
            // 存储桶里的对象名称（13位时间戳 + 13位随机数 + 后缀）
            String fileName = DateUtil.getTimeStamp13() + RandomUtil.randomNumbers(13) + suffixFileName;

            // 存储桶名称.
            String bucketName = createBucketName();
            minioClient.putObject(bucketName, fileName, file.getInputStream(), new PutObjectOptions(file.getInputStream().available(), -1));
            return bucketName + "/" + fileName;
        } catch (Exception e) {
            logger.error("minio通过InputStream上传对象，失败原因：{}", ExceptionUtils.getStackTrace(e));
            return "";
        }
    }

    /**
     * 创建存储桶（规则：yyyyMMddHHmmss）
     * @return
     */
    public String createBucketName() throws Exception {
        String bucketName = DateUtil.format(LocalDateTime.now(), SystemConst.FORMAT8 );
        // 检查存储桶是否存在
        if(! minioClient.bucketExists(bucketName)) {
            // 创建一个新的存储桶
            minioClient.makeBucket(bucketName);
        }
        return bucketName;
    }
}
