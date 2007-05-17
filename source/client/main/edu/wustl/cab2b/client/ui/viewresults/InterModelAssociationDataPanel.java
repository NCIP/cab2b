package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.query.ClientQueryBuilder;
import edu.wustl.cab2b.common.datalist.DataRow;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IQuery;

public class InterModelAssociationDataPanel extends AbstractAssociatedDataPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 9084708789685939922L;

    public InterModelAssociationDataPanel(
            Collection associations,
            ActionListener associatedDataActionListener,
            Object id,
            DataRow dataRow,
            IRecord record) {
        super(associations, associatedDataActionListener, id, dataRow, record);
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.AbstractAssociatedDataPanel#getLabel()
     */

    String getLabel() {
        return " Inter Model Associations : ";
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.AbstractAssociatedDataPanel#processAssociation()
     */

    void processAssociation() {
        Iterator assoIter = associations.iterator();
        while (assoIter.hasNext()) {
            IInterModelAssociation interModelAssociation = (IInterModelAssociation) assoIter.next();
            if (!interModelAssociation.isBidirectional()) {
                continue;
            }
            String tooTipText = "Target attribute name : " + interModelAssociation.getSourceAttribute().getName();
            HyperLinkUserObject hyperLinkUserObject = new HyperLinkUserObject();
            hyperLinkUserObject.setAssociation(interModelAssociation);
            hyperLinkUserObject.setParentDataRow(dataRow);
            hyperLinkUserObject.setTargetEntity(interModelAssociation.getTargetEntity());

            this.add("br", getHyperlink(hyperLinkUserObject, tooTipText));
        }
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.AbstractAssociatedDataPanel#getQuery(edu.wustl.common.querysuite.metadata.associations.IAssociation)
     */
    IQuery getQuery(IAssociation association) {
        ClientQueryBuilder queryObject = new ClientQueryBuilder();

        /*Create the objects needed for adding the rule based on the source.*/
        AttributeInterface idAttribute = Utility.getIdAttribute(association.getSourceEntity());
        List<AttributeInterface> attributes = Collections.singletonList(idAttribute);
        List<String> operators = Collections.singletonList("Equals");
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(Collections.singletonList(id.toString()));

        IExpressionId targetExpressionID = queryObject.addRule(attributes, operators, values);

        /* Get the source expression id. Needed to add the path.*/
        IExpressionId sourceExpressionID = queryObject.createDummyExpression(association.getTargetEntity());

        try {
            queryObject.addAssociation(sourceExpressionID, targetExpressionID, association.reverse());
        } catch (CyclicException exCyclic) {
            exCyclic.printStackTrace();
        }
        queryObject.setOutputForQuery(association.getTargetEntity());
        return queryObject.getQuery();
    }

}
