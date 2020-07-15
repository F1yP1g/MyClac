package com.lusai.mycalc.tools;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LU on 2018/2/28.
 */

public class DateUtils {
    @SuppressLint("SimpleDateFormat")
    public static String dateToString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        return df.format(date);
    }
}
