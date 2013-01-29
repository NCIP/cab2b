/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.ServiceGroup;
import edu.wustl.cab2b.common.queryengine.ServiceGroupItem;
import edu.wustl.cab2b.common.queryengine.result.FQPUrlStatus;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.ICategoryResult;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.QueryResultFactory;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.DcqlConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.GroupConstraint;
import edu.wustl.common.querysuite.metadata.category.Category;
import gov.nih.nci.cagrid.dcql.ForeignAssociation;
import gov.nih.nci.cagrid.dcql.Group;
import gov.nih.nci.cagrid.dcql.Object;
import org.apache.log4j.Logger;


/**
 * @author gaurav_mehta
 *
 */
public class QueryExecutorHelper {
    /**
     * Method that splits  
     * @param query query
     * @return
     */
    public static List<ICab2bQuery> splitQUeryPerUrl(ICab2bQuery query) {
        List<ICab2bQuery> singleUrlQueries = new ArrayList<ICab2bQuery>(query.getOutputUrls().size());
        for (String url : query.getOutputUrls()) {
            ICab2bQuery singleUrlQuery = (ICab2bQuery) DynamicExtensionsUtility.cloneObject(query);
            List<String> targetUrls = new ArrayList<String>(1);
            targetUrls.add(url);
            singleUrlQuery.setOutputUrls(targetUrls);
            singleUrlQueries.add(singleUrlQuery);
        }
        return singleUrlQueries;
    }
    

    public static List<ICab2bQuery> splitQueryPerGroup(ICab2bQuery query) {        
    	Collection<ServiceGroup> sGroups = query.getServiceGroups();    	
        List<ICab2bQuery> singleUrlQueries = new ArrayList<ICab2bQuery>(sGroups.size());    
        
		for(int x=0; x< sGroups.size(); x++){
            ICab2bQuery singleUrlQuery = (ICab2bQuery) DynamicExtensionsUtility.cloneObject(query); 
            singleUrlQueries.add(singleUrlQuery);
        
		}
		return singleUrlQueries;
        
    }
   			   
    /**
     * @param categoryResults
     * @return
     */
    public static IQueryResult<ICategorialClassRecord> mergeCatResults(
                                                                       List<IQueryResult<ICategorialClassRecord>> categoryResults,
                                                                       Category outputCategory) {
        ICategoryResult<ICategorialClassRecord> res = QueryResultFactory.createCategoryResult(outputCategory);
        for (IQueryResult<ICategorialClassRecord> categoryResult : categoryResults) {
            //Adding all failed URLs: FQP 1.3 updates
            Collection<FQPUrlStatus> urlStatus = categoryResult.getFQPUrlStatus();
            if (urlStatus != null) {
                urlStatus.addAll(categoryResult.getFQPUrlStatus());
                res.setFQPUrlStatus(urlStatus);
            }
            for (Map.Entry<String, List<ICategorialClassRecord>> entry : categoryResult.getRecords().entrySet()) {
                res.addRecords(entry.getKey(), entry.getValue());
            }
        }
        //TODO pivots the results around the original root and then merges them...
        return res;
    }
}
