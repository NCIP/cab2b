package edu.wustl.cab2b.client.ui.experiment;

import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.jdesktop.swingx.decorator.Filter;
import org.jdesktop.swingx.decorator.FilterPipeline;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bComboBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.filter.CaB2BFilterInterface;
import edu.wustl.cab2b.client.ui.filter.CaB2BPatternFilter;
import edu.wustl.cab2b.client.ui.filter.Cab2bFilterPopup;
import edu.wustl.cab2b.client.ui.filter.EnumeratedFilterPopUp;
import edu.wustl.cab2b.client.ui.filter.FilterComponent;
import edu.wustl.cab2b.client.ui.filter.PatternPopup;
import edu.wustl.cab2b.client.ui.filter.RangeFilter;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.queryobject.DataType;

/**
 * @author Hrishikesh Rajpathak
 * 
 * This class returns a panel whic consists of a lable and a combo-box. On click
 * of an item in a combo-box, a filter pop-up appears and filter is applied to
 * the table.
 */
public class ApplyFilterPanel extends Cab2bPanel {
	private static final long serialVersionUID = 1L;

	private ExperimentTableModel table;

	private Map<String, Integer> indexToName;

	private List<String> elements;

	private Map<String, CaB2BFilterInterface> filterMap;

	private Cab2bComboBox combo;

	public ApplyFilterPanel(ExperimentTableModel myTable) {
		this.table = myTable;
		this.setName("applyFilterPanel");

		indexToName = new HashMap<String, Integer>();
		elements = new ArrayList<String>();
		filterMap = new HashMap<String, CaB2BFilterInterface>();

		Cab2bLabel filterLabel = new Cab2bLabel("Apply Filter:   ");
		filterLabel.setFont(new Font("Arial", Font.BOLD, 13));
		this.add(filterLabel);

		Cab2bComboBox combo = applyingFilter();
		this.add(combo);
	}

	/**
	 * This method creates a combo-box with elements as ass the headers of the
	 * table.
	 * 
	 * @return Cab2bComboBox
	 */
	private Cab2bComboBox applyingFilter() {
		combo = new Cab2bComboBox();
		int columnCount = table.getModel().getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			String colName = table.getModel().getColumnName(i);
			combo.addItem(colName);
			indexToName.put(colName, i);
		}
		combo.setPreferredSize(null);
		combo.setSelectedItem(null);
		combo.addItemListener(new ComboItemListener(this));

		return combo;
	}

	/**
	 * Adding a filter to the filter map
	 */
	public void addFilter(String columnName, CaB2BFilterInterface filter) {
		filterMap.put(columnName, filter);
	}

	/**
	 * Clear all the filter in the filterMap.
	 */
	public void clearMap() {
		filterMap.clear();
	}

	/**
	 * Get the vector that contains all the filters applied currently.
	 * 
	 * @return Vector
	 */
	public Vector<CaB2BFilterInterface> getFilterMap() {
		Vector<CaB2BFilterInterface> vector = new Vector<CaB2BFilterInterface>();
		for (CaB2BFilterInterface filter : filterMap.values()) {
			vector.add(filter);
		}

		return vector;
	}

	/**
	 * This Listener class popups the corresponding filter dialogs on the
	 * selection of items in combo box.
	 */
	class ComboItemListener implements ItemListener {
		private ApplyFilterPanel applyFilterpanel;

		public ComboItemListener(ApplyFilterPanel applyFilterpanel) {
			this.applyFilterpanel = applyFilterpanel;
		}

		public void itemStateChanged(ItemEvent ie) {
			if (ie.getStateChange() == ItemEvent.SELECTED) {
				boolean filterNotToBeApplied = false;
				elements.clear();
				String columnName = ie.getItem().toString();
				int columnIndex = indexToName.get(columnName);
				CaB2BFilterInterface oldFilter = null;
				if (filterMap.containsKey(columnName)) {
					oldFilter = filterMap.get(columnName);
				}

				Cab2bFilterPopup filterPopup = null;
				AttributeInterface attribute = table.getColumnAttribute(columnName);
				if (Utility.isEnumerated(attribute)) {
					Collection<PermissibleValueInterface> permissibleValueCollection = Utility
							.getPermissibleValues(attribute);
					filterPopup = new EnumeratedFilterPopUp(applyFilterpanel,
							permissibleValueCollection, (CaB2BPatternFilter) oldFilter, columnName,
							columnIndex);
				} else {
					DataType dataType = Utility
							.getDataType(attribute.getAttributeTypeInformation());

					// If the clicked column is of type String.
					if (DataType.String == dataType || DataType.Boolean == dataType) {
						filterPopup = new PatternPopup(applyFilterpanel,
								(CaB2BPatternFilter) oldFilter, columnName, columnIndex);
						// If the clicked column is of type int/long
					} else if (DataType.Double == dataType || DataType.Float == dataType
							|| DataType.Long == dataType || DataType.Integer == dataType) {
						double[] columnVal = null;
						if (oldFilter == null) {
							for (int i = 0; i < table.getRowCount(); i++) {
								String value = (String) table.getValueAt(i, columnIndex);
								if (value != null && !value.equals("")) {
									elements.add(value);
								}
							}

							if (elements.isEmpty()) {
								filterNotToBeApplied = true;
							}
						}

						columnVal = new double[elements.size()];
						int count = 0;
						for (Object obj : elements) {
							columnVal[count] = Double.parseDouble(obj.toString());
							count++;
						}
						filterPopup = new FilterComponent("Range Filter", applyFilterpanel,
								(RangeFilter) oldFilter, columnVal, columnName, columnIndex);
					}
				}

				if (filterNotToBeApplied == false) {
					filterPopup.showInDialog();
					applyFilter();
				}
				combo.setSelectedItem(null);
			}
		}

		/**
		 * This method applies all the filters that are there in the filter map
		 * to the table
		 */
		public void applyFilter() {
			int len = filterMap.size();
			Filter[] filters = new Filter[len];
			int i = 0;
			for (CaB2BFilterInterface filter : filterMap.values()) {
				filters[i] = (Filter) filter.copy();
				i++;
			}
			table.setFilters(new FilterPipeline(filters));
		}
	}
}
