/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.experiment;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.LinkRenderer;
import org.jdesktop.swingx.action.LinkAction;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.cab2b.common.domain.ExperimentGroup;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.common.tree.ExperimentTreeNode;


/**
 * A panel to display details of the selected experiment or experiment group in
 * a spreadsheet.
 * 
 * @author chetan_bh
 */
public class ExperimentDetailsPanel extends Cab2bPanel {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ExperimentDetailsPanel.class);

    private Cab2bTable expTable;

    private Object[] tableHeader = { "Title", "Date Created", "Last Updated", "Description" };

    private Vector<String> tHeader = new Vector<String>();

    private ExperimentOpenPanel expPanel = null;

    /**
     * Default Constructor
     */
    public ExperimentDetailsPanel() {
        initGUI();
        tHeader.add("Title");
        tHeader.add("Date Created ");
        tHeader.add("Last Updated");
        tHeader.add("Description");
    }

    /**
     * This method returns JScrollPane containing  experiment details
     * @return
     */
    private JScrollPane initExperimentTableWithScrolls() {
        ExperimentLinkAction experimentLinkAction = new ExperimentLinkAction();
        expTable.getColumn(1).setCellRenderer(new LinkRenderer(experimentLinkAction));
        expTable.getColumn(1).setCellEditor(new LinkRenderer(experimentLinkAction));

        expTable.setBorder(null);
        expTable.setShowGrid(false);
        expTable.setRowHeightEnabled(true);
        expTable.getColumnModel().getColumn(2).setPreferredWidth(35);
        expTable.getColumnModel().getColumn(3).setPreferredWidth(35);
        expTable.getColumnModel().getColumn(4).setPreferredWidth(325);
        expTable.getColumnModel().getColumn(4).setCellRenderer(new CellWrapRenderer());

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.getViewport().add(expTable);
        this.add("br hfill vfill", jScrollPane);

        return jScrollPane;
    }

    /**
     * Method for updating  experiment details on experiment panel
     * @param newTableData
     */
    private void refreshExperimentDetailPanel(Vector<Vector<Object>> newTableData) {
        this.removeAll();
        expTable = new Cab2bTable(true, newTableData, tHeader);
        refreshExperimentDetailPanel();
    }

    /**
     *  Method for updating  experiment details on experiment panel
     * @param newTableData
     */
    private void refreshExperimentDetailPanel(Object[][] newTableData) {
        this.removeAll();
        expTable = new Cab2bTable(true, newTableData, tableHeader);
        refreshExperimentDetailPanel();
    }

    /**
     *  Method for updating  experiment details on experiment panel
     */
    private void refreshExperimentDetailPanel() {
        JScrollPane jScrollPane = initExperimentTableWithScrolls();
        this.add("br hfill vfill", jScrollPane);
        this.updateUI();
    }

    /**
     * GUI initilisation method
     */
    private void initGUI() {
        this.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));

        expTable = new Cab2bTable(true, new Object[][] {}, tableHeader);
        HighlighterPipeline highlighters = new HighlighterPipeline();
        highlighters.addHighlighter(new AlternateRowHighlighter());
        expTable.setHighlighters(highlighters);

        refreshExperimentDetailPanel();
        highlighters.updateUI();
    }

    /**
     * Method for updating table data
     * @param treeNode
     * @param tableData
     */
    private void updateTableData(ExperimentTreeNode treeNode, Vector<Vector<Object>> tableData) {
        if (treeNode.getChildNodes().size() == 0) {
            logger.debug("found child node zero");
            return;
        }

        Iterator iter = treeNode.getChildNodes().iterator();
        while (iter.hasNext()) {
            ExperimentTreeNode child = (ExperimentTreeNode) iter.next();
            if (child.isExperimentGroup() == false) {
                Vector<Object> nextRow = new Vector<Object>();
                nextRow.add(child);
                nextRow.add(child.getCreatedOn());
                nextRow.add(child.getLastUpdatedOn());
                nextRow.add(child.getDesc());

                tableData.add(nextRow);
                updateTableData(child, tableData);
            }
        }
    }

    /**
     * Method for updating table details mapped with ExperimentTreeNode 
     * @param expTreeNode
     */
    public void refreshDetails(ExperimentTreeNode expTreeNode) {
        Vector<Vector<Object>> newTableData = new Vector<Vector<Object>>();
        if (expTreeNode.getChildNodes().size() == 0) {
            Vector<Object> firstRow = new Vector<Object>();

            firstRow.add(expTreeNode);
            firstRow.add(expTreeNode.getCreatedOn());
            firstRow.add(expTreeNode.getLastUpdatedOn());
            firstRow.add(expTreeNode.getDesc());

            newTableData.add(firstRow);
        } else {
            updateTableData(expTreeNode, newTableData);
        }

        refreshExperimentDetailPanel(newTableData);
    }

    /**
     * Method for updating table details mapped with Experiment
     * @param exp
     */
    public void refreshDetails(Experiment exp) {
        Object[][] newTableData = { { exp, exp.getCreatedOn(), exp.getLastUpdatedOn(), exp.getDescription() } };
        refreshExperimentDetailPanel(newTableData);
    }

    /**
     * Method for updating table details mapped with ExperimentGroup
     * @param expGrp
     */
    public void refreshDetails(ExperimentGroup expGrp) {
        Iterator expIter = expGrp.getExperimentCollection().iterator();
        Object[][] newTableData = new Object[expGrp.getExperimentCollection().size()][];

        int counter = 0;
        while (expIter.hasNext()) {
            Object obj = expIter.next();
            if (obj instanceof Experiment) {
                Experiment exp = (Experiment) obj;
                newTableData[counter++] = new Object[] { exp, exp.getCreatedOn(), exp.getLastUpdatedOn(), exp.getDescription() };
            } else if (obj instanceof ExperimentGroup) {

            }
        }

        refreshExperimentDetailPanel(newTableData);
    }

    /**
     * Action class for adding hyperlinks in Jtable rows 
     * @author deepak_shingan
     *
     */
    class ExperimentLinkAction extends LinkAction {
        private static final long serialVersionUID = 1L;

        public void actionPerformed(ActionEvent e) {
            setVisited(true);

            // getting the selected hyperlink row
            int selectionIndex = expTable.getSelectionModel().getLeadSelectionIndex();

            // getting object associated with hyperlink column Number will be
            // always 1
            final Object expObj = (ExperimentTreeNode) expTable.getValueAt(selectionIndex, 1);

            CustomSwingWorker swingWorker = new CustomSwingWorker(ExperimentDetailsPanel.this) {
                @Override
                protected void doNonUILogic() throws RuntimeException {

                    if (expObj instanceof ExperimentTreeNode) {
                        ExperimentTreeNode expNodeObj = (ExperimentTreeNode) expObj;
                        if (expNodeObj.isExperimentGroup() == false) {
                            ExperimentDetailsPanel.this.expPanel = new ExperimentOpenPanel(expNodeObj,
                                    ExperimentDetailsPanel.this);
                        } else {
                            // TODO
                            /*
                             * If user clicks on experimentGroup name then
                             * Refresh the table and display all child nodes for
                             * selected experimentGroup
                             */
                        }
                    }
                }

                @Override
                protected void doUIUpdateLogic() throws RuntimeException {

                    if (expObj instanceof ExperimentTreeNode) {
                        ExperimentTreeNode expNodeObj = (ExperimentTreeNode) expObj;

                        if (expNodeObj.isExperimentGroup() == false) {
                            /*
                             * If user clicks on experiment name then Open
                             * experiment in new ExperimentOpenPanel with
                             * details
                             */
                            logger.debug("ExperimentTreeNode node :" + expNodeObj.getIdentifier()
                                    + " is not a experimentGroup");

                            MainFrame mainframe = NewWelcomePanel.getMainFrame();
                            mainframe.getExperimentPanel().removeAll();
                            mainframe.getExperimentPanel().add(ExperimentDetailsPanel.this.expPanel);
                            updateUI();
                        }
                    }
                }
            };
            swingWorker.start();
        }
    }

    /**
     * @author deepak_shingan
     *
     */
    class CellWrapRenderer extends JTextArea implements TableCellRenderer {
        private static final long serialVersionUID = 1L;

        /**
         * 
         */
        public CellWrapRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            if (value != null) {
                setText(value.toString());
            }

            setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height + 5);
            if (table.getRowHeight(row) != getPreferredSize().height + 5) {
                table.setRowHeight(row, getPreferredSize().height + 5);
            }
            return this;
        }
    }
}
