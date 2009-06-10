/**
 * <p>Title: ClientQueryBuilder Class>
 * <p>Description:  This class provides implementations for the APIs for creating query
 * object from the DAG view.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.cab2b.client.ui.query;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.cache.UserCache;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.category.CategoryBusinessInterface;
import edu.wustl.cab2b.common.ejb.category.CategoryHomeInterface;
import edu.wustl.cab2b.common.queryengine.Cab2bQueryObjectFactory;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.metadata.category.Category;
import edu.wustl.common.querysuite.utils.ConstraintsObjectBuilder;

/**
 * This class provides the APIs for creating the query object from the 
 * DAG view.
 * @author gautam_shetty
 */
public class ClientQueryBuilder extends ConstraintsObjectBuilder implements IClientQueryBuilderInterface {

    public ClientQueryBuilder() {
        super(Cab2bQueryObjectFactory.createCab2bQuery());
    }

    /**
     * Sets the output of the query to the specified entity.
     * @param entity The entity to be set as output.
     * @throws RemoteException 
     */
    public void setOutputForQuery(EntityInterface entity) throws RemoteException {
        EntityInterface en = entity;
        if (Utility.isCategory(entity)) {
            CategoryBusinessInterface bus = (CategoryBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                         EjbNamesConstants.CATEGORY_BEAN,
                                                                                                         CategoryHomeInterface.class);
            Category cat = bus.getCategoryByEntityId(entity.getId());
            en = cat.getRootClass().getCategorialClassEntity();
        }

        String[] outputServiceUrls = getServiceUrls(en);
        List<String> urlList = new ArrayList<String>();
        for(String url:outputServiceUrls){
            urlList.add(url);
        }
        setOutputForQuery(entity, urlList);
    }

    public void setOutputForQuery(EntityInterface entity, List<String> urls) {
        ICab2bQuery cab2bQuery = (ICab2bQuery) getQuery();
        cab2bQuery.setOutputUrls(urls);
        cab2bQuery.setOutputEntity(entity);
    }

    public void setOutputForQueryForSpecifiedURL(EntityInterface entity, String strURL) {
        setOutputForQuery(entity, Collections.singletonList(strURL));
    }

    @Override
    public String[] getServiceUrls(EntityInterface entity) {
        return UserCache.getInstance().getServiceURLs(entity);
    }

}