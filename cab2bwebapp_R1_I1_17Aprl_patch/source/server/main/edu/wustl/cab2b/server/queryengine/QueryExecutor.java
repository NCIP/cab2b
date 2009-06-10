package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.FailedTargetURL;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.ICategoryResult;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.QueryResultFactory;
import edu.wustl.cab2b.common.queryengine.result.RecordId;
import edu.wustl.cab2b.common.util.TreeNode;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.queryengine.querybuilders.CategoryPreprocessor;
import edu.wustl.cab2b.server.queryengine.querybuilders.CategoryPreprocessorResult;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.Cab2bGroup;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.ConstraintsBuilder;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.ConstraintsBuilderResult;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.AbstractAssociationConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.AttributeConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.DcqlConstraint;
import edu.wustl.cab2b.server.queryengine.resulttransformers.IQueryResultTransformer;
import edu.wustl.cab2b.server.queryengine.resulttransformers.QueryResultTransformerFactory;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.querysuite.metadata.category.Category;
import edu.wustl.common.querysuite.queryobject.DataType;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import gov.nih.nci.cagrid.dcql.DCQLQuery;

/**
 * Processes an input {@link ICab2bQuery} and returns the results for the query.<br>
 * In this process, this class uses
 * <ol>
 * <li>The {@link CategoryPreprocessor} to pre-process categories in the query</li>
 * <li>The {@link ConstraintsBuilder} to obtain DCQL-specific representation of
 * the constraints</li>
 * <li>Appropriate {@link IQueryResultTransformer} to transform the DCQL to
 * results.</li>
 * </ol>
 * This class primarily handles the output related portion of the query
 * <ol>
 * <li>If output is a class, the DCQL target is simply the output class name.</li>
 * <li>If output is a category, then multiple DCQLs are formed and executed to
 * obtain results for the multiple classes within the category.</li>
 * </ol>
 * 
 * @author srinath_k
 */

public class QueryExecutor {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(QueryExecutor.class);

    private ICab2bQuery query;

    private ConstraintsBuilderResult constraintsBuilderResult;

    private CategoryPreprocessorResult categoryPreprocessorResult;

    private IQueryResultTransformer<IRecord, ICategorialClassRecord> transformer;

    private GlobusCredential credential;

    /**
     * Constructor initializes object with query and globus credentials
     * 
     * @param query
     * @param cred
     */
    public QueryExecutor(ICab2bQuery query, GlobusCredential credential) {
        setQuery(query);
        this.transformer = QueryResultTransformerFactory.createTransformer(query.getOutputEntity(), IRecord.class,
                                                                           ICategorialClassRecord.class);
        this.credential = credential;
    }

    private CategoryPreprocessorResult preProcessCategories(IQuery query) {
        return new CategoryPreprocessor().processCategories(query);
    }

    /**
     * This method returns the ICab2bQuery object
     * 
     * @return ICab2bQuery object
     */
    public ICab2bQuery getQuery() {
        return query;
    }

    /**
     * This method sets the ICab2bQuery object
     * 
     * @param query
     *            ICab2bQuery object
     */
    public void setQuery(ICab2bQuery query) {
        this.query = query;
    }

    /**
     * This methods generates DCQL(s) for ICab2bQuery object and gets the
     * results using appropriate {@link IQueryResultTransformer}. If output is
     * a class, then just set the target as the class name, and appropriate
     * constraints.
     * 
     * @return Returns the IQueryResult
     */
    public IQueryResult<? extends IRecord> executeQuery() {
        logger.info("Entered QueryExecutor...");
        this.categoryPreprocessorResult = preProcessCategories(query);
        ConstraintsBuilder constraintsBuilder = new ConstraintsBuilder(query, categoryPreprocessorResult);
        this.constraintsBuilderResult = constraintsBuilder.buildConstraints();

        IQueryResult<? extends IRecord> queryResult = null;

        if (isCategoryOutput()) {
            Set<TreeNode<IExpression>> rootOutputExprNodes = this.categoryPreprocessorResult.getExprsSourcedFromCategories().get(
                                                                                                                                 getOutputEntity());
            List<IQueryResult<ICategorialClassRecord>> categoryResults = new ArrayList<IQueryResult<ICategorialClassRecord>>(
                    rootOutputExprNodes.size());
            for (TreeNode<IExpression> rootOutputExprNode : rootOutputExprNodes) {
                IExpression rootOutputExpr = rootOutputExprNode.getValue();
                DcqlConstraint rootExprDcqlConstraint = this.constraintsBuilderResult.getExpressionToConstraintMap().get(
                                                                                                                         rootOutputExpr);
                EntityInterface outputEntity = rootOutputExpr.getQueryEntity().getDynamicExtensionsEntity();
                DCQLQuery rootDCQLQuery = DCQLGenerator.createDCQLQuery(query, outputEntity.getName(),
                                                                        rootExprDcqlConstraint);
                CategorialClass catClassForRootExpr = this.categoryPreprocessorResult.getCatClassForExpr().get(
                                                                                                               rootOutputExpr);
                IQueryResult<ICategorialClassRecord> allRootExprCatRecs = transformer.getCategoryResults(
                                                                                                         rootDCQLQuery,
                                                                                                         catClassForRootExpr,
                                                                                                         credential);
                categoryResults.add(allRootExprCatRecs);
                // process children in parallel.
                for (Map.Entry<String, List<ICategorialClassRecord>> entry : allRootExprCatRecs.getRecords().entrySet()) {
                    List<ChildQueryExecutor> childQueryExecList = new ArrayList<ChildQueryExecutor>();
                    for (ICategorialClassRecord rootExprCatRec : entry.getValue()) {
                        childQueryExecList.add(new ChildQueryExecutor(rootExprCatRec, rootOutputExprNode,
                                rootExprCatRec.getRecordId()));
                    }
                    callChildrenQueryExecutors(childQueryExecList);
                }
            }
            queryResult = mergeCatResults(categoryResults);
        } else {
            // if output is a class, then just set the target as the class name,
            // and appropriate constraints.
            DCQLQuery dcqlQuery = DCQLGenerator.createDCQLQuery(
                                                                query,
                                                                getOutputEntity().getName(),
                                                                this.constraintsBuilderResult.getDcqlConstraintForClass(getOutputEntity()));
            queryResult = transformer.getResults(dcqlQuery, getOutputEntity(), credential);
        }
        logger.info("Exiting QueryExecutor.");
        return queryResult;
    }

    private static final boolean MULTITHREAD = true;

    private void callChildrenQueryExecutors(List<ChildQueryExecutor> childQueryExecList) {
        List<Callable<java.lang.Object>> callables = new ArrayList<Callable<java.lang.Object>>();

        for (ChildQueryExecutor foo : childQueryExecList) {
            if (MULTITHREAD) {
                Callable<java.lang.Object> p = Executors.callable(foo);
                callables.add(p);
            } else {
                foo.run();
            }
        }
        if (MULTITHREAD) {
            ExecutorService executorService = Executors.newCachedThreadPool();
            try {
                executorService.invokeAll(callables);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private IQueryResult<ICategorialClassRecord> mergeCatResults(
                                                                 List<IQueryResult<ICategorialClassRecord>> categoryResults) {
        Category outputCategory = this.categoryPreprocessorResult.getCategoryForEntity().get(getOutputEntity());
        ICategoryResult<ICategorialClassRecord> res = QueryResultFactory.createCategoryResult(outputCategory);
        for (IQueryResult<ICategorialClassRecord> categoryResult : categoryResults) {
            for (Map.Entry<String, List<ICategorialClassRecord>> entry : categoryResult.getRecords().entrySet()) {
                res.addRecords(entry.getKey(), entry.getValue());
                //Adding all failed urls Deepak : FQP 1.3 updates 
                Collection<FailedTargetURL> failedURLs = categoryResult.getFailedURLs();
                if (failedURLs != null) {
                    failedURLs.addAll(categoryResult.getFailedURLs());
                    logger.info("Category Failed URL number " + failedURLs.size());
                    res.setFailedURLs(failedURLs);
                }
            }
        }
        // TODO pivots the results around the original root and then merges
        // them...
        return res;
    }

    private DcqlConstraint addParentIdConstraint(DcqlConstraint constraint, DcqlConstraint parentIdConstraint) {
        Cab2bGroup cab2bGroup = new Cab2bGroup(LogicalOperator.And);
        cab2bGroup.addConstraint(constraint);
        cab2bGroup.addConstraint(parentIdConstraint);
        return cab2bGroup.getDcqlConstraint();
    }

    private AttributeConstraint createIdConstraint(AttributeInterface attribute, String id) {
        return ConstraintsBuilder.createAttributeConstraint(attribute.getName(), RelationalOperator.Equals, id,
                                                            DataType.String);
    }

    private AttributeInterface getIdAttribute(EntityInterface entity) {
        return Utility.getIdAttribute(entity);
    }

    private AbstractAssociationConstraint createAssociationConstraint(IAssociation association) {
        return ConstraintsBuilder.createAssociation(association);
    }

    private EntityInterface getOutputEntity() {
        return getQuery().getOutputEntity();
    }

    private boolean isCategoryOutput() {
        return Utility.isCategory(getOutputEntity());
    }

    private class ChildQueryExecutor implements Runnable {
        private ICategorialClassRecord parentCatClassRec;

        private TreeNode<IExpression> parentExprNode;

        private RecordId parentId;

        public ChildQueryExecutor(
                ICategorialClassRecord parentCatClassRec,
                TreeNode<IExpression> parentExprNode,
                RecordId parentId) {
            this.parentCatClassRec = parentCatClassRec;
            this.parentExprNode = parentExprNode;
            this.parentId = parentId;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Runnable#run()
         */
        public void run() {
            try {
                IExpression parentExpr = parentExprNode.getValue();
                for (TreeNode<IExpression> childExprNode : parentExprNode.getChildren()) {
                    IExpression childExpr = childExprNode.getValue();
                    IAssociation association = getQuery().getConstraints().getJoinGraph().getAssociation(
                                                                                                         parentExpr,
                                                                                                         childExpr);

                    AbstractAssociationConstraint parentIdConstraint;
                    try {
                        parentIdConstraint = createAssociationConstraint(association.reverse());
                    } catch (Exception e) {
                        // caused by reverse on unidirectional association
                        e.printStackTrace();
                        continue;
                    }
                    parentIdConstraint.addChildConstraint(createIdConstraint(
                                                                             getIdAttribute(childExpr.getQueryEntity().getDynamicExtensionsEntity()),
                                                                             parentId.getId()));
                    DcqlConstraint constraintForChildExpr = addParentIdConstraint(
                                                                                  QueryExecutor.this.constraintsBuilderResult.getExpressionToConstraintMap().get(
                                                                                                                                                                 childExpr),
                                                                                  parentIdConstraint);
                    EntityInterface childEntity = childExpr.getQueryEntity().getDynamicExtensionsEntity();
                    String entityName = childEntity.getName();
                    DCQLQuery queryForChildExpr = DCQLGenerator.createDCQLQuery(query, entityName,
                                                                                constraintForChildExpr,
                                                                                parentId.getUrl());

                    List<ChildQueryExecutor> childQueryExecList = new ArrayList<ChildQueryExecutor>();
                    CategorialClass catClassForChildExpr = QueryExecutor.this.categoryPreprocessorResult.getCatClassForExpr().get(
                                                                                                                                  childExpr);
                    if (catClassForChildExpr == null) {
                        // expr was formed for entity on path between
                        // catClasses...
                        IQueryResult<IRecord> childExprClassRecs = transformer.getResults(queryForChildExpr,
                                                                                          childEntity, credential);

                        // Previous Comment : Only one url at a time; so directly do next();
                        // Scene changed : Deepak Update : confirm with srinath
                        Iterator<List<IRecord>> iter = childExprClassRecs.getRecords().values().iterator();
                        while (iter.hasNext()) {
                            List<IRecord> listRec = iter.next();
                            if (listRec.iterator().hasNext()) {
                                IRecord record = listRec.iterator().next();
                                childQueryExecList.add(new ChildQueryExecutor(parentCatClassRec, childExprNode,
                                        record.getRecordId()));
                            }
                        }
                    } else {
                        // expr is for a catClass; add recs to parentCatClassRec
                        IQueryResult<ICategorialClassRecord> childExprCatResult = transformer.getCategoryResults(
                                                                                                                 queryForChildExpr,
                                                                                                                 catClassForChildExpr,
                                                                                                                 credential);

                        List<ICategorialClassRecord> childExprCatRecs = childExprCatResult.getRecords().get(
                                                                                                            parentId.getUrl());
                        //Added by Deepak
                        //have to verify with srinath
                        if (childExprCatRecs != null) {
                            parentCatClassRec.addCategorialClassRecords(catClassForChildExpr, childExprCatRecs);
                            for (ICategorialClassRecord childExprCatRec : childExprCatRecs) {
                                childQueryExecList.add(new ChildQueryExecutor(childExprCatRec, childExprNode,
                                        childExprCatRec.getRecordId()));
                            }
                        }
                    }
                    callChildrenQueryExecutors(childQueryExecList);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
