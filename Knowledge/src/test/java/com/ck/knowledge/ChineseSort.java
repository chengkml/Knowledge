package com.ck.knowledge;

import org.junit.Test;

import java.text.Collator;
import java.util.*;
import java.util.stream.Collector;


/**
 * @Title: ChineseSort
 * @Description: TODO
 * @Author: Chengkai
 * @Date: 2019/10/29 17:43
 * @Version: 1.0
 */
public class ChineseSort {

    @Test
    public void testSort(){
        Collator collector = Collator.getInstance(Locale.CHINA);
        List<String> values = Arrays.asList(new String[]{"我们","成功","吊炸天","ck"});
        Collections.sort(values,(s1,s2)->{return collector.compare(s1,s2);});
        System.out.println(values);
    }

    @Test
    public void test(){
        Map<String,Object> map = new HashMap<>();
        map.put("TABLENAME","asdfsaf");
        System.out.println(map);
    }
}
