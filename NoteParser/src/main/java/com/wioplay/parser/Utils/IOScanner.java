package com.wioplay.parser.Utils;

import java.util.ArrayList;
import java.util.Scanner;

public class IOScanner {

    private final static Scanner scanner =  new Scanner(System.in);

    public static void printSeparator() {
        String separator = "";
        while(separator.length() < 10) {
            separator += "-";
        }
        print(separator);
    }

    public static void printList(String title, ArrayList<String> list) {

        int lastIndex = list.size() - 1;
        println(title + ":");
        for (int i = 0; i < lastIndex; i++) {
            println(formatEntry(i, list.get(i)));
        }
        print(formatEntry(lastIndex, list.get(lastIndex)));

    }

    private static String formatEntry(int index, Object element) {
        return "\t" + index + "- " + element.toString();
    }

    public static void print(String message) {
        System.out.print(message);
    }

    public static void println(String message) {
        System.out.println(message);
    }

}
