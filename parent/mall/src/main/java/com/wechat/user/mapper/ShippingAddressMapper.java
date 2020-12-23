package com.wechat.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wechat.user.entity.ShippingAddressBean;
import org.springframework.stereotype.Component;

@Component
public interface ShippingAddressMapper extends BaseMapper<ShippingAddressBean> {
  /*  int deleteById(String id);

    int insert(ShippingAddressBean record);

    ShippingAddressBean selectById(String id);*/

    /**
     * 取消默认地址
     * @param cardId 卡号
     * @return
     */
    Boolean cancelDefault(String cardId);

}