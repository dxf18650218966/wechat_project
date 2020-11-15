package com.wechat.crypto;

import cn.hutool.core.util.ObjectUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 签名
 * @Author dai
 * @Date 2020/1/12
 */
public class SignUtil {

    /**
     * MD5签名
     * 签名规则：
     *   第一步，设所有发送或者接收到的数据为集合M，将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序），使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA。
     *   第二步，拼接API密钥。在stringA最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行MD5运算并转换为大写，得到sign值
     * @param paramMap 参与加密的数据
     * @param key 加密密钥
     * @return 32位大写字符串
     */
    public static String signMD5(Map<String, String> paramMap, String key){
        //有的接口是先排序拼接后再加上key，有的是将key一起排序拼接
        String sign = "";
        if(ObjectUtil.isNull(key)){
            sign = jointUrl(paramMap);
        }else{
            sign = jointUrl(paramMap) + "&key=" + key;
        }
        //MD5 32位加密转大写
        return MD5Util.md5UpperCase(sign);
    }

    /**
     * 将集合M内非空参数按照ASCII码从小到大排序,使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA
     * @param paramMap
     * @return key1=value1&key2=value2…
     */
    public static String jointUrl(Map<String, String> paramMap) {
        List<String> keys = new ArrayList<String>(paramMap.keySet());
        //按照ASCII码从小到大排序
        Collections.sort(keys);
        StringBuilder prestr = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = paramMap.get(key);
            if(value != "" && value != null){
                // 使用URL键值对的格式进行拼接（即key1=value1&key2=value2…）
                prestr.append(key).append("=").append(value).append("&");
            }
        }
        //删除拼接完成后最有一个字符'&'
        prestr.deleteCharAt(prestr.length() - 1);
        return prestr.toString();
    }
}