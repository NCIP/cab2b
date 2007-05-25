package edu.wustl.cab2b.client.ui.controls;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;

public class Cab2bTable extends JXTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	boolean m_showCheckBox;

	public Cab2bTable(boolean showCheckBox, Object[][] data, Object[] headers) {
		super(new Cab2bDefaultTableModel(showCheckBox, data, headers));
		m_showCheckBox = showCheckBox;
		initUI();
	}

	private void initUI() {
		if (true == m_showCheckBox) {
			// Create an ItemListener
			HeaderItemListener itemListener = new HeaderItemListener((Cab2bDefaultTableModel) this
					.getModel());
			this.getColumnModel().getColumn(0).setHeaderRenderer(
					new Cab2bCheckBoxHeader(itemListener));
			this.getColumn(0).setMaxWidth(25);
		}

		this.setHighlighters(new HighlighterPipeline(
				new Highlighter[] { AlternateRowHighlighter.genericGrey }));

		// this.packAll();
		this.setSortable(false);
		this.setHorizontalScrollEnabled(true);
		this.getTableHeader().setSize(new Dimension(0, 28));
		this.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
		this.setRowHeight(23);
		this.getTableHeader().setReorderingAllowed(false);
		this.packAll();
	}

	public Cab2bTable(boolean showCheckBox, Vector data, Vector headers) {
		super(new Cab2bDefaultTableModel(showCheckBox, data, headers));

		m_showCheckBox = showCheckBox;
		initUI();

	}

	public int[] getSelectedRows() {
		if (m_showCheckBox) {
			Vector<Integer> values = ((Cab2bDefaultTableModel) this.getModel())
					.getCheckedRowIndexes();
			int[] indexes = new int[values.size()];
			for (int i = 0; i < values.size(); i++)
				indexes[i] = values.get(i).intValue();
			return indexes;
		} else {
			return super.getSelectedRows();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Object[][] data = {
				{ "sddsdsgd gfgfd ", "dfdgf hgghfhg hghg" },
				{
						"<a href=\"http://google.com\">fshjfsdjghdljkgh  fddddddddddddddddddddd dddd klfjhkldjhlkfdhigjhlfgjhlgfhjg",
						"22" }, { "111", "222" } };
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

	public HeaderItemListener(Cab2bDefaultTableModel tableModel) {
		m_tableModel = tableModel;
	}

	/**
	 * Called when a CheckBox is checked/unchecked
	 */
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getSource();
		if (false == source instanceof AbstractButton)
			return;
		if (e.getStateChange() == ItemEvent.SELECTED) {
			m_tableModel.selectAllRows();
		} else {
			m_tableModel.unSelectAllRows();
		}

	}
}
