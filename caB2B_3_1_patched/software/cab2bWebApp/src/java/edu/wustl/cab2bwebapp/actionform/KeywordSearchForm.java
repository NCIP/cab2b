/**
 *
 */
package edu.wustl.cab2bwebapp.actionform;

import org.apache.struts.action.ActionForm;

/**
 * @author chetan_patil
 *
 */
public class KeywordSearchForm extends ActionForm {
    
    private static final long serialVersionUID = 1L;    

    private String keyword;

    private String[] modelGroups;

    /**
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword the keyword to set
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * @return the modelGroups
     */
    public String[] getModelGroups() {
        return modelGroups;
    }

    /**
     * @param modelGroups the modelGroups to set
     */
    public void setModelGroups(String[] modelGroups) {
        this.modelGroups = modelGroups;
    }

}
