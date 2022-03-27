package com.wyb.test.java.map;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @author: Marcher丶
 * @Date: 2022-03-27 20:28
 **/
public class HashMapModCountTest {

    public static void main(String[] args) {
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(1, 2);
        map.put(3, 4);// 必须有2个 一个不会报错 fast-fail
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            map.remove(1);
        }
        System.out.println(map);
    }
}
