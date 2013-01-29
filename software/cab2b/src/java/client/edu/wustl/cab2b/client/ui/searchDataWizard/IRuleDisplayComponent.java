/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * <p>Title: IRuleDisplayComponent Class>
 * <p>Description:  Rule Display component interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.cab2b.client.ui.searchDataWizard;

/**
 * Rule Display component interface.
 * @author gautam_shetty
 */
public interface IRuleDisplayComponent
{
    /**
     * Returns the label of this rule.
     * @return the label of this rule.
     */
    public String getLabel();
    
    /**
     * Returns the operator selected for this rule.
     * @return the operator selected for this rule.
     */
    public String getLogicalOperator();
}