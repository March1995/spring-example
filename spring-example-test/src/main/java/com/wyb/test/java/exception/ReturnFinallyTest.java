package com.wyb.test.java.exception;

/**
 * try.catch 中的return在执行完finally的代码后再执行，若finally有return不执行之前的  return返回的值为各模块的值 互不影响
 * <p>
 * 1、finally中的代码总会被执行(用一种情况finally中的代码不会执行，那就是在try{}块中包含遇到System.exit(0))。
 * 2、当try、catch中有return时，也会执行finally。return的时候，要注意返回值的类型，是否受到finally中代码的影响。如果是基本数据类型，返回的是try、catch中的值，是引用数据类型时，返回finally中的值
 * 3、finally中有return时，会直接在finally中退出，导致try、catch中的return失效。
 *
 * @author Kunzite
 */
public class ReturnFinallyTest {

    public ReturnFinallyTest() {
    }

    boolean testEx() throws Exception {
        boolean ret = true;
        try {
            ret = testEx1();
        } catch (Exception e) {
            System.out.println("testEx, catch exception");
            ret = false;
            throw e;
        } finally {
            System.out.println("testEx, finally; return value=" + ret);
            return ret;
        }
    }

    boolean testEx1() throws Exception {
        boolean ret = true;
        try {
            ret = testEx2();
            if (!ret) {
                return false;
            }
            System.out.println("testEx1, at the end of try");
            return ret;
        } catch (Exception e) {
            System.out.println("testEx1, catch exception");
            ret = false;
            throw e;
        } finally {
            System.out.println("testEx1, finally; return value=" + ret);
            return ret;
        }
    }

    boolean testEx2() throws Exception {
        boolean ret = true;
        try {
            int b = 12;
            int c;
            for (int i = 2; i >= -2; i--) {
                c = b / i;
                System.out.println("i=" + i);
            }
            return true;
        } catch (Exception e) {
            System.out.println("testEx2, catch exception");
            ret = false;
            throw e;
        } finally {
            ret = !ret;
            System.out.println("testEx2, finally; return value=" + !ret);
//            return ret;
        }
    }

    public static void main(String[] args) {
        ReturnFinallyTest testException1 = new ReturnFinallyTest();
        try {
            testException1.testEx();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
