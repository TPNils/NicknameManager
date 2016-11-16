package be.spyproof.nickmanager.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Spyproof on 29/10/2016.
 */
public class StringUtils
{
    /**
     * The input string s will be converted to lower chase and the very first character will then be set in uppercase
     * @param s The String that needs to be capitalised
     * @return A capitalised String
     */
    public static String capitalise(String s)
    {
        char[] chars = s.toLowerCase().toCharArray();
        if (chars.length > 0)
            chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    /**
     * Find all colours present in the String s
     * @see Colour
     * @param s The string that contains the possible colours
     * @return A List of all colours present in the String s
     */
    public static List<Colour> getPresentColours(String s)
    {
        s = s.toLowerCase();
        List<Colour> present = new ArrayList<>();

        for (Colour c : Colour.values())
            if (s.contains(c.toString()) || s.contains("&" + c.getColourChar()))
                present.add(c);

        return present;
    }


    /**
     * Find all styles present in the String s
     * @see Style
     * @param s The string that contains the possible styles
     * @return A List of all styles present in the String s
     */
    public static List<Style> getPresentStyles(String s)
    {
        s = s.toLowerCase();
        List<Style> present = new ArrayList<>();

        for (Style c : Style.values())
            if (s.contains(c.toString()))
                present.add(c);

        return present;
    }
}
