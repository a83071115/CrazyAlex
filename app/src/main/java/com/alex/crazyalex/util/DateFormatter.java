package com.alex.crazyalex.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * long类型转换String,并且转换成时间
 * Created by Administrator on 2017/3/16.
 */

public class DateFormatter {
    /**
     * 将long类型date转换为String类型
     */
    public String ZhiHuDailyDeteFormat(long date){
        String sDate;
        Date d = new Date(date + 24*60*60*1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        sDate = format.format(d);
        return sDate;
    }

    public String DoubanDateFormat(long date){
        String sDate;
        Date d = new Date(date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        sDate = format.format(d);

        return sDate;
    }
}
