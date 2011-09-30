package edu.wustl.cab2b.server.category;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.category.CategoryPopularity;
import edu.wustl.cab2b.common.queryengine.Cab2bQueryObjectFactory;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;
import edu.wustl.cab2b.server.util.TestUtil;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;

public class PopularCategoryOperationsTest extends TestCase {

    public void testSetPopularity() {

        EntityInterface gene = TestUtil.getEntityWithGrp("caFE", "edu.wustl.fe.Gene", "id");
        gene.setId(new Long(100000));
        AttributeInterface name = TestUtil.getAttribute("name");
        gene.addAttribute(name);

        EntityGroupInterface eg = gene.getEntityGroupCollection().iterator().next();
        DynamicExtensionUtility.addTaggedValue(eg, Constants.CAB2B_ENTITY_GROUP, Constants.CAB2B_ENTITY_GROUP);

        AttributeInterface id = gene.getAttributeCollection().iterator().next();
        List<String> values = new ArrayList<String>();
        values.add("3");
        ICondition condition = QueryObjectFactory.createCondition(id, RelationalOperator.LessThan, values);

        IRule rule = QueryObjectFactory.createRule();
        rule.addCondition(condition);

        IQueryEntity queryEntity = QueryObjectFactory.createQueryEntity(gene);

        IConstraints constraints = Cab2bQueryObjectFactory.createConstraints();
        IExpression expr = constraints.addExpression(queryEntity);
        expr.addOperand(rule);

        List<String> urls = new ArrayList<String>();
        urls.add("http://128.252.227.94:9094/wsrf/services/cagrid/CaFE");

        ICab2bQuery query = Cab2bQueryObjectFactory.createCab2bQuery();
        query.setOutputEntity(gene);
        query.setOutputUrls(urls);
        query.setConstraints(constraints);

        try {
            new PopularCategoryOperations().setPopularity(query);
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }

    public void testGetPopularityForAllCategories() {
        testSetPopularity();
        Collection<CategoryPopularity> categoryPopColl = new PopularCategoryOperations().getPopularityForAllCategories();
        boolean isPresent = false;
        for (CategoryPopularity categoryPopularity : categoryPopColl) {
            if (categoryPopularity.getEntityId() == 100000) {
                isPresent = true;
            }
        }
        assertTrue(isPresent);
    }
}
