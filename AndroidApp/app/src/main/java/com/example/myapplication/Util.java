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

    public static String[] splitStringInSegments(String initialString, int targetN, int maxCharBoundary)
    {
        String[] segments = new String[0];
        int subStringLength = 0;

        try
        {
            subStringLength = (int)Math.ceil((double)initialString.length() / targetN);

            if (subStringLength > maxCharBoundary)
            {
                targetN = findMaxN(maxCharBoundary, initialString.length());
                subStringLength = maxCharBoundary;
            }

            segments = new String[targetN];
        }
        catch (NegativeArraySizeException exception)
        {
            System.out.println(exception.getMessage());
        }

        for (int i = 0; i < targetN; i++)
        {
            int startIdx = i * subStringLength;
            int endIdx = Math.min((i + 1) * subStringLength, initialString.length());

            segments[i] = initialString.substring(startIdx, endIdx);
        }

        return segments;
    }

    // n * segmentLength = initialStirng <=> n * maxCharBoundary = initialString
    // Find the number of segments/elements to return of the intitial string where each string is bounded by the predefined max-length
    private static int findMaxN(int maxCharBoundary, int initialStringLength)
    {
        try
        {
            return initialStringLength / maxCharBoundary;
        }
        catch (ArithmeticException exception)
        {
            System.out.println(exception.getMessage());
        }

        return -1;
    }

    public static int[][] splitIntegerArrayInSegments(int[] allSongNotes, int targetN, int maxCharBoundary)
    {
        int[][] segments = new int[targetN][];
        int subArrLength = 0;

        try
        {
            subArrLength = (int)Math.ceil((double)allSongNotes.length / targetN);

            if (subArrLength > maxCharBoundary)
            {
                targetN = findMaxN(maxCharBoundary, allSongNotes.length);
                subArrLength = maxCharBoundary;
            }

            segments = new int[targetN][];
        }
        catch (NegativeArraySizeException exception)
        {
            System.out.println(exception.getMessage());
        }

        int x = 0; // current element in sub-arr
        int y = 0; // current sub-arr
        int t = 0; // total
        segments[0] = new int[subArrLength];
        while (y < targetN)
        {
            segments[y][x] = allSongNotes[t];


            if (++x == subArrLength)
            {
                x = 0;
                y++;

                if (y == targetN) // Remove this break later
                    break;

                segments[y] = new int[subArrLength]; // Note: for the last iteration, subtract the space of the array
            }

            // x++;
            t++;
        }

        return segments;
    }


    public static int[][] chunkify(int[] array, int chunkSize) {
        int numOfChunks = (int) Math.ceil((double) array.length / chunkSize);
        int[][] result = new int[numOfChunks][chunkSize];

        int arrayIndex = 0;
        for (int i = 0; i < numOfChunks; i++) {
            for (int j = 0; j < chunkSize; j++) {
                if (arrayIndex < array.length) {
                    result[i][j] = array[arrayIndex];
                    arrayIndex++;
                } else {
                    break;
                }
            }
        }

        return result;
    }


}
