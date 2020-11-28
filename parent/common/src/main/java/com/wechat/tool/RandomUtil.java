package com.wechat.tool;

import java.util.UUID;

/**
 * 随机数工具
 * @Author dai
 * @Date 2020/11/25 
 */
public class RandomUtil {
    /**
     * 生成会员卡号 ( 10位时间戳 + 6位随机数)
     */
    public static String cardId(){
        return System.currentTimeMillis()/1000 + cn.hutool.core.util.RandomUtil.randomNumbers(6);
    }

    /**
     * 获取随机UUID
     * @return 36位UUID   如：c4b69217-a90e-43e4-9c4b-e2823d8f4975
     */
    public static String randomUUID()
    {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取随机UUID
     * @return 36位UUID + 6位随机数
     */
    public static String ensureNoRiskAtAll(){
        return UUID.randomUUID().toString() + cn.hutool.core.util.RandomUtil.randomNumbers(6);
    }

}
