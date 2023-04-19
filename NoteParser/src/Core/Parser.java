package Core;

/*
This class handles all processing of musical notes to an array of frequencies
which can then be used in arduino
*/

public class Parser {

    public static String[] splitBars(String content) {
        return content.trim().split("\n\n");
    }

}
