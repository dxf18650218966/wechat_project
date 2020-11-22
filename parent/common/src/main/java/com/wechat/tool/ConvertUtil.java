package com.wechat.tool;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;

/**
 * 类型转换
 * @Author dai
 * @Date 2020/1/28
 */
public class ConvertUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(ConvertUtil.class);

    /**
     * Object转String
     * @param obj
     * @return
     */
    public static String toStr(final Object obj) {
        if (obj != null) {
            return obj.toString();
        }
        return "";
    }

    /**
     * Object转String
     * @param obj
     * @param defaultValue 如果obj为null,则返回默认值
     * @return
     */
    public static String toStr(final Object obj, String defaultValue) {
        String str = toStr(obj);
        if (str == null) {
            str = defaultValue;
        }
        return str;
    }

    /**
     * Object转Number
     * @param obj
     * @return
     */
    public static Number toNumber(final Object obj) {
        if (obj != null) {
            if (obj instanceof Number) {
                return (Number) obj;
            } else if (obj instanceof Boolean) {
                Boolean flag = (Boolean) obj;
                return flag ? 1 : 0;
            } else if (obj instanceof String) {
                try {
                    String text = (String) obj;
                    return NumberFormat.getInstance().parse(text);
                } catch (Exception e) {
                    LOGGER.info(ExceptionUtils.getStackTrace(e));
                }
            }
        }
        return null;
    }

    /**
     * Object转Number
     * @param obj
     * @param defaultValue  如果obj为null,则返回默认值
     * @return
     */
    public static Number toNumber(final Object obj, Number defaultValue) {
        Number answer = toNumber(obj);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     * Object转Boolean   支持的值为：true、false、1,0  【0为false,1为true】
     * @param obj
     * @return
     */
    public static Boolean toBool(final Object obj) {
        if (obj != null) {
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            } else if (obj instanceof String) {
                return Boolean.valueOf((String) obj);
            } else if (obj instanceof Number) {
                Number n = (Number) obj;
                return (n.intValue() != 0) ? Boolean.TRUE : Boolean.FALSE;
            }
        }
        return null;
    }

    /**
     * Object转Boolean   支持的值为：true、false、1,0  【0为false,1为true】
     * @param obj
     * @param defaultValue  如果obj为null,则返回默认值
     * @return
     */
    public static Boolean toBool(final Object obj, Boolean defaultValue) {
        Boolean answer = toBool(obj);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     * Object转Integer
     * @param obj
     * @return
     */
    public static Integer toInteger(final Object obj) {
        Number answer = toNumber(obj);
        if (answer == null) {
            return null;
        } else if (answer instanceof Integer) {
            return (Integer) answer;
        }
        return Integer.valueOf(answer.intValue());
    }

    /**
     * Object转Integer
     * @param obj
     * @param defaultValue 如果obj为null,则返回默认值
     * @return
     */
    public static Integer toInteger(final Object obj, Integer defaultValue) {
        Integer answer = toInteger(obj);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     * Object转Long
     * @param obj
     * @return
     */
    public static Long toLong(final Object obj) {
        Number answer = toNumber(obj);
        if (answer == null) {
            return null;
        } else if (answer instanceof Long) {
            return (Long) answer;
        }
        return Long.valueOf(answer.longValue());
    }

    /**
     * Object转Long
     * @param obj
     * @param defaultValue 如果obj为null,则返回默认值
     * @return
     */
    public static Long toLong(final Object obj, Long defaultValue) {
        Long answer = toLong(obj);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     * Object转Float
     * @param obj
     * @return
     */
    public static Float toFloat(final Object obj) {
        Number answer = toNumber(obj);
        if (answer == null) {
            return null;
        } else if (answer instanceof Float) {
            return (Float) answer;
        }
        return Float.valueOf(answer.floatValue());
    }

    /**
     * Object转Float
     * @param obj
     * @param defaultValue 如果obj为null,则返回默认值
     * @return
     */
    public static Float toFloat(final Object obj, Float defaultValue) {
        Float answer = toFloat(obj);
        if ( answer == null ) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     * Object转Double
     * @param obj
     * @return
     */
    public static Double toDouble(final Object obj) {
        Number answer = toNumber(obj);
        if (answer == null) {
            return null;
        } else if (answer instanceof Double) {
            return (Double) answer;
        }
        return Double.valueOf(answer.doubleValue());
    }

    /**
     * Object转Double
     * @param obj
     * @param defaultValue 如果obj为null,则返回默认值
     * @return
     */
    public static Double toDouble(final Object obj, Double defaultValue) {
        Double answer = toDouble(obj);
        if ( answer == null ) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     * Object转BigInteger
     * @param obj
     * @return
     */
    public static BigInteger toBigInteger(final Object obj) {
        Long longObject = toLong(obj);
        if (longObject == null) {
            return BigInteger.ZERO;
        }
        return BigInteger.valueOf(longObject.longValue());
    }

    /**
     * Object转BigInteger
     * @param obj
     * @param defaultValue 如果obj为null,则返回默认值
     * @return
     */
    public static BigInteger toBigInteger(final Object obj, BigInteger defaultValue) {
        Long longObject = toLong(obj);
        if (longObject == null) {
            return defaultValue;
        }
        return BigInteger.valueOf(longObject.longValue());
    }

    /**
     * Object转BigDecimal
     * @param obj
     * @return
     */
    public static BigDecimal toBigDecimal(final Object obj) {
        Double doubleObject = toDouble(obj);
        if (doubleObject == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(doubleObject.doubleValue());
    }

    /**
     * Object转BigDecimal
     * @param obj
     * @param defaultValue  如果obj为null,则返回默认值
     * @return
     */
    public static BigDecimal toBigDecimal(final Object obj, BigDecimal defaultValue) {
        Double doubleObject = toDouble(obj);
        if (doubleObject == null) {
            return defaultValue;
        }
        return BigDecimal.valueOf(doubleObject.doubleValue());
    }
}
