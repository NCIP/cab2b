package edu.wustl.cab2b.server.queryengine.querybuilders.dcql;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.server.queryengine.querybuilders.CategoryPreprocessor;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.AttributeConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.DcqlConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.ForeignAssociationConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.GroupConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.LocalAssociationConstraint;
import edu.wustl.cab2b.server.util.ConnectionUtil;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcql.Object;

import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.namespace.QName;

// TODOs:
// selected output attributes
// categories
// multiple outputs

public class QueryBuilder {
    private static final String LOG_BASE_DIR = System.getProperty("user.home")
            + "/dcqlLog";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "MM-dd-yy_HH-mm");

    private static final String LOG_FILE_NAME_PREFIX = "dcql_";

    static {
        File file = new File(LOG_BASE_DIR);
        file.mkdir();
    }

    public DCQLQuery buildQuery(ICab2bQuery query) {
        Object targetObject = new Object();
        EntityInterface output = query.getRootOutputClass().getOutputEntity().getDynamicExtensionsEntity();
        IConstraints constraints = query.getConstraints();

        preProcessCategories(output, constraints);
        ConstraintsBuilder constraintsBuilder = new ConstraintsBuilder(
                constraints);

        ConstraintsBuilderResult constraintsBuilderResult = constraintsBuilder.buildConstraints();
        assignDcqlConstraintToTarget(
                                     targetObject,
                                     constraintsBuilderResult.getDcqlConstraintForClass(output));

        targetObject.setName(output.getName());

        DCQLQuery dcqlQuery = new DCQLQuery();
        dcqlQuery.setTargetObject(targetObject);
        dcqlQuery.setTargetServiceURL(query.getOutputClassUrls().toArray(
                                                                         new String[0]));
        log(dcqlQuery);
        return dcqlQuery;
    }

    private void preProcessCategories(EntityInterface masterEntry,
                                      IConstraints constraints) {
        Connection connection = ConnectionUtil.getConnection();
        new CategoryPreprocessor().processCategories(constraints, masterEntry,
                                                     connection);
        ConnectionUtil.close(connection);
    }

    private void assignDcqlConstraintToTarget(Object targetObject,
                                              DcqlConstraint constraint) {
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

    private static boolean logDCQL() {
        return Logger.out.isInfoEnabled();
    }

    private static void log(DCQLQuery dcqlQuery) {
        if (!logDCQL()) {
            return;
        }
        try {
            Utils.serializeDocument(
                                    LOG_BASE_DIR + "/" + getLogFileName(),
                                    dcqlQuery,
                                    new QName(
                                            "http://caGrid.caBIG/1.0/gov.nih.nci.cagrid.dcql",
                                            "DCQLQuery"));
        } catch (Exception e) {
            Logger.out.info("Could not log dcql.");
        }

    }

    private static synchronized String getLogFileName() {
        Date currDate = new Date(System.currentTimeMillis());
        return LOG_FILE_NAME_PREFIX + dateFormat.format(currDate) + ".xml";
    }
}
