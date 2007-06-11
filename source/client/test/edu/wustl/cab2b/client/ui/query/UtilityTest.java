package edu.wustl.cab2b.client.ui.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.wustl.cab2b.client.ui.util.ClientConstants;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.QueryResultFactory;
import edu.wustl.cab2b.common.queryengine.result.RecordId;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.associations.impl.InterModelAssociation;
import edu.wustl.common.querysuite.metadata.associations.impl.IntraModelAssociation;
import edu.wustl.common.querysuite.metadata.path.Path;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;

/**
 * @author Chandrakant Talele
 */
public class UtilityTest extends TestCase {
    DomainObjectFactory fact = DomainObjectFactory.getInstance();

    RelationalOperator[] arr = { RelationalOperator.Equals, RelationalOperator.NotEquals, RelationalOperator.Between, RelationalOperator.IsNull, RelationalOperator.IsNotNull, RelationalOperator.LessThan, RelationalOperator.LessThanOrEquals, RelationalOperator.GreaterThan, RelationalOperator.GreaterThanOrEquals, RelationalOperator.In, RelationalOperator.Contains, RelationalOperator.StartsWith, RelationalOperator.EndsWith, RelationalOperator.NotIn };

    String[] res = { "Equals", "Not Equals", "Between", "Is Null", "Is Not Null", "Less than", "Less than or Equal to", "Greater than", "Greater than or Equal to", "In", "Contains", "Starts With", "Ends With", "Not In" };

    public void testGetRoleNameForIntraModel() {
        String name = "someName";

        RoleInterface role = fact.createRole();
        role.setName(name);
        AssociationInterface association = fact.createAssociation();
        association.setTargetRole(role);
        IntraModelAssociation intraModelAssociation = new IntraModelAssociation(association);
        assertEquals(name, Utility.getRoleName(intraModelAssociation));
    }

    public void testGetRoleNameForInterModel() {
        String srcName = "srcName";
        String tgtName = "tgtName";

        AttributeInterface src = fact.createStringAttribute();
        src.setName(srcName);

        AttributeInterface tgt = fact.createStringAttribute();
        tgt.setName(tgtName);

        InterModelAssociation a = new InterModelAssociation(src, tgt);
        assertEquals(srcName + " = " + tgtName, Utility.getRoleName(a));
    }

    public void testGetRecordNumNoRecords() {
        IQueryResult queryResult = QueryResultFactory.createResult(fact.createEntity());
        assertEquals(0, Utility.getRecordNum(queryResult));
    }

    @SuppressWarnings("unchecked")
    public void testGetRecordNum() {
        int size = 4;
        int urls = 3;
        IQueryResult queryResult = QueryResultFactory.createResult(fact.createEntity());
        List<IRecord> list = new ArrayList<IRecord>(size);
        for (int i = 0; i < size; i++) {
            Set<AttributeInterface> map = Collections.singleton(DomainObjectFactory.getInstance().createStringAttribute());
            list.add(QueryResultFactory.createRecord(map, new RecordId("","")));
        }
        for (int i = 0; i < urls; i++) {
            queryResult.addRecords("URL" + i, list);
        }
        assertEquals(size * urls, Utility.getRecordNum(queryResult));
    }

    public void testGetPathDisplayString() {
        EntityInterface source = fact.createEntity();
        source.setName("sourceEntity");

        EntityInterface target = fact.createEntity();
        target.setName("targetEntity");

        AssociationInterface association = fact.createAssociation();
        association.setTargetEntity(target);

        IntraModelAssociation intraModelAssociation = new IntraModelAssociation(association);

        List<IAssociation> list = new ArrayList<IAssociation>();
        list.add(intraModelAssociation);

        Path path = new Path(source, target, list);
        String str = Utility.getPathDisplayString(path);
        assertEquals("<HTML><B>Path</B>:sourceEntity<B>----></B>targetEntity<HTML>", str);
    }

    public void testGetPathDisplayStringTwoAssociations() {
        EntityInterface source = fact.createEntity();
        source.setName("sourceEntity");

        EntityInterface target = fact.createEntity();
        target.setName("targetEntity");

        EntityInterface inter = fact.createEntity();
        inter.setName("inter");

        AssociationInterface association1 = fact.createAssociation();
        association1.setTargetEntity(inter);

        AssociationInterface association2 = fact.createAssociation();
        association2.setTargetEntity(target);

        IntraModelAssociation intraModelAssociation1 = new IntraModelAssociation(association1);
        IntraModelAssociation intraModelAssociation2 = new IntraModelAssociation(association2);

        List<IAssociation> list = new ArrayList<IAssociation>();
        list.add(intraModelAssociation1);
        list.add(intraModelAssociation2);

        Path path = new Path(source, target, list);
        String str = Utility.getPathDisplayString(path);
        assertEquals("<HTML><B>Path</B>:sourceEntity<B>----></B>inter<B>----></B>targetEntity<HTML>", str);
    }

    public void testGetLogicalOperatorOR() {
        assertEquals(LogicalOperator.Or, Utility.getLogicalOperator(ClientConstants.OPERATOR_OR));
    }

    public void testGetLogicalOperatorAND() {
        assertEquals(LogicalOperator.And, Utility.getLogicalOperator(ClientConstants.OPERATOR_AND));
    }

    public void testDisplayStringForRelationalOperator() {
        assertEquals(arr.length, res.length);
        for (int i = 0; i < arr.length; i++) {
            String str = Utility.displayStringForRelationalOperator(arr[i]);
            assertEquals(res[i], str);
        }
    }

    public void testGetRelationalOperator() {
        for (int i = 0; i < arr.length; i++) {
            RelationalOperator opr = Utility.getRelationalOperator(res[i]);
            assertEquals(arr[i], opr);
        }
    }
}