package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.event.ActionListener;
import java.util.Collection;

import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.common.datalist.IDataRow;
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
     * dataRow
     */
    protected IDataRow dataRow;

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
    public AbstractAssociatedDataPanel(Collection associations, ActionListener associatedDataActionListener,

    IDataRow dataRow, IRecord record) {

        this.associations = associations;
        this.associatedDataActionListener = associatedDataActionListener;
        this.dataRow = dataRow;
        this.record = record;
        iniGUI();
    }

    protected void iniGUI() {

        addLabel();
        processAssociation();
    }

    protected void addLabel() {

    }

    abstract void processAssociation();

    abstract IQuery getQuery(IAssociation association);

    public IRecord getAssociatedRecord() {
        return record;
    }

    /*
     * The method initializes the user object for the hyperlink, so that both
     * target and source entities for the association, as well as the
     * association itself are remembered by the link
     */
    protected Cab2bHyperlink getHyperlink(HyperLinkUserObject hyperLinkUserObject, String tooltip) {
        Cab2bHyperlink hyperlink = new Cab2bHyperlink(true);
        /* Set the hyperlink text */
        hyperlink.setText(Utility.getDisplayName(hyperLinkUserObject.getTargetEntity()));
        hyperlink.setToolTipText(tooltip);
        hyperlink.setUserObject(hyperLinkUserObject);

        hyperlink.addActionListener(associatedDataActionListener);
        return hyperlink;
    }
}