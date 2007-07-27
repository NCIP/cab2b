package edu.wustl.cab2b.client.ui.experiment;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.LinkRenderer;
import org.jdesktop.swingx.action.LinkAction;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.cab2b.common.domain.ExperimentGroup;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.common.tree.ExperimentTreeNode;
import edu.wustl.common.util.logger.Logger;

/**
 * A panel to display details of the selected experiment or experiment 
 * group in a spreadsheet.
 * 
 * @author chetan_bh
 */
public class ExperimentDetailsPanel extends Cab2bPanel {
    Cab2bTable expTable;

    Object[] tableHeader = { "Title", "Date Created", "Last Updated", "Description" };

    Object[][] emptyTableData = {};

    Vector tHeader = new Vector();

    Cab2bButton deleteButton;

    ExperimentOpenPanel expPanel = null;

    public ExperimentDetailsPanel() {
        initGUI();
        tHeader.add("Title");
        tHeader.add("Date Created ");
        tHeader.add("Last Updated");
        tHeader.add("Description");
    }

    private void initGUI() {
        this.setLayout(new RiverLayout());
        this.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));
        expTable = new Cab2bTable(true, emptyTableData, tableHeader);

        MyLinkAction myLinkAction = new MyLinkAction();
        expTable.getColumn(1).setCellRenderer(new LinkRenderer(myLinkAction));
        expTable.getColumn(1).setCellEditor(new LinkRenderer(myLinkAction));

        HighlighterPipeline highlighters = new HighlighterPipeline();
        highlighters.addHighlighter(new AlternateRowHighlighter());
        expTable.setHighlighters(highlighters);

        this.add("br hfill vfill", new JScrollPane(expTable));
        deleteButton = new Cab2bButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

            }
        });
        deleteButton.setEnabled(false);
        Cab2bPanel deleteButtonPanel= new Cab2bPanel(new RiverLayout(5,5));
        deleteButtonPanel.add(deleteButton);
        deleteButtonPanel.add("br",new JLabel(""));
        
        this.add("br", deleteButtonPanel);
        highlighters.updateUI();
    }

    public void refreshDetails(ExperimentTreeNode expTreeNode) {
        Vector newTableData = new Vector();
        if (expTreeNode.getChildNodes().size() == 0) {
            Vector firstRow = new Vector();
            firstRow.add(expTreeNode);
            firstRow.add(expTreeNode.getCreatedOn());
            firstRow.add(expTreeNode.getLastUpdatedOn());
            firstRow.add(expTreeNode.getDesc());
            newTableData.add(firstRow);
        } else {
            updateTableData(expTreeNode, newTableData);
        }

        expTable = new Cab2bTable(true, newTableData, tHeader);
        MyLinkAction myLinkAction = new MyLinkAction();
        expTable.getColumn(1).setCellRenderer(new LinkRenderer(myLinkAction));
        expTable.getColumn(1).setCellEditor(new LinkRenderer(myLinkAction));

        Component comp = this.getComponent(0);
        this.removeAll();
        //Logger.out.debug("Component 0 "+comp.getClass());
        this.add("br hfill vfill", new JScrollPane(expTable));
        Cab2bPanel deleteButtonPanel= new Cab2bPanel(new RiverLayout(5,5));
        deleteButtonPanel.add(deleteButton);
        deleteButtonPanel.add("br",new JLabel(""));
        
        this.add("br", deleteButtonPanel);
        this.updateUI();
    }

    private void updateTableData(ExperimentTreeNode treeNode, Vector tableData) {
        if (treeNode.getChildNodes().size() == 0) {
            Logger.out.debug("found child node zero");
            return;
        }

        Iterator iter = treeNode.getChildNodes().iterator();
        int i = 0;

        while (iter.hasNext()) {
            ExperimentTreeNode child = (ExperimentTreeNode) iter.next();
            if (child.isExperimentGroup() == false) {
                Vector nextRow = new Vector();
                nextRow.add(child);
                nextRow.add(child.getCreatedOn());
                nextRow.add(child.getLastUpdatedOn());
                nextRow.add(child.getDesc());
                tableData.add(nextRow);
                updateTableData(child, tableData);
            }
        }
    }

    public void refreshDetails(Experiment exp) {
        //Object[][] newTableData = {{exp.getName(),exp.getCreatedOn(),exp.getLastUpdatedOn(),exp.getDescription()}};
        Object[][] newTableData = { { exp, exp.getCreatedOn(), exp.getLastUpdatedOn(), exp.getDescription() } };

        //expTable.setModel(new Cab2bTable(newTableData, tableHeader));
        expTable = new Cab2bTable(true, newTableData, tableHeader);
        MyLinkAction myLinkAction = new MyLinkAction();
        expTable.getColumn(1).setCellRenderer(new LinkRenderer(myLinkAction));
        expTable.getColumn(1).setCellEditor(new LinkRenderer(myLinkAction));

        Component comp = this.getComponent(0);
        this.removeAll();
        Logger.out.debug("Component 0 " + comp.getClass());
        this.add("br hfill vfill", new JScrollPane(expTable));
        Cab2bPanel deleteButtonPanel= new Cab2bPanel(new RiverLayout(5,5));
        deleteButtonPanel.add(deleteButton);
        deleteButtonPanel.add("br",new JLabel(""));
        
        this.add("br", deleteButtonPanel);
        this.updateUI();
    }

    public void refreshDetails(ExperimentGroup expGrp) {
        Iterator expIter = expGrp.getExperimentCollection().iterator();
        Object[][] newTableData = new Object[expGrp.getExperimentCollection().size()][];

        int counter = 0;

        while (expIter.hasNext()) {
            Object obj = expIter.next();
            if (obj instanceof Experiment) {
                Experiment exp = (Experiment) obj;
                //newTableData[counter++] = new Object[] {exp.getName(),exp.getCreatedOn(),exp.getLastUpdatedOn(),exp.getDescription()};
                newTableData[counter++] = new Object[] { exp, exp.getCreatedOn(), exp.getLastUpdatedOn(), exp.getDescription() };

            } else if (obj instanceof ExperimentGroup) {

            }
        }

        expTable = new Cab2bTable(true, newTableData, tableHeader);

        /*Adding Hyperlink to experiment Name*/
        MyLinkAction myLinkAction = new MyLinkAction();
        expTable.getColumn(1).setCellRenderer(new LinkRenderer(myLinkAction));
        expTable.getColumn(1).setCellEditor(new LinkRenderer(myLinkAction));

        this.removeAll();
        this.add("br hfill vfill", new JScrollPane(expTable));
        Cab2bPanel deleteButtonPanel= new Cab2bPanel(new RiverLayout(5,5));
        deleteButtonPanel.add(deleteButton);
        deleteButtonPanel.add("br",new JLabel(""));
        
        this.add("br", deleteButtonPanel);
        this.updateUI();
    }

    public static void main(String[] args) {
        ExperimentDetailsPanel expDetPanel = new ExperimentDetailsPanel();

        JFrame frame = new JFrame("Experiment");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(expDetPanel);
        frame.setVisible(true);
    }

    /*Class used for adding hyperlinks in Jtable rows*/
    class MyLinkAction extends LinkAction {
        public MyLinkAction(Object obj) {
            super(obj);
        }

        public MyLinkAction() {

        }

        public void actionPerformed(ActionEvent e) {
            Logger.out.debug("link is working");
            setVisited(true);

            //getting the selected hyperlink row
            int selectionIndex = expTable.getSelectionModel().getLeadSelectionIndex();

            //getting object associated with hyperlink
            //column Number will be always 1
            final Object expObj = (ExperimentTreeNode) expTable.getValueAt(selectionIndex, 1);

            CustomSwingWorker swingWorker = new CustomSwingWorker(MainFrame.openExperimentWelcomePanel) {
                @Override
                protected void doNonUILogic() throws RuntimeException {

                    if (expObj instanceof ExperimentTreeNode) {
                        ExperimentTreeNode expNodeObj = (ExperimentTreeNode) expObj;
                        if (expNodeObj.isExperimentGroup() == false) {
                            ExperimentDetailsPanel.this.expPanel = new ExperimentOpenPanel(expNodeObj,
                                    ExperimentDetailsPanel.this);
                        } else {
                            //TODO
                            /*If user clicks on experimentGroup name then Refresh the table  
                             * and display all child nodes for selected experimentGroup */
                        }
                    }
                }

                @Override
                protected void doUIUpdateLogic() throws RuntimeException {

                    if (expObj instanceof ExperimentTreeNode) {
                        ExperimentTreeNode expNodeObj = (ExperimentTreeNode) expObj;

                        if (expNodeObj.isExperimentGroup() == false) {
                            /*If user clicks on experiment name then Open experiment 
                             * in new ExperimentOpenPanel with details*/
                            Logger.out.debug("ExperimentTreeNode node :" + expNodeObj.getIdentifier()
                                    + " is not a experimentGroup");
                            MainFrame.openExperimentWelcomePanel.removeAll();
                            MainFrame.openExperimentWelcomePanel.add(ExperimentDetailsPanel.this.expPanel);
                            updateUI();
                        }
                    }
                }
            };
            swingWorker.start();
        }
    }
}
