package edu.wustl.cab2b.client.ui.viewresults;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.wustl.cab2b.client.ui.query.TransformCategoryResult;
import edu.wustl.cab2b.client.ui.treetable.B2BTreeNode;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
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
    private Vector<String> categoryTableHeader = new Vector<String>();

    B2BTreeNode b2BTreeRootNode = new B2BTreeNode();

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

        TransformCategoryResult transformCategoryResult = new TransformCategoryResult();
        b2BTreeRootNode.setDisplayName("Associated Classes");

        for (List<ICategorialClassRecord> categorialClassList : mapChildClasses.values()) {
            b2BTreeRootNode = transformCategoryResult.getB2BRootTreeNode(categorialClassList, b2BTreeRootNode);
        }
        Iterator<B2BTreeNode> b2BTreeNodeIterator1 = b2BTreeRootNode.getChildren().iterator();
        while (b2BTreeNodeIterator1.hasNext()) {

            B2BTreeNode treeNode = b2BTreeNodeIterator1.next();
            boolean isAllAtributes = true;
            for (B2BTreeNode childTreeNode : treeNode.getChildren()) {
                if (childTreeNode.getChildren() != null) {
                    isAllAtributes = false;
                    break;
                }
            }

            if (isAllAtributes) {
                //put this node up trreNode
                for (B2BTreeNode childTreeNode : treeNode.getChildren()) {
                    Vector<String> row = new Vector<String>();
                    row.add(childTreeNode.getDisplayName());
                    row.add("" + childTreeNode.getValue());
                    tableData.add(row);
                }
                b2BTreeNodeIterator1.remove();
            }
        }
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.ResultObjectDetailsPanel#initTableGUI()
     */
    protected void initTableGUI() {
        super.initTableGUI();
        adjustRows();

        tablePanel.add("br hfill vfill", b2BTreeRootNode.getCategoryResultPanel());
        detailsTablePanel.updateUI();
    }

    private static final long serialVersionUID = 1L;
}
