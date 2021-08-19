package com.wyb.test.algorithms.loadbalance;

/**
 * @author Marcher丶
 */
class Invoker {

    private int weight;

    public Invoker(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
