/**
 * 
 */
package edu.wustl.cab2b.client.ui.dag.ambiguityresolver;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.IDialogInterface;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.util.Utility;

/**
 * This abstract class defines the what an ambiguity resolver must do. It also implements some common methods
 * @author chetan_patil
 */
public abstract class AbstractAmibuityResolver extends Cab2bPanel implements IDialogInterface {
    private static final long serialVersionUID = 1L;

    protected JDialog parentWindow = null;

    protected final String[] AMBIGUITY_PATH_TABLE_HEADERS = new String[] { "Select", "Paths", "Path\nPopularity" };

    protected JTable ambiguityPathTable;

    protected Cab2bPanel buttonPanel;

    /**
     * Key is a vector of source, target entity interface (which is the input to
     * ambiguity resolver). Value is a List of user selected paths, a path is a
     * PathInterfaces.
     */
    protected List<IPath> userSelectedPaths;

    protected List<IPath> selectedPathList;

    /**
     * Default constructor
     */
    AbstractAmibuityResolver() {
        this.setSize(Constants.WIZARD_SIZE2_DIMENSION);
        this.setLayout(new BorderLayout());
    }

    abstract protected void initializeGUI();

    /**
     * This method refreshes the table data
     */
    abstract protected void refreshAmbiguityTable();

    /**
     * This method sets the renderers to the columns of the table and adds the table panel to this object
     */
    abstract protected void addTablePanel();

    /**
     * Returns the data for the Ambiguity Resolver table
     * @return Array of Object array containing the table data
     */
    abstract protected AbstractTableModel getAmbiguityTableModel();

    /**
     * This method returns a panel containing table
     * @return panel of table
     */
    protected Cab2bPanel createTablePanel(JTable jTable) {
        JScrollPane jScrollPane = new JScrollPane(jTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setBorder(null);
        jScrollPane.setBackground(Color.white);
        Cab2bPanel tablePanel = new Cab2bPanel(new RiverLayout());
        tablePanel.setBackground(Color.white);
        tablePanel.add("br center hfill vfill ", jScrollPane);

        return tablePanel;
    }

    /**
     * This method returns the table of path list
     * @return table of path list
     */
    protected JTable createAmbiguityPathTable(AbstractTableModel abstractTableModel) {
        JTable pathTable = new JTable(abstractTableModel);
        pathTable.getTableHeader().setReorderingAllowed(false);
        pathTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        pathTable.setFont(new Font("Arial", Font.PLAIN, 12));
        pathTable.setRowSelectionAllowed(false);
        pathTable.setRowMargin(4);

        TableColumnModel tableColumnModel = pathTable.getColumnModel();
        tableColumnModel.getColumn(0).setMaxWidth(45);
        tableColumnModel.getColumn(0).setMinWidth(45);

        tableColumnModel.getColumn(1).setMaxWidth(580);
        tableColumnModel.getColumn(1).setMinWidth(580);
        tableColumnModel.getColumn(1).setPreferredWidth(580);

        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JTextField.CENTER);
        tableColumnModel.getColumn(2).setMaxWidth(105);
        tableColumnModel.getColumn(2).setMinWidth(80);
        tableColumnModel.getColumn(2).setPreferredWidth(105);
        tableColumnModel.getColumn(2).setCellRenderer(defaultTableCellRenderer);

        return pathTable;
    }

    /**
     * This method returns the complete path to be displayed
     * @param path 
     * @return path to be displayed
     */
    protected String getFullPathNames(IPath path) {
        StringBuffer returner = new StringBuffer();
        String roleName;
        List<IAssociation> associationList = path.getIntermediateAssociations();
        boolean isFirstAssociation = true;
        for (IAssociation association : associationList) {
            roleName = edu.wustl.cab2b.client.ui.query.Utility.getRoleName(association);
            if (isFirstAssociation) {
                EntityInterface srcEntity = association.getSourceEntity();
                EntityInterface tarEntity = association.getTargetEntity();
                String srcEntityName = Utility.parseClassName(srcEntity.getName());
                String tarEntityName = Utility.parseClassName(tarEntity.getName());
                isFirstAssociation = false;
                returner.append(srcEntityName + " >> (" + roleName + ") >> " + tarEntityName);
            } else {
                EntityInterface tarEntity = association.getTargetEntity();
                String tarEntityName = Utility.parseClassName(tarEntity.getName());
                returner.append(" >> (" + roleName + ") >> " + tarEntityName);
            }
        }
        return returner.toString();
    }

    /**
     * This method returns the panel of the buttons
     * @return panel of buttons
     */
    protected Cab2bPanel createButtonPanel(ActionListener submitButtonListener, ActionListener cancelButtonListener) {
        Cab2bButton submitButton = new Cab2bButton("Submit");
        submitButton.addActionListener(submitButtonListener);

        Cab2bButton cancelButton = new Cab2bButton("Cancel");
        cancelButton.addActionListener(cancelButtonListener);

        Cab2bPanel buttonPanel = new Cab2bPanel();
        buttonPanel.add("right ", submitButton);
        buttonPanel.add("right ", cancelButton);

        return buttonPanel;
    }

    public void setParentWindow(JDialog dialog) {
        parentWindow = dialog;
    }

}
