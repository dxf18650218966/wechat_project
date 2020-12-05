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
     * 查询卡号
     * @param projectId 项目id
     * @param phone 手机号
     * @return 会员卡号
     */
    String selectCardIdByPhone(String projectId, String phone);

    /**
     * 查询卡号
     * @param openId 公众号openId
     * @return 会员卡号
     */
    String selectCardIdByOpenId(String openId);

    /**
     * 更新
     * @param userInfoBean 会员信息
     * @return
     */
    Boolean updateByOpenId(UserInfoBean userInfoBean);

    /**
     * 会员信息
     * @param cardId 会员卡号
     * @return
     */
    UserInfoBean selectByCardId(String cardId);


}

