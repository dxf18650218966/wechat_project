package com.wechat.user.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.wechat.constant.BusinessMsgConst;
import com.wechat.constant.SystemConst;
import com.wechat.model.ErrCode;
import com.wechat.model.ResponseUtil;
import com.wechat.tool.DateUtil;
import com.wechat.tool.RandomUtil;
import com.wechat.tool.RedisUtil;
import com.wechat.user.entity.ShippingAddressBean;
import com.wechat.user.service.ShippingAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 收货地址管理
 * @author dxf
 * @date 2020/12/13 10:57
 * @version 1.0
 */
@RestController
@CrossOrigin
@RequestMapping("user/shipping_address")
public class ShippingAddressController {
    @Autowired
    private ShippingAddressService shippingAddressService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 添加收货地址
     * consignee 收货人
     * region 所在地区
     * address 详细地址
     * set_to_default 是否设为默认地址   0否1是
     */
    @PostMapping("add")
    public Object add(@Validated @RequestBody JSONObject jsonObject){
        String cardId = jsonObject.getString("card_id");
        String consignee = jsonObject.getString("consignee");
        String phone = jsonObject.getString("phone");
        String region = jsonObject.getString("region");
        String address = jsonObject.getString("address");
        String setToDefault = jsonObject.getString("set_to_default");
        String sessionToken = jsonObject.getString("session_token");

        if(! StrUtil.isAllNotBlank( sessionToken, cardId, consignee, phone, region, address, setToDefault)){
            return ResponseUtil.returnFail( ErrCode.MISSING_REQUEST_PARAMETERS );
        }
        if( StrUtil.isBlank(redisUtil.get(sessionToken))){
            return ResponseUtil.returnFail( ErrCode.PLEASE_LOG_IN );
        }
        ShippingAddressBean shippingAddressBean = new ShippingAddressBean();
        shippingAddressBean.setCardId(cardId);
        shippingAddressBean.setConsignee(consignee);
        shippingAddressBean.setPhone(phone);
        shippingAddressBean.setRegion(region);
        shippingAddressBean.setAddress(address);
        shippingAddressBean.setSetToDefault(setToDefault);
        shippingAddressBean.setSetToDefaultName(SystemConst.ZERO .equals(setToDefault) ? SystemConst.NO : SystemConst.YES );
        shippingAddressBean.setCreateTime(DateUtil.date());
        shippingAddressBean.setId(RandomUtil.randomUUID());

        Boolean res = shippingAddressService.addAddress(shippingAddressBean);
        if(res){
            return ResponseUtil.resultSuccess( BusinessMsgConst.SAVE_OK );
        }
        return ResponseUtil.resultFail( ErrCode.SAVE_FAIL );
    }

    /**
     *  删除收货地址
     */
    @PostMapping("del")
    public Object del(@Validated @RequestBody JSONObject jsonObject){
        String id = jsonObject.getString("id");
        String cardId = jsonObject.getString("card_id");
        String sessionToken = jsonObject.getString("session_token");

        if(! StrUtil.isAllNotBlank(id, cardId, sessionToken )){
            return ResponseUtil.returnFail( ErrCode.MISSING_REQUEST_PARAMETERS );
        }
        if( StrUtil.isBlank(redisUtil.get(sessionToken))){
            return ResponseUtil.returnFail( ErrCode.PLEASE_LOG_IN );
        }

        Boolean res = shippingAddressService.delAddress(id, cardId);
        if(res){
            return ResponseUtil.resultSuccess( BusinessMsgConst.DEL_OK );
        }
        return ResponseUtil.resultFail( ErrCode.DEL_FAIL );
    }

    /**
     * 编辑收货地址
     */
    @PostMapping("editor")
    public Object editor(@Validated @RequestBody JSONObject jsonObject){
        String id =  jsonObject.getString("id");
        String cardId = jsonObject.getString("card_id");
        String consignee = jsonObject.getString("consignee");
        String phone = jsonObject.getString("phone");
        String region = jsonObject.getString("region");
        String address = jsonObject.getString("address");
        String setToDefault = jsonObject.getString("set_to_default");
        String sessionToken = jsonObject.getString("session_token");

        if(! StrUtil.isAllNotBlank( id, sessionToken, cardId, consignee, phone, region, address, setToDefault)){
            return ResponseUtil.returnFail( ErrCode.MISSING_REQUEST_PARAMETERS );
        }
        if( StrUtil.isBlank(redisUtil.get(sessionToken))){
            return ResponseUtil.returnFail( ErrCode.PLEASE_LOG_IN );
        }
        ShippingAddressBean shippingAddressBean = new ShippingAddressBean();
        shippingAddressBean.setId(id);
        shippingAddressBean.setCardId(cardId);
        shippingAddressBean.setConsignee(consignee);
        shippingAddressBean.setPhone(phone);
        shippingAddressBean.setRegion(region);
        shippingAddressBean.setAddress(address);
        shippingAddressBean.setSetToDefault(setToDefault);
        shippingAddressBean.setSetToDefaultName(SystemConst.ZERO .equals(setToDefault) ? SystemConst.NO : SystemConst.YES );

        Boolean res = shippingAddressService.editorAddress(shippingAddressBean);
        if(res){
            return ResponseUtil.resultSuccess( BusinessMsgConst.SAVE_OK );
        }
        return ResponseUtil.resultFail( ErrCode.SAVE_FAIL );
    }

    /**
     * 查询默认收货地址
     */
    @PostMapping("get_default")
    public Object getDefault(@Validated @RequestBody JSONObject jsonObject){
        String cardId = jsonObject.getString("card_id");
        String sessionToken = jsonObject.getString("session_token");

        if(! StrUtil.isAllNotBlank(cardId, sessionToken)){
            return ResponseUtil.returnFail( ErrCode.MISSING_REQUEST_PARAMETERS );
        }
        if( StrUtil.isBlank(redisUtil.get(sessionToken))){
            return ResponseUtil.returnFail( ErrCode.PLEASE_LOG_IN );
        }
        return shippingAddressService.selectDefault(cardId);
    }

    /**
     * 查询所有收货地址
     */
    @PostMapping("get_all")
    public Object getAll(@Validated @RequestBody JSONObject jsonObject){
        String cardId = jsonObject.getString("card_id");
        String sessionToken = jsonObject.getString("session_token");

        if(! StrUtil.isAllNotBlank(cardId, sessionToken)){
            return ResponseUtil.returnFail( ErrCode.MISSING_REQUEST_PARAMETERS );
        }
        if( StrUtil.isBlank(redisUtil.get(sessionToken))){
            return ResponseUtil.returnFail( ErrCode.PLEASE_LOG_IN );
        }
        return shippingAddressService.selectAll(cardId);
    }
}
