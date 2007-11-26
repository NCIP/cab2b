/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wustl.cab2b.client.ui.controls.sheet;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 *
 * @author jasbir_sachdeva
 */
public class DefaultPatternFilter implements PatternFilterModel {

    private boolean isRegExp = false;
    private boolean isStrIncludeTypeFilter = true;
    private String userSpecifiedStr;
    private boolean isRegExCompileFailed = true;
    private Pattern patternRegExSearch;
    /** This keeps the default values for all the subssquently created DefaultPatternFilter instances.  */
    static private PatternFilterModel dpf = new DefaultPatternFilter();

    /**     Creates new instances of DefaultPatternFilter, with default values */
    public DefaultPatternFilter() {
        dpf = getDefault();

        if (null != dpf) {
            //      this is NOT the first time that this constructor have een called...
            isRegExp = dpf.isRegExp();
            isStrIncludeTypeFilter = dpf.isStrIncludeTypeFilter();
            userSpecifiedStr = dpf.getUserSpecifiedStr();
            isRegExCompileFailed = dpf.isRegExCompileFailed();
            patternRegExSearch = dpf.getPatternRegExSearch();
        }
    }

    public boolean include(String value) {
        if (isRegExp) {
            if (isRegExCompileFailed) {
                //  bad reg exp, so ignore it...
                return true;
            }
            return patternRegExSearch.matcher(value).matches();
        }

        //  String based search...
        boolean found = value.indexOf( userSpecifiedStr) >= 0;
//        boolean found = userSpecifiedStr.indexOf(value) >= 0;
        if (isStrIncludeTypeFilter) {
            return found;
        }
        return !found;
    }

    //    
//  Setters...        
//        

    String getFilterDescription() {
        if (isRegExp) //  using reg extends based filter...
        {
            return "Reg Exp = " + userSpecifiedStr;
        }

        //  Using String based simple filter...
        return (isStrIncludeTypeFilter ? "include" : "exclude") + "string '" + userSpecifiedStr + "'";
    }

    public void setRegularExpFilter(String patternStr)
        throws PatternSyntaxException {

        //  Clear State, so that incomplete descrption may be required...
        isRegExp = true;
        isRegExCompileFailed = true;
        userSpecifiedStr = "";
        patternRegExSearch = null;
        patternRegExSearch = Pattern.compile(patternStr);

        //  if reached here, compilation of RegEx succeded...
        isRegExCompileFailed = false;
        userSpecifiedStr = patternStr;
    }

    public void setSearchStringFilter(String patternStr, boolean includeType) {
        isRegExp = false;
        //  Keep lower case version, will perform case-insensitive search...
        userSpecifiedStr = patternStr.toLowerCase();
        isStrIncludeTypeFilter = includeType;

        //  clear extra state...
        isRegExCompileFailed = true;
        patternRegExSearch = null;
    }

    //
//  Getters...        
//        

    public Pattern getPatternRegExSearch() {
        return patternRegExSearch;
    }

    public boolean isRegExp() {
        return isRegExp;
    }

    public boolean isStrIncludeTypeFilter() {
        return isStrIncludeTypeFilter;
    }

    public String getUserSpecifiedStr() {
        return userSpecifiedStr;
    }

    public boolean isRegExCompileFailed() {
        return isRegExCompileFailed;
    }

    /** DefaultPatternFilter keeps class instance of itself that keeps default values. 
     * This instance can be accessed by this method. If any attrbutes are changed, it will 
        subsequently effect the creation of new instances of DefaultPatternFilter.      */
    public static PatternFilterModel getDefault() {
        return dpf;
    }
}
