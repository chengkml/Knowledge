package com.ck.common.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    private final static SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final static SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");

    public static String formatDateTime(Date date) {
        return dateTimeSdf.format(date);
    }

}
