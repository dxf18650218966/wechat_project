package com.wechat.mall.model;

import com.wechat.mall.entity.QuickEntryBean;
import com.wechat.mall.entity.SlideShowBean;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 首页初始化模型
 * @author dxf
 * @date 2020/12/7 20:58
 * @version 1.0
 */
@Data
public class HomePageInitModel implements Serializable {
    private static final long serialVersionUID = 4662357786757192600L;

    /**
     * 首页快捷入口配置
     */
    private List<QuickEntryBean> quickEntryBeanList;

    /**
     * 首页轮播图片
     */
    private List<SlideShowBean> slideShowBeanList;
}
