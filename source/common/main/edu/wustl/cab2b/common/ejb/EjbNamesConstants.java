package edu.wustl.cab2b.common.ejb;

/**
 * Interface which has all the constants representing EJB names. 
 * If anything is edited here, same change should be reflected in ejb-jar.xml <ejb-name> tag of that EJB.
 *  
 * @author Chandrakant Talele
 */
public interface EjbNamesConstants {
	
	final static String ADVANCED_SEARCH_BEAN = "AdvanceSearch";
	
	final static String ENTITY_CACHE = "EntityCache";
	
	final static String EXPERIMENT = "edu.wustl.cab2b.server.ejb.experiment.Experiment";
	
	final static String EXPERIMENT_GROUP = "edu.wustl.cab2b.server.ejb.experiment.ExperimentGroup";
	
    final static String SQL_QUERY_BEAN = "edu.wustl.cab2b.server.ejb.sqlquery.SQLQueryBean";

    final static String PATH_FINDER_BEAN = "edu.wustl.cab2b.server.ejb.path.PathFinderBean";

    final static String PATH_BUILDER_BEAN = "edu.wustl.cab2b.server.ejb.path.PathBuilderBean";

    final static String QUERY_ENGINE_BEAN = "edu.wustl.cab2b.server.ejb.queryengine.QueryEngineBean";

    final static String CATEGORY_BEAN = "edu.wustl.cab2b.server.ejb.category.CategoryBean";
     
    final static String DATALIST_BEAN = "edu.wustl.cab2b.server.ejb.datalist.DataListBean";
    
    final static String ANALYTICAL_SERVICE_BEAN = "edu.wustl.cab2b.server.ejb.analyticalservice.AnalyticalServiceOperationsBean";
}
