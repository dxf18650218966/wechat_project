package com.wechat.tool;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 数字运算
 * @Author dai
 * @Date 2020/1/28
 */
public class NumberUtil {
    //--------------------  加法  -------------------------

    /**
     * 加法
     * @param values 所有参数相加
     * @return
     */
    public static BigDecimal add(Number... values) {
        if (values.length <= 0) {
            return BigDecimal.ZERO;
        } else {
            Number value = values[0];
            BigDecimal result = new BigDecimal(null == value ? "0" : value.toString());

            for(int i = 1; i < values.length; ++i) {
                value = values[i];
                if (null != value) {
                    result = result.add(new BigDecimal(value.toString()));
                }
            }
            return result;
        }
    }

    /**
     * 加法
     * @param values 所有参数相加
     * @return
     */
    public static BigDecimal add(String... values) {
        if (values.length <= 0) {
            return BigDecimal.ZERO;
        } else {
            String value = values[0];
            BigDecimal result = new BigDecimal(null == value ? "0" : value);

            for(int i = 1; i < values.length; ++i) {
                value = values[i];
                if (null != value) {
                    result = result.add(new BigDecimal(value));
                }
            }

            return result;
        }
    }

    /**
     * 加法
     * @param values 所有参数相加
     * @return
     */
    public static BigDecimal add(BigDecimal... values) {
        if (values.length <= 0) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal value = values[0];
            BigDecimal result = null == value ? BigDecimal.ZERO : value;

            for(int i = 1; i < values.length; ++i) {
                value = values[i];
                if (null != value) {
                    result = result.add(value);
                }
            }

            return result;
        }
    }

    //--------------------  减法  -------------------------

    /**
     * 减法
     * @param values 第一个参数减去后面所有参数值
     * @return
     */
    public static BigDecimal sub(Number... values) {
        if (values.length <= 0) {
            return BigDecimal.ZERO;
        } else {
            Number value = values[0];
            BigDecimal result = new BigDecimal(null == value ? "0" : value.toString());

            for(int i = 1; i < values.length; ++i) {
                value = values[i];
                if (null != value) {
                    result = result.subtract(new BigDecimal(value.toString()));
                }
            }

            return result;
        }
    }

    /**
     * 减法
     * @param values 第一个参数减去后面所有参数值
     * @return
     */
    public static BigDecimal sub(String... values) {
        if (values.length <= 0) {
            return BigDecimal.ZERO;
        } else {
            String value = values[0];
            BigDecimal result = new BigDecimal(null == value ? "0" : value);

            for(int i = 1; i < values.length; ++i) {
                value = values[i];
                if (null != value) {
                    result = result.subtract(new BigDecimal(value));
                }
            }

            return result;
        }
    }

    /**
     * 减法
     * @param values 第一个参数减去后面所有参数值
     * @return
     */
    public static BigDecimal sub(BigDecimal... values) {
        if (values.length <= 0) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal value = values[0];
            BigDecimal result = null == value ? BigDecimal.ZERO : value;

            for(int i = 1; i < values.length; ++i) {
                value = values[i];
                if (null != value) {
                    result = result.subtract(value);
                }
            }

            return result;
        }
    }
    //--------------------  乘法  -------------------------
    /**
     * 乘法
     * @param values 所有参数相乘
     * @return
     */
    public static BigDecimal mul(Number... values) {
        if (values.length <= 0) {
            return BigDecimal.ZERO;
        } else {
            Number value = values[0];
            BigDecimal result = new BigDecimal(null == value ? "0" : value.toString());

            for(int i = 1; i < values.length; ++i) {
                value = values[i];
                if (null != value) {
                    result = result.multiply(new BigDecimal(value.toString()));
                }
            }

            return result;
        }
    }

    /**
     * 乘法
     * @param values 所有参数相乘
     * @return
     */
    public static BigDecimal mul(String... values) {
        if (values.length <= 0) {
            return BigDecimal.ZERO;
        } else {
            String value = values[0];
            BigDecimal result = new BigDecimal(null == value ? "0" : value);

            for(int i = 1; i < values.length; ++i) {
                value = values[i];
                if (null != value) {
                    result = result.multiply(new BigDecimal(value));
                }
            }

            return result;
        }
    }

    /**
     * 乘法
     * @param values 所有参数相乘
     * @return
     */
    public static BigDecimal mul(BigDecimal... values) {
        if (values.length <= 0) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal value = values[0];
            BigDecimal result = null == value ? BigDecimal.ZERO : value;

            for(int i = 1; i < values.length; ++i) {
                value = values[i];
                if (null != value) {
                    result = result.multiply(value);
                }
            }

            return result;
        }
    }

    //--------------------  除法  -------------------------

    /**
     * 除法
     * @param v1 被除数
     * @param v2 除数
     * @param scale 保留几位小数
     * @param roundingMode 枚举类型：小数点处理方式
     * RoundingMode.UP 正数向上取整，负数向下取整  例如：5.5->6、 5.3->6、 (-1.4)->(-2)、 (-1.9)->(-2)
     * RoundingMode.DOWN 正数向下取整，负数向上取整 例如：5.5->5、 5.3->5、 (-1.4)->(-1)、 (-1.9)->(-1)
     * RoundingMode.UNNECESSARY 不需要四舍五入
     * RoundingMode.CEILING 正数向上取整，负数向上取整  例如：5.5->6、 5.3->6、 (-1.4)->(-1)、 (-1.9)->(-1)
     * RoundingMode.FLOOR 正数向下取整，负数向下取整  例如：5.5->5、 5.3->5、 (-1.4)->(-2)、 (-1.9)->(-2)
     * @return
     */
    public static BigDecimal div(Number v1, Number v2, int scale, RoundingMode roundingMode) {
        return div(v1.toString(), v2.toString(), scale, roundingMode);
    }

    /**
     * 除法
     * @param v1 被除数
     * @param v2 除数
     * @param scale 保留几位小数
     * @param roundingMode 枚举类型：小数点处理方式
     * RoundingMode.UP 正数向上取整，负数向下取整  例如：5.5->6、 5.3->6、 (-1.4)->(-2)、 (-1.9)->(-2)
     * RoundingMode.DOWN 正数向下取整，负数向上取整 例如：5.5->5、 5.3->5、 (-1.4)->(-1)、 (-1.9)->(-1)
     * RoundingMode.UNNECESSARY 不需要四舍五入
     * RoundingMode.CEILING 正数向上取整，负数向上取整  例如：5.5->6、 5.3->6、 (-1.4)->(-1)、 (-1.9)->(-1)
     * RoundingMode.FLOOR 正数向下取整，负数向下取整  例如：5.5->5、 5.3->5、 (-1.4)->(-2)、 (-1.9)->(-2)
     * @return
     */
    public static BigDecimal div(String v1, String v2, int scale, RoundingMode roundingMode) {
        return div(new BigDecimal(v1), new BigDecimal(v2), scale, roundingMode);
    }

    /**
     * 除法
     * @param v1 被除数
     * @param v2 除数
     * @param scale 保留几位小数
     * @param roundingMode 枚举类型：小数点处理方式
     * RoundingMode.UP 正数向上取整，负数向下取整  例如：5.5->6、 5.3->6、 (-1.4)->(-2)、 (-1.9)->(-2)
     * RoundingMode.DOWN 正数向下取整，负数向上取整 例如：5.5->5、 5.3->5、 (-1.4)->(-1)、 (-1.9)->(-1)
     * RoundingMode.UNNECESSARY 不需要四舍五入
     * RoundingMode.CEILING 正数向上取整，负数向上取整  例如：5.5->6、 5.3->6、 (-1.4)->(-1)、 (-1.9)->(-1)
     * RoundingMode.FLOOR 正数向下取整，负数向下取整  例如：5.5->5、 5.3->5、 (-1.4)->(-2)、 (-1.9)->(-2)
     * @return
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale, RoundingMode roundingMode) {
        if (null == v1) {
            return BigDecimal.ZERO;
        } else {
            if (scale < 0) {
                scale = -scale;
            }
            return v1.divide(v2, scale, roundingMode);
        }
    }

    //---------------- 比较 ----------------

    /**
     * bigNum1是否大于bigNum2
     * @param bigNum1
     * @param bigNum2
     * @return 大于返回true
     */
    public static boolean isGreater(BigDecimal bigNum1, BigDecimal bigNum2) {
        return bigNum1.compareTo(bigNum2) > 0;
    }

    /**
     * bigNum1是否大于等于bigNum2
     * @param bigNum1
     * @param bigNum2
     * @return 大于等于返回true
     */
    public static boolean isGreaterOrEqual(BigDecimal bigNum1, BigDecimal bigNum2) {
        return bigNum1.compareTo(bigNum2) >= 0;
    }

    /**
     * bigNum1是否小于bigNum2
     * @param bigNum1
     * @param bigNum2
     * @return 小于返回true
     */
    public static boolean isLess(BigDecimal bigNum1, BigDecimal bigNum2) {
        return bigNum1.compareTo(bigNum2) < 0;
    }

    /**
     * bigNum1是否小于等于bigNum2
     * @param bigNum1
     * @param bigNum2
     * @return 小于等于返回true
     */
    public static boolean isLessOrEqual(BigDecimal bigNum1, BigDecimal bigNum2) {
        return bigNum1.compareTo(bigNum2) <= 0;
    }

    /**
     * 两数是否相等
     * @param bigNum1
     * @param bigNum2
     * @return 相等返回true
     */
    public static boolean equals(BigDecimal bigNum1, BigDecimal bigNum2) {
        return 0 == bigNum1.compareTo(bigNum2);
    }
}
