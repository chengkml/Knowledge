package doc;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Title: Test
 * @Description: TODO
 * @Author: Chengkai
 * @Date: 2019/11/14 17:54
 * @Version: 1.0
 */
public class Test {

    public static void main(String[] args){
        System.out.println(getCodeFromField("一体机数据源DWPUB(DWDB)"));
    }

    private static String getCodeFromField(String content) {
        if(StringUtils.isBlank(content)) {
            return "";
        }
        int index = content.lastIndexOf("(");
        if(index==-1) {
            return "";
        }
        String temp = content.substring(index);
        String reg = "\\((.+?)\\)";
        Pattern pt = Pattern.compile(reg);
        Matcher mc = pt.matcher(temp);
        if(mc.find()) {
            return mc.group(1);
        }else {
            return "";
        }
    }
}
