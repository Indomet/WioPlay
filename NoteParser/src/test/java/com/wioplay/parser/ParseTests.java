<<<<<<<< HEAD:NoteParser/src/test/java/com/wioplay/parser/ParseTests.java
package com.wioplay.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
========
import org.junit.jupiter.api.Assertions;
>>>>>>>> main:NoteParser/src/test/java/TestParse.java
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import com.wioplay.parser.Core.Parser;

<<<<<<<< HEAD:NoteParser/src/test/java/com/wioplay/parser/ParseTests.java
public class ParseTests {
========
public class TestParse {
>>>>>>>> main:NoteParser/src/test/java/TestParse.java

    @Test
    public void shouldNotSplitSameBar() {

            String notes = """
                    5|e--D--e--D--e-----d--c----|
                    4|---------------b--------a-|
                    """;
            String[] chunks = {notes.trim()};
            Assertions.assertEquals(Arrays.toString(chunks), Arrays.toString(Parser.splitBars(notes)));

    }

    @Test
    public void shouldSplitDistinctBars() {

        String notes = """
                    5|e--D--e--D--e-----d--c----|
                    
                    4|---------------b--------a-|
                    """;
        String[] chunks = {"5|e--D--e--D--e-----d--c----|", "4|---------------b--------a-|"};
        Assertions.assertEquals(Arrays.toString(chunks), Arrays.toString(Parser.splitBars(notes)));

    }

    @Test
    public void shouldParseCorrectly() {

        String bar = "5|e--D--e|";
        String[] bars = Parser.splitBars(bar);
        String[] result = Parser.parse(bars);
        Assertions.assertEquals("[NOTE_E5, 0, 0, NOTE_DS5, 0, 0, NOTE_E5]", Arrays.toString(result));

    }


}
