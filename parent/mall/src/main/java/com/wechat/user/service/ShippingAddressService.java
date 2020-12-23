package com.wechat.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechat.constant.SystemConst;
import com.wechat.model.ResponseUtil;
import com.wechat.user.entity.ShippingAddressBean;
import com.wechat.user.mapper.ShippingAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 收货地址管理
 * @author dxf
 * @date 2020/12/13 10:59
 * @version 1.0
 */
@Service
public class ShippingAddressService {
    @Autowired
    private ShippingAddressMapper shippingAddressMapper;

    /**
     * 添加收货地址
     * @param shippingAddressBean 收货地址
     * @return successful: true
     */
    public Boolean addAddress(ShippingAddressBean shippingAddressBean) {
        // 如果设为默认地址, 需要取消掉之前的默认地址
        if(SystemConst.ONE .equals(shippingAddressBean.getSetToDefault())){
            shippingAddressMapper.cancelDefault(shippingAddressBean.getCardId());
        }

        int insert = shippingAddressMapper.insert(shippingAddressBean);
        return insert > 0 ? true : false;
    }

    /**
     * 删除收货地址
     * @param id 收货地址主键
     * @param cardId 卡号
     * @return successful: true
     */
    public Boolean delAddress(String id, String cardId) {
        QueryWrapper<ShippingAddressBean> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id).eq("card_id",cardId);

        int delete = shippingAddressMapper.delete(queryWrapper);
        return delete > 0 ? true : false;
    }

    /**
     * 编辑收货地址
     * @param shippingAddressBean 收货地址
     * @return successful: true
     */
    public Boolean editorAddress(ShippingAddressBean shippingAddressBean) {
        QueryWrapper<ShippingAddressBean> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",shippingAddressBean.getId()).eq("card_id",shippingAddressBean.getCardId());

        int update = shippingAddressMapper.update(shippingAddressBean, queryWrapper);
        return update > 0 ? true : false;
    }

    /**
     * 查询默认收货地址
     * @param cardId 卡号
     * @return
     */
    public Object selectDefault(String cardId) {
        QueryWrapper<ShippingAddressBean> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("card_id", cardId);
        queryWrapper.eq("set_to_default", "1");
        return ResponseUtil.resultSuccess(shippingAddressMapper.selectOne(queryWrapper));
    }

    /**
     * 查询所有收货地址
     * 默认收货地址置顶，其余按时间降序
     * @param cardId 卡号
     * @return
     */
    public Object selectAll(String cardId) {
        QueryWrapper<ShippingAddressBean> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("card_id", cardId);
        queryWrapper.orderByDesc("set_to_default", "create_time");
        return ResponseUtil.resultSuccess(shippingAddressMapper.selectList(queryWrapper));
    }
}
