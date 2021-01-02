package com.wechat.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

/** 自动填充功能
 * @Author dai
 * @Date 2021/1/2 
 */
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    // 插入时填充
    public void insertFill(MetaObject metaObject) {

    }

    @Override
    // 更新时填充
    public void updateFill(MetaObject metaObject) {

    }
}
