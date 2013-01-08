/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateParser {
    private static final String DATE_SEPARATOR_SLASH = "/";

    private static final String DATE_SEPARATOR = "-";

    public static Date parseDate(String date, String pattern) {
        if (date == null) {
            throw new NullPointerException();

        }
        if (date.trim().equals("")) {
            throw new IllegalArgumentException();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date dateObj;
        try {
            dateObj = dateFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return dateObj;
    }

    public static String changeToHypenMDY(String dateStr) {
        Date date = new Date();
        date = parseDate(dateStr);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-"
                + calendar.get(Calendar.YEAR);
    }

    public static Date parseDate(String date) {
        String pattern = datePattern(date);

        return parseDate(date, pattern);
    }

    public static String datePattern(String strDate) {
        String datePattern = "";
        String dtSep = "";
        boolean result = true;

        Pattern re = Pattern.compile("[0-9]{2}-[0-9]{2}-[0-9]{4}", Pattern.CASE_INSENSITIVE);
        Matcher mat = re.matcher(strDate);
        result = mat.matches();

        if (result) {
            dtSep = DATE_SEPARATOR;
            datePattern = "MM" + dtSep + "dd" + dtSep + "yyyy";
        }

        // check for / separator
        if (!result) {
            re = Pattern.compile("[0-9]{2}/[0-9]{2}/[0-9]{4}", Pattern.CASE_INSENSITIVE);
            mat = re.matcher(strDate);
            result = mat.matches();
            // System.out.println("is Valid Date Pattern : / : "+result);
            if (result) {
                dtSep = DATE_SEPARATOR_SLASH;
                datePattern = "MM" + dtSep + "dd" + dtSep + "yyyy";
            }
        }

        if (!result) {
            re = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}", Pattern.CASE_INSENSITIVE);
            mat = re.matcher(strDate);
            result = mat.matches();

            if (result) {
                dtSep = DATE_SEPARATOR;
                datePattern = "yyyy" + dtSep + "mm" + dtSep + "dd";
            }
        }

        // check for / separator
        if (!result) {
            re = Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9]{2}", Pattern.CASE_INSENSITIVE);
            mat = re.matcher(strDate);
            result = mat.matches();
            if (result) {
                dtSep = DATE_SEPARATOR_SLASH;
                datePattern = "yyyy" + dtSep + "mm" + dtSep + "dd";
            }
        }

        return datePattern;
    }
}
