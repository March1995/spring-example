package com.wyb.test.algorithms.loadbalance;

/**
 * @author Marcher丶
 */
class Invoker {

    private String name;
    /**
     * 默认权重
     */
    private int weight;

    /**
     * 当前权重
     */
//    private AtomicLong current = new AtomicLong(0);
    private int current = 0;

    public Invoker(int weight) {
        this.weight = weight;
    }

    public Invoker(int weight, String name) {
        this.weight = weight;
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
//        current.set(0);
    }


    public long increaseCurrent() {
//        return current.addAndGet(weight);
        current = current + weight;
        return current;
    }

    public void sel(int total) {
//        current.addAndGet(-1 * total);
        current = current - total;
    }

    @Override
    public String toString() {
        return "{name " + name +
                ", weight=" + weight +
                ", current=" + current +
                '}';
    }
}
