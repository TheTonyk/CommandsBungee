package com.thetonyk.CommandsBungee.Utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* By Essentials, modified by LeonTG77 & D4mnX */
public class DateUtils {
	
	private static final Pattern TIME_PATTERN = Pattern.compile(
			
            "(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?" +     // Captured group 1 = Years     (y)
            "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?" +    // Captured group 2 = Months    (mo)
            "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?" +     // Captured group 3 = Weeks     (w)
            "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?" +     // Captured group 4 = Days      (d)
            "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?" +     // Captured group 5 = Hours     (h)
            "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?" +     // Captured group 6 = Minutes   (m)
            "(?:([0-9]+)\\s*(?:s[a-z]*)?)?",        // Captured group 2 = Seconds   (s)
            Pattern.CASE_INSENSITIVE
            
    );

	public static long parseDateDiff (String time) {
		
    	final Matcher matcher = TIME_PATTERN.matcher(time);
 
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
 
        boolean found = false;
 
        while (matcher.find()) {
        	
            if (matcher.group() == null || matcher.group().isEmpty()) continue;
 
            for (int i = 0; i < matcher.groupCount(); i++) {
            	
                if (matcher.group(i) == null || matcher.group(i).isEmpty()) continue;
                	
                found = true;
                break;
                
            }
 
            if (found) {
            	
                years = parseGroup(matcher, 1);
                months = parseGroup(matcher, 2);
                weeks = parseGroup(matcher, 3);
                days = parseGroup(matcher, 4);
                hours = parseGroup(matcher, 5);
                minutes = parseGroup(matcher, 6);
                seconds = parseGroup(matcher, 7);
                break;
                
            }
            
        }
 
        if (!found) return 0;
 
        Calendar cal = new GregorianCalendar();
        
        cal.add(Calendar.YEAR, years);
        cal.add(Calendar.MONTH, months);
        cal.add(Calendar.WEEK_OF_YEAR, weeks);
        cal.add(Calendar.DAY_OF_MONTH, days);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        cal.add(Calendar.MINUTE, minutes);
        cal.add(Calendar.SECOND, seconds);
 
        Calendar max = new GregorianCalendar();
        max.add(Calendar.YEAR, 10);
        
        if (cal.after(max)) return max.getTimeInMillis();
 
        return cal.getTimeInMillis();
        
    }
	
	private static int parseGroup (Matcher matcher, int groupNumber) {
		
    	String group = matcher.group(groupNumber);
 
        if (group == null || group.isEmpty()) return 0;
 
        try {
        	
            return Integer.parseInt(group);
            
        } catch (Exception exception) {
        	
            return 0;
            
        }
        
    }
	
}
