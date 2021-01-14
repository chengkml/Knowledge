package com.ck.common.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IdHelper {

    public static String getProcessId() {
        return getId("process");
    }

    private static String getId(String type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return type + sdf.format(new Date());
    }

}
