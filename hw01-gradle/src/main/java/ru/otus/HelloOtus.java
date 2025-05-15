package ru.otus;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class HelloOtus {
    public static void main(String[] args) {
        String hello = "Hello, Otus!";
        System.out.println("============================================");
        System.out.println("Before: " + hello);
        Iterable<String> strings = Splitter.on("Hello,").trimResults().split(hello);
        String result = Joiner.on("After: I prefer learning Java at ").join(strings);
        System.out.println("============================================");
        System.out.println(result);
        System.out.println("============================================");
    }
}
