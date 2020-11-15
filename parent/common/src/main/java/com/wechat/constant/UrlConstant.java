package com.wechat.constant;

/**
 * 接口地址
 * @Author dai
 * @Date 2020/1/10
 */
public interface UrlConstant {
    //-----------------------------------微信模块-------------------------------------
    /**
     * 统一下单接口
     */
    public static final String WECHAT_PAY = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 查询订单
     */
    public static final String WECHAT_QUERY_ORDER = "https://api.mch.weixin.qq.com/pay/orderquery";

    /**
     * 申请退款
     */
    public static final String WECHAT_REFUND = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    /**
     * 退款查询
     */
    public static final String WECHAT_REFUND_QUERY = "https://api.mch.weixin.qq.com/pay/refundquery";

    /**
     * 关闭订单
     */
    public static final String WECHAT_CLOSE_ORDER = "https://api.mch.weixin.qq.com/pay/closeorder";
    //---------------------------------------------------------------------------
}
