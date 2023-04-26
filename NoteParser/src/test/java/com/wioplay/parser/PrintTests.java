package com.wioplay.parser;

import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.PrintStream;
import java.util.ArrayList;
import com.wioplay.parser.Utils.IOScanner;


import static org.junit.jupiter.api.Assertions.assertEquals;


public class PrintTests {

    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;
    private static final PrintStream originalErr = System.err;

    private static String NEW_LINE = System.lineSeparator();

    //redirect output stream to referenced data so that we can test
    @BeforeAll
    public static void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    // clear the stream before every test
    @BeforeEach
    public void clearStream() {
        outContent.reset();
    }

    //restore the streams to their original targets after all tests have concluded
    @AfterAll
    public static void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void shouldSeparateCorrectly() {
        IOScanner.printSeparator();
        assertEquals("----------", outContent.toString());
    }

    @Test
    public void shouldListCorrectly() {

        ArrayList<String> fruits = new ArrayList<>();
        fruits.add("apple");
        fruits.add("orange");
        fruits.add("pear");

        IOScanner.printList("Fruits", fruits);
        String result = "Fruits:" + NEW_LINE +
        "\t0- apple" + NEW_LINE +
        "\t1- orange" + NEW_LINE +
        "\t2- pear";

        assertEquals(result, outContent.toString());

    }


}
