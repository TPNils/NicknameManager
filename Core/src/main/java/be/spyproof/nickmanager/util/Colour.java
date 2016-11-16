package be.spyproof.nickmanager.util;

/**
 * Created by Spyproof on 29/10/2016.
 *
 * Refers to the default minecraft chat colours and their matching codes
 */
public enum Colour
{
    BLACK('0'),
    DARK_BLUE('1'),
    DARK_GREEN('2'),
    DARK_AQUA('3'),
    DARK_RED('4'),
    DARK_PURPLE('5'),
    GOLD('6'),
    GRAY('7'),
    DARK_GRAY('8'),
    BLUE('9'),
    GREEN('a'),
    AQUA('b'),
    RED('c'),
    LIGHT_PURPLE('d'),
    YELLOW('e'),
    WHITE('f');

    private char colourChar;

    Colour(char colourChar)
    {
        this.colourChar = colourChar;
    }

    /**
     * @return The character associated with the colour
     */
    public char getColourChar()
    {
        return colourChar;
    }

    /**
     * @return The default chat string that will be used to identify a colour
     */
    @Override
    public String toString()
    {
        return new String(new char[]{'&', this.colourChar});
    }
}
