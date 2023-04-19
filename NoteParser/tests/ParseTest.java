import Core.Parser;
import Utils.IOScanner;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParseTest {

    @Test
    public void shouldNotSplitSameBar() {

            String notes = """
                    5|e--D--e--D--e-----d--c----|
                    4|---------------b--------a-|
                    """;
            String[] chunks = {notes.trim()};
            assertEquals(Arrays.toString(chunks), Arrays.toString(Parser.splitBars(notes)));

    }

    @Test
    public void shouldSplitDistinctBars() {

        String notes = """
                    5|e--D--e--D--e-----d--c----|
                    
                    4|---------------b--------a-|
                    """;
        String[] chunks = {"5|e--D--e--D--e-----d--c----|", "4|---------------b--------a-|"};
        assertEquals(Arrays.toString(chunks), Arrays.toString(Parser.splitBars(notes)));

    }


}
