package be.spyproof.nickmanager.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copied code from Essentials
 */
public class DateUtil
{
    private static Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);

    /**
     * Copied from
     * https://github.com/essentials/Essentials/blob/2.x/Essentials/src/com/earth2me/essentials/utils/DateUtil.java
     *
     * Allows to convert a string with a matching format to be turned into a timestamp
     */
    public static long parseDateDiff(String time, boolean future) throws IllegalArgumentException
    {
        Matcher m = timePattern.matcher(time);
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        boolean found = false;
        while (m.find())
        {
            if (m.group() == null || m.group().isEmpty())
            {
                continue;
            }
            for (int i = 0; i < m.groupCount(); i++)
            {
                if (m.group(i) != null && !m.group(i).isEmpty())
                {
                    found = true;
                    break;
                }
            }
            if (found)
            {
                if (m.group(1) != null && !m.group(1).isEmpty())
                {
                    years = Integer.parseInt(m.group(1));
                }
                if (m.group(2) != null && !m.group(2).isEmpty())
                {
                    months = Integer.parseInt(m.group(2));
                }
                if (m.group(3) != null && !m.group(3).isEmpty())
                {
                    weeks = Integer.parseInt(m.group(3));
                }
                if (m.group(4) != null && !m.group(4).isEmpty())
                {
                    days = Integer.parseInt(m.group(4));
                }
                if (m.group(5) != null && !m.group(5).isEmpty())
                {
                    hours = Integer.parseInt(m.group(5));
                }
                if (m.group(6) != null && !m.group(6).isEmpty())
                {
                    minutes = Integer.parseInt(m.group(6));
                }
                if (m.group(7) != null && !m.group(7).isEmpty())
                {
                    seconds = Integer.parseInt(m.group(7));
                }
                break;
            }
        }
        if (!found)
        {
            throw new IllegalArgumentException("Wronge date");
        }
        Calendar c = new GregorianCalendar();
        if (years > 0)
        {
            c.add(Calendar.YEAR, years * (future ? 1 : -1));
        }
        if (months > 0)
        {
            c.add(Calendar.MONTH, months * (future ? 1 : -1));
        }
        if (weeks > 0)
        {
            c.add(Calendar.WEEK_OF_YEAR, weeks * (future ? 1 : -1));
        }
        if (days > 0)
        {
            c.add(Calendar.DAY_OF_MONTH, days * (future ? 1 : -1));
        }
        if (hours > 0)
        {
            c.add(Calendar.HOUR_OF_DAY, hours * (future ? 1 : -1));
        }
        if (minutes > 0)
        {
            c.add(Calendar.MINUTE, minutes * (future ? 1 : -1));
        }
        if (seconds > 0)
        {
            c.add(Calendar.SECOND, seconds * (future ? 1 : -1));
        }
        Calendar max = new GregorianCalendar();
        max.add(Calendar.YEAR, 10);
        if (c.after(max))
        {
            return max.getTimeInMillis();
        }
        return c.getTimeInMillis();
    }

    /**
     * Allows to format a time difference expressed in milliseconds to a readable string
     *
     * @param timeDiff amount of milliseconds
     * @return A formatted readable time string
     */
    public static String timeformat(long timeDiff)
    {
        int milli = (int) (timeDiff%1000);
		timeDiff = timeDiff/1000;
        int seconds = (int)timeDiff%60;
        timeDiff = timeDiff / 60;
        int minutes = (int)timeDiff%60;
        timeDiff = timeDiff / 60;
        int hours = (int)timeDiff%24;
        timeDiff = timeDiff / 24;
        int days = (int)timeDiff;

        String timeFormat;

        //Formatting
        if (days > 7)
            timeFormat = days + " days";
        else if (days > 0)
            timeFormat = days + (days == 1 ? " day " : "days ") + hours + (hours == 1 ? "hour" : "hours");
        else if (days == 0 && hours > 0){
            timeFormat = hours + (hours == 1 ? " hour " : " hours ") + minutes + (minutes == 1 ? " minute " : " minutes ") + seconds + (seconds == 1 ? " second" : " seconds");
        }else if (days == 0 && hours == 0 && minutes > 0){
            timeFormat = minutes + (minutes == 1 ? " minute " : " minutes ") + seconds + (seconds == 1 ? " second" : " seconds");
        }else{
            timeFormat = seconds + (seconds == 1 ? " second " : " seconds ") + milli + (milli == 1 ? " millisecond " : " milliseconds ");
        }

            return timeFormat;
    }
}
