package com.megayu.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static String editDate(Date date){
        String yearstr = new SimpleDateFormat("yyyy").format(date);
        String createtimestr = new SimpleDateFormat("MM月dd日 HH:mm").format(date);
        Calendar now = Calendar.getInstance();
        int year =  now.get(Calendar.YEAR);
        if(year!=Integer.valueOf(yearstr)){
            //不是今年
            createtimestr = yearstr+"年"+createtimestr;
        }
        return createtimestr;
    }
}
