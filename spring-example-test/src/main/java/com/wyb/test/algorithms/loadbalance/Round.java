package com.wyb.test.algorithms.loadbalance;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marcher丶
 * 轮询
 */
public class Round {

    public static void main(String[] args) {
        List<String> servers = Arrays.asList("127.0.0.1", "127.0.0.2",
                "127.0.0.3");

        for (int i = 0; i < 10; i++) {
            int index = i % servers.size();
            System.out.println(servers.get(index));
        }

    }
}
