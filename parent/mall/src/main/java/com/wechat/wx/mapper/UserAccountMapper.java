package com.wechat.wx.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wechat.wx.entity.UserAccountBean;
import org.springframework.stereotype.Component;

/**
 * 用户账户信息
 * @author dxf
 * @version 1.0
 * @date 2020/11/26 21:22
 */
@Component
@TableName("user_account")
public interface UserAccountMapper extends BaseMapper<UserAccountBean> {
    /**
     * 查询账户信息
     * @param cardId
     * @return
     */
    UserAccountBean selectByCardId(String cardId);

    int updateByCardId(UserAccountBean userAccountBean);


}
