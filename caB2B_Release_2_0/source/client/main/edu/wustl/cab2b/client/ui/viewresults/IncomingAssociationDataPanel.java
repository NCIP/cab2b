package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JLabel;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.wustl.cab2b.client.ui.query.ClientQueryBuilder;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.associations.IIntraModelAssociation;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IQuery;

/**
 * @author rahul_ner
 *
 */
public class IncomingAssociationDataPanel extends AbstractAssociatedDataPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 9084708789685939921L;

    public IncomingAssociationDataPanel(
            Collection associations,
            ActionListener associatedDataActionListener,
            IDataRow dataRow,
            IRecord record) {
        super(associations, associatedDataActionListener, dataRow, record);
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.AbstractAssociatedDataPanel#addLabel()
     */
    protected void addLabel() {
        JLabel label = new JLabel(" Intra Model Associations : ");
        label.setForeground(Color.black);
        this.add("br ", label);
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.AbstractAssociatedDataPanel#processAssociation()
     */
    void processAssociation() {
        List<AssociationInterface> list = new ArrayList<AssociationInterface>(associations);
        Collections.sort(list, new Comparator<AssociationInterface>() {
            public int compare(AssociationInterface association1, AssociationInterface association2) {
                return association1.getEntity().getName().compareTo(association2.getEntity().getName());
            }
        });

        for (AssociationInterface deAssociation : list) {
            IIntraModelAssociation intraModelAssociation = (IIntraModelAssociation) QueryObjectFactory.createIntraModelAssociation(deAssociation);

            AssociationInterface associationInterface = intraModelAssociation.getDynamicExtensionsAssociation();
            RoleInterface role = associationInterface.getSourceRole();
            String roleName = role.getName();
            if (roleName == null || roleName.equals("")) {
                if (associationInterface.getSourceRole() != null)
                    roleName = associationInterface.getTargetRole().getName();
            }

            String tooTipText = "Target role name : " + roleName;

            HyperLinkUserObject hyperLinkUserObject = new HyperLinkUserObject();
            hyperLinkUserObject.setAssociation(intraModelAssociation);
            hyperLinkUserObject.setParentDataRow(dataRow);
            hyperLinkUserObject.setTargetEntity(deAssociation.getEntity());

            this.add("br ", getHyperlink(hyperLinkUserObject, tooTipText));
        }
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.AbstractAssociatedDataPanel#getQuery(edu.wustl.common.querysuite.metadata.associations.IAssociation)
     */
    IQuery getQuery(IAssociation association) {
        ClientQueryBuilder queryObject = new ClientQueryBuilder();

        /*Create the objects needed for adding the rule based on the source.*/
        AttributeInterface idAttribute = Utility.getIdAttribute(association.getTargetEntity());
        List<AttributeInterface> attributes = Collections.singletonList(idAttribute);
        List<String> operators = Collections.singletonList("Equals");
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(Collections.singletonList(dataRow.getId().toString()));

        IExpressionId targetExpressionID = queryObject.addRule(attributes, operators, values);

        /* Get the source expression id. Needed to add the path.*/
        IExpressionId sourceExpressionID = queryObject.createDummyExpression(association.getSourceEntity());

        try {
            queryObject.addAssociation(sourceExpressionID, targetExpressionID, association);
        } catch (CyclicException exCyclic) {
            exCyclic.printStackTrace();
        }
        queryObject.setOutputForQueryForSpecifiedURL(association.getSourceEntity(), dataRow.getURL());
        return queryObject.getQuery();
    }

}
