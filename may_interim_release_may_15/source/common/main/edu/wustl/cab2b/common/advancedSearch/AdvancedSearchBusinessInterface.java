/**
 * <p>Title: AdvancedSearchBusinessInterface Interface>
 * <p>Description:	Advanced Search business interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */
package edu.wustl.cab2b.common.advancedSearch;

import java.rmi.RemoteException;

import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.beans.MatchedClass;

/**
 * Advanced Search business interface.
 * @author gautam_shetty
 */
public interface AdvancedSearchBusinessInterface extends BusinessInterface
{
    
    /**
     * Searches the string passed in the targets on basedOn parameter. 
     * Returns the MatchedClass instance containing the matched entities in the search.
     * @param searchTarget The target on which the search is to be performed.
     * @param searchString The string to be searched.
     * @param basedOn The search to be based on. 
     * @return the MatchedClass instance containing the matched entities in the search.
     * @throws RemoteException
     */
    public MatchedClass search(int[] searchTarget, String[] searchString, int basedOn) throws RemoteException;
}