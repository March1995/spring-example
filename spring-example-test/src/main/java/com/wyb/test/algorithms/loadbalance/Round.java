package com.wyb.test.algorithms.loadbalance;


import java.util.Arrays;
import java.util.List;

/**
 * @author Marcher丶
 * 普通轮询
 */
public class Round {

    static List<String> servers = Arrays.asList("127.0.0.1", "127.0.0.2",
            "127.0.0.3");
    static int current = 0;

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            System.out.println(getInvoker());
        }

    }

    private static String getInvoker() {
        if (current >= servers.size()) {
            current = 0;
        }
        String ip = servers.get(current);
        current++;
        return ip;
    }
}
