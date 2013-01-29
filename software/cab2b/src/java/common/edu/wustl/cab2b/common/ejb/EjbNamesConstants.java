/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.ejb;

/**
 * Interface which has all the constants representing EJB names. 
 * If anything is edited here, same change should be reflected in ejb-jar.xml <ejb-name> tag of that EJB.
 *  
 * @author Chandrakant Talele
 */
public interface EjbNamesConstants {
	final static String EXPERIMENT = "edu.wustl.cab2b.server.ejb.experiment.Experiment";
	
	final static String EXPERIMENT_GROUP = "edu.wustl.cab2b.server.ejb.experiment.ExperimentGroup";
	
    final static String SQL_QUERY_BEAN = "edu.wustl.cab2b.server.ejb.sqlquery.SQLQueryBean";

    final static String PATH_FINDER_BEAN = "edu.wustl.cab2b.server.ejb.path.PathFinderBean";

    final static String PATH_BUILDER_BEAN = "edu.wustl.cab2b.server.ejb.path.PathBuilderBean";

    final static String QUERY_ENGINE_BEAN = "edu.wustl.cab2b.server.ejb.queryengine.QueryEngineBean";

    final static String CATEGORY_BEAN = "edu.wustl.cab2b.server.ejb.category.CategoryBean";
     
    final static String DATACATEGORY_BEAN = "edu.wustl.cab2b.server.ejb.datacategory.DataCategoryBean";
    
    final static String DATALIST_BEAN = "edu.wustl.cab2b.server.ejb.datalist.DataListBean";
    
    final static String ANALYTICAL_SERVICE_BEAN = "edu.wustl.cab2b.server.ejb.analyticalservice.AnalyticalServiceOperationsBean";
    
    final static String UTILITY_BEAN = "edu.wustl.cab2b.server.ejb.utility.UtilityBean";
    
    final static String USER_BEAN = "edu.wustl.cab2b.server.ejb.user.UserBean";
    
    final static String SERVICE_URL_BEAN = "edu.wustl.cab2b.server.ejb.serviceurl.ServiceURLBean";
}
