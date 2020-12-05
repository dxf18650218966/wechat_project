package com.wechat.wx.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wechat.wx.entity.XcxInfoBean;
import org.springframework.stereotype.Component;

/**
 * 小程序信息
 * @author dxf
 * @version 1.0
 * @date 2020/11/6 22:07
 */
@Component
@TableName("xcx_info")
public interface XcxInfoMapper extends BaseMapper<XcxInfoBean> {
}
