package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.cagrid.fqp.results.metadata.ProcessingStatus;
import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.QueryExecutorPropertes;
import edu.wustl.cab2b.common.queryengine.querystatus.AbstractStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatusImpl;
import edu.wustl.cab2b.common.queryengine.querystatus.URLStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.URLStatusImpl;
import edu.wustl.cab2b.common.queryengine.result.FQPUrlStatus;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.ICategoryResult;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.QueryResultFactory;
import edu.wustl.cab2b.common.queryengine.result.RecordId;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.common.util.Constants;
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
import edu.wustl.cab2b.server.queryengine.querystatus.QueryURLStatusOperations;
import edu.wustl.cab2b.server.queryengine.resulttransformers.IQueryResultTransformer;
import edu.wustl.cab2b.server.queryengine.resulttransformers.QueryResultTransformerFactory;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.cab2b.server.util.UtilityOperations;
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

    private BlockingQueue<Runnable> waitingQueue = new LinkedBlockingQueue<Runnable>();

    private QueryExecutorThreadPool threadPoolExecutor =
            new QueryExecutorThreadPool(QueryExecutorPropertes.getPerQueryMaxThreadLimit(), QueryExecutorPropertes
                .getPerQueryMaxThreadLimit(), 1, TimeUnit.SECONDS, waitingQueue);

    private ICab2bQuery query;

    private boolean recordStatus;

    private ConstraintsBuilderResult constraintsBuilderResult;

    private CategoryPreprocessorResult categoryPreprocessorResult;

    private IQueryResultTransformer<IRecord, ICategorialClassRecord> transformer;

    private GlobusCredential credential;

    private IQueryResult<ICategorialClassRecord> catQueryResult = null;

    private int noOfRecordsCreated = 0;

    private IQueryResult<? extends IRecord> queryResult = null;

    private List<IQueryResult<ICategorialClassRecord>> categoryResults = null;

    private boolean normalQueryFinished = false;

    private QueryStatus qStatus = null;

    private UserInterface user = null;

    /**
     * Constructor initializes object with query and globus credentials
     *
     * @param query
     * @param cred
     */
    public QueryExecutor(ICab2bQuery query, GlobusCredential credential) {
        recordStatus = query.getId() != null;
        String userName = Constants.ANONYMOUS;
        if (credential != null) {
            userName = credential.getIdentity();
        }
        user = new UserOperations().getUserByName(userName);

        setQuery(query);
        this.transformer =
                QueryResultTransformerFactory.createTransformer(query.getOutputEntity(), IRecord.class,
                                                                ICategorialClassRecord.class);
        this.credential = credential;
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        this.categoryPreprocessorResult = preProcessCategories();
        ConstraintsBuilder constraintsBuilder = new ConstraintsBuilder(query, categoryPreprocessorResult);
        this.constraintsBuilderResult = constraintsBuilder.buildConstraints();
        threadPoolExecutor.setThreadFactory(new QueryExecutorThreadFactory());
    }

    /**
     * @return
     */
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
     * This method sets the ICab2bQuery object.     
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
        initilizeQueryStatus();
        logger.info("Entered QueryExecutor...");

        if (isCategoryOutput()) {
            List<ICab2bQuery> queries = getQueriesPerURL();
            for (ICab2bQuery queryWithSingleUrl : queries) {
                Thread workerThread = new WorkerThread(queryWithSingleUrl);
                threadPoolExecutor.execute(workerThread);
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
     * Method to update url status properties. It will update only the in-memory query status.
     */
    private synchronized void updateQueryStatus() {
        String status = qStatus.getStatus();
        if (status != null
                && (status.equals(AbstractStatus.Complete) || status.equals(AbstractStatus.Complete_With_Error))) {
            return;
        }

        if (queryResult != null) {
            int totalRecCount = 0;
            Collection<FQPUrlStatus> fqpUrlStatus = queryResult.getFQPUrlStatus();
            Map<String, ?> mapUrlResult = queryResult.getRecords();

            for (FQPUrlStatus fqpUrl : fqpUrlStatus) {
                String url = fqpUrl.getTargetUrl();
                URLStatus uStatusObj = getStatusUrl(url);
                uStatusObj.setStatus(fqpUrl.getStatus());
                List<? extends IRecord> resultPerUrl = (List<? extends IRecord>) mapUrlResult.get(url);
                if (resultPerUrl != null) {
                    int urlRecCount = resultPerUrl.size();
                    uStatusObj.setResultCount(new Integer(urlRecCount));
                    totalRecCount = +urlRecCount;
                    logger.info("Updated record count for url:" + url + "  " + urlRecCount);
                }
            }
            qStatus.setResultCount(totalRecCount);
            //Deriving the query status from URL status
            qStatus.setStatus(AbstractStatus.Processing);
            if (isProcessingFinished() && areAllUrlsFinished(fqpUrlStatus)) {
                qStatus.setQueryEndTime(new Date());
                boolean isEveryUrlWorked = true;
                for (FQPUrlStatus fqpUrl : fqpUrlStatus) {
                    String urlStatus = fqpUrl.getStatus();
                    if (urlStatus.equals(ProcessingStatus._Complete_With_Error)) {
                        qStatus.setStatus(AbstractStatus.Complete_With_Error);
                        isEveryUrlWorked = false;
                        break;
                    }
                }
                if (isEveryUrlWorked) {
                    qStatus.setStatus(AbstractStatus.Complete);
                }
            }
        }
    }

    /**
     * @param fqpUrlStatus
     * @return
     */
    private boolean areAllUrlsFinished(Collection<FQPUrlStatus> fqpUrlStatus) {
        boolean res = true;
        for (FQPUrlStatus fqpUrl : fqpUrlStatus) {
            String urlStatus = fqpUrl.getStatus();
            if (urlStatus.equals(ProcessingStatus._Processing)
                    || urlStatus.equals(ProcessingStatus._Waiting_To_Begin)) {
                res = false;
                break;
            }
        }
        return res;
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.ICab2bQueryExecutor#getStatus()
     */
    public QueryStatus getStatus() {
        updateQueryStatus();
        return qStatus;
    }

    /**
     * Method which returns corresponding URL object for given url string. 
     * @param url
     * @return
     */
    private URLStatus getStatusUrl(String url) {
        if (qStatus != null) {
            Collection<URLStatus> urlStatus = qStatus.getUrlStatus();
            for (URLStatus uStatus : urlStatus) {
                String fqpUrl = uStatus.getUrl();
                if (fqpUrl != null && fqpUrl.equals(url)) {
                    return uStatus;
                }
            }
        }
        // logger.info("Returning null URLStatus for URL : " + url);
        return null;
    }

    /**
     * Method to initilise query status object. 
     */
    private void initilizeQueryStatus() {
        qStatus = new QueryStatusImpl();
        qStatus.setQuery(query);
        qStatus.setUser(user);
        qStatus.setQueryStartTime(new Date());
        qStatus.setVisible(Boolean.FALSE);
        qStatus.setQueryConditions(UtilityOperations.getStringRepresentationofConstraints(query.getConstraints()));
        qStatus.setDescription(query.getName() + " Query Initialized.");
        qStatus.setStatus(AbstractStatus.Processing);

        List<String> outputUrlList = query.getOutputUrls();
        Set<URLStatus> urlStatusCollection = new HashSet<URLStatus>(outputUrlList.size());
        for (String url : outputUrlList) {
            URLStatus urlStatus = new URLStatusImpl();
            urlStatus.setStatus(AbstractStatus.Processing);
            urlStatus.setMessage(url + " url initilized.");
            urlStatus.setUrl(url);
            urlStatusCollection.add(urlStatus);
        }
        qStatus.setUrlStatus(urlStatusCollection);
        if (recordStatus) {
            QueryURLStatusOperations qso = new QueryURLStatusOperations();
            qso.insertQueryStatus(qStatus);
        }
    }

    /**
     * @param categoryResults
     * @return
     */
    private IQueryResult<ICategorialClassRecord> mergeCatResults(
                                                                 List<IQueryResult<ICategorialClassRecord>> categoryResults) {
        Category outputCategory = categoryPreprocessorResult.getCategoryForEntity().get(getOutputEntity());
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

    /**
     * @param constraint
     * @param parentIdConstraint
     * @return
     */
    private DcqlConstraint addParentIdConstraint(DcqlConstraint constraint, DcqlConstraint parentIdConstraint) {
        DcqlConstraint dcqlConstraint = parentIdConstraint;
        if (!query.isKeywordSearch()) {
            Cab2bGroup cab2bGroup = new Cab2bGroup(LogicalOperator.And);
            cab2bGroup.addConstraint(constraint);
            cab2bGroup.addConstraint(parentIdConstraint);
            dcqlConstraint = cab2bGroup.getDcqlConstraint();
        }
        return dcqlConstraint;
    }

    /**
     * @param attribute
     * @param id
     * @return
     */
    private AttributeConstraint createIdConstraint(AttributeInterface attribute, String id) {
        return ConstraintsBuilder.createAttributeConstraint(attribute.getName(), RelationalOperator.Equals, id,
                                                            DataType.String);
    }

    /**
     * @param entity
     * @return
     */
    private AttributeInterface getIdAttribute(EntityInterface entity) {
        return Utility.getIdAttribute(entity);
    }

    /**
     * @param association
     * @return
     */
    private AbstractAssociationConstraint createAssociationConstraint(IAssociation association) {
        return ConstraintsBuilder.createAssociation(association);
    }

    /**
     * Returns output entity for the query.
     * @return EntityInterface
     */
    private EntityInterface getOutputEntity() {
        return getQuery().getOutputEntity();
    }

    /**
     * Returns true if output is of category type.
     * @return
     */
    private boolean isCategoryOutput() {
        return Utility.isCategory(getOutputEntity());
    }

    /**
     * Method to verify record count. If exceeds limit throws exception.
     * @param count
     */
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

    /**
     * Method that splits  
     * @return
     */
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
        private static final int allowedPriorityRange = Thread.MAX_PRIORITY - Thread.NORM_PRIORITY;

        ICab2bQuery queryPerUrl = null;

        WorkerThread(ICab2bQuery queryCopy) {
            this.queryPerUrl = queryCopy;
        }

        public void run() {

            catQueryResult = executeCategoryQuery(queryPerUrl);
            if (threadPoolExecutor.noTasksToExecuteOrTerminated()) {
                threadPoolExecutor.shutdown();
            }
            queryResult = catQueryResult;
        }

        /**
         * @param queryPerUrl
         * @return
         */
        private IQueryResult<ICategorialClassRecord> executeCategoryQuery(ICab2bQuery queryPerUrl) {
            Set<TreeNode<IExpression>> rootOutputExprNodes =
                    categoryPreprocessorResult.getExprsSourcedFromCategories().get(getOutputEntity());
            categoryResults = new ArrayList<IQueryResult<ICategorialClassRecord>>(rootOutputExprNodes.size());
            for (TreeNode<IExpression> rootOutputExprNode : rootOutputExprNodes) {
                IExpression rootOutputExpr = rootOutputExprNode.getValue();
                DcqlConstraint rootExprDcqlConstraint =
                        constraintsBuilderResult.getExpressionToConstraintMap().get(rootOutputExpr);
                EntityInterface outputEntity = rootOutputExpr.getQueryEntity().getDynamicExtensionsEntity();
                DCQLQuery rootDCQLQuery =
                        DCQLGenerator.createDCQLQuery(queryPerUrl, outputEntity.getName(), rootExprDcqlConstraint);
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
                queryResult = mergeCatResults(categoryResults);
                Map<String, List<ICategorialClassRecord>> urlToRecords = allRootExprCatRecs.getRecords();
                for (String url : urlToRecords.keySet()) {
                    int noOfRecords = urlToRecords.get(url).size();
                    int blockSize = 1;

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

            IQueryResult<ICategorialClassRecord> res = mergeCatResults(categoryResults);
            return res;
        }
    }

    private class ChildQueryExecutor implements Runnable {
        private ICategorialClassRecord parentCatClassRec;

        private TreeNode<IExpression> parentExprNode;

        private RecordId parentId;

        private int priority;

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
                queryResult = mergeCatResults(categoryResults);
            } catch (Throwable e) {
                //e.printStackTrace();
                logger.error(e.getMessage());
            }
        }

        public int getPriority() {
            return priority;
        }

    }

    /**
     * Returns complete query results.
     * @return the queryResult
     */
    public IQueryResult<? extends IRecord> getCompleteResults() {

        while (!isProcessingFinished()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException("Thread to get CompleteResults was interrupted.", e);
            }
        }
        updateQueryStatus();
        return queryResult;
    }

    /**
     * Returns whatever results available in memory. 
     * @return the queryResult
     */
    public IQueryResult<? extends IRecord> getPartialResult() {
        updateQueryStatus();
        if (!(getStatus().getStatus().equals(AbstractStatus.Processing))) {
            updateStatus();
        }

        return queryResult;
    }

    /**
     * @return the isProcessingFinished
     */
    public boolean isProcessingFinished() {
        return threadPoolExecutor.noTasksToExecuteOrTerminated() || normalQueryFinished;
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
            Thread t = null;
            if (Thread.activeCount() < QueryExecutorPropertes.getGlobalThreadLimit()) {
                t = new Thread(r);
            } else {
                threadPoolExecutor.shutdownNow();
                logger.info("Maximum Thread Limit Reached. Shutting down Thread Pool Executor for the Query : "
                        + query.getName());
            }
            return t;
        }
    }

    /**
     * Returns set of failed urls.
     * @return
     */
    public Set<String> getFailedURLs() {
        Set<URLStatus> urlStatusSet = getStatus().getUrlStatus();
        Set<String> failedUrls = new HashSet<String>();
        for (URLStatus urlStatus : urlStatusSet) {
            if (urlStatus.getStatus().equals(AbstractStatus.Complete_With_Error)) {
                failedUrls.add(urlStatus.getUrl());
            }
        }
        return failedUrls;
    }

    /**
     * Returns whatever results available and updates only query status in database.     
     * @return
     */
    public IQueryResult<? extends IRecord> getResult() {
        updateQueryStatus();
        updateStatus();
        return queryResult;
    }

    /**
     * Updates query status in database.
     */
    private void updateStatus() {
        if (recordStatus) {
            QueryURLStatusOperations qso = new QueryURLStatusOperations();
            qso.updateQueryStatus(qStatus);
        }
    }
}