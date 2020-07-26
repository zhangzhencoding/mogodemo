package com.angsi.shop.repository;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class okNowTimeUtil {
    /**
     *
     * @param startTime1	开始时间
     * @param endTime1   结束时间
     * @return
     * @author sunran   判断当前时间在时间区间内
     */
    public static boolean isEffectiveDate(String startTime1, String endTime1) {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = ft.parse(startTime1);
            endTime = ft.parse(endTime1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date nowTime = new Date();

        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
}
