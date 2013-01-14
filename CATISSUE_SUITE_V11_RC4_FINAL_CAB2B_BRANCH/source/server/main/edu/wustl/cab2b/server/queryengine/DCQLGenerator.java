/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.server.queryengine;

import java.io.StringWriter;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.domain.DCQL;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.queryengine.querybuilders.CategoryPreprocessor;
import edu.wustl.cab2b.server.queryengine.querybuilders.CategoryPreprocessorResult;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.ConstraintsBuilder;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.ConstraintsBuilderResult;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.AttributeConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.DcqlConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.ForeignAssociationConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.GroupConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.LocalAssociationConstraint;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcql.Object;
import gov.nih.nci.cagrid.fqp.common.SerializationUtils;

/**
 * @author chetan_patil
 *
 */
public class DCQLGenerator {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(QueryExecutor.class);

    private ICab2bQuery query;

    private ConstraintsBuilderResult constraintsBuilderResult;

    private CategoryPreprocessorResult categoryPreprocessorResult;

    public DCQLGenerator(ICab2bQuery query) {
        this.query = query;
    }

    /**
     * This methods generates DCQL(s) for ICab2bQuery object
     * 
     * @param query Query which needs to be executed.
     * @return Returns the IQueryResult
     */
    public DCQL generateDCQL() throws RuntimeException {
        logger.info("Entered DCQLGenerator...");

        categoryPreprocessorResult = new CategoryPreprocessor().processCategories(query);
        ConstraintsBuilder constraintsBuilder = new ConstraintsBuilder(query, categoryPreprocessorResult);
        constraintsBuilderResult = constraintsBuilder.buildConstraints();

        EntityInterface outputEntity = query.getOutputEntity();
        DCQL dcql = null;
        if (!Utility.isCategory(query.getOutputEntity())) {
            DCQLQuery dcqlQuery = createDCQLQuery(query, outputEntity.getName(),
                                                  constraintsBuilderResult.getDcqlConstraintForClass(outputEntity));

            String dcqlString = serializeDCQLQuery(dcqlQuery);
            dcql = new DCQL(outputEntity.getName(), dcqlString);
        } else {
            throw new IllegalArgumentException("Cannot process Queries involving Category");
        }
        return dcql;
    }

    private String serializeDCQLQuery(DCQLQuery dcqlQuery) {
        StringWriter writer = new StringWriter();

        try {
            SerializationUtils.serializeDCQLQuery(dcqlQuery, writer);
        } catch (Exception e) {
            logger.error(e.getStackTrace());
            throw new RuntimeException(e.getMessage());
        }
        return writer.toString();
    }

    public static DCQLQuery createDCQLQuery(ICab2bQuery query, String outputName, DcqlConstraint constraint, String... outputUrls) {
        Object targetObject = new Object();
        targetObject.setName(outputName);

        DCQLQuery dcqlQuery = new DCQLQuery();
        dcqlQuery.setTargetObject(targetObject);
        if (outputUrls.length == 0) {
            outputUrls = query.getOutputUrls().toArray(new String[0]);
        }
        dcqlQuery.setTargetServiceURL(outputUrls);

        assignDcqlConstraintToTarget(targetObject, constraint);
        return dcqlQuery;
    }

    private static void assignDcqlConstraintToTarget(Object targetObject, DcqlConstraint constraint) {
        if (constraint == null) {
            return;
        }
        switch (constraint.getConstraintType()) {
            case Attribute:
                AttributeConstraint attributeConstraint = (AttributeConstraint) constraint;
                targetObject.setAttribute(attributeConstraint.getAttribute());
                break;

            case LocalAssociation:
                LocalAssociationConstraint localAssociationConstraint = (LocalAssociationConstraint) constraint;
                targetObject.setAssociation(localAssociationConstraint.getLocalAssociation());
                break;

            case ForeignAssociation:
                ForeignAssociationConstraint foreignAssociationConstraint = (ForeignAssociationConstraint) constraint;
                targetObject.setForeignAssociation(foreignAssociationConstraint.getForeignAssociation());
                break;

            case Group:
                GroupConstraint groupConstraint = (GroupConstraint) constraint;
                targetObject.setGroup(groupConstraint.getGroup());
        }
    }
}
