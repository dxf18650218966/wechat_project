package com.wechat.wx.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wechat.wx.entity.AutoReplyBean;
import org.springframework.stereotype.Component;

/**
 * 自动回复
 * @author dxf
 * @version 1.0
 * @date 2020/11/3 22:39
 */
@Component
@TableName("auto_reply")
public interface AutoReplyMapper extends BaseMapper<AutoReplyBean> {
    AutoReplyBean selectById(String id);
}