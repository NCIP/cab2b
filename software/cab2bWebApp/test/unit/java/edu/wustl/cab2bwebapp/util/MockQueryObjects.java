/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 *
 */
package edu.wustl.cab2bwebapp.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.cab2b.common.queryengine.Cab2bQueryObjectFactory;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
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
 * @author chetan_patil
 *
 */
public class MockQueryObjects {

    /**
     * This method returns a mock query object of caFE Gene.
     * @return
     */
    public ICab2bQuery createCaFEGeneQuery() {
        ICab2bQuery query = Cab2bQueryObjectFactory.createCab2bQuery();
        IConstraints constraints = query.getConstraints();

        String[] attributeNames = new String[] { "name", "unigeneClusterId" };
        RelationalOperator[] operators = new RelationalOperator[] { RelationalOperator.LessThan, RelationalOperator.Contains };
        String[][] values = new String[][] { new String[] { "3" }, new String[] { "alpha" } };

        IExpression exp_Gene = createExpression(constraints, "caFE", "edu.wustl.fe.Gene", attributeNames,
                                                operators, values);

        String url = "http://128.252.227.94:9094/wsrf/services/cagrid/CaFE";
        query.setOutputEntity(exp_Gene.getQueryEntity().getDynamicExtensionsEntity());
        query.setOutputUrls(Arrays.asList(url));

        return query;
    }

    /**
     * Create           ---> mRNA
     *          Gene --|
     *                  ---> Protein
     * @return
     */
    public ICab2bQuery createQuery_Gene_mRNA_Protein() {
        ICab2bQuery query = Cab2bQueryObjectFactory.createCab2bQuery();
        IConstraints constraints = query.getConstraints();

        IExpression exp_Gene = createGeneConnect_Gene(constraints);
        IExpression exp_mRNA = createGeneConnect_mRNA(constraints);
        IExpression exp_Protein = createGeneConnect_Proteins(constraints);

        exp_Gene.addOperand(QueryObjectFactory.createLogicalConnector(LogicalOperator.And, 0), exp_mRNA);
        addAssociation(exp_Gene, exp_mRNA, "messengerRNACollection", constraints);

        exp_Gene.addOperand(QueryObjectFactory.createLogicalConnector(LogicalOperator.And, 0), exp_Protein);
        addAssociation(exp_Gene, exp_Protein, "proteinCollection", constraints);

        String url = "http://wucabig1.wustl.edu:9092/wsrf/services/cagrid/GeneConnect";
        query.setOutputEntity(exp_Gene.getQueryEntity().getDynamicExtensionsEntity());
        query.setOutputUrls(Arrays.asList(url));

        return query;
    }

    /**
     * Create Gene ---> mRNA ---> Protein
     *
     * @return
     */
    public ICab2bQuery createQuery_GenemRNAProtein() {
        ICab2bQuery query = Cab2bQueryObjectFactory.createCab2bQuery();
        IConstraints constraints = query.getConstraints();

        IExpression exp_Gene = createGeneConnect_Gene(constraints);
        IExpression exp_mRNA = createGeneConnect_mRNA(constraints);
        IExpression exp_Protein = createGeneConnect_Proteins(constraints);

        exp_Gene.addOperand(QueryObjectFactory.createLogicalConnector(LogicalOperator.And, 0), exp_mRNA);
        addAssociation(exp_Gene, exp_mRNA, "messengerRNACollection", constraints);

        exp_mRNA.addOperand(QueryObjectFactory.createLogicalConnector(LogicalOperator.And, 0), exp_Protein);
        addAssociation(exp_mRNA, exp_Protein, "proteinCollection", constraints);

        String url = "http://wucabig1.wustl.edu:9092/wsrf/services/cagrid/GeneConnect";
        query.setOutputEntity(exp_Gene.getQueryEntity().getDynamicExtensionsEntity());
        query.setOutputUrls(Arrays.asList(url));

        return query;
    }

    private IExpression createGeneConnect_Gene(final IConstraints constraints) {
        String[] attributeNames = new String[] { "ensemblGeneId", "unigeneClusterId" };
        RelationalOperator[] operators = new RelationalOperator[] { RelationalOperator.Contains, RelationalOperator.Contains };
        String[][] values = new String[][] { new String[] { "abc" }, new String[] { "def" } };

        return createExpression(constraints, "GeneConnect", "edu.wustl.geneconnect.domain.Gene", attributeNames,
                                operators, values);
    }

    private IExpression createGeneConnect_mRNA(final IConstraints constraints) {
        String[] attributeNames = new String[] { "ensemblTranscriptId", "genbankAccession", "refseqId" };
        RelationalOperator[] operators = new RelationalOperator[] { RelationalOperator.Contains, RelationalOperator.Contains, RelationalOperator.Contains };
        String[][] values = new String[][] { new String[] { "qwe" }, new String[] { "asd" }, new String[] { "12345" } };

        return createExpression(constraints, "GeneConnect", "edu.wustl.geneconnect.domain.MessengerRNA",
                                attributeNames, operators, values);
    }

    private IExpression createGeneConnect_Proteins(final IConstraints constraints) {
        String[] attributeNames = new String[] { "ensemblPeptideId", "genbankAccession", "refseqId", "uniprotkbPrimaryAccession" };
        RelationalOperator[] operators = new RelationalOperator[] { RelationalOperator.Contains, RelationalOperator.Contains, RelationalOperator.Contains, RelationalOperator.Contains };
        String[][] values = new String[][] { new String[] { "9876" }, new String[] { "dfg" }, new String[] { "6543" }, new String[] { "vfre" } };

        return createExpression(constraints, "GeneConnect", "edu.wustl.geneconnect.domain.Protein",
                                attributeNames, operators, values);
    }

    // Creates Expression
    private IExpression createExpression(final IConstraints constraints, final String entityGroup,
                                         final String entityName, final String[] attributeNames,
                                         final RelationalOperator[] operators, final String[][] values) {
        EntityInterface entity = TestUtil.getEntityWithCaB2BGrp(entityGroup, entityName, "id");

        Collection<AttributeInterface> attributes = new ArrayList<AttributeInterface>();
        for (String attributeName : attributeNames) {
            AttributeInterface attribute = TestUtil.getAttribute(attributeName);
            entity.addAttribute(attribute);

            attributes.add(attribute);
        }

        int index = 0;
        List<ICondition> conditions = new ArrayList<ICondition>();
        for (AttributeInterface attribute : attributes) {
            conditions.add(getCondition(attribute, operators[index], values[index]));
        }

        IRule rule = QueryObjectFactory.createRule(conditions);
        IQueryEntity queryEntity = QueryObjectFactory.createQueryEntity(entity);

        IExpression expression = constraints.addExpression(queryEntity);
        expression.addOperand(rule);

        return expression;
    }

    // Creates association
    private void addAssociation(IExpression expSource, IExpression expTarget, String roleName,
                                IConstraints constraints) {
        AssociationInterface association = TestUtil.getAssociation(
                                                                   expSource.getQueryEntity().getDynamicExtensionsEntity().getName(),
                                                                   expTarget.getQueryEntity().getDynamicExtensionsEntity().getName());
        association.setAssociationDirection(Constants.AssociationDirection.SRC_DESTINATION);
        association.getTargetRole().setName(roleName);
        IIntraModelAssociation iassociation = QueryObjectFactory.createIntraModelAssociation(association);

        try {
            constraints.getJoinGraph().putAssociation(expSource, expTarget, iassociation);
        } catch (CyclicException e) {
            e.printStackTrace();
        }
    }

    // Creates condition
    private ICondition getCondition(AttributeInterface attr, RelationalOperator opr, String[] val) {
        return QueryObjectFactory.createCondition(attr, opr, Arrays.asList(val));
    }

}
