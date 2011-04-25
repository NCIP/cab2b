package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.ServiceGroup;
import edu.wustl.cab2b.common.queryengine.ServiceGroupItem;
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
import edu.wustl.cab2b.common.queryengine.result.RecordId;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.common.util.Cab2bServerProperty;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.common.util.QueryStatusUtil;
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
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.GroupConstraint;
import edu.wustl.cab2b.server.queryengine.querystatus.QueryURLStatusOperations;
import edu.wustl.cab2b.server.queryengine.resulttransformers.IQueryResultTransformer;
import edu.wustl.cab2b.server.queryengine.resulttransformers.QueryResultTransformerFactory;
import edu.wustl.cab2b.server.queryengine.utils.QueryExecutorUtil;
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
import gov.nih.nci.cagrid.dcql.ForeignAssociation;
import gov.nih.nci.cagrid.dcql.Group;
import gov.nih.nci.cagrid.dcql.Object;



//import edu.wustl.cab2bwebapp.bizlogic.executequery.AttributeFilter;


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

	private boolean hasQueryStarted = false;

	/**
	 * Constructor initializes object with query and globus credentials
	 * @param query
	 * @param credential
	 */
	public QueryExecutor(ICab2bQuery query, GlobusCredential credential) {
		this.gc = credential;
		this.query = query;

		if(credential == null ) logger.info("JJJ NULL globus credential");
		
		recordStatus = query.getId() != null;
		initializeQueryStatus(credential);
		transformer =
			QueryResultTransformerFactory.createTransformer(getOutputEntity(), IRecord.class,
					ICategorialClassRecord.class);
		PriorityBlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>(100);
		int max = Cab2bServerProperty.getPerQueryMaxThreadLimit();
		int min = Cab2bServerProperty.getPerQueryMaxThreadLimit();
		executor = new QueryExecutorThreadPool(max, min, 1, TimeUnit.SECONDS, queue);
		executor.allowCoreThreadTimeOut(true);
		executor.setThreadFactory(new QueryExecutorThreadFactory());

		categoryPreprocessorResult = preProcessCategories();
		ConstraintsBuilder constraintsBuilder = new ConstraintsBuilder(query, categoryPreprocessorResult);
		constraintsBuilderResult = constraintsBuilder.buildConstraints();

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
	 */
	@SuppressWarnings("unused")
	public void executeQuery() {

		qStatus.setQueryStartTime(new Date());
		if (Utility.isCategory(getOutputEntity())) {

			List<ICab2bQuery> queries = QueryExecutorHelper.splitQUeryPerUrl(query);
			hasQueryStarted = true;
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

			if(query.getServiceGroups() == null) logger.error("JJJ NO SERVICE GROUPS!");
			
        	if(query.getServiceGroups().size() >0){      	// if ServiceGroups exist	        		
                String output = getOutputEntity().getName();
                List<ICab2bQuery> queries = QueryExecutorHelper.splitQueryPerGroup(query); 
                DcqlConstraint constraints[] = new DcqlConstraint[queries.size()];
                
                

                for (int i = 0; i < queries.size(); i++) {
            		ConstraintsBuilder cb = new ConstraintsBuilder(queries.get(i), categoryPreprocessorResult);
            		ConstraintsBuilderResult cbr = cb.buildConstraints();

                	
                    ICab2bQuery queryWithSingleUrl = queries.get(i);       
                    constraints[i] = cbr.getDcqlConstraintForClass(getOutputEntity());
                }
                
                
                
                DCQLQuery[] dcqlQueries = new DCQLQuery[queries.size()];           
                hasQueryStarted = true;
                

                
        		Collection<ServiceGroup> sGroups = query.getServiceGroups();    	
        		int sgc=0;       		
        		List<IQueryResult<? extends IRecord>> results = new ArrayList<IQueryResult<? extends IRecord>>(1);

        		List<FutureTask<IQueryResult<? extends IRecord>>> futures = new ArrayList<FutureTask<IQueryResult<? extends IRecord>>>();
        		
        		for(ServiceGroup group : sGroups){
        			
        			logger.info("JJJ ***Service Group loop for : "+group.getName());
        			
               		GroupConstraint gr = (GroupConstraint) constraints[sgc];   		
            		Group[] gr1 = new Group[1];         
            		gr1[0]=gr.getGroup();

            		Collection<ForeignAssociation> fc = getForeignAssociations(gr1);

            		for(ServiceGroupItem item : group.getItems()){
            			if(item.getTargetObject().toString().equals(queries.get(sgc).getOutputEntity().getName())){
            				List<String> targetUrls = new ArrayList<String>(1);            
            				targetUrls.add(item.getServiceUrl().getUrlLocation());
            				queries.get(sgc).setOutputUrls(targetUrls); // Outside URL
            				logger.info("JJJ Set OUTSIDE targetURL="+item.getServiceUrl().getUrlLocation());
            			} else { 		
            				for( ForeignAssociation forAss : fc){
            					Object cFObj = forAss.getForeignObject(); 
            					String sgObj = item.getTargetObject();

            					if(sgObj.equals(cFObj.getName())){ 
            						forAss.setTargetServiceURL(item.getServiceUrl().getUrlLocation());   
            						logger.info("JJJ Just set inner targetURL="+item.getServiceUrl().getUrlLocation());

            					}
            				} //fc for
            			}
            		} //for GroupItem


            		dcqlQueries[sgc] = DCQLGenerator.createDCQLQuery(queries.get(sgc), output, constraints[sgc]);
            		FutureTask<IQueryResult<? extends IRecord>> executionTask = executeWithFuture(dcqlQueries[sgc]);
            		futures.add(executionTask);
            		sgc++;
            		
        		} // Groups
        		
        		
        		try {
        			logger.info("WAITING FOR THREADS");
        			executor.shutdown();
					executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
					logger.info("COMPLETED ALL THREADS");
					for(FutureTask<IQueryResult<? extends IRecord>> future : futures) {
						results.add(future.get());
		        		transformer.mergeStatus(qStatus, results);
						result = transformer.mergeResults(results, getOutputEntity());
					}
	        		normalQueryFinished = true;     

	        		qStatus.setStatus(AbstractStatus.Complete);

	        		
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
        		
        	} else {
        		hasQueryStarted = true;
        		String output = getOutputEntity().getName();
        		DcqlConstraint constraints = constraintsBuilderResult.getDcqlConstraintForClass(getOutputEntity());
        		DCQLQuery dcqlQuery = DCQLGenerator.createDCQLQuery(query, output, constraints);        		
        		result = transformer.getResults(dcqlQuery, getOutputEntity(), gc);
        		normalQueryFinished = true;
        	}       	       	
        }
	}
	
	private FutureTask<IQueryResult<? extends IRecord>> executeWithFuture(final DCQLQuery dcqlQuery) {
		FutureTask<IQueryResult<? extends IRecord>> future = new FutureTask<IQueryResult<? extends IRecord>>(
            	new Callable<IQueryResult<? extends IRecord>>() {
                 	public IQueryResult<? extends IRecord> call() {
                 			logger.info("ABOUT TO SUBMIT QUERY");
                            return transformer.getResultsNoUpdate(dcqlQuery, getOutputEntity(), gc);

                    }
        });
		executor.execute(future);
		return future;
	}


	private static Collection<ForeignAssociation> getForeignAssociations(ForeignAssociation faa){	
		ArrayList<ForeignAssociation> fas=  new ArrayList<ForeignAssociation>();    	

		Object fo = faa.getForeignObject();
		Group fog = fo.getGroup();

		if(fog != null){
			Group[] gr1 = new Group[1];         
			gr1[0]=fog;
			fas.addAll(getForeignAssociations(gr1));
		}


		ForeignAssociation fofa = fo.getForeignAssociation();
		if(fofa != null){
			fas.add(fofa);
			
			fas.addAll(getForeignAssociations(fofa));
		}

		return fas;
	}



	private static Collection<ForeignAssociation> getForeignAssociations(Group[] g){
		ArrayList<ForeignAssociation> fas=  new ArrayList<ForeignAssociation>();    	
		for(int y=0;y<g.length;y++){

			ForeignAssociation[] faa = g[y].getForeignAssociation();
			

			for(int x=0;x<faa.length;x++){
				fas.add(faa[x]);
				fas.addAll(getForeignAssociations(faa[x]));
			}

			Group[] g2= g[y].getGroup();      

			if(g2.length>0){
				fas.addAll(getForeignAssociations(g2));
			}
		}
		return fas;

	}
	

	/**
	 * Method to initialize query status object. 
	 */
	private void initializeQueryStatus(GlobusCredential credential) {
		String userName = Constants.ANONYMOUS;
		if (credential != null) {
			userName = credential.getIdentity();
		}
		qStatus = new QueryStatusImpl();
		qStatus.setQuery(query);
		qStatus.setVisible(Boolean.FALSE);
		qStatus.setQueryConditions(UtilityOperations.getStringRepresentationofConstraints(query.getConstraints()));
		qStatus.setStatus(AbstractStatus.Processing);

		List<String> outputUrlList = query.getOutputUrls();
		Set<URLStatus> urlStatusCollection = new HashSet<URLStatus>(outputUrlList.size());
		for (String url : outputUrlList) {
			URLStatus urlStatus = new URLStatusImpl();
			urlStatus.setStatus(AbstractStatus.Processing);
			urlStatus.setUrl(url);
			urlStatusCollection.add(urlStatus);
		}
		qStatus.setUrlStatus(urlStatusCollection);
		if (recordStatus) {
			UserInterface user = new UserOperations().getUserByName(userName);
			qStatus.setUser(user);
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
				setRevertException(e);
			}
		}

		/**
		 * Changing exception message and logger properties in case of bidirectional conditions. 
		 */
		 private void setRevertException(Throwable e) {
			if (e.getMessage().equals("Association ain't bidirectional... cannot reverse.")) {
				logger.info("Can not get related data as path is unidirectional");
			} else {
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
					 setRevertException(e);
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
			if (Thread.activeCount() < Cab2bServerProperty.getGlobalThreadLimit()) {
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
	 * Setting resources for background query execution. 
	 */
	public void setQueryExecutionInBackground() {
		executor.setCorePoolSize(10);
	}

	/**
	 * Returns complete query results.  
	 * (Most of the times this function is called in getting result for queries coming from thick client)  
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
		saveStatusInDB();
		return result;
	}

	/**
	 * @return the isProcessingFinished
	 */
	public boolean isProcessingFinished() {
		if (hasQueryStarted) {
			return executor.noTasksToExecuteOrTerminated() || normalQueryFinished;
		} else {
			return hasQueryStarted;
		}
	}

	/**
	 * @return {@link QueryStatus}
	 */
	public QueryStatus getStatus() {
		updateQueryStatus();
		return qStatus;
	}

	/**
	 * Returns whatever results available and updates only query status in database.     
	 * @return {@link IQueryResult}
	 */
	public IQueryResult<? extends IRecord> getResult() {
		updateQueryStatus();
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

		if (QueryStatusUtil.isStatusProcessingDone(qStatus)) {
			return;
		}
		
		if(query.getServiceGroups().size() >0 ) return; // TODO update status

		int totalResultCount = 0;
		Collection<FQPUrlStatus> fqpUrlStatus = result.getFQPUrlStatus();
		boolean isResultAvailable = false;
		for (FQPUrlStatus fqpUrl : fqpUrlStatus) {
			String url = fqpUrl.getTargetUrl();
			URLStatus uStatusObj = QueryStatusUtil.getStatusUrlObject(url, qStatus);
			uStatusObj.setStatus(fqpUrl.getStatus());
			uStatusObj.setDescription(fqpUrl.getDescription());
			uStatusObj.setMessage(fqpUrl.getMessage());
			int urlRecCount = getRecordCountForUrl(url);
			if (urlRecCount != -1) {
				isResultAvailable = true;
				totalResultCount += urlRecCount;
				uStatusObj.setResultCount(urlRecCount);
			}
		}
		//sets total result count only if it is available
		if (isResultAvailable) {
			qStatus.setResultCount(totalResultCount);
		}
		//Deriving the query status from URL status
		qStatus.setStatus(AbstractStatus.Processing);
		if (isProcessingFinished() && QueryStatusUtil.areAllUrlsFinished(qStatus)) {
			qStatus.setQueryEndTime(new Date());
			String statusStr = AbstractStatus.Complete_With_Error;
			if (QueryStatusUtil.isEveryUrlStatusEqualsTo(AbstractStatus.Complete_With_Error, qStatus)
					&& !isResultAvailable) {
				statusStr = AbstractStatus.FAILED;
			} else {
				if (QueryStatusUtil.isEveryUrlStatusEqualsTo(AbstractStatus.Complete, qStatus)) {
					statusStr = AbstractStatus.Complete;
				}
			}
			QueryStatusUtil.checkAndSetIfUrlFailedFor(qStatus);
			qStatus.setStatus(statusStr);
		}
	}

	/**
	 * Returns record counts for url.
	 * @param url
	 * @return record count
	 */
	@SuppressWarnings("unchecked")
	public int getRecordCountForUrl(String url) {
		int urlRecCount = -1;
		if (result instanceof ICategoryResult) {
			ICategoryResult<ICategorialClassRecord> categoryResult = (ICategoryResult) result;
			Map<String, List<ICategorialClassRecord>> urlToRecordMap = categoryResult.getRecords();
			List<ICategorialClassRecord> records = urlToRecordMap.get(url);
			if (records != null) {
				urlRecCount = QueryExecutorUtil.getSpreadSheetRecordsCount(records);
			}
		} else {
			IQueryResult<IRecord> queryResult = (IQueryResult<IRecord>) result;
			List<IRecord> resultPerUrl = queryResult.getRecords().get(url);
			if (resultPerUrl != null) {
				urlRecCount = resultPerUrl.size();
			}
		}
		return urlRecCount;
	}

	/**
	 * Method to verify record count. If exceeds limit throws exception.
	 * @param count
	 */
	private void verifyRecordLimit(int count) {
		noOfRecordsCreated = noOfRecordsCreated + count;
		if (noOfRecordsCreated > Cab2bServerProperty.getPerQueryAllowedRecords()) {
			int limit = Cab2bServerProperty.getPerQueryAllowedRecords();
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

	/**
	 * Returns set of failed urls.
	 * @return {@link Set}
	 */
	public Set<String> getFailedURLs() {
		return QueryStatusUtil.getFailedURLs(qStatus);
	}
}
