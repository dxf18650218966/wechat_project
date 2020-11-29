package com.wechat.wx.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechat.wx.entity.SysFileBean;
import com.wechat.wx.mapper.SysFileMapper;
import com.wechat.common.define.RedisKeyConst;
import com.wechat.wx.model.WechatUrlConst;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * 微信素材管理、服务器图片处理
 * @Author dai
 * @Date 2020/11/7 
 */
@Component
public class MaterialImgUtil {
    private static Logger logger = LoggerFactory.getLogger(MaterialImgUtil.class);
    /**
     * 域名
     */
    @Value("${sys.domain-name}")
    private String domainName;

    /**
     * 本地文件路径
     */
    @Value("${sys.upload-path}")
    private String uploadPath;

    /**
     * 域名访问
     */
    public static final String DOMAIN = "domain";

    /**
     * 本地访问
     */
    public static final String LOCAL = "local";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SysFileMapper sysFileMapper;


    /**
     * 新增临时素材
     * @param type 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
     * @param accessToken 调用接口凭证
     * @param fileUrl 文件路径（本地）
     * @return media_id 上传后临时素材标识 (空则上传失败)
     */
    public static String uploadMaterial(String type, String accessToken, String fileUrl) {
        String url = String.format(WechatUrlConst.MEDIA_UPLOAD, accessToken, type);
        // 创建文件
        File file = new File(fileUrl);
        if(file != null){
            try {
                String s = HttpUtilByPost.connectHttpsByPost(url, file);
                JSONObject jsonObject = JSON.parseObject(s);
                String errcode = jsonObject.getString("errcode");
                if(StrUtil.isNotBlank(errcode)){
                    logger.error("新增临时素材异常:::{}",errcode);
                }
                return jsonObject.getString("media_id");
            }catch (Exception e){
                logger.error("上传素材异常:::{}", ExceptionUtils.getStackTrace(e));
            }
        }
        return "";
    }

    /**
     * 处理服务器图片路径
     * @param imgData 需要处理的图片数据
     * @param type 路径类型   domain域名访问   local本地
     * @return path
     */
    public String getImgUrl(String imgData, String type){
        if (StrUtil.isBlank(imgData)) {
            return "";
        }
        JSONObject jsonObject = JSON.parseArray(imgData).getJSONObject(0);
        // 文件标识
        String fileId = jsonObject.getString("fileId");
        // 存在则从缓存中取
        String filePath = stringRedisTemplate.opsForValue().get(RedisKeyConst.FILE_ID + fileId);
        if(StrUtil.isBlank(filePath)){
            SysFileBean sysFileBean = sysFileMapper.selectOne(new QueryWrapper<SysFileBean>().eq("FILE_ID_", fileId));
            if(sysFileBean == null && StrUtil.isBlank(sysFileBean.getPath()) ){
                return "";
            }
            filePath = sysFileBean.getPath();
            // 写入缓存
            stringRedisTemplate.opsForValue().set(RedisKeyConst.FILE_ID  + fileId, filePath, 1, TimeUnit.DAYS);
        }

        if(DOMAIN.equals(type)){
            return domainName + filePath;
        }else if (LOCAL.equals(type)){
            return uploadPath + filePath;
        }
        return "";
    }
}
