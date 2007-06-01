package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.LinkRenderer;
import org.jdesktop.swingx.action.LinkAction;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.util.UserObjectWrapper;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.QueryResultFactory;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.util.logger.Logger;

/**
 * Classs for displaying category result records
 * @author deepak_shingan
 *
 */
public class CategoryObjectDetailsPanel extends ResultObjectDetailsPanel {

    /**
     * Table for displaying category result records.
     */
    private Cab2bTable categoryTable;

    private Vector<Vector<UserObjectWrapper>> categoryTableData = new Vector<Vector<UserObjectWrapper>>();

    private Vector<String> categoryTableHeader = new Vector<String>();

    public CategoryObjectDetailsPanel(
            SimpleSearchResultBreadCrumbPanel searchPanel,
            IDataRow dataRow,
            IRecord record,
            Collection<AssociationInterface> incomingAssociationCollection,
            List<IInterModelAssociation> intraModelAssociationCollection) {
        super(searchPanel, dataRow, record, incomingAssociationCollection, intraModelAssociationCollection);
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.ResultObjectDetailsPanel#initData()
     */
    protected void initData() {
        super.initData();

        Logger.out.debug("Setting table data");
        categoryTableHeader.add("Children Classes");

        ICategorialClassRecord iCategorialClassRecord = (ICategorialClassRecord) record;

        Map<CategorialClass, List<ICategorialClassRecord>> mapChildClasses = iCategorialClassRecord.getChildrenCategorialClassRecords();
        Logger.out.debug("Size of class Records :" + mapChildClasses.keySet().size());

        Vector<UserObjectWrapper> vect = new Vector<UserObjectWrapper>();
        for (CategorialClass categorialClass : mapChildClasses.keySet()) {
            String str = edu.wustl.cab2b.common.util.Utility.getDisplayName(categorialClass.getCategorialClassEntity())
                    + "_" + categorialClass.getId();
            vect.add(new UserObjectWrapper<CategorialClass>(categorialClass, str));
        }

        categoryTableData.add(vect);
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.ResultObjectDetailsPanel#initTableGUI()
     */
    protected void initTableGUI() {
        super.initTableGUI();

        categoryTable = new Cab2bTable(false, categoryTableData, categoryTableHeader);
        CategoryLinkAction myLinkAction = new CategoryLinkAction();
        categoryTable.getColumn(0).setCellRenderer(new LinkRenderer(myLinkAction));
        categoryTable.getColumn(0).setCellEditor(new LinkRenderer(myLinkAction));
        JScrollPane tableScrollPane = new JScrollPane(categoryTable);

        //detailsTablePanel.add(tableScrollPane, BorderLayout.SOUTH);
        detailsTablePanel.add("br hfill vfill", tableScrollPane);
        //detailsTablePanel.updateUI();
    }

    /**
     * @author deepak_shingan
     *
     */
    class CategoryLinkAction extends LinkAction {

        public CategoryLinkAction() {

        }

        public void actionPerformed(ActionEvent e) {
            setVisited(true);

            //getting the selected hyperlink row
            int selectionIndex = categoryTable.getSelectionModel().getLeadSelectionIndex();

            //Getting object associated with hyperlink
            //column Number will be always 0 

            ICategorialClassRecord iCategorialClassRecord = (ICategorialClassRecord) record;

            Map<CategorialClass, List<ICategorialClassRecord>> mapChildClasses = iCategorialClassRecord.getChildrenCategorialClassRecords();

            UserObjectWrapper<CategorialClass> userObjectWrapper = (UserObjectWrapper) categoryTable.getValueAt(
                                                                                                                selectionIndex,
                                                                                                                0);
            if (userObjectWrapper != null) {
                CategorialClass categorialClass = userObjectWrapper.getUserObject();
                List<ICategorialClassRecord> listICategorialClassRecord = mapChildClasses.get(categorialClass);

                IQueryResult queryResult = QueryResultFactory.createResult(categorialClass.getCategorialClassEntity());
                queryResult.addRecords(dataRow.getURL(), listICategorialClassRecord);
                String breadCrumbText = edu.wustl.cab2b.common.util.Utility.getDisplayName(categorialClass.getCategorialClassEntity())
                        + "_" + categorialClass.getId();
                JXPanel resultPanel = ResultPanelFactory.getResultPanel(searchPanel, queryResult, dataRow, null);
                searchPanel.addToPanel(resultPanel, breadCrumbText);

                //searchPanel.addToPanel(searchPanel.getResultPanel(queryResult, dataRow, null), );
                searchPanel.updateUI();

            }

        }
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

}
