package com.wechat.user.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 收货地址管理  shipping_address
 * @author dxf
 * @date 2020/12/13 10:57
 * @version 1.0
 */
@Data
public class ShippingAddressBean implements Serializable {
    private static final long serialVersionUID = 1452837861129426999L;
    /**
     * 主键
     */
    private String id;

    /**
     * 会员卡
     */
    private String cardId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 所在地区
     */
    private String region;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 设为默认地址ID
     */
    private String setToDefault;

    /**
     * 设为默认地址
     */
    private String setToDefaultName;
}