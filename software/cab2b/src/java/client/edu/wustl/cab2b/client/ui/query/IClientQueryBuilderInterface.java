/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * <p>Title: IClientQueryBuilderInterface interface>
 * <p>Description:  This interface provides APIs for creating the query
 * object from the DAG view.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.cab2b.client.ui.query;

import java.rmi.RemoteException;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.utils.IConstraintsObjectBuilderInterface;

/**
 * This interface provides APIs for creating the query
 * object from the DAG view.
 * @author gautam_shetty
 */
public interface IClientQueryBuilderInterface extends IConstraintsObjectBuilderInterface {

    /**
     * Sets the output of the query to the specified entity.
     * @param entity The entity to be set as output.
     * @throws RemoteException 
     */
    public void setOutputForQuery(EntityInterface entity) throws RemoteException;

    public void setOutputForQueryForSpecifiedURL(EntityInterface entity, String strURL);

}