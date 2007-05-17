package edu.wustl.cab2b.server.queryengine;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.ICategoryResult;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.QueryResultFactory;
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
import edu.wustl.cab2b.server.queryengine.resulttransformers.IQueryResultTransformer;
import edu.wustl.cab2b.server.queryengine.resulttransformers.QueryResultTransformerFactory;
import edu.wustl.cab2b.server.queryengine.utils.TreeNode;
import edu.wustl.cab2b.server.util.ConnectionUtil;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
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

/**
 * This class is responsible for building a DCQL out of ICab2bQuery, and get
 * results by executing that DCQL.
 * @author Srinath
 */

public class QueryExecutor {
    private ICab2bQuery query;

    private ConstraintsBuilderResult constraintsBuilderResult;

    private CategoryPreprocessorResult categoryPreprocessorResult;

    private IQueryResultTransformer<IRecord, ICategorialClassRecord> transformer;

    public QueryExecutor(ICab2bQuery query) {
        setQuery(query);
        this.transformer = QueryResultTransformerFactory.createTransformer(
                                                                           query.getOutputEntity(),
                                                                           IRecord.class,
                                                                           ICategorialClassRecord.class);
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
    public IQueryResult<?> executeQuery() {
        Logger.out.info("Entered QueryExecutor...");
        this.categoryPreprocessorResult = preProcessCategories(query);
        ConstraintsBuilder constraintsBuilder = new ConstraintsBuilder(query,
                categoryPreprocessorResult);

        this.constraintsBuilderResult = constraintsBuilder.buildConstraints();

        IQueryResult queryResult;

        if (isCategoryOutput()) {
            Set<TreeNode<IExpression>> rootOutputExprNodes = this.categoryPreprocessorResult.getExprsSourcedFromCategories().get(
                                                                                                                                 getOutputEntity());
            List<IQueryResult<ICategorialClassRecord>> categoryResults = new ArrayList<IQueryResult<ICategorialClassRecord>>(
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
                IQueryResult<ICategorialClassRecord> allRootExprCatRecs = transformer.getCategoryResults(
                                                                                                         rootDCQLQuery,
                                                                                                         catClassForRootExpr);

                for (Map.Entry<String, List<ICategorialClassRecord>> entry : allRootExprCatRecs.getRecords().entrySet()) {
                    String url = entry.getKey();
                    for (ICategorialClassRecord rootExprCatRec : entry.getValue()) {
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

            queryResult = transformer.getResults(dcqlQuery, getOutputEntity());
        }
        Logger.out.info("Exiting QueryExecutor.");
        return queryResult;
    }

    // parentExpr need not correspond to parentCatClassRec.
    // parentCatClassRec will be updated for a recursive call only if the expr
    // causing the recursive call belongs to a catClass.
    private void getChildrenRecords(ICategorialClassRecord parentCatClassRec,
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

            if (catClassForChildExpr != null) {
                // expr is for a catClass; add recs to parentCatClassRec
                IQueryResult<ICategorialClassRecord> childExprCatResult = transformer.getCategoryResults(
                                                                                                         queryForChildExpr,
                                                                                                         catClassForChildExpr);

                List<ICategorialClassRecord> childExprCatRecs = childExprCatResult.getRecords().get(
                                                                                                    url);
                parentCatClassRec.addCategorialClassRecords(
                                                            catClassForChildExpr,
                                                            childExprCatRecs);
                for (ICategorialClassRecord childExprCatRec : childExprCatRecs) {
                    getChildrenRecords(childExprCatRec, childExprNode, url,
                                       childExprCatRec.getId());
                }
            } else {
                // expr was formed for entity on path between catClasses...
                IQueryResult<IRecord> childExprClassRecs = transformer.getResults(
                                                                                  queryForChildExpr,
                                                                                  catClassForChildExpr.getCategorialClassEntity());

                // only one url at a time; so directly do next();
                for (IRecord record : childExprClassRecs.getRecords().values().iterator().next()) {
                    String childRecId = record.getId();
                    getChildrenRecords(parentCatClassRec, childExprNode, url,
                                       childRecId);
                }
            }

        }
    }

    private IQueryResult<ICategorialClassRecord> mergeCatResults(
                                                                 List<IQueryResult<ICategorialClassRecord>> categoryResults) {
        Category outputCategory = this.categoryPreprocessorResult.getCategoryForEntity().get(
                                                                                             getOutputEntity());
        ICategoryResult<ICategorialClassRecord> res = QueryResultFactory.createCategoryResult(outputCategory);
        for (IQueryResult<ICategorialClassRecord> categoryResult : categoryResults) {
            for (Map.Entry<String, List<ICategorialClassRecord>> entry : categoryResult.getRecords().entrySet()) {
                categoryResult.addRecords(entry.getKey(), entry.getValue());
            }
        }
        // TODO pivots the results around the original root and then merges
        // them...
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
        return ConstraintsBuilder.createAttributeConstraint(
                                                            attribute.getName(),
                                                            RelationalOperator.Equals,
                                                            id, DataType.String);
    }

    private AttributeInterface getIdAttribute(CategorialClass catClass) {
        return getIdAttribute(catClass.getCategorialClassEntity());
    }

    private AttributeInterface getIdAttribute(EntityInterface entity) {
        return Utility.getIdAttribute(entity);
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
}
