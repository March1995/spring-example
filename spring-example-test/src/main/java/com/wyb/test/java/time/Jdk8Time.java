package com.wyb.test.java.time;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author Marcher丶
 * doc:
 * https://mp.weixin.qq.com/mp/appmsgalbum?__biz=MzI0MTUwOTgyOQ==&action=getalbum&album_id=1696358010555547649&scene=173&from_msgid=2247491866&from_itemidx=1&count=3&nolastread=1#wechat_redirect
 */
public class Jdk8Time {

    public static void main(String[] args) {
        Instant now = Instant.now();
        now.getEpochSecond();// 秒
        now.toEpochMilli();// 毫秒
        Instant afterPlusSecondInstant = now.plusSeconds(1000); // 秒数增加1000
        boolean b = now.isBefore(afterPlusSecondInstant); // 比较


        LocalDateTime dateTime = LocalDateTime.now();
        System.out.println(dateTime);

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println(format.format(dateTime));
        System.out.println(dateTime.format(format));

        DateTimeFormatter fullFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL);
        DateTimeFormatter longFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
        DateTimeFormatter mediumFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        DateTimeFormatter shortFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
//        System.out.println(dateTime.format(fullFormat));
        System.out.println("longFormat:         " + dateTime.format(longFormat));
        System.out.println("mediumFormat:       " + dateTime.format(mediumFormat));
        System.out.println("shortFormat:        " + dateTime.format(shortFormat));
        System.out.println("iso_date:           " + dateTime.format(DateTimeFormatter.ISO_DATE));
        System.out.println("iso_date_time:      " + dateTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        System.out.println("iso_local_date:     " + dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
        System.out.println("iso_local_date_time:" + dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        // 加一天
        LocalDateTime dayAfterOneDay = dateTime.plusDays(1).plus(20, ChronoUnit.MINUTES);
        System.out.println(dayAfterOneDay.format(format));

        // 算两天的差值
        long year = LocalDateTimeUtils.betweenTwoTime(dateTime, dayAfterOneDay, ChronoUnit.YEARS);
        long month = LocalDateTimeUtils.betweenTwoTime(dateTime, dayAfterOneDay, ChronoUnit.MONTHS);
        long day = LocalDateTimeUtils.betweenTwoTime(dateTime, dayAfterOneDay, ChronoUnit.DAYS);
        long hour = LocalDateTimeUtils.betweenTwoTime(dateTime, dayAfterOneDay, ChronoUnit.HOURS);
        long minute = LocalDateTimeUtils.betweenTwoTime(dateTime, dayAfterOneDay, ChronoUnit.MINUTES);
        long second = LocalDateTimeUtils.betweenTwoTime(dateTime, dayAfterOneDay, ChronoUnit.SECONDS);
        System.out.println("两者相差" + year + "年" + month + "月" + day + "天" + hour + "小时" + minute + "分钟" + second + "秒");

        // 时区问题 本地时区生成dateTime 设置到UTC+0时间戳相当于多了8个小时，再进行北京时间格式化就相当于比北京时间多8小时 标准UTC时间应为北京时间少8小时 比标准UTC时间多16小时
        System.out.println(dateTime.toEpochSecond(ZoneOffset.UTC));
        System.out.println(dateTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli());
        // 获取秒数 （东8区，也就是北京时间）
        System.out.println(dateTime.toEpochSecond(ZoneOffset.of("+8")));
        // 获取毫秒数 （东8区，也就是北京时间）
        System.out.println(dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        System.out.println(dateTime.getLong(ChronoField.NANO_OF_DAY));// ?


        // LocalDateTime、Instant、与Date的转换
        Date instantToDate = Date.from(Instant.now());// Instant转换为Date
        Instant dateToInstant = new Date().toInstant();// Date转换为Instant
        // Date 与 LocalDateTime 的转换是通过 Instant 中间的转换来进行的
        LocalDateTime localDateTime = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()); // Date转换为LocalDateTime
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
