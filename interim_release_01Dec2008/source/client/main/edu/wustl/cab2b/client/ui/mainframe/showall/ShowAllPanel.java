package edu.wustl.cab2b.client.ui.mainframe.showall;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.LinkRenderer;
import org.jdesktop.swingx.action.LinkAction;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;

/**
 * Abstract panel class for Showing details in tabular format
 * @author deepak_shingan,gaurav_mehta
 */
public abstract class ShowAllPanel extends Cab2bPanel {

    private Cab2bTable table;

    private LinkAction linkAction;

    private Object[] tableHeader;

    private Object[][] data;

    private int hyperLinkColumnNumber = -1;

    /**
     * Constructor
     * @param tableHeader
     * @param data
     * @param columnName
     */
    public ShowAllPanel(Object[] tableHeader, Object[][] data, String columnName) {
        this.tableHeader = tableHeader;
        this.data = data;
        if (!(null == columnName)) {
            for (int i = 0; i < tableHeader.length; i++) {
                if (columnName.equals(tableHeader[i])) {
                    this.hyperLinkColumnNumber = i;
                }
            }
        }
        linkAction = new ShowAllLinkActionClass();
        initGUI();
    }

    /**
     * GUI initialisation method
     */
    private void initGUI() {
        setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));
        if (data != null && tableHeader != null) {
            table = new Cab2bTable(false, data, tableHeader);
            if (hyperLinkColumnNumber != -1) {
                table.getColumn(hyperLinkColumnNumber).setCellRenderer(new LinkRenderer(linkAction));
                table.getColumn(hyperLinkColumnNumber).setCellEditor(new LinkRenderer(linkAction));
            }
        } else
            table = new Cab2bTable();
        HighlighterPipeline highlighters = new HighlighterPipeline();
        highlighters.addHighlighter(new AlternateRowHighlighter());
        table.setHighlighters(highlighters);
        highlighters.updateUI();

        table.setBorder(null);
        table.setShowGrid(false);
        table.setRowHeightEnabled(true);

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.getViewport().add(table);
        this.add("br hfill vfill", jScrollPane);
        this.updateUI();
    }

    /**
     * @author deepak_shingan
     *
     */
    private class ShowAllLinkActionClass extends LinkAction {

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         * @param e ActionEvent
         */
        public void actionPerformed(ActionEvent e) {
            setVisited(true);
            linkActionPerformed();
        }
    }

    /**
     * Action method for each hyperlink click on the table
     */
    public abstract void linkActionPerformed();

    /**
     * Returns Cab2bTable associated with panel
     * @return the table
     */
    public Cab2bTable getTable() {
        return table;
    }
}
