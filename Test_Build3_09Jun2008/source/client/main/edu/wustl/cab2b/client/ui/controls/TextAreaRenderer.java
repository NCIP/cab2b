package edu.wustl.cab2b.client.ui.controls;

import java.awt.Component;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class TextAreaRenderer extends JTextPane implements TableCellRenderer {
    private static final long serialVersionUID = 1L;

    /** This is the Map stores the height of rows */
    private final Map<JTable, Map<Integer, Map<Integer, Integer>>> cellSizes = new HashMap<JTable, Map<Integer, Map<Integer, Integer>>>();

    /**
     * Default constructor
     */
    public TextAreaRenderer() {
        setEditable(false);
        setContentType("text/html");
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        final DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, column);
        setForeground(defaultTableCellRenderer.getForeground());
        setBackground(defaultTableCellRenderer.getBackground());
        setBorder(defaultTableCellRenderer.getBorder());
        setFont(defaultTableCellRenderer.getFont());

        String cellValue = defaultTableCellRenderer.getText();
        String displayValue = getDisplayValue(cellValue);
        setText(displayValue);

        TableColumnModel tableColumnModel = table.getColumnModel();
        setSize(tableColumnModel.getColumn(column).getWidth(), 100000);

        int height_wanted = (int) getPreferredSize().getHeight();
        addSize(table, row, column, height_wanted);
        height_wanted = findTotalMaximumRowSize(table, row);

        if (height_wanted != table.getRowHeight(row)) {
            table.setRowHeight(row, height_wanted);
        }
        return this;
    }

    /**
     * This method converts the given cell value into style formated string 
     * @param cellValue
     * @return
     */
    private String getDisplayValue(String cellValue) {
        StringBuffer displayValue = new StringBuffer(
                "<html><body><table height='100%' width='100%'><tr><td style='vertical-align: middle; font-family: Arial'>");
        final String redArrow = "<font color=\"red\"> >> </font>";

        String[] pathCrumbs = cellValue.split("\n");
        for (int j = 0; j < pathCrumbs.length; j++) {
            String[] crumbs = pathCrumbs[j].split(">>");
            for (int i = 0; i < crumbs.length; i++) {
                String crumbValue = crumbs[i].trim();
                if (crumbValue.matches("\\(.*\\)")) {
                    displayValue.append("<i> " + crumbValue + " </i>");
                } else {
                    displayValue.append(crumbValue);
                }

                if (i < crumbs.length - 1) {
                    displayValue.append(redArrow);
                }
            }
            displayValue.append("<br>");
        }
        displayValue.append("</td></tr></table></body></html>");

        return displayValue.toString();
    }

    /**
     * This method adds the given height into the Map given the table and the corresponding row and column number.
     * The Map of height is further used for the row height calculation.
     * @param table
     * @param row
     * @param column
     * @param height
     */
    private void addSize(JTable table, int row, int column, int height) {
        Map<Integer, Map<Integer, Integer>> rowMap = cellSizes.get(table);
        if (rowMap == null) {
            rowMap = new HashMap<Integer, Map<Integer, Integer>>();
            cellSizes.put(table, rowMap);
        }

        Map<Integer, Integer> rowHeightMap = rowMap.get(new Integer(row));
        if (rowHeightMap == null) {
            rowHeightMap = new HashMap<Integer, Integer>();
            rowMap.put(new Integer(row), rowHeightMap);
        }
        rowHeightMap.put(new Integer(column), new Integer(height));
    }

    /**
     * This method looks through all columns and get the renderer. If it is also a
     * TextAreaRenderer, we look at the maximum height in its hash table for
     * this row.
     * @param table
     * @param row
     * @return
     */
    private int findTotalMaximumRowSize(JTable table, int row) {
        int maximum_height = 0;
        Enumeration columns = table.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn tc = (TableColumn) columns.nextElement();
            TableCellRenderer cellRenderer = tc.getCellRenderer();
            if (cellRenderer instanceof TextAreaRenderer) {
                TextAreaRenderer textAreaRenderer = (TextAreaRenderer) cellRenderer;
                maximum_height = Math.max(maximum_height, textAreaRenderer.findMaximumRowSize(table, row));
            }
        }
        return maximum_height;
    }

    /**
     * This method finds and returns the maximum row size for the given table and the row number
     * @param table
     * @param row
     * @return
     */
    private int findMaximumRowSize(JTable table, int row) {
        Map<Integer, Map<Integer, Integer>> rows = cellSizes.get(table);
        if (rows == null)
            return 0;

        Map<Integer, Integer> rowHeightMap = rows.get(new Integer(row));
        if (rowHeightMap == null)
            return 0;

        int maximum_height = 0;
        Set<Map.Entry<Integer, Integer>> rowHeightsEntrySet = rowHeightMap.entrySet();
        for (Map.Entry<Integer, Integer> rowHeightsEntry : rowHeightsEntrySet) {
            int cellHeight = rowHeightsEntry.getValue().intValue();
            maximum_height = Math.max(maximum_height, cellHeight);
        }

        return maximum_height;
    }

}
