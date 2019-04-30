package com.example.yzwy.lprmag.test;


public class TryFinallyTest {
    public static void main(String[] args) {
        int i = initTryFinally();
        System.out.println("Result output:" + String.valueOf(i));
    }

    /**
     * @return int
     */
    private static int initTryFinally() {
        try {
            return 0;
        } catch (Exception e) {
            return 2;
        } finally {
            return 1;
        }
    }

}
