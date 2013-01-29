/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XSSVulnerableDetector {

    /**
     * This is regular expression to check for XSS vulnerable characters and strings, e.g <, >, [, ], etc
     * */
    private static final String REGEX_XSS_VULNERABLE = "([|<|>|])";

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
     * This method check for XSS vulnerable characters, like, <, >, [, ], etc.
     * @param String for which XSS vulnerable character [ ,< or > ] to be checked.
     * @return true if the string contains any XSS vulnerable character else false.
     */
    public static boolean isXssVulnerable(String value) {
        boolean isXssVulnerable = false;
        if (value != null) {
            Pattern p = Pattern.compile(REGEX_XSS_VULNERABLE, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(value);
            isXssVulnerable = m.find();
        }
        return isXssVulnerable;
    }
 
    /**
     * This method check for XSS vulnerable keywords, like, alert, etc.
     * @param String for which XSS vulnerable keyword to be checked.
     * @return true if the string contains any XSS vulnerable keyword else false.
     */    
    public static boolean isXssKeywordVulnerable(String value) {
        boolean isXssKeywordVulnerable = false;
        if (value != null) {
            Pattern p = Pattern.compile(REGEX_XSS_VULNERABLE_KEYWORDS, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(value);
            isXssKeywordVulnerable = m.find();
        }
        return isXssKeywordVulnerable;
    }    

    /**
     * This method check for XSS vulnerable characters like <, >, (, ) etc.
     * This method also checks to see if the input contains any SQL injection
     * or Cross scripting vulnerable keywords. 
     * @param String for which XSS vulnerable character [ (,),< or > ] to be checked.
     * @return true if the string contains any XSS vulnerable character else false.
     */
    public static boolean isXssSQLVulnerable(String value) {
        boolean isXssVulnerable = false;
        if (value != null) {
            Pattern p = Pattern.compile(REGEX_SQLINJECTION_VULNERABLE, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(value);
            isXssVulnerable = m.find();

            if (!isXssVulnerable) {
                p = Pattern.compile(REGEX_XSS_VULNERABLE_KEYWORDS, Pattern.CASE_INSENSITIVE);
                m = p.matcher(value);
                isXssVulnerable = m.find();
            }
        }
        return isXssVulnerable;
    }
}