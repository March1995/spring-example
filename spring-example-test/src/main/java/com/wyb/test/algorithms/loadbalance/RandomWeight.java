package com.wyb.test.algorithms.loadbalance;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Marcher丶
 * 按权重随机
 */
public class RandomWeight {

    public static void main(String[] args) {
        List<Invoker> invokers = Arrays.asList(
                new Invoker(20),
                new Invoker(30),
                new Invoker(50)
        );


        int[] weight = new int[invokers.size()];
        int totalWeight = 0;
        for (int i = 0; i < invokers.size(); i++) {
            totalWeight += invokers.get(i).getWeight();
            weight[i] = totalWeight;
        }
        Random random = new Random();
        int randomNum = random.nextInt(totalWeight);
        for (int i = 0; i < weight.length; i++) {
            if (randomNum < weight[i]) {
                System.out.println(invokers.get(i).getWeight());
            }
        }
    }
}

