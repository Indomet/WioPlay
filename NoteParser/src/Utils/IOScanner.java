package Utils;

import java.util.ArrayList;
import java.util.Scanner;

public class IOScanner {

    private final static Scanner scanner =  new Scanner(System.in);

    private static void print(String message) {
        System.out.print(message);
    }

    private static void println(String message) {
        System.out.println(message);
    }

    public static void printSeparator() {
        print("----------");
    }

    public static void printList(String title, ArrayList list) {

        int lastIndex = list.size() - 1;
        println(title + ":");
        for (int i = 0; i < lastIndex; i++) {
            println("\t" + i + "- " + list.get(i));
        }
        print("\t"+ lastIndex + "- " + list.get(lastIndex));

    }

}
