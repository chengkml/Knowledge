package com.ck.ddl;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DDL解析器
 */
public class DdlResolver {

    private static final String TAB_NAME_REG = "((create)|(CREATE))[\\s]+((table)|(TABLE))[\\s]+[a-zA-Z_]+[\\s]*[(]";

    private static final String STR_PART_REG = "[\\S]+";

    private static final String PRIMARY_KEY_REG = "(PRIMARY|primary)[\\s]+(KEY|key)[\\s]+\\(((?!\\;).)+\\)";

    public static Table resolveDdl(String ddl) {
        Table table = resolveTabPart(getTabStr(ddl));
        table.setColumns(resolveColumnPart(getColumnStr(ddl)));
        String priStr = getPrimaryStr(ddl);
        if (StringUtils.isNotBlank(priStr)) {
            Map<String, Column> colMap = new HashMap<>();
            table.getColumns().forEach(col -> {
                colMap.put(col.getName(), col);
            });
            Arrays.asList(priStr.substring(priStr.indexOf("(") + 1, priStr.indexOf(")")).split(",")).forEach(p -> {
                if (colMap.containsKey(p)) {
                    colMap.get(p).setPk(true);
                }
            });
        }
        return table;
    }

    private static String getPrimaryStr(String ddl) {
        Pattern reg = Pattern.compile(PRIMARY_KEY_REG);
        Matcher matcher = reg.matcher(ddl);
        String str = null;
        while (matcher.find()) {
            str = matcher.group(0);
        }
        return str;
    }

    private static String getColumnStr(String ddl) {
        String str = null;
        if (ddl.contains(";")) {
            str = ddl.substring(0, ddl.indexOf(";"));
        }
        if (str.contains("(") && str.contains(")")) {
            return str.substring(str.indexOf("(") + 1, str.lastIndexOf(")"));
        } else {
            throw new RuntimeException("字段为空！");
        }
    }

    private static String getTabStr(String ddl) {
        Pattern reg = Pattern.compile(TAB_NAME_REG);
        Matcher matcher = reg.matcher(ddl);
        String str = null;
        while (matcher.find()) {
            str = matcher.group(0);
        }
        Objects.requireNonNull(str, "表名不存在！");
        return str;
    }

    private static Table resolveTabPart(String str) {
        Table table = new Table();
        table.setName(str.substring(str.toLowerCase().indexOf("table") + 5, str.indexOf("(")).trim());
        return table;
    }

    private static List<Column> resolveColumnPart(String str) {
        List<String> colStrs = Arrays.asList(str.split(","));
        List<Column> res = new ArrayList<>();
        Pattern reg = Pattern.compile(STR_PART_REG);
        boolean notCol = false;
        for(String ss : colStrs){
            Matcher matcher = reg.matcher(ss);
            Column col = new Column();
            int count = 0;
            String temp;
            while (!notCol&&matcher.find()) {
                notCol = false;
                switch (count) {
                    case 0:
                        String tempP = matcher.group(0);
                        if(!matcher.group(0).equalsIgnoreCase("primary")){
                            col.setName(tempP);
                        }else{
                            notCol = true;
                        }
                        break;
                    case 1:
                        temp = matcher.group(0);
                        if (temp.contains("(")) {
                            col.setDataType(temp.substring(0, temp.indexOf("(")));
                            if(temp.indexOf("(")<temp.indexOf(")")){
                                col.setLength(Integer.parseInt(temp.substring(temp.indexOf("(") + 1, temp.indexOf(")"))));
                            }
                        } else {
                            col.setDataType(temp);
                        }
                        break;
                }
                if (notCol||++count > 1) {
                    break;
                }
            }
            if (!(ss.contains("not") && ss.toString().contains("null"))) {
                col.setNullable(true);
            }
            if(ss.toLowerCase().contains("comment")){
                matcher = reg.matcher(ss.substring(ss.toLowerCase().indexOf("comment")+6));
                while(matcher.find()){
                    temp = matcher.group(0);
                    if(temp.length()>2){
                        col.setComment(temp.substring(1,temp.length()-1));
                    }
                }
            }else if (ss.contains("/*") && ss.contains("*/")) {
                col.setComment(ss.substring(ss.indexOf("/*") + 2, ss.indexOf("*/")));
            }
            if(!notCol){
                res.add(col);
            }
        };
        return res;
    }

    public static List<Table> batchResolveDdls(String ddls) {
        List<Table> res = new ArrayList<>();
        Pattern reg = Pattern.compile(TAB_NAME_REG);
        Matcher matcher = reg.matcher(ddls);
        int startIndex = 0;
        String lastCreateStr = null;
        while(matcher.find()){
            lastCreateStr = matcher.group(0);
            if(lastCreateStr!=null&&startIndex!=0){
                res.add(resolveDdl(ddls.substring(startIndex,ddls.indexOf(lastCreateStr)!=-1?ddls.indexOf(lastCreateStr):ddls.length())));
            }
            startIndex = ddls.indexOf(lastCreateStr);
        }
        res.add(resolveDdl(ddls.substring(startIndex)));
        return res;
    }

}
