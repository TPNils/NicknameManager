package be.spyproof.nickmanager.test.util;

import be.spyproof.nickmanager.util.Colour;
import be.spyproof.nickmanager.util.StringUtils;
import be.spyproof.nickmanager.util.Style;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by Spyproof on 04/11/2016.
 */
public class StringUtilTest
{
    @Test
    public void capitalise() throws Exception
    {
        Assert.assertEquals(StringUtils.capitalise("a WeIrd STRing"), "A weird string");
    }

    @Test
    public void findColours() throws Exception
    {
        List<Colour> colours = StringUtils.getPresentColours("&1&2&3&o");
        Assert.assertEquals(colours.size(), 3);
    }

    @Test
    public void findStyles() throws Exception
    {
        List<Style> styles = StringUtils.getPresentStyles("&1&2&3&o");
        Assert.assertEquals(styles.size(), 1);
    }
}
