package edu.wustl.cab2b.common.ejb;

/**
 * Interface which has all the constants representing EJB names. 
 * Updated  for ejb 3.0
 * Format is : EAR_NAME /BEAN_NAME/TYPE_OF_BUSINESS_INTERFACE.
 * All the beans are accessed by remote business interface
 *  
 * @author Chandrakant Talele
 * @author lalit_chand 
 */
public interface EjbNamesConstants {

    final static String EXPERIMENT = "cab2bServer/ExperimentSessionBean/remote";

    final static String EXPERIMENT_GROUP = "cab2bServer/ExperimentGroupSessionBean/remote";

    final static String SQL_QUERY_BEAN = "cab2bServer/SQLQueryBean/remote";

    final static String PATH_FINDER_BEAN = "cab2bServer/PathFinderBean/remote";

    final static String PATH_BUILDER_BEAN = "cab2bServer/PathBuilderBean/remote";

    final static String QUERY_ENGINE_BEAN = "cab2bServer/QueryEngineBean/remote";

    final static String CATEGORY_BEAN = "cab2bServer/CategoryBean/remote";

    final static String DATACATEGORY_BEAN = "cab2bServer/DataCategoryBean/remote";

    final static String DATALIST_BEAN = "cab2bServer/DataListBean/remote";

    final static String ANALYTICAL_SERVICE_BEAN = "cab2bServer/AnalyticalServiceOperationsBean/remote";

    final static String UTILITY_BEAN = "cab2bServer/UtilityBean/remote";

    final static String USER_BEAN = "cab2bServer/UserBean/remote";

    final static String SERVICE_URL_BEAN = "cab2bServer/ServiceURLBean/remote";
}
