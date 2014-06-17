package com.indivisible.clearmeout.util;

/**
 * Created by indiv on 17/06/14.
 */
public class Utils
{

    //ASK: generics? prob not worth the hassle to cast the array
    public static String[] appendToArray(String[] baseArray, String newElement)
    {
        String[] newArray = new String[baseArray.length + 1];
        for (int i = 0; i < baseArray.length; i++)
        {
            newArray[i] = baseArray[i];
        }
        newArray[newArray.length - 1] = newElement;
        return newArray;
    }
}
