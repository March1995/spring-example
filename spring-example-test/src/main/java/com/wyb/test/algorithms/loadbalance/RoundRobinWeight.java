package com.wyb.test.algorithms.loadbalance;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marcher丶
 * 权重轮询
 */
public class RoundRobinWeight {


    private static Invoker doSelect(List<Invoker> invokers) {
        Invoker selectInvoker = null;
        int totalWeight = 0;
        long maxCurrent = Long.MIN_VALUE;
        for (Invoker invoker : invokers) {
            int weight = invoker.getWeight();
            long currentWidth = invoker.increaseCurrent();
            if (currentWidth > maxCurrent) {
                maxCurrent = currentWidth;
                selectInvoker = invoker;
            }
            totalWeight += weight;
        }
        if (null != selectInvoker) {
            selectInvoker.sel(totalWeight);
        }
        return selectInvoker;
    }

    public static void main(String[] args) {

        List<Invoker> invokers = Arrays.asList(
                new Invoker(2, "调用1"),
                new Invoker(7, "调用2"),
                new Invoker(1, "调用3")
        );
        for (int i = 0; i < 10; i++) {
            System.out.println(doSelect(invokers));
        }

    }
}
