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
public interface PatternFilterModel {

    
        /**     Regular Expression filter is set by this method, on this filter.
         * Specify what Reg-Exc filter is required by the user. After successful completion of this call 
         * the mode of this model is switched to Regular Expresion search.  The calls fails with PatternSyntaxException
         if the specifed Regular expresion <tt>patternStr</tt> fails is invalid.    */
    void setRegularExpFilter(String patternStr) throws PatternSyntaxException;
    
        /**     Simple String search filter is set by this method, on this filter.
         * Collects information regarding simple search string.
                        @param patternStr: Search String specified by the End user.
                        @param: includeType: String Search could be of two types. 1) Select all values that contains 
         * specified string patterns. Or 2) Pick al values that do NOT contains specified value. 
         * Note both conditions are negation of each other. 
         * If <tt>true</tt> search if of type 1, else of type 2, as described.*/
    void setSearchStringFilter(String patternStr, boolean includeType);

    /** */
    Pattern getPatternRegExSearch();
    String getUserSpecifiedStr();
    boolean isRegExCompileFailed();
    boolean isRegExp();
    boolean isStrIncludeTypeFilter();

}
