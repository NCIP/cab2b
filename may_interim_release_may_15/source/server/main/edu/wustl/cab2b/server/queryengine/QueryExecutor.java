package edu.wustl.cab2b.server.queryengine;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.CategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.CategoryResult;
import edu.wustl.cab2b.common.queryengine.result.IClassRecords;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.queryengine.querybuilders.CategoryPreprocessor;
import edu.wustl.cab2b.server.queryengine.querybuilders.CategoryPreprocessorResult;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.ConstraintsBuilder;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.ConstraintsBuilderResult;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.MyGroup;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.AbstractAssociationConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.AttributeConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.DcqlConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.ForeignAssociationConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.GroupConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.LocalAssociationConstraint;
import edu.wustl.cab2b.server.queryengine.utils.TreeNode;
import edu.wustl.cab2b.server.util.ConnectionUtil;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.querysuite.metadata.category.Category;
import edu.wustl.common.querysuite.queryobject.DataType;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcql.Object;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.fqp.processor.FederatedQueryEngine;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;

/**
 * This class is responsible for building a DCQL out of ICab2bQuery, and get
 * results by executing that DCQL.
 * @author Chandrakant Talele
 */

// TODO move logging to separate class...
public class QueryExecutor {
    private ICab2bQuery query;

    private ConstraintsBuilderResult constraintsBuilderResult;

    private CategoryPreprocessorResult categoryPreprocessorResult;

    private DcqlLogger dcqlLogger;

    public QueryExecutor(ICab2bQuery query) {
        setQuery(query);
        setDcqlLogger(new DcqlLogger());
    }

    private DCQLQuery createDCQLQuery(String outputName,
                                      DcqlConstraint constraint,
                                      String... outputUrls) {
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

    private CategoryPreprocessorResult preProcessCategories(IQuery query) {
        Connection connection = ConnectionUtil.getConnection();
        CategoryPreprocessorResult res = new CategoryPreprocessor().processCategories(
                                                                                      query,
                                                                                      connection);
        ConnectionUtil.close(connection);
        return res;
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

    public ICab2bQuery getQuery() {
        return query;
    }

    public void setQuery(ICab2bQuery query) {
        this.query = query;
    }

    /**
     * This methods generates DCQL out of input ICab2bQuery object and fires it,
     * and returns the results.
     * @param query
     *            Query which needs to be executed.
     * @return Returns the IQueryResult
     */
    public IQueryResult executeQuery() {
        Logger.out.info("Entered QueryExecutor...");
        this.categoryPreprocessorResult = preProcessCategories(query);
        ConstraintsBuilder constraintsBuilder = new ConstraintsBuilder(query,
                categoryPreprocessorResult);

        this.constraintsBuilderResult = constraintsBuilder.buildConstraints();

        IQueryResult queryResult;

        if (isCategoryOutput()) {
            Set<TreeNode<IExpression>> rootOutputExprNodes = this.categoryPreprocessorResult.getExprsSourcedFromCategories().get(
                                                                                                                                 getOutputEntity());
            List<CategoryResult> categoryResults = new ArrayList<CategoryResult>(
                    rootOutputExprNodes.size());
            for (TreeNode<IExpression> rootOutputExprNode : rootOutputExprNodes) {
                IExpression rootOutputExpr = rootOutputExprNode.getValue();
                DcqlConstraint rootExprDcqlConstraint = this.constraintsBuilderResult.getExpressionToConstraintMap().get(
                                                                                                                         rootOutputExpr);

                EntityInterface outputEntity = rootOutputExpr.getConstraintEntity().getDynamicExtensionsEntity();
                DCQLQuery rootDCQLQuery = createDCQLQuery(
                                                          outputEntity.getName(),
                                                          rootExprDcqlConstraint);
                CategorialClass catClassForRootExpr = this.categoryPreprocessorResult.getCatClassForExpr().get(
                                                                                                               rootOutputExpr);
                IClassRecords rootExprClassRecs = executeDCQL(
                                                              rootDCQLQuery,
                                                              catClassForRootExpr.getCategorialClassEntity().getAttributeCollection());

                Map<String, List<CategorialClassRecord>> allRootExprCatRecs = classRecsToCatRecs(
                                                                                                 rootExprClassRecs,
                                                                                                 catClassForRootExpr);

                CategoryResult catResult = new CategoryResult();
                categoryResults.add(catResult);

                for (Map.Entry<String, List<CategorialClassRecord>> entry : allRootExprCatRecs.entrySet()) {
                    String url = entry.getKey();
                    List<CategorialClassRecord> rootExprCatRecs = entry.getValue();
                    catResult.putCategorialClassRecordsForUrl(url,
                                                              rootExprCatRecs);
                    for (CategorialClassRecord rootExprCatRec : rootExprCatRecs) {
                        getChildrenRecords(rootExprCatRec, rootOutputExprNode,
                                           url, rootExprCatRec.getId());
                    }
                }

            }
            queryResult = mergeCatResults(categoryResults);
        } else {
            DCQLQuery dcqlQuery = createDCQLQuery(
                                                  getOutputEntity().getName(),
                                                  this.constraintsBuilderResult.getDcqlConstraintForClass(getOutputEntity()));

            // log(dcqlQuery);
            queryResult = executeDCQL(dcqlQuery);
        }
        Logger.out.info("Exiting QueryExecutor.");
        return queryResult;
    }

    // parentExpr need not correspond to parentCatClassRec.
    // parentCatClassRec will be updated for a recursive call only if the expr
    // causing the recursive call belongs to a catClass.
    private void getChildrenRecords(CategorialClassRecord parentCatClassRec,
                                    TreeNode<IExpression> parentExprNode,
                                    String url, String parentId) {
        IExpression parentExpr = parentExprNode.getValue();
        for (TreeNode<IExpression> childExprNode : parentExprNode.getChildren()) {
            IExpression childExpr = childExprNode.getValue();
            IAssociation association = getQuery().getConstraints().getJoinGraph().getAssociation(
                                                                                                 parentExpr.getExpressionId(),
                                                                                                 childExpr.getExpressionId());

            CategorialClass catClassForChildExpr = this.categoryPreprocessorResult.getCatClassForExpr().get(
                                                                                                            childExpr);

            AbstractAssociationConstraint parentIdConstraint = createAssociationConstraint(association.reverse());
            parentIdConstraint.addChildConstraint(createIdConstraint(
                                                                     getIdAttribute(catClassForChildExpr),
                                                                     parentId));
            DcqlConstraint constraintForChildExpr = addParentIdConstraint(
                                                                          this.constraintsBuilderResult.getExpressionToConstraintMap().get(
                                                                                                                                           childExpr),
                                                                          parentIdConstraint);
            String entityName = childExpr.getConstraintEntity().getDynamicExtensionsEntity().getName();
            DCQLQuery queryForChildExpr = createDCQLQuery(
                                                          entityName,
                                                          constraintForChildExpr,
                                                          url);

            IClassRecords childExprClassRecs = executeDCQL(
                                                           queryForChildExpr,
                                                           catClassForChildExpr.getCategorialClassEntity().getAttributeCollection());

            if (catClassForChildExpr != null) {
                // expr is for a catClass; add recs to parentCatClassRec
                List<CategorialClassRecord> childExprCatRecs = classRecsToCatRecs(
                                                                                  childExprClassRecs,
                                                                                  catClassForChildExpr).get(
                                                                                                            url);
                parentCatClassRec.addCategorialClassRecords(
                                                            catClassForChildExpr,
                                                            childExprCatRecs);
                for (CategorialClassRecord childExprCatRec : childExprCatRecs) {
                    getChildrenRecords(childExprCatRec, childExprNode, url,
                                       childExprCatRec.getId());
                }
            } else {
                // expr was formed for entity on path between catClasses...
                int idAttributeIndex = getIdAttributeIndex(childExprClassRecs.getAttributes());
                // only one url at a time; so directly do next();
                for (String[] record : childExprClassRecs.getAllRecords().values().iterator().next()) {
                    String childRecId = record[idAttributeIndex];
                    getChildrenRecords(parentCatClassRec, childExprNode, url,
                                       childRecId);
                }
            }

        }
    }

    private CategoryResult mergeCatResults(List<CategoryResult> categoryResults) {
        CategoryResult res = new CategoryResult();
        Category outputCategory = this.categoryPreprocessorResult.getCategoryForEntity().get(
                                                                                             getOutputEntity());
        res.setCategory(outputCategory);

        for (CategoryResult categoryResult : categoryResults) {
            res.copyRecords(categoryResult);
        }
        // TODO pivots the results around the original root and then merges
        // them...
        return res;
    }

    private AttributeInterface getIdAttribute(CategorialClass catClass) {
        return getIdAttribute(catClass.getCategorialClassEntity());
    }

    private AttributeInterface getIdAttribute(EntityInterface entity) {
        return Utility.getIdAttribute(entity);
    }

    private int getIdAttributeIndex(List<AttributeInterface> attributes) {
        AttributeInterface idAttribute = getIdAttribute(attributes.get(0).getEntity());
        int idAttributeIndex = attributes.indexOf(idAttribute);
        return idAttributeIndex;
    }

    private Map<String, List<CategorialClassRecord>> classRecsToCatRecs(
                                                                        IClassRecords classRecords,
                                                                        CategorialClass categorialClass) {
        Map<String, List<CategorialClassRecord>> res = new HashMap<String, List<CategorialClassRecord>>();

        List<AttributeInterface> classAttributes = classRecords.getAttributes();
        int idAttributeIndex = getIdAttributeIndex(classAttributes);

        Set<CategorialAttribute> catAttributes = categorialClass.getCategorialAttributeCollection();
        for (Map.Entry<String, String[][]> entry : classRecords.getAllRecords().entrySet()) {
            String url = entry.getKey();
            String[][] values = entry.getValue();

            List<CategorialClassRecord> catClassRecs = new ArrayList<CategorialClassRecord>();
            res.put(url, catClassRecs);

            Map<CategorialAttribute, Integer> catAttrToIndex = new HashMap<CategorialAttribute, Integer>(
                    catAttributes.size());

            for (CategorialAttribute catAttr : catAttributes) {
                int index = classAttributes.indexOf(catAttr.getSourceClassAttribute());
                catAttrToIndex.put(catAttr, index);
            }

            for (String[] rec : values) {
                CategorialClassRecord catClassRec = new CategorialClassRecord();
                catClassRecs.add(catClassRec);
                catClassRec.setCategorialClass(categorialClass);
                catClassRec.setId(rec[idAttributeIndex]);
                for (CategorialAttribute catAttr : catAttributes) {
                    catClassRec.getAttributesValues().put(
                                                          catAttr,
                                                          rec[catAttrToIndex.get(catAttr)]);
                }
            }
        }

        return res;
    }

    private DcqlConstraint addParentIdConstraint(
                                                 DcqlConstraint constraint,
                                                 DcqlConstraint parentIdConstraint) {
        MyGroup myGroup = new MyGroup(LogicalOperator.And);
        myGroup.addConstraint(constraint);
        myGroup.addConstraint(parentIdConstraint);
        return myGroup.getDcqlConstraint();
    }

    private AttributeConstraint createIdConstraint(
                                                   AttributeInterface attribute,
                                                   String id) {
        return ConstraintsBuilder.createAttribute(attribute.getName(),
                                                  RelationalOperator.Equals,
                                                  id, DataType.String);
    }

    private AbstractAssociationConstraint createAssociationConstraint(
                                                                      IAssociation association) {
        return ConstraintsBuilder.createAssociation(association);
    }

    private EntityInterface getOutputEntity() {
        return getQuery().getOutputEntity();
    }

    private boolean isCategoryOutput() {
        return Utility.isCategory(getOutputEntity());
    }

    /**
     * Gets DCQL and fires it and returns generated CQLQueryResults.
     * @param query
     *            Input query
     * @return Returns the DCQLQueryResults
     */
    private IClassRecords executeDCQL(DCQLQuery dcqlQuery) {
        return executeDCQL(dcqlQuery, new ArrayList<AttributeInterface>(
                getOutputEntity().getAttributeCollection()));
    }

    private IClassRecords executeDCQL(
                                      DCQLQuery dcqlQuery,
                                      Collection<AttributeInterface> outputAttributes) {
        FederatedQueryEngine federatedQueryEngine = new FederatedQueryEngine();
        DCQLQueryResultsCollection queryResults = null;
        try {
            Logger.out.info("Executing DCQL... Target is : "
                    + dcqlQuery.getTargetObject().getName());
            log(dcqlQuery);
            queryResults = federatedQueryEngine.execute(dcqlQuery);
            Logger.out.info("Executed DCQL successfully.");
        } catch (FederatedQueryProcessingException e) {
            throw new RuntimeException("Exception while executing DCQL", e,
                    ErrorCodeConstants.QUERY_EXECUTION_ERROR);
        }

        DCQLResultsTransformer resultsTransformer = new DCQLResultsTransformer();
        IClassRecords classRecs = resultsTransformer.getClassRecords(
                                                                     queryResults,
                                                                     new ArrayList<AttributeInterface>(
                                                                             outputAttributes));
        return classRecs;
    }

    private void log(DCQLQuery dcqlQuery) {
        getDcqlLogger().log(dcqlQuery);
    }

    public DcqlLogger getDcqlLogger() {
        return dcqlLogger;
    }

    public void setDcqlLogger(DcqlLogger dcqlLogger) {
        this.dcqlLogger = dcqlLogger;
    }

}
