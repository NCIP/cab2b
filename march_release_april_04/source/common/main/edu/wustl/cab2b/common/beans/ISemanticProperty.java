/**
 * <p>Title: ISemanticProperty Interface>
 * <p>Description:	ISemanticProperty interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */
package edu.wustl.cab2b.common.beans;

import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;

/**
 * ISemanticProperty interface.
 * @author gautam_shetty
 */
public interface ISemanticProperty extends SemanticPropertyInterface
{
    
//    /**
//     * @return Returns the conceptCode.
//     */
//    public String getConceptCode();
//    
//    /**
//     * @param conceptCode The conceptCode to set.
//     */
//    public void setConceptCode(String conceptCode);
//    
//    /**
//     * @return Returns the term.
//     */
//    public String getTerm();
//    
//    /**
//     * @param term The term to set.
//     */
//    public void setTerm(String term);
//    
//    /**
//     * @return Returns the thesaurasName.
//     */
//    public String getThesaurasName();
//    
//    /**
//     * @param thesaurasName The thesaurasName to set.
//     */
//    public void setThesaurasName(String thesaurasName);
    
    public boolean newEquals(Object obj);
    
}
