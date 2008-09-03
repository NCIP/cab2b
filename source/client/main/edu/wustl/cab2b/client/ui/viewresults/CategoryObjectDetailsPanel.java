package edu.wustl.cab2b.client.ui.viewresults;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import edu.wustl.cab2b.client.ui.query.TransformCategoryResult;
import edu.wustl.cab2b.client.ui.treetable.B2BTreeNode;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;

/**
 * Class for displaying category result records
 * @author deepak_shingan
 *
 */
public class CategoryObjectDetailsPanel extends DefaultDetailedPanel<ICategorialClassRecord> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(CategoryObjectDetailsPanel.class);
    /**
     * Table for displaying category result records.
     */
    private Vector<String> categoryTableHeader = new Vector<String>();

    private B2BTreeNode b2BTreeRootNode = new B2BTreeNode();

    public CategoryObjectDetailsPanel(IRecord record) {
        super((ICategorialClassRecord) record);
        isVFill = false;
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.ResultObjectDetailsPanel#initData()
     */
    protected void initData() {
        super.initData();

        logger.debug("Setting table data");
        categoryTableHeader.add("Category Details");

        Map<CategorialClass, List<ICategorialClassRecord>> mapChildClasses = record.getChildrenCategorialClassRecords();
        logger.debug("Size of class Records :" + mapChildClasses.keySet().size());

        TransformCategoryResult transformCategoryResult = new TransformCategoryResult();
        b2BTreeRootNode.setDisplayName("Associated Information");

        for (List<ICategorialClassRecord> categorialClassList : mapChildClasses.values()) {
            b2BTreeRootNode = transformCategoryResult.getB2BRootTreeNode(categorialClassList, b2BTreeRootNode);
        }

        Iterator<B2BTreeNode> b2BTreeNodeIterator = b2BTreeRootNode.getChildren().iterator();
        while (b2BTreeNodeIterator.hasNext()) {
            B2BTreeNode treeNode = b2BTreeNodeIterator.next();
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
                b2BTreeNodeIterator.remove();
            }
        }
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.ResultObjectDetailsPanel#initTableGUI()
     */
    protected void initGUI() {
        if (b2BTreeRootNode.getChildren().size() == 0) {
            super.isVFill = true;
            super.initGUI();
        } else {
            super.initGUI();
            adjustRows();
            this.add("br hfill vfill", b2BTreeRootNode.getCategoryResultPanel());
        }
    }

}
