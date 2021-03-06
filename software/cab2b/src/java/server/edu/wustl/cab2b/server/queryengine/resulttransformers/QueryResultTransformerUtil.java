/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine.resulttransformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.exception.RuntimeException;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.LogicalOperator;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.cqlresultset.CQLAttributeResult;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.cqlresultset.TargetAttribute;
import gov.nih.nci.cagrid.data.client.DataServiceClient;

/**
 * Util for the query result transformers.
 * 
 * @author srinath_k
 * 
 */
public class QueryResultTransformerUtil {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(QueryResultTransformerUtil.class);
    private QueryLogger queryLogger;

    public QueryResultTransformerUtil(QueryLogger logger) {
        this.queryLogger = logger;
    }

    /**
     * Creates and executes CQL to obtain desired attributes' values.
     * 
     * @param className the target class
     * @param attributeNames the attributes whose values are to be fetched
     * @param idAttrName the name of the id attribute
     * @param ids identifies which objects of the target class are to be fetched
     *            (translates to constraints in the CQL)
     * @param url the service url
     * @param cred security credentials
     * @return the list of records obtained by executing the CQL; each record is
     *         an attribute-value map.
     */
    public List<Map<String, String>> getAttributeResult(String className, String[] attributeNames,
                                                        String idAttrName, String[] ids, String url,
                                                        GlobusCredential cred) {
        List<Map<String, String>> records = new ArrayList<Map<String, String>>();
        if (ids.length == 0) {
            return records;
        }
        List<Attribute> constraints = new ArrayList<Attribute>(ids.length);
        for (String id : ids) {
            constraints.add(createIdConstraint(idAttrName, id));
        }
        Object target;
        if (constraints.size() == 1) {
            target = createTarget(className, constraints.get(0));
        } else {
            target = createTarget(className, createGroup(constraints));
        }

        CQLQuery cql = createCQLQuery(target, createQueryModifier(attributeNames));
        CQLAttributeResult[] attributeResults = executeCQL(cql, url, cred).getAttributeResult();

        for (CQLAttributeResult attributeResult : attributeResults) {
            Map<String, String> oneRow = new HashMap<String, String>();
            records.add(oneRow);
            for (TargetAttribute targetAttribute : attributeResult.getAttribute()) {
                String val = targetAttribute.getValue();
                oneRow.put(targetAttribute.getName(), val == null ? "" : val);
            }
        }
        return records;
    }

    /**
     * Creates and executes CQL to get object of given id.
     * 
     * @param className the target class
     * @param idAttrName the name of the id attribute in the class
     * @param id the id of the desired object
     * @param url the service url
     * @param cred security credentials
     * @return the cql results.
     */
    public CQLQueryResults getCQLResultsById(String className, String idAttrName, String id, String url,
                                             GlobusCredential cred) {
        Object target = createTarget(className, createIdConstraint(idAttrName, id));
        return executeCQL(createCQLQuery(target), url, cred);
    }

    private Group createGroup(List<Attribute> constraints) {
        Group group = new Group();
        group.setAttribute(constraints.toArray(new Attribute[0]));
        group.setLogicRelation(LogicalOperator.OR);
        return group;
    }

    private Attribute createIdConstraint(String idAttrName, String id) {
        Attribute attribute = new Attribute();
        attribute.setName(idAttrName);
        attribute.setValue(id);
        attribute.setPredicate(Predicate.EQUAL_TO);
        return attribute;
    }

    private CQLQueryResults executeCQL(CQLQuery cql, String url, GlobusCredential cred) {
        queryLogger.log(cql);
        logger.info("Executing CQL to fetch " + cql.getTarget().getName() + " from service " + url);
        try {
            DataServiceClient dataServiceClient = new DataServiceClient(url, cred);
            CQLQueryResults results = dataServiceClient.query(cql);
            logger.info("Executed CQL successfully.");
            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object createTarget(String className) {
        Object target = new Object();
        target.setName(className);
        return target;
    }

    private Object createTarget(String className, Attribute attribute) {
        Object target = createTarget(className);
        target.setAttribute(attribute);
        return target;
    }

    private Object createTarget(String className, Group group) {
        Object target = createTarget(className);
        target.setGroup(group);
        return target;
    }

    private CQLQuery createCQLQuery(Object target) {
        CQLQuery cql = new CQLQuery();
        cql.setTarget(target);
        return cql;
    }

    private CQLQuery createCQLQuery(Object target, QueryModifier queryModifier) {
        CQLQuery cql = createCQLQuery(target);
        cql.setQueryModifier(queryModifier);
        return cql;
    }

    private QueryModifier createQueryModifier(String[] attributesNames) {
        QueryModifier queryModifier = new QueryModifier();
        queryModifier.setAttributeNames(attributesNames);
        return queryModifier;
    }
}
