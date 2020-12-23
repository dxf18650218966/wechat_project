package com.wechat.mall.mapper;

import com.wechat.mall.entity.SlideShowBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * slide_show 首页轮播图片
 * @Author dai
 * @Date 2020/12/07
 */
@Component
public interface SlideShowMapper {

    /**
     * 通过项目查询
     * @param project
     * @return
     */
    List<SlideShowBean> selectByProject(String project);

}