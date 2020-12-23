package com.wechat.tool;


import cn.hutool.core.util.RandomUtil;
import com.wechat.constant.SystemConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 时间工具类
 * @Author dai
 * @Date 2020/5/02
 */
@Component
@PropertySource(value = {"classpath:/properties/holiday.properties"},
        ignoreResourceNotFound = false, encoding = "UTF-8")
public class DateUtil {
    @Autowired
    public RedisTemplate<String,String> redisTemplate;

    private static String recessday;
    private static String workday;

    @Value("${holiday.recessday}")
    public void setRecessday(String recessday) {
        DateUtil.recessday = recessday;
    }

    @Value("${holiday.workday}")
    public void setWorkday(String workday) {
        DateUtil.workday = workday;
    }

    public String getRecessday() {
        return recessday;
    }

    public String getWorkday() {
        return workday;
    }

    // ---------------------- 获取当前日期 ------------------------
    //yyyyMMddHHmmss + 14随机数字  （28随机数）
    public static String noncestr(){
        return format(date(), SystemConst.FORMAT10) + RandomUtil.randomNumbers(14);
    }

    // yyyy-MM-dd HH:mm:ss
    public static LocalDateTime date(){
        return LocalDateTime.now();
    }

    // yyyy-MM-dd HH:mm:ss
    public static String now(){
        return LocalDateTime.now().format(SystemConst.FORMATTER1);
    }

    // yyyy-MM-dd
    public static LocalDate today(){
        return LocalDate.now();
    }

    // yyyy-MM-dd
    public static String todayStr(){
        return LocalDate.now().toString();
    }

    // HH:mm:ss
    public static LocalTime time(){
        return LocalTime.now().withNano(0);
    }

    // HH:mm:ss
    public static String nowTime(){
        return LocalTime.now().withNano(0).toString();
    }

    // HH:mm
    public static LocalTime timeOmit(){
        return LocalTime.now().withSecond(0).withNano(0);
    }

    // HH:mm
    public static String nowTimeOmit(){
        return LocalTime.now().withSecond(0).withNano(0).toString();
    }

    // 年份
    public static int getYear(){
        return LocalDate.now().getYear();
    }
    public static int getYear(LocalDateTime localDateTime){
        return localDateTime.getYear();
    }

    // 月份
    public static int getMonthValue(){
        return LocalDate.now().getMonthValue();
    }
    public static int getMonthValue(LocalDateTime localDateTime){
        return localDateTime.getMonthValue();
    }

    // 天数
    public static int getDayOfMonth(){
        return LocalDate.now().getDayOfMonth();
    }
    public static int getDayOfMonth(LocalDateTime localDateTime){
        return localDateTime.getDayOfMonth();
    }

    // 时
    public static int getHour(){
        return LocalTime.now().getHour();
    }
    public static int getHour(LocalDateTime localDateTime){
        return localDateTime.getHour();
    }

    //分
    public static int getMinute(){
        return LocalTime.now().getMinute();
    }
    public static int getMinute(LocalDateTime localDateTime){
        return localDateTime.getMinute();
    }

    // 秒
    public static int getSecond(){
        return LocalTime.now().getSecond();
    }
    public static int getSecond(LocalDateTime localDateTime){
        return localDateTime.getSecond();
    }

    // 毫秒
    public static int getNano(){
        return LocalTime.now().getNano();
    }
    public static int getNano(LocalDateTime localDateTime){
        return localDateTime.getNano();
    }

    //时间戳 (13位)
    public static long getTimeStamp13(){
        return  System.currentTimeMillis();
    }
    //时间戳 (10位)
    public static long getTimeStamp10(){
        return System.currentTimeMillis()/1000;
    }

    /**
     * 当前日期是否为节假日(国家规定节假日)
     * @return true:节假日
     */
    public boolean isHoliday() {
        return isHoliday(today());
    }

    /**
     * 是否为节假日
     * @param date 指定日期
     * @return true:节假日
     */
    public boolean isHoliday(LocalDate date) {
        // 当前日期
        String today = date.toString();
        String key = "holiday_" + today;
        // 将指定日期是否为节假日写入缓存，有效期1天
        if(redisTemplate.hasKey(key)){
            String s = redisTemplate.opsForValue().get(key);
            return ConvertUtil.toBool(s);
        }

        try {
            // 节假日
            String[] propertyList = recessday.split(",");
            // 调休日（补课）
            String[] workdayList = workday.split(",");

            for (String property : propertyList) {
                if (today.equals(property)) {
                    // 将指定日期是否为节假日写入缓存，有效期1天
                    redisTemplate.opsForValue().set(key,"true", 1, TimeUnit.DAYS);
                    return true;
                }
            }
            for (String workday : workdayList) {
                if (today.equals(workday)) {
                    redisTemplate.opsForValue().set(key,"true", 1, TimeUnit.DAYS);
                    return false;
                }
            }
            // 获取当前日期是星期几
            int dayOfWeek = date.getDayOfMonth();
            if (dayOfWeek == 1 || dayOfWeek == 7) {
                // 周六、周日
                redisTemplate.opsForValue().set(key,"true", 1, TimeUnit.DAYS);
                return true;
            }
            redisTemplate.opsForValue().set(key,"true", 1, TimeUnit.DAYS);
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //----------------- 时间偏移【正数向未来偏移，负数向过去偏移】 -----------------------

    /**
     * 年份偏移
     * @param offset 偏移数，正数向未来偏移，负数向过去偏移
     * @return LocalDate [yyyy-MM-dd]
     */
    public static LocalDate offsetYear(long offset){
        return LocalDate.now().plusYears(offset);
    }

    /**
     * 年份偏移
     * @param offset 偏移数，正数向未来偏移，负数向过去偏移
     * @return LocalDateTime
     */
    public static LocalDateTime plusYears(long offset){
        return LocalDateTime.now().plusYears(offset);
    }

    /**
     * 年份偏移
     * @param offset 偏移数，正数向未来偏移，负数向过去偏移
     * @return String [yyyy-MM-dd HH:mm:ss]
     */
    public static String plusYearsToStr(long offset){
        return LocalDateTime.now().plusYears(offset).format(SystemConst.FORMATTER1);
    }

    /**
     * 月份偏移
     * @param offset 偏移数，正数向未来偏移，负数向过去偏移
     * @return LocalDate  [yyyy-MM-dd]
     */
    public static LocalDate offsetMonth(long offset){
        return LocalDate.now().plusMonths(offset);
    }

    /**
     * 月份偏移
     * @param offset 偏移数，正数向未来偏移，负数向过去偏移
     * @return LocalDateTime
     */
    public static LocalDateTime plusMonths(long offset){
        return LocalDateTime.now().plusMonths(offset);
    }

    /**
     * 月份偏移
     * @param offset 偏移数，正数向未来偏移，负数向过去偏移
     * @return String [yyyy-MM-dd HH:mm:ss]
     */
    public static String plusMonthsToStr(long offset){
        return LocalDateTime.now().plusMonths(offset).format(SystemConst.FORMATTER1);
    }

    /**
     * 天数偏移
     * @param offset 偏移数，正数向未来偏移，负数向过去偏移
     * @return LocalDate  [yyyy-MM-dd]
     */
    public static LocalDate offsetDay(long offset){
        return LocalDate.now().plusDays(offset);
    }

    /**
     * 天数偏移
     * @param offset 偏移数，正数向未来偏移，负数向过去偏移
     * @return LocalDateTime
     */
    public static LocalDateTime plusDays(long offset){
        return LocalDateTime.now().plusDays(offset);
    }

    /**
     * 天数偏移
     * @param offset 偏移数，正数向未来偏移，负数向过去偏移
     * @return String [yyyy-MM-dd HH:mm:ss]
     */
    public static String plusDaysToStr(long offset){
        return LocalDateTime.now().plusDays(offset).format(SystemConst.FORMATTER1);
    }

    /**
     * 小时偏移
     * @param offset 偏移数，正数向未来偏移，负数向过去偏移
     * @return LocalDate  [yyyy-MM-dd]
     */
    public static LocalTime offsetHour(long offset){
        return LocalTime.now().plusHours(offset);
    }

    /**
     * 小时偏移
     * @param offset 偏移数，正数向未来偏移，负数向过去偏移
     * @return LocalDateTime
     */
    public static LocalDateTime plusHours(long offset){
        return LocalDateTime.now().plusDays(offset);
    }

    /**
     * 小时偏移
     * @param offset 偏移数，正数向未来偏移，负数向过去偏移
     * @return String [yyyy-MM-dd HH:mm:ss]
     */
    public static String plusHoursToStr(long offset){
        return LocalDateTime.now().plusDays(offset).format(SystemConst.FORMATTER1);
    }

    /**
     * 分钟偏移
     * @param offset 偏移数，正数向未来偏移，负数向过去偏移
     * @return LocalDate  [yyyy-MM-dd]
     */
    public static LocalTime offsetMinute(long offset){
        return LocalTime.now().plusMinutes(offset);
    }

    /**
     * 分钟偏移
     * @param offset 偏移数，正数向未来偏移，负数向过去偏移
     * @return LocalDateTime
     */
    public static LocalDateTime plusMinutes(long offset){
        return LocalDateTime.now().plusDays(offset);
    }

    /**
     * 分钟偏移
     * @param offset 偏移数，正数向未来偏移，负数向过去偏移
     * @return String [yyyy-MM-dd HH:mm:ss]
     */
    public static String plusMinutesToStr(long offset){
        return LocalDateTime.now().plusDays(offset).format(SystemConst.FORMATTER1);
    }

    /**
     * 秒数偏移
     * @param offset 偏移数，正数向未来偏移，负数向过去偏移
     * @return LocalDate  [yyyy-MM-dd]
     */
    public static LocalTime offsetSecond(long offset){
        return LocalTime.now().plusSeconds(offset);
    }

    /**
     * 秒数偏移
     * @param offset 偏移数，正数向未来偏移，负数向过去偏移
     * @return LocalDateTime
     */
    public static LocalDateTime plusSeconds(long offset) {
        return LocalDateTime.now().plusDays(offset);
    }

    /**
     * 秒数偏移
     * @param offset 偏移数，正数向未来偏移，负数向过去偏移
     * @return String [yyyy-MM-dd HH:mm:ss]
     */
    public static String plusSecondsToStr(long offset) {
        return LocalDateTime.now().plusDays(offset).format(SystemConst.FORMATTER1);
    }

    // ------------------- 日期比较 ----------------

    /**
     * 当前日期是否在指定日期之后 (时间图： 指定日期 ——> 当前日期 )
     * @param localDate
     * @return
     */
    public static Boolean isAfter(LocalDate localDate){
        return LocalDate.now().isAfter(localDate);
    }

    /**
     * 当前日期是否在指定日期之后 (时间图： 指定日期 ——> 当前日期 )
     * @param localDateTime
     * @return
     */
    public static Boolean isAfter(LocalDateTime localDateTime){
        return LocalDateTime.now().isAfter(localDateTime);
    }

    /**
     * 当前日期是否在指定日期之前 (时间图： 当前日期 ——> 指定日期 )
     * @param localDate
     * @return
     */
    public static Boolean isBefore(LocalDate localDate){
        return LocalDate.now().isBefore(localDate);
    }

    /**
     * 当前日期是否在指定日期之前 (时间图： 当前日期 ——> 指定日期 )
     * @param localDateTime
     * @return
     */
    public static Boolean isBefore(LocalDateTime localDateTime){
        return LocalDateTime.now().isBefore(localDateTime);
    }

    /**
     * 当前日期与指定日期是否同一天
     * @param localDate
     * @return
     */
    public static Boolean isEqual(LocalDate localDate){
        return LocalDate.now().isEqual(localDate);
    }

    /**
     * 当前日期与指定日期是否同一天
     * @param localDateTime
     * @return
     */
    public static Boolean isEqual(LocalDateTime localDateTime){
        LocalDateTime now = LocalDateTime.now();
        if(localDateTime.getYear() == now.getYear() && localDateTime.getMonthValue() == now.getMonthValue()
                && localDateTime.getDayOfMonth() == now.getDayOfMonth()){
            return true;
        }
        return false;
    }

    /**
     * date1是否在date2之后  (时间图 date2 ——> date1)
     * @param date1
     * @param date2
     * @return
     */
    public static Boolean isAfter(LocalDate date1, LocalDate date2){
        return date1.isAfter(date2);
    }

    /**
     * date1是否在date2之后 (时间图 date2 ——> date1)
     * @param date1
     * @param date2
     * @return
     */
    public static Boolean isAfter(LocalDateTime date1, LocalDateTime date2){
        return date1.isAfter(date2);
    }

    /**
     * date1是否在date2之前 (时间图 date1 ——> date2)
     * @param date1
     * @param date2
     * @return
     */
    public static Boolean isBefore(LocalDate date1, LocalDate date2){
        return date1.isBefore(date2);
    }

    /**
     * date1是否在date2之前 (时间图 date1 ——> date2)
     * @param date1
     * @param date2
     * @return
     */
    public static Boolean isBefore(LocalDateTime date1, LocalDateTime date2){
        return date1.isBefore(date2);
    }

    /**
     * date1和date2是否同一天
     * @param date1
     * @param date2
     * @return
     */
    public static Boolean isEqual(LocalDate date1, LocalDate date2){
        return date1.isEqual(date2);
    }

    /**
     * date1和date2是否同一天
     * @param date1
     * @param date2
     * @return
     */
    public static Boolean isEqual(LocalDateTime date1, LocalDateTime date2){
        if(date1.getYear() == date2.getYear() && date1.getMonthValue() == date2.getMonthValue()
                && date1.getDayOfMonth() == date2.getDayOfMonth()){
            return true;
        }
        return false;
    }

    /**
     * 当前日期是否在【beginDate,endDate】之间
     * @param beginDate  日期开始时间
     * @param endDate  日期结束时间
     * @return
     */
    public static Boolean isIn(LocalDateTime beginDate, LocalDateTime endDate){
        return isAfter(beginDate) && isBefore(endDate);
    }

    /**
     * 当前日期是否在【beginDate,endDate】之间
     * @param beginDate  日期开始时间
     * @param endDate  日期结束时间
     * @return
     */
    public static Boolean isIn(LocalDate beginDate, LocalDate endDate){
        return isAfter(beginDate) && isBefore(endDate);
    }

    /**
     * date是否在【beginDate,endDate】之间
     * @param date 被比较的时间
     * @param beginDate 日期开始时间
     * @param endDate 日期结束时间
     * @return
     */
    public static Boolean isIn(LocalDate date, LocalDate beginDate, LocalDate endDate){
        return isAfter(date,beginDate) && isBefore(date,endDate);
    }

    /**
     * date是否在【beginDate,endDate】之间
     * @param date 被比较的时间
     * @param beginDate 日期开始时间
     * @param endDate 日期结束时间
     * @return
     */
    public static Boolean isIn(LocalDateTime date, LocalDateTime beginDate, LocalDateTime endDate){
        return isAfter(date,beginDate) && isBefore(date,endDate);
    }

    // -------------------- 日期格式化 -----------------
    /**
     * 按照指定格式格式化日期
     * @param localDateTime 格式化日期
     * @param format 格式
     *        yyyy-MM-dd HH:mm:ss
     *        yyyy-MM-dd HH:mm
     *        yyyy-MM-dd
     *        yyyy/MM/dd HH:mm:ss
     *        yyyy/MM/dd HH:mm
     *        yyyy/MM/dd
     *        HH:mm:ss
     *        yyyyMMdd
     *        HHmmss
     * @return
     */
    public static String format(LocalDateTime localDateTime, String format){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(dateTimeFormatter);
    }

    /**
     * 字符串转日期
     * @param dateStr yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static LocalDateTime parseDateTime(String dateStr){
        return LocalDateTime.parse(dateStr, SystemConst.FORMATTER1);
    }

    /**
     * 字符串转日期
     * @param dateStr yyyy-MM-dd
     * @return yyyy-MM-dd
     */
    public static LocalDate parseDate(String dateStr){
        return LocalDate.parse(dateStr, SystemConst.FORMATTER5);
    }

    /**
     * 字符串转日期
     * @param dateStr HH:mm:ss
     * @return HH:mm:ss
     */
    public static LocalTime parseTime(String dateStr){
        return LocalTime.parse(dateStr, SystemConst.FORMATTER7);
    }


    // ------------------- 日期相差时长 -----------------

    /** 相差时长
     * @param start 开始时间
     * @param end 结束时间
     * @return 相差天数
     */
    public static long differDays(LocalDateTime start, LocalDateTime end){
        Duration duration = Duration.between(start,end);
        return duration.toDays();
    }

    /** 相差时长
     * @param start 开始时间
     * @param end 结束时间
     * @return 相差小时数
     */
    public static long differHours(LocalDateTime start, LocalDateTime end){
        Duration duration = Duration.between(start,end);
        return duration.toHours();
    }

    /** 相差时长
     * @param start 开始时间
     * @param end 结束时间
     * @return 相差分钟数
     */
    public static long differMinutes(LocalDateTime start, LocalDateTime end){
        Duration duration = Duration.between(start,end);
        return duration.toMinutes();
    }

    /** 相差时长
     * @param start 开始时间
     * @param end 结束时间
     * @return 相差秒数
     */
    public static long differSeconds(LocalDateTime start, LocalDateTime end){
        Duration duration = Duration.between(start,end);
        return duration.toMillis()/1000;
    }

    /** 相差时长
     * @param start 开始时间
     * @param end 结束时间
     * @return 相差毫秒数
     */
    public static long differMillis(LocalDateTime start, LocalDateTime end){
        Duration duration = Duration.between(start,end);
        return duration.toMillis();
    }


    /**
     * 开始日期到结束日期相差时间
     * @param beginDate
     * @param endDate
     * @return 相差时间（格式：x天 x时 x分 x秒）
     */
    public static String betweenDate(LocalDateTime beginDate, LocalDateTime endDate){
        Duration duration = Duration.between(beginDate,endDate);
        StringBuilder stringBuilder = new StringBuilder(32);
        long hours = duration.toHours()%24;
        long minutes = duration.toMinutes()%60;
        long seconds = (duration.toMillis()/1000)%60;
        stringBuilder.append(duration.toDays()).append("天")
                .append(hours).append("时")
                .append(minutes).append("分")
                .append(seconds).append("秒");
        return stringBuilder.toString();
    }

    /**
     * 开始日期到结束日期相差时间
     * @param beginDate
     * @param endDate
     * @return 相差时间（格式：x天 x时 x分 x秒）
     */
    public static String betweenDate(Date beginDate, Date endDate) {
        if (ObjectUtil.isNull(beginDate) || ObjectUtil.isNull(endDate)){
            return "";
        }
        long nd = 1000 * 24 * 60 * 60L;
        long nh = 1000 * 60 * 60L;
        long nm = 1000 * 60L;
        long ns = 1000L;

        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - beginDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = diff % nd % nh % nm /ns;

        return new StringBuilder().append(day).append( "天").append(hour).append("时")
                .append(min).append("分").append(sec).append("秒").toString();
    }
}
