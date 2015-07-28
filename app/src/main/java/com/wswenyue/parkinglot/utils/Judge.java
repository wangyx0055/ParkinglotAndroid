package com.wswenyue.parkinglot.utils;

public class Judge {
    //是奇数返回true， 偶数返回false
    public static boolean isOdd(int n){
        return (n & 1) != 0;
    }
}