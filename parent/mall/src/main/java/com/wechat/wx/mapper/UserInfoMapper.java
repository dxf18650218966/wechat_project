package com.wechat.wx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wechat.wx.entity.UserInfoBean;
import org.springframework.stereotype.Component;

/**
 * 用户信息
 * @author dxf
 * @version 1.0
 * @date 2020/11/23 22:16
 */
@Component
public interface UserInfoMapper extends BaseMapper<UserInfoBean> {

    /**
     * 通过手机号码查询卡号
     * @return 会员卡号
     */
    String selectCardIdByPhone(String phone);


    /**
     * 通过公众号openId查询卡号
     * @param openId 公众号openId
     * @return 会员卡号
     */
    String selectCardIdByOpenId(String openId);

    /**
     * 通过手机号更新会员信息
     * @return
     */
    Boolean updateByPhoneNumber(UserInfoBean userInfoBean);


    UserInfoBean selectByCardId(String cardId);


}
