package com.wechat.rocketmq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wechat.rocketmq.model.TransactionLogBean;
import org.springframework.stereotype.Component;

/**
 * 事务日志
 * @author dxf
 * @date 2020/7/11 22:58
 * @version 1.0
 */
@Component
public interface TransactionLogMapper extends BaseMapper<TransactionLogBean> {

}
