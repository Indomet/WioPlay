package com.example.myapplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;


public class Util {

    public static String objectToJSON(Object object) throws IllegalArgumentException, IllegalAccessException {
        ObjectWriter jsonWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = null;
        try {
            json = jsonWriter.writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    // Note for developers: If the sub-string-length is uneven,
    // each segment has length n except the last one which has
    // a length of initialString.length - ( subString.length * (n-1)).
    // In plain English this means that when the length of 'initialString'
    // can't be divided evenly in n-pieces, the last element in 'segments'
    // has a string with a length equivalent to the remaining characters
    // required to include the entire 'intialString', while the other
    // string-elements has length 'subStringLength'
    public static String[] splitStringInSegments(String initialString, int n)
    {
        String[] segments = new String[0];
        int subStringLength = 0;

        try
        {
            segments = new String[n];
            subStringLength = (int)Math.ceil((double)initialString.length() / n);
        }
        catch (NegativeArraySizeException exception)
        {
            System.out.println(exception.getMessage());
        }

        // Formula:
        // (0, subStringLength)
        // (subStringLength, 2 * subStringLength)
        // (2 * subStringLength, 3 * subStringLength)
        // . . .
        // ((n-1) * subStringLength, n * subStringLength)

        for (int i = 0; i < n; i++)
        {
            int startIdx = i * subStringLength;
            int endIdx = Math.min((i + 1) * subStringLength, initialString.length());

            segments[i] = initialString.substring(startIdx, endIdx);
        }

        return segments;
    }
}
