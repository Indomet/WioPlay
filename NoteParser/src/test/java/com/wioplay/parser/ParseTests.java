package com.wioplay.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import com.wioplay.parser.Core.Parser;

public class ParseTests {

    @Test
    public void shouldNotSplitSameBar() {

        Parser parser = new Parser();
        String notes = """
                    5|e--D--e--D--e-----d--c----|
                    4|---------------b--------a-|
                    """;
        String[] chunks = {notes.trim()};
        assertEquals(Arrays.toString(chunks), Arrays.toString(parser.splitBars(notes)));

    }

    @Test
    public void shouldSplitDistinctBars() {

        Parser parser = new Parser();
        String notes = """
                    5|e--D--e--D--e-----d--c----|
                    
                    4|---------------b--------a-|
                    """;
        String[] chunks = {"5|e--D--e--D--e-----d--c----|", "4|---------------b--------a-|"};
        assertEquals(Arrays.toString(chunks), Arrays.toString(parser.splitBars(notes)));

    }

    @Test
    public void shouldParseCorrectly() {

        Parser parser = new Parser();
        String bar = "5|e--D--e|";
        String[] bars = parser.splitBars(bar);
        String[] result = parser.parse(bars);
        assertEquals("[NOTE_E5, 0, 0, NOTE_DS5, 0, 0, NOTE_E5]", Arrays.toString(result));

    }


}