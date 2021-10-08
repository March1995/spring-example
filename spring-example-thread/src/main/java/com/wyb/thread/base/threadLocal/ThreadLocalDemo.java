package com.wyb.thread.base.threadLocal;


/**
 * @author Kunzite
 * ThreadLocal的具体使用方法
 */
public class ThreadLocalDemo {

    //存放线程变量
    private static ThreadLocal<Integer> seqNum = new ThreadLocal<Integer>() {
        public Integer initialValue() {
            return 0;
        }
    };//①通过匿名内部类覆盖ThreadLocal的initialValue()方法，指定初始值

    //②获取下一个序列值
    public int getNextNum() {
        seqNum.set(seqNum.get() + 1);
        return seqNum.get();
    }

    //存放线程变量
    private static ThreadLocal<Integer> seqNum1 = new ThreadLocal<Integer>() {
        @Override
        public Integer initialValue() {
            return 0;
        }
    };//①通过匿名内部类覆盖ThreadLocal的initialValue()方法，指定初始值


    private static class TestClient extends Thread {

        private ThreadLocalDemo sn;

        public TestClient(ThreadLocalDemo sn) {
            this.sn = sn;
        }

        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                int a = seqNum1.get() + 1;
                seqNum1.set(a);
                //④每个线程打出3个序列值
                System.out.println("thread[" +
                        Thread.currentThread().getName() + "]sn[" +
                        a + "]");
            }
        }
    }

    private static class TestClient1 extends Thread {

        private ThreadLocalDemo sn;

        public TestClient1(ThreadLocalDemo sn) {
            this.sn = sn;
        }

        public void run() {
            for (int i = 0; i < 5; i++) {
                int a = seqNum1.get() + 1;
                seqNum1.set(a);
                //④每个线程打出3个序列值
                System.out.println("thread[" +
                        Thread.currentThread().getName() + "]sn[" +
                        a + "]");
            }
        }
    }

    public static void main(String[] args) {
        ThreadLocalDemo sn = new ThreadLocalDemo();
        //③ 3个线程共享sn，各自产生序列号
        TestClient t1 = new TestClient(sn);
        TestClient1 t2 = new TestClient1(sn);
        TestClient t3 = new TestClient(sn);
        t1.start();
        t2.start();
        t3.start();
    }
}
