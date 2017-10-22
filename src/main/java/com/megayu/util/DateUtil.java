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
    public static String editDateTime(Date date){
        String createtimestr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        return createtimestr;
    }

    public static void main(String[] args){
        String aa = "哦时代峻峰卡兰蒂斯房间卡23423dfasdf";
        String bb = "我们是共产主义一啊啊啊啊";
        System.out.println(aa.length());
        System.out.println(bb.length());
    }

}
