package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.QueryExecutorPropertes;
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

    private BlockingQueue<Runnable> waitingQueue =
            new ArrayBlockingQueue<Runnable>(QueryExecutorPropertes.getPerQueryMinThreadLimit());

    private QueryExecutorThreadPool threadPoolExecutor =
            new QueryExecutorThreadPool(QueryExecutorPropertes.getPerQueryMinThreadLimit(), QueryExecutorPropertes
                .getPerQueryMaxThreadLimit(), 5, TimeUnit.SECONDS, waitingQueue);

    private ICab2bQuery query;

    private ConstraintsBuilderResult constraintsBuilderResult;

    private CategoryPreprocessorResult categoryPreprocessorResult;

    private IQueryResultTransformer<IRecord, ICategorialClassRecord> transformer;

    private GlobusCredential credential;

    private IQueryResult<ICategorialClassRecord> catQueryResult = null;

    private int noOfRecordsCreated = 0;

    private IQueryResult<? extends IRecord> queryResult = null;

    private List<IQueryResult<ICategorialClassRecord>> categoryResults = null;

    private boolean normalQueryFinished = false;

    private List<Thread> workerThreadList = new ArrayList<Thread>();

    /**
     * Constructor initializes object with query and globus credentials
     *
     * @param query
     * @param cred
     */
    public QueryExecutor(ICab2bQuery query, GlobusCredential credential) {
        setQuery(query);
        this.transformer =
                QueryResultTransformerFactory.createTransformer(query.getOutputEntity(), IRecord.class,
                                                                ICategorialClassRecord.class);
        this.credential = credential;

        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        this.categoryPreprocessorResult = preProcessCategories();
        ConstraintsBuilder constraintsBuilder = new ConstraintsBuilder(query, categoryPreprocessorResult);
        this.constraintsBuilderResult = constraintsBuilder.buildConstraints();
        threadPoolExecutor.setThreadFactory(new QueryExecutorThreadFactory());
    }

    private CategoryPreprocessorResult preProcessCategories() {
        CategoryPreprocessorResult x = new CategoryPreprocessor().processCategories(query);
        if (query.isKeywordSearch()) {
            query = new QueryConverter().convertToKeywordQuery((ICab2bQuery) query);
        }
        return x;
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
    public void executeQuery() {
        logger.info("Entered QueryExecutor...");
        if (isCategoryOutput()) {
            List<ICab2bQuery> queries = getQueriesPerURL();
            for (ICab2bQuery queryWithSingleUrl : queries) {
                Thread workerThread = new WorkerThread(queryWithSingleUrl);
                workerThreadList.add(workerThread);
                workerThread.start();
            }
        } else {
            // if output is a class, then just set the target as the class name,
            // and appropriate constraints.
            DcqlConstraint constraints = constraintsBuilderResult.getDcqlConstraintForClass(getOutputEntity());
            String output = getOutputEntity().getName();
            DCQLQuery dcqlQuery = DCQLGenerator.createDCQLQuery(query, output, constraints);
            queryResult = transformer.getResults(dcqlQuery, getOutputEntity(), credential);
            normalQueryFinished = true;
        }
    }

    /**
     * @param query
     * @return
     */
    private IQueryResult<ICategorialClassRecord> executeCategoryQuery(ICab2bQuery query) {
        Set<TreeNode<IExpression>> rootOutputExprNodes =
                categoryPreprocessorResult.getExprsSourcedFromCategories().get(getOutputEntity());
        categoryResults = new ArrayList<IQueryResult<ICategorialClassRecord>>(rootOutputExprNodes.size());
        for (TreeNode<IExpression> rootOutputExprNode : rootOutputExprNodes) {
            IExpression rootOutputExpr = rootOutputExprNode.getValue();
            DcqlConstraint rootExprDcqlConstraint =
                    constraintsBuilderResult.getExpressionToConstraintMap().get(rootOutputExpr);
            EntityInterface outputEntity = rootOutputExpr.getQueryEntity().getDynamicExtensionsEntity();
            DCQLQuery rootDCQLQuery =
                    DCQLGenerator.createDCQLQuery(query, outputEntity.getName(), rootExprDcqlConstraint);
            CategorialClass catClassForRootExpr =
                    categoryPreprocessorResult.getCatClassForExpr().get(rootOutputExpr);
            IQueryResult<ICategorialClassRecord> allRootExprCatRecs =
                    transformer.getCategoryResults(rootDCQLQuery, catClassForRootExpr, credential);
            Map<String, List<ICategorialClassRecord>> records = allRootExprCatRecs.getRecords();
            int recordSize = 0;
            for (String url : records.keySet()) {
                List<ICategorialClassRecord> listOfRecords = records.get(url);
                recordSize = recordSize + listOfRecords.size();
            }
            verifyRecordLimit(recordSize);
            categoryResults.add(allRootExprCatRecs);
            // process children in parallel.
            queryResult = mergeCatResults(categoryResults, false);
            Map<String, List<ICategorialClassRecord>> urlToRecords = allRootExprCatRecs.getRecords();
            for (String url : urlToRecords.keySet()) {
                int noOfRecords = urlToRecords.get(url).size();
                int blockSize = 1;
                int allowedPriorityRange = Thread.MAX_PRIORITY - Thread.NORM_PRIORITY;
                if (noOfRecords > allowedPriorityRange) {
                    blockSize = noOfRecords / allowedPriorityRange;
                }
                int currentPriority = Thread.MAX_PRIORITY - 1;
                int pointerInBlock = 0;
                for (ICategorialClassRecord rootExprCatRec : urlToRecords.get(url)) {
                    if (pointerInBlock == blockSize) {
                        pointerInBlock = 0;
                        currentPriority--;
                    }
                    threadPoolExecutor.execute(new ChildQueryExecutor(rootExprCatRec, rootOutputExprNode,
                            rootExprCatRec.getRecordId(), currentPriority, ""));
                    pointerInBlock++;
                }
            }
        }
        return mergeCatResults(categoryResults, true);
    }

    private IQueryResult<ICategorialClassRecord> mergeCatResults(
                                                                 List<IQueryResult<ICategorialClassRecord>> categoryResults,
                                                                 boolean isFinal) {
        Category outputCategory = categoryPreprocessorResult.getCategoryForEntity().get(getOutputEntity());
        ICategoryResult<ICategorialClassRecord> res = QueryResultFactory.createCategoryResult(outputCategory);
        for (IQueryResult<ICategorialClassRecord> categoryResult : categoryResults) {
            //Adding all failed URLs: FQP 1.3 updates
            if (isFinal) {
                Collection<FailedTargetURL> failedURLs = categoryResult.getFailedURLs();
                if (failedURLs != null) {
                    failedURLs.addAll(categoryResult.getFailedURLs());
                    res.setFailedURLs(failedURLs);
                }
            }
            for (Map.Entry<String, List<ICategorialClassRecord>> entry : categoryResult.getRecords().entrySet()) {
                res.addRecords(entry.getKey(), entry.getValue());
            }
        }
        //TODO pivots the results around the original root and then merges them...
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

    private void verifyRecordLimit(int count) {
        noOfRecordsCreated = noOfRecordsCreated + count;
        if (noOfRecordsCreated > QueryExecutorPropertes.getPerQueryAllowedRecords()) {
            logger.error("---------------------------------------------------------");
            logger.error("Given query exceeds max number of Records : "
                    + QueryExecutorPropertes.getPerQueryAllowedRecords());
            logger.error("Shutting down the executor..." + noOfRecordsCreated);
            logger.error("---------------------------------------------------------");
            threadPoolExecutor.shutdownNow();
            throw new RuntimeException("Given query exceeds max number of Records");
        }
    }

    private List<ICab2bQuery> getQueriesPerURL() {
        List<ICab2bQuery> queriesWithSingleUrl = new ArrayList<ICab2bQuery>(query.getOutputUrls().size());
        for (String url : query.getOutputUrls()) {
            ICab2bQuery queryWithSingleUrl = (ICab2bQuery) DynamicExtensionsUtility.cloneObject(query);
            List<String> targetUrls = new ArrayList<String>(1);
            targetUrls.add(url);
            queryWithSingleUrl.setOutputUrls(targetUrls);
            queriesWithSingleUrl.add(queryWithSingleUrl);
        }
        return queriesWithSingleUrl;
    }

    /**
     * @author deepak_shingan
     */
    private class WorkerThread extends Thread {
        ICab2bQuery queryPerUrl = null;

        WorkerThread(ICab2bQuery queryCopy) {
            this.queryPerUrl = queryCopy;
        }

        public void run() {
            IQueryResult<ICategorialClassRecord> resultWithSingleUrl = executeCategoryQuery(queryPerUrl);
            Map<String, List<ICategorialClassRecord>> recordMap = resultWithSingleUrl.getRecords();

            if (catQueryResult == null) {
                catQueryResult = resultWithSingleUrl;
            } else {
                if (recordMap.keySet().size() > 1) {
                    throw new RuntimeException("More then one URL found in result even after splitting per URL");
                }
                for (String url : recordMap.keySet()) {
                    catQueryResult.addRecords(url, recordMap.get(url));
                }
                int recordSize = 0;
                for (String url : catQueryResult.getRecords().keySet()) {
                    List<ICategorialClassRecord> listOfRecords = catQueryResult.getRecords().get(url);
                    recordSize = recordSize + listOfRecords.size();
                }
                verifyRecordLimit(recordSize);
                Collection<FailedTargetURL> urls = resultWithSingleUrl.getFailedURLs();
                if (urls != null) {
                    catQueryResult.setFailedURLs(urls);
                }
            }
            if (threadPoolExecutor.isProcessingFinished()) {
                threadPoolExecutor.shutdown();
            }
            queryResult = catQueryResult;
        }
    }

    private class ChildQueryExecutor implements Runnable {
        private ICategorialClassRecord parentCatClassRec;

        private TreeNode<IExpression> parentExprNode;

        private RecordId parentId;

        private int priority;

        private String name;

        public ChildQueryExecutor(
                ICategorialClassRecord parentCatClassRec,
                TreeNode<IExpression> parentExprNode,
                RecordId parentId,
                int priority,
                String name) {
            this.parentCatClassRec = parentCatClassRec;
            this.parentExprNode = parentExprNode;
            this.parentId = parentId;
            this.priority = priority;
            this.name = name;
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
                    IAssociation association =
                            getQuery().getConstraints().getJoinGraph().getAssociation(parentExpr, childExpr);

                    AbstractAssociationConstraint parentIdConstraint;
                    try {
                        parentIdConstraint = createAssociationConstraint(association.reverse());
                    } catch (Exception e) {
                        // caused by reverse on unidirectional association
                        logger.error(e.getMessage());
                        continue;
                    }

                    parentIdConstraint.addChildConstraint(createIdConstraint(getIdAttribute(childExpr
                        .getQueryEntity().getDynamicExtensionsEntity()), parentId.getId()));
                    Map<IExpression, DcqlConstraint> map = constraintsBuilderResult.getExpressionToConstraintMap();
                    DcqlConstraint constraintForChildExpr =
                            addParentIdConstraint(map.get(childExpr), parentIdConstraint);
                    EntityInterface childEntity = childExpr.getQueryEntity().getDynamicExtensionsEntity();
                    String entityName = childEntity.getName();
                    DCQLQuery queryForChildExpr =
                            DCQLGenerator.createDCQLQuery(query, entityName, constraintForChildExpr, parentId
                                .getUrl());

                    CategorialClass catClassForChildExpr =
                            categoryPreprocessorResult.getCatClassForExpr().get(childExpr);
                    if (catClassForChildExpr == null) {
                        // expr was formed for entity on path between catClasses...
                        IQueryResult<IRecord> childExprClassRecs =
                                transformer.getResults(queryForChildExpr, childEntity, credential);
                        //queryResult = childExprClassRecs;
                        Collection<List<IRecord>> records = childExprClassRecs.getRecords().values();
                        verifyRecordLimit(records.size());

                        for (List<IRecord> listRec : records) {
                            if (listRec.iterator().hasNext()) {
                                IRecord record = listRec.iterator().next();
                                threadPoolExecutor.execute(new ChildQueryExecutor(parentCatClassRec,
                                        childExprNode, record.getRecordId(), priority, ""));
                            }
                        }
                    } else {
                        // expr is for a catClass; add recs to parentCatClassRec
                        IQueryResult<ICategorialClassRecord> childExprCatResult =
                                transformer
                                    .getCategoryResults(queryForChildExpr, catClassForChildExpr, credential);
                        List<ICategorialClassRecord> childExprCatRecs =
                                childExprCatResult.getRecords().get(parentId.getUrl());
                        if (childExprCatRecs != null && !childExprCatRecs.isEmpty()) {
                            verifyRecordLimit(childExprCatRecs.size());
                            parentCatClassRec.addCategorialClassRecords(catClassForChildExpr, childExprCatRecs);
                            Set<CategorialClass> children =
                                    childExprCatRecs.get(0).getCategorialClass().getChildren();
                            if (children != null && !children.isEmpty()) {
                                for (ICategorialClassRecord childExprCatRec : childExprCatRecs) {
                                    threadPoolExecutor.execute(new ChildQueryExecutor(childExprCatRec,
                                            childExprNode, childExprCatRec.getRecordId(), priority, ""));
                                }
                            }
                        }
                    }
                }
                queryResult = mergeCatResults(categoryResults, false);
            } catch (Throwable e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }

        public int getPriority() {
            return priority;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * @return the queryResult
     */
    public IQueryResult<? extends IRecord> getCompleteResults() {
        while (!isProcessingFinished()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException("Thread to get CompleteResults was interrupted.", e);
            }
        }
        return queryResult;
    }

    /**
     * @return the queryResult
     */
    public IQueryResult<? extends IRecord> getPartialResult() {
        return queryResult;
    }

    /**
     * @return the isProcessingFinished
     */
    public boolean isProcessingFinished() {
        boolean threadsDone = true;
        for (Thread t : workerThreadList) {
            if (t.getState() != Thread.State.TERMINATED) {
                threadsDone = false;
            }
        }
        if (threadsDone) {
            normalQueryFinished = true;
            return threadPoolExecutor.isProcessingFinished() || normalQueryFinished;
        } else {
            return false;
        }
    }

    /**
     * ThreadFactory designed to enable creation of priority based threads
     * @author chandrakant_talele
     */
    class QueryExecutorThreadFactory implements ThreadFactory {

        /**
         * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
         */
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            if (r instanceof ChildQueryExecutor) {
                ChildQueryExecutor exe = (ChildQueryExecutor) r;
                t.setPriority(exe.getPriority());
            }
            return t;
        }

    }
}
