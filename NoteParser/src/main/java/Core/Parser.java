package Core;

/*
This class handles all processing of musical notes to an array of frequencies
which can then be used in arduino
*/

import java.util.HashSet;

public class Parser {

    public static String[] splitBars(String content) {
        return content.trim().split("\n\n");
    }

    public static String[] parse(String[] sequences) {

        StringBuilder result = new StringBuilder();

        for (String sequence : sequences) {

            String[] rows = sequence.split("\n");
            HashSet<Integer> slot = new HashSet<>();

            for (int i = 0; i < rows.length; i++) {

                for (int k = 2; k < rows[i].length() - 1; k++) {

                    if(slot.contains(k)) continue;
                    char octave = rows[i].charAt(0);
                    char letter = rows[i].charAt(k);

                    int j = i + 1;
                    while (letter == '-' && j < rows.length) {
                        octave = rows[j].charAt(0);
                        letter = rows[j++].charAt(k);
                    }

                    //add position to used up slots so next row doesn't count if present
                    slot.add(k);

                    if(letter == '-') {
                        result.append('0').append(',');
                    }

                    else {
                        result
                                .append("NOTE_")
                                .append((parseNote(letter) + octave).toUpperCase())
                                .append(",");
                    }
                }
            }
        }

        String content = result.toString();
        return content.substring(0, content.length() - 1).split(",");
    }

    private static String parseNote(char x) {
        StringBuilder y = new StringBuilder();
        y.append(Character.toUpperCase(x));
        if(Character.isUpperCase(x)) {
            y.append("S");
        }
        return y.toString();
    }

}
