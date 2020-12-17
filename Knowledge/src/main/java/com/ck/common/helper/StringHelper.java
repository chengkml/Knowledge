package com.ck.common.helper;

import org.slf4j.helpers.MessageFormatter;

public class StringHelper {

    public static String format(String format, Object... args){
        return MessageFormatter.arrayFormat(format, args).getMessage();
    }

}
