package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.cab2b.common.queryengine.Cab2bQueryObjectFactory;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.server.util.TestUtil;
import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.metadata.associations.IIntraModelAssociation;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;

/**
 * @author Chandrakant Talele
 */
public class QueryExecutorTest extends TestCase {

    /**
     * Test case for partial query result feature of FQP1.3
     * here query is executed with 2 wrong urls and once correct URL    
     */
   public void testExecuteQyeryForMultipleURL() {

        EntityInterface gene = TestUtil.getEntityWithCaB2BGrp("GeneConnect", "edu.wustl.geneconnect.domain.Gene", "id");
        AttributeInterface id = gene.getAttributeCollection().iterator().next();
        AttributeInterface unigeneClusterId = TestUtil.getAttribute("unigeneClusterId");
        gene.addAttribute(unigeneClusterId);

        List<ICondition> conditions = new ArrayList<ICondition>();
        conditions.add(getCondition(id, RelationalOperator.LessThan, "3"));
        conditions.add(getCondition(unigeneClusterId, RelationalOperator.Contains, "Hs."));

        IRule rule = QueryObjectFactory.createRule(conditions);

        IQueryEntity queryEntity = QueryObjectFactory.createQueryEntity(gene);

        IConstraints constraints = Cab2bQueryObjectFactory.createConstraints();
        IExpression expr = constraints.addExpression(queryEntity);
        expr.addOperand(rule);

        String url = "http://128.252.227.94:9092/wsrf/services/cagrid/GeneConnect";
        List<String> urls = new ArrayList<String>();
        urls.add(url);
        url = "http://128.252.227.94:9092/wsrf/services/cagrid/GeneConnect1";
        urls.add(url);
        url = "http://128.252.227.94:9092/wsrf/services/cagrid/GeneConnect2";
        urls.add(url);

        ICab2bQuery query = Cab2bQueryObjectFactory.createCab2bQuery();
        query.setOutputEntity(gene);

        query.setOutputUrls(urls);
        query.setConstraints(constraints);
        QueryExecutor executor = new QueryExecutor(query, null);
        executor.executeQuery();
        IQueryResult<? extends IRecord> res = executor.getCompleteResults();
        //checking failed urls                
        assertEquals(2, res.getFailedURLs().size());

        //checking correct URLs 
        Map<String, ?> urlVsRecords = res.getRecords();
        assertEquals(1, urlVsRecords.size());
    }

    @SuppressWarnings("unchecked")
    public void testExecuteQuery() {

        EntityInterface gene = TestUtil.getEntityWithCaB2BGrp("GeneConnect", "edu.wustl.geneconnect.domain.Gene", "id");
        AttributeInterface id = gene.getAttributeCollection().iterator().next();
        AttributeInterface unigeneClusterId = TestUtil.getAttribute("unigeneClusterId");
        gene.addAttribute(unigeneClusterId);

        List<ICondition> conditions = new ArrayList<ICondition>();
        conditions.add(getCondition(id, RelationalOperator.LessThan, "3"));
        conditions.add(getCondition(unigeneClusterId, RelationalOperator.Contains, "Hs."));

        IRule rule = QueryObjectFactory.createRule(conditions);

        IQueryEntity queryEntity = QueryObjectFactory.createQueryEntity(gene);

        IConstraints constraints = Cab2bQueryObjectFactory.createConstraints();
        IExpression expr = constraints.addExpression(queryEntity);
        expr.addOperand(rule);

        String url = "http://128.252.227.94:9092/wsrf/services/cagrid/GeneConnect";

        ICab2bQuery query = Cab2bQueryObjectFactory.createCab2bQuery();
        query.setOutputEntity(gene);
        query.setOutputUrls(Arrays.asList(url));
        query.setConstraints(constraints);

        QueryExecutor executor = new QueryExecutor(query, null);
        executor.executeQuery();
        IQueryResult<? extends IRecord> res = executor.getCompleteResults();

        Map<String, ?> urlVsRecords = res.getRecords();
        assertEquals(1, urlVsRecords.size());
        assertTrue(urlVsRecords.containsKey(url));

        List<IRecord> records = (List<IRecord>) urlVsRecords.get(url);
        Set<String> set = new HashSet<String>();
        set.add("1");
        set.add("2");
        for (IRecord record : records) {
            String str = (String) record.getValueForAttribute(id);
            assertTrue(set.remove(str));
        }
        assertEquals(0, set.size());
    }

    public void testExecuteQueryTwoEntity() {

        EntityInterface messengerRNA = TestUtil.getEntityWithCaB2BGrp("GeneConnect", "edu.wustl.geneconnect.domain.MessengerRNA", "id");
        AttributeInterface ensemblTranscriptId = TestUtil.getAttribute("ensemblTranscriptId");
        messengerRNA.addAttribute(ensemblTranscriptId);

        ICab2bQuery query = Cab2bQueryObjectFactory.createCab2bQuery();

        List<ICondition> conditionGeneOntology = new ArrayList<ICondition>();
        conditionGeneOntology.add(getCondition(ensemblTranscriptId, RelationalOperator.Contains, "ENST00000262"));
        IRule ruleGeneOntology = QueryObjectFactory.createRule(conditionGeneOntology);
        IQueryEntity queryEntityGeneOntology = QueryObjectFactory.createQueryEntity(messengerRNA);
        IConstraints constraintsGeneOntology = query.getConstraints();
        IExpression exprGeneOntology = constraintsGeneOntology.addExpression(queryEntityGeneOntology);
        exprGeneOntology.addOperand(ruleGeneOntology);

        EntityInterface gene = TestUtil.getEntityWithCaB2BGrp("GeneConnect", "edu.wustl.geneconnect.domain.Gene", "id");
        AttributeInterface id = gene.getAllAttributes().iterator().next();
//        gene.addAttribute(symbol);
        List<ICondition> conditions = new ArrayList<ICondition>();
        conditions.add(getCondition(id, RelationalOperator.LessThan, "3"));

        IRule rule = QueryObjectFactory.createRule(conditions);
        IQueryEntity queryEntity = QueryObjectFactory.createQueryEntity(gene);
        IConstraints constraints = query.getConstraints();
        IExpression expr = constraints.addExpression(queryEntity);
        expr.addOperand(rule);
        expr.addOperand(QueryObjectFactory.createLogicalConnector(LogicalOperator.And, 0), exprGeneOntology);

        AssociationInterface association = TestUtil.getAssociation(gene, messengerRNA);
        association.setAssociationDirection(Constants.AssociationDirection.SRC_DESTINATION);
        association.getTargetRole().setName("messengerRNACollection");

        IIntraModelAssociation iassociation = QueryObjectFactory.createIntraModelAssociation(association);

        try {
            constraints.getJoinGraph().putAssociation(expr, exprGeneOntology, iassociation);
        } catch (CyclicException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String url ="http://128.252.227.94:9092/wsrf/services/cagrid/GeneConnect";

        query.setOutputEntity(gene);
        query.setOutputUrls(Arrays.asList(url));

        QueryExecutor executor = new QueryExecutor(query, null);
        executor.executeQuery();
        IQueryResult<? extends IRecord> res = executor.getCompleteResults();

        Map<String, ?> urlVsRecords = res.getRecords();
        assertEquals(1, urlVsRecords.size());
        assertTrue(urlVsRecords.containsKey(url));

        List<IRecord> records = (List<IRecord>) urlVsRecords.get(url);

        Set<String> set = new HashSet<String>();
        set.add("1");
        set.add("2");
        for (IRecord record : records) {
            String str = (String) record.getValueForAttribute(id);
            assertTrue(set.contains(str));
            System.out.println(id.getEntity().getName() +" "+ id.getName() + " = " + str);

        }
    }

    private ICondition getCondition(AttributeInterface attr, RelationalOperator opr, String...val) {
        return QueryObjectFactory.createCondition(attr, opr, Arrays.asList(val));
    }
}
