package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.cagrid.fqp.results.metadata.ProcessingStatus;
import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
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
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
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
 * @author Chandrakant Talele
 * @author Deepak
 * @author Gaurav Mehta
 * @author srinath_k
 */
public class QueryExecutor {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(QueryExecutor.class);

    private QueryExecutorThreadPool executor;

    private ICab2bQuery query;

    private ConstraintsBuilderResult constraintsBuilderResult;

    private CategoryPreprocessorResult categoryPreprocessorResult;

    private IQueryResultTransformer<IRecord, ICategorialClassRecord> transformer;

    private GlobusCredential gc;

    private IQueryResult<? extends IRecord> result;

    private List<IQueryResult<ICategorialClassRecord>> categoryResults;

    private QueryStatus qStatus;

    private boolean normalQueryFinished = false;

    private boolean recordStatus;

    private int noOfRecordsCreated = 0;

    /**
     * Constructor initializes object with query and globus credentials
     * @param query
     * @param cred
     */
    public QueryExecutor(ICab2bQuery query, GlobusCredential credential) {
        this.gc = credential;
        this.query = query;

        recordStatus = query.getId() != null;
        transformer =
                QueryResultTransformerFactory.createTransformer(getOutputEntity(), IRecord.class,
                                                                ICategorialClassRecord.class);
        PriorityBlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>(100);
        int max = QueryExecutorPropertes.getPerQueryMaxThreadLimit();
        int min = QueryExecutorPropertes.getPerQueryMaxThreadLimit();
        executor = new QueryExecutorThreadPool(max, min, 1, TimeUnit.SECONDS, queue);
        executor.allowCoreThreadTimeOut(true);
        executor.setThreadFactory(new QueryExecutorThreadFactory());

        categoryPreprocessorResult = preProcessCategories();
        ConstraintsBuilder constraintsBuilder = new ConstraintsBuilder(query, categoryPreprocessorResult);
        constraintsBuilderResult = constraintsBuilder.buildConstraints();
        initializeQueryStatus(credential);
    }

    /**
     * @return
     */
    private CategoryPreprocessorResult preProcessCategories() {
        if (query.isKeywordSearch()) {
            query = new QueryConverter().convertToKeywordQuery((ICab2bQuery) query);
        }
        return new CategoryPreprocessor().processCategories(query);
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
        qStatus.setQueryStartTime(new Date());
        logger.info("Entered QueryExecutor...");
        if (Utility.isCategory(getOutputEntity())) {
            List<ICab2bQuery> queries = QueryExecutorHelper.splitQUeryPerUrl(query);
            float offset = 0.5f / queries.size();
            for (int i = 0; i < queries.size(); i++) {
                ICab2bQuery queryWithSingleUrl = queries.get(i);
                float maxPriority = (float) (1.0 - i * offset);
                float minPriority = maxPriority - offset;
                executor.execute(new SingleUrlQueryTask(queryWithSingleUrl, minPriority, maxPriority));
            }
        } else {
            // if output is a class, then just set the target as the class name,
            // and appropriate constraints.
            String output = getOutputEntity().getName();
            DcqlConstraint constraints = constraintsBuilderResult.getDcqlConstraintForClass(getOutputEntity());
            DCQLQuery dcqlQuery = DCQLGenerator.createDCQLQuery(query, output, constraints);
            result = transformer.getResults(dcqlQuery, getOutputEntity(), gc);
            normalQueryFinished = true;
        }
    }

    /**
     * Method to initialize query status object. 
     */
    private void initializeQueryStatus(GlobusCredential credential) {
        String userName = Constants.ANONYMOUS;
        if (credential != null) {
            userName = credential.getIdentity();
        }
        UserInterface user = new UserOperations().getUserByName(userName);

        qStatus = new QueryStatusImpl();
        qStatus.setQuery(query);
        qStatus.setUser(user);
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
     * SingleUrlQueryTask
     * @author deepak_shingan
     */
    private class SingleUrlQueryTask extends AbstractQueryTask {
        private ICab2bQuery queryPerUrl;

        private SingleUrlQueryTask(ICab2bQuery queryCopy, float minPriority, float maxPriority) {
            super(minPriority, maxPriority);
            this.queryPerUrl = queryCopy;
        }

        public void run() {
            IQueryResult<ICategorialClassRecord> catQueryResult = executeCategoryQuery(queryPerUrl);
            if (executor.noTasksToExecuteOrTerminated()) {
                executor.shutdown();
            }
            result = catQueryResult;
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
                        transformer.getCategoryResults(rootDCQLQuery, catClassForRootExpr, gc);
                Map<String, List<ICategorialClassRecord>> records = allRootExprCatRecs.getRecords();
                int recordSize = 0;
                for (String url : records.keySet()) {
                    List<ICategorialClassRecord> listOfRecords = records.get(url);
                    recordSize = recordSize + listOfRecords.size();
                }
                verifyRecordLimit(recordSize);
                categoryResults.add(allRootExprCatRecs);
                // process children in parallel.
                Category outputCategory = categoryPreprocessorResult.getCategoryForEntity().get(getOutputEntity());
                result = QueryExecutorHelper.mergeCatResults(categoryResults, outputCategory);
                Map<String, List<ICategorialClassRecord>> urlToRecords = allRootExprCatRecs.getRecords();
                for (String url : urlToRecords.keySet()) {
                    int noOfRecords = urlToRecords.get(url).size();
                    float allowedPriorityRange = (maxPriority - minPriority) / noOfRecords;
                    for (int j = 0; j < noOfRecords; j++) {
                        ICategorialClassRecord rootExprCatRec = urlToRecords.get(url).get(j);

                        float maxPri = (float) (maxPriority - j * allowedPriorityRange);
                        float minPri = maxPri - allowedPriorityRange;

                        executor.execute(new ChildQueryTask(rootExprCatRec, rootOutputExprNode, rootExprCatRec
                            .getRecordId(), minPri, maxPri));

                    }
                }
            }
            Category outputCategory = categoryPreprocessorResult.getCategoryForEntity().get(getOutputEntity());
            IQueryResult<ICategorialClassRecord> res =
                    QueryExecutorHelper.mergeCatResults(categoryResults, outputCategory);
            return res;
        }
    }

    /**
     * ChildQueryTask
     * @author gaurav_mehta
     */
    private class ChildQueryTask extends AbstractQueryTask {
        private ICategorialClassRecord parentCatClassRec;

        private TreeNode<IExpression> parentExprNode;

        private RecordId parentId;

        public ChildQueryTask(
                ICategorialClassRecord parentCatClassRec,
                TreeNode<IExpression> parentExprNode,
                RecordId parentId,
                float minPriority,
                float maxPriority) {
            super(minPriority, maxPriority);
            this.parentCatClassRec = parentCatClassRec;
            this.parentExprNode = parentExprNode;
            this.parentId = parentId;
        }

        public void run() {
            try {
                process();
            } catch (Throwable e) {
                //e.printStackTrace();
                logger.error(e.getMessage());
            }
        }

        private void process() {
            IExpression parentExpr = parentExprNode.getValue();
            for (TreeNode<IExpression> childExprNode : parentExprNode.getChildren()) {
                IExpression childExpr = childExprNode.getValue();
                AbstractAssociationConstraint parentIdConstraint;
                try {
                    parentIdConstraint =
                            createAssociationConstraint(getAssociation(parentExpr, childExpr).reverse());
                } catch (Exception e) {
                    // caused by reverse on unidirectional association
                    logger.error(e.getMessage());
                    continue;
                }
                EntityInterface childEntity = childExpr.getQueryEntity().getDynamicExtensionsEntity();
                parentIdConstraint.addChildConstraint(createIdConstraint(childEntity, parentId.getId()));
                Map<IExpression, DcqlConstraint> map = constraintsBuilderResult.getExpressionToConstraintMap();
                DcqlConstraint constraintForChild = addParentIdConstraint(map.get(childExpr), parentIdConstraint);

                String name = childEntity.getName();
                DCQLQuery dcql = DCQLGenerator.createDCQLQuery(query, name, constraintForChild, parentId.getUrl());

                CategorialClass catClassForChildExpr =
                        categoryPreprocessorResult.getCatClassForExpr().get(childExpr);
                if (catClassForChildExpr == null) {
                    processIntermediateClasses(dcql, childEntity, childExprNode);
                } else {
                    processCategoryClasses(dcql, catClassForChildExpr, childExprNode);
                }
            }
            Category outputCategory = categoryPreprocessorResult.getCategoryForEntity().get(getOutputEntity());
            result = QueryExecutorHelper.mergeCatResults(categoryResults, outputCategory);
        }

        /**
         * USed when expression was formed for entity on path between catClasses
         * @param dcql
         * @param childEntity
         * @param childExprNode
         */
        private void processIntermediateClasses(DCQLQuery dcql, EntityInterface childEntity,
                                                TreeNode<IExpression> childExprNode) {

            IQueryResult<IRecord> childExprClassRecs = transformer.getResults(dcql, childEntity, gc);
            List<List<IRecord>> records = new ArrayList<List<IRecord>>(childExprClassRecs.getRecords().values());
            int size = records.size();
            verifyRecordLimit(size);
            float range = (maxPriority - minPriority) / size;
            for (int k = 0; k < size; k++) {
                List<IRecord> listRec = records.get(k);
                //TODO  need to revisit this, are we ignoring records other than first ?
                if (listRec.iterator().hasNext()) {
                    IRecord record = listRec.iterator().next();
                    float max = (float) (maxPriority - k * range);
                    float min = max - range;
                    executor.execute(new ChildQueryTask(parentCatClassRec, childExprNode, record.getRecordId(),
                            min, max));
                }
            }
        }

        /**
         * Expression is for a catClass; add recs to parentCatClassRec
         * @param dcql
         * @param clazz
         * @param childExprNode
         */
        private void processCategoryClasses(DCQLQuery dcql, CategorialClass clazz,
                                            TreeNode<IExpression> childExprNode) {
            IQueryResult<ICategorialClassRecord> childExprCatResult =
                    transformer.getCategoryResults(dcql, clazz, gc);
            List<ICategorialClassRecord> records = childExprCatResult.getRecords().get(parentId.getUrl());
            if (records != null && !records.isEmpty()) {
                int size = records.size();
                verifyRecordLimit(records.size());
                parentCatClassRec.addCategorialClassRecords(clazz, records);
                Set<CategorialClass> children = records.get(0).getCategorialClass().getChildren();
                if (children != null && !children.isEmpty()) {
                    float range = (maxPriority - minPriority) / size;
                    for (int k = 0; k < size; k++) {
                        ICategorialClassRecord childExprCatRec = records.get(k);
                        float max = (float) (maxPriority - k * range);
                        float min = max - range;
                        executor.execute(new ChildQueryTask(childExprCatRec, childExprNode, childExprCatRec
                            .getRecordId(), min, max));
                    }
                }
            }
        }

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

        private AttributeConstraint createIdConstraint(EntityInterface entity, String id) {
            AttributeInterface attribute = Utility.getIdAttribute(entity);
            return ConstraintsBuilder.createAttributeConstraint(attribute.getName(), RelationalOperator.Equals,
                                                                id, DataType.String);
        }

        private IAssociation getAssociation(IExpression parentExpr, IExpression childExpr) {
            return getQuery().getConstraints().getJoinGraph().getAssociation(parentExpr, childExpr);
        }

        private AbstractAssociationConstraint createAssociationConstraint(IAssociation association) {
            return ConstraintsBuilder.createAssociation(association);
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
            Thread t = null;
            if (Thread.activeCount() < QueryExecutorPropertes.getGlobalThreadLimit()) {
                t = new Thread(r);
            } else {
                executor.shutdownNow();
                logger.info("Maximum Thread Limit Reached. Shutting down Thread Pool Executor for the Query : "
                        + query.getName());
            }
            return t;
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
        return result;
    }

    /**
     * @return the isProcessingFinished
     */
    public boolean isProcessingFinished() {
        return executor.noTasksToExecuteOrTerminated() || normalQueryFinished;
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
     * Returns whatever results available in memory. 
     * @return the queryResult
     */
    public IQueryResult<? extends IRecord> getPartialResult() {
        updateQueryStatus();
        if (!(getStatus().getStatus().equals(AbstractStatus.Processing))) {
            saveStatusInDB();
        }

        return result;
    }

    /**
     * @see edu.wustl.cab2b.server.queryengine.ICab2bQueryExecutor#getStatus()
     */
    public QueryStatus getStatus() {
        updateQueryStatus();
        return qStatus;
    }

    /**
     * Returns whatever results available and updates only query status in database.     
     * @return
     */
    public IQueryResult<? extends IRecord> getResult() {
        updateQueryStatus();
        saveStatusInDB();
        return result;
    }

    /**
     * Updates query status in database.
     */
    private void saveStatusInDB() {
        if (recordStatus) {
            QueryURLStatusOperations qso = new QueryURLStatusOperations();
            qso.updateQueryStatus(qStatus);
        }
    }

    /**
     * This method returns the ICab2bQuery object
     * @return ICab2bQuery object
     */
    public ICab2bQuery getQuery() {
        return query;

    }

    /**
     * Method to update url status properties. It will update only the in-memory query status.
     */
    private synchronized void updateQueryStatus() {
        if (result == null) {
            return;
        }
        String status = qStatus.getStatus();
        if (status != null
                && (status.equals(AbstractStatus.Complete) || status.equals(AbstractStatus.Complete_With_Error))) {
            return;
        }

        int totalRecCount = 0;
        Collection<FQPUrlStatus> fqpUrlStatus = result.getFQPUrlStatus();
        Map<String, ?> mapUrlResult = result.getRecords();

        for (FQPUrlStatus fqpUrl : fqpUrlStatus) {
            String url = fqpUrl.getTargetUrl();
            URLStatus uStatusObj = getStatusUrl(url);
            uStatusObj.setStatus(fqpUrl.getStatus());
            List<? extends IRecord> resultPerUrl = (List<? extends IRecord>) mapUrlResult.get(url);
            if (resultPerUrl != null) {
                int urlRecCount = resultPerUrl.size();
                uStatusObj.setResultCount(new Integer(urlRecCount));
                totalRecCount = +urlRecCount;
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
                if (urlStatus.equals(AbstractStatus.Complete_With_Error)) {
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
        return null;
    }

    /**
     * @param fqpUrlStatus
     * @return
     */
    private boolean areAllUrlsFinished(Collection<FQPUrlStatus> fqpUrlStatus) {
        boolean res = true;
        for (FQPUrlStatus fqpUrl : fqpUrlStatus) {
            String urlStatus = fqpUrl.getStatus();
            if (urlStatus.equals(AbstractStatus.Processing)) {
                res = false;
                break;
            }
        }
        return res;
    }

    /**
     * Method to verify record count. If exceeds limit throws exception.
     * @param count
     */
    private void verifyRecordLimit(int count) {
        noOfRecordsCreated = noOfRecordsCreated + count;
        if (noOfRecordsCreated > QueryExecutorPropertes.getPerQueryAllowedRecords()) {
            int limit = QueryExecutorPropertes.getPerQueryAllowedRecords();
            logger.error("---------------------------------------------------------");
            logger.error("Given query exceeds max number of Records : " + limit);
            logger.error("Shutting down the executor..." + noOfRecordsCreated);
            logger.error("---------------------------------------------------------");
            executor.shutdownNow();
            throw new RuntimeException("Given query exceeds max number of Records");
        }
    }

    /**
     * Returns output entity for the query.
     * @return EntityInterface
     */
    private EntityInterface getOutputEntity() {
        return getQuery().getOutputEntity();
    }
}