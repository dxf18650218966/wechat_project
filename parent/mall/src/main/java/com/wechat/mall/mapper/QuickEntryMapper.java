package com.wechat.mall.mapper;

import com.wechat.mall.entity.QuickEntryBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * quick_entry 首页快捷入口配置
 * @Author dai
 * @Date 2020/12/07
 */
@Component
public interface QuickEntryMapper {

    /**
     * 通过项目查询
     * @param project
     * @return
     */
    List<QuickEntryBean> selectByProject(String project);

}