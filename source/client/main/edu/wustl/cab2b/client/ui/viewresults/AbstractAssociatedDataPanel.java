package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JLabel;

import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.common.datalist.DataRow;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.queryobject.IQuery;

/**
 * This class is base for all associated data panel
 * @author Rahul Ner
 *
 */
public abstract class AbstractAssociatedDataPanel extends Cab2bPanel {

    /**
     * 
     */
    protected Collection associations;

    /**
     * 
     */
    protected ActionListener associatedDataActionListener;

    /**
     * current identifier value
     */
    protected Object id;

    /**
     * dataRow
     */
    protected DataRow dataRow;
    
    /**
     * 
     */
    protected IRecord record;

    /**
     * @param associations
     * @param associatedDataActionListener
     * @param id
     * @param dataRow
     */
    public AbstractAssociatedDataPanel(
            Collection associations,
            ActionListener associatedDataActionListener,
            Object id,
            DataRow dataRow, IRecord record) {

        this.associations = associations;
        this.associatedDataActionListener = associatedDataActionListener;
        this.id = id;
        this.dataRow = dataRow;
        this.record = record;
        iniGUI();
    }

    protected void iniGUI() {
        JLabel label = new JLabel(getLabel());
        label.setForeground(Color.red);
        this.add("br", label);

        processAssociation();
    }

    abstract String getLabel();

    abstract void processAssociation();

    abstract IQuery getQuery(IAssociation association);
    
    public   IRecord  getAssociatedRecord() {
        return record;
    }

    /*
     * The method initializes the user object for the hyperlink, so that both
     * target and source entities for the association, as well as the
     * association itself are remembered by the link
     */
    protected Cab2bHyperlink getHyperlink(HyperLinkUserObject hyperLinkUserObject, String tooltip) {
        Cab2bHyperlink hyperlink = new Cab2bHyperlink();
        /* Set the hyperlink text */
        hyperlink.setText(Utility.getDisplayName(hyperLinkUserObject.getTargetEntity()));
        hyperlink.setToolTipText(tooltip);
        hyperlink.setUserObject(hyperLinkUserObject);

        hyperlink.addActionListener(associatedDataActionListener);
        return hyperlink;
    }
}