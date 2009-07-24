package edu.wustl.cab2b.server.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XSSVulnerableDetector {

    /**
     * This is regular expression to check for XSS vulnerable characters e.g <, >, [, ] etc
     * */
    private static final String REGEX_XSS_VULNERABLE = "[<>]";

    /**
     * This is regular expression to check for XXS vulnerable strings like script, table etc.
     * */
    private static final String REGEX_XSS_VULNERABLE_KEYWORDS =
            "/*(table|drop|delete|applet|meta|xml|blink|style|script|embed|object|iframe|frame|frameset|ilayer|layer|bgsound|title|base|alert|absolute|relative)[^>(gt)(lt)]*";

    /**
     * This is regular expression to check for SQL injection characters e.g =,*, etc
     * */
    private static final String REGEX_SQLINJECTION_VULNERABLE = "[()<>'=\\*&;]";

    /**
     * This method check for Xss vulnerable characters like <, >, [, ] etc.
     * @param String for which Xss vulnerable character [ ,< or > ] to be checked.
     * @return true if the string contains any Xss vulnerable character else false.
     */
    public static boolean isXssVulnerable(String value) {
        boolean isXssVulnerable = false;
        if (value != null) {
            Pattern p = Pattern.compile(REGEX_XSS_VULNERABLE);
            Matcher m = p.matcher(value);
            isXssVulnerable = m.find();
        }
        return isXssVulnerable;
    }

    /**
     * This method check for Xss vulnerable characters like <, >, (, ) etc.
     * This method also checks to see if the input contains any SQL injection
     * or Cross scripting vulnerable keywords. 
     * @param String for which Xss vulnerable character [ (,),< or > ] to be checked.
     * @return true if the string contains any Xss vulnerable character else false.
     */
    public static boolean isXssSQLVulnerable(String value) {
        boolean isXssVulnerable = false;
        if (value != null) {
            Pattern p = Pattern.compile(REGEX_SQLINJECTION_VULNERABLE);
            Matcher m = p.matcher(value);
            isXssVulnerable = m.find();

            if (!isXssVulnerable) {
                p = Pattern.compile(REGEX_XSS_VULNERABLE_KEYWORDS);
                m = p.matcher(value);
                isXssVulnerable = m.find();
            }
        }
        return isXssVulnerable;
    }
}