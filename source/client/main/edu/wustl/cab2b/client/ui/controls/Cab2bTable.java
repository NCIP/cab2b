package edu.wustl.cab2b.client.ui.controls;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;
import org.jdesktop.swingx.table.ColumnHeaderRenderer;

/**
 * The Cab2bTable is extended from JXTable and used to display and edit regular 
 * two-dimensional tables of cells.
 * @author Chtan_BH
 *
 */
public class Cab2bTable extends JXTable {
    private static final long serialVersionUID = 1L;

    /**
     * Flag to show checkbox in first column
     */
    boolean showCheckBox;

    /**
     * Constructs a Cab2bTable to display the values in the two dimensional array,
     * data, with column names, headers and checkbox flag.
     * @param showCheckBox
     * @param data
     * @param headers
     */
    public Cab2bTable(boolean showCheckBox, Object[][] data, Object[] headers) {
        super(new Cab2bDefaultTableModel(showCheckBox, data, headers));
        this.showCheckBox = showCheckBox;
        initUI();
    }

    /**
     * Constructs a Cab2bTable that is initialized with tableModel 
     * as the data model, a default column model, and a default selection model.
     * @param tableModel
     */
    public Cab2bTable(TableModel tableModel) {
        super(tableModel);
        initUI();
    }

    /**
     * Constructs a default Cab2bTable that is initialized with a default data model, 
     * a default column model, and a default selection model.
     */
    public Cab2bTable() {
        super();
        initUI();
    }

    /**
     * Method for initilizing UI
     */
    private void initUI() {
        if (true == showCheckBox) {
            // Create an ItemListener
            HeaderItemListener itemListener = new HeaderItemListener((Cab2bDefaultTableModel) this.getModel());
            this.getColumnModel().getColumn(0).setHeaderRenderer(new Cab2bCheckBoxHeader(itemListener));
            this.getColumn(0).setMaxWidth(25);
        }

        this.setHighlighters(new HighlighterPipeline(new Highlighter[] { AlternateRowHighlighter.genericGrey }));

        // this.packAll();
        this.setSortable(false);
        this.setHorizontalScrollEnabled(true);
        this.getTableHeader().setSize(new Dimension(0, 28));
        this.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        this.setRowHeight(23);
        this.getTableHeader().setReorderingAllowed(false);
        ((ColumnHeaderRenderer) this.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
        this.packAll();
    }

    /**
     * Constructs a Cab2bTable to display the values in the Vector of Vectors,
     * data, with column names, headers and specified showCheckbox flag.
     * @param showCheckBox
     * @param data
     * @param headers
     */
    public Cab2bTable(boolean showCheckBox, Vector data, Vector headers) {
        super(new Cab2bDefaultTableModel(showCheckBox, data, headers));
        this.showCheckBox = showCheckBox;
        initUI();
    }

    /** Returns the indices of selected rows
     * @return indexes
     * @see javax.swing.JTable#getSelectedRows()
     */
    public int[] getSelectedRows() {
        if (showCheckBox) {
            Vector<Integer> values = ((Cab2bDefaultTableModel) this.getModel()).getCheckedRowIndexes();
            int[] indexes = new int[values.size()];
            for (int i = 0; i < values.size(); i++)
                indexes[i] = values.get(i).intValue();
            return indexes;
        } else {
            return super.getSelectedRows();
        }
    }

    /**
     * Sets table header bold
     */
    public void setTableHeaderBold() {
        JTableHeader header = getTableHeader();
        final Font boldFont = header.getFont().deriveFont(Font.BOLD);
        final TableCellRenderer headerRenderer = header.getDefaultRenderer();
        header.setDefaultRenderer(new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component component = headerRenderer.getTableCellRendererComponent(table, value, isSelected,
                                                                                   hasFocus, row, column);
                component.setFont(boldFont);
                return component;
            }
        });
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Object[][] data = { { "sddsdsgd gfgfd ", "dfdgf hgghfhg hghg" }, { "<a href=\"http://google.com\">fshjfsdjghdljkgh  fddddddddddddddddddddd dddd klfjhkldjhlkfdhigjhlfgjhlgfhjg", "22" }, { "111", "222" } };
        Object[] headers = { "first", "second" };

        Cab2bTable cab2bTable = new Cab2bTable(true, data, headers);
        cab2bTable.setEditable(true);
        // myTable.set
        JFrame frame = new JFrame("New Frame");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /* cab2bTable.setAutoResizeMode(JXTable.AUTO_RESIZE_OFF); */
        JScrollPane pane = new JScrollPane(cab2bTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        frame.getContentPane().add(pane);
        frame.setVisible(true);
    }
}

/**
 * ItemListener to listen to header click event
 */
class HeaderItemListener implements ItemListener {
    Cab2bDefaultTableModel m_tableModel;

    /**
     * @param tableModel
     */
    public HeaderItemListener(Cab2bDefaultTableModel tableModel) {
        m_tableModel = tableModel;
    }

    /**
     * Called when a CheckBox is checked/unchecked
     * @param e
     */
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getSource();
        if (false == source instanceof AbstractButton) {
            return;
        }
        if (e.getStateChange() == ItemEvent.SELECTED) {
            m_tableModel.selectAllRows();
        } else {
            m_tableModel.unSelectAllRows();
        }

    }
}
