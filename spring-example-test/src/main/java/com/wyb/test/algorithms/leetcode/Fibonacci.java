package com.wyb.test.algorithms.leetcode;

/**
 * @author Marcher丶
 * 一只羊饲养2个月后，每个月可生只1小羊，目前2只羊，6个月后一共有多少只羊
 * 斐波那契数列
 */
public class Fibonacci {

    public static void main(String[] args) {
        System.out.println(2 * fibonacci(7));
        long P1 = Runtime.getRuntime().freeMemory();
        System.out.println(P1 / 1024);
    }

    private static int fibonacci(int index) {
        if (index == 1 || index == 2) {
            return 1;
        }
        return fibonacci(index - 1) + fibonacci(index - 2);
    }

}
