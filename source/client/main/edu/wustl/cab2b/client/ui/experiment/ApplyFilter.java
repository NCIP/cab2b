package edu.wustl.cab2b.client.ui.experiment;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JLabel;

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

public class ApplyFilter {
	private ExperimentTableModel table;

	private boolean filterNotToBeApplied;

	private Map<String, Integer> indexToName = new HashMap<String, Integer>();

	private List<String> elements = new ArrayList<String>();

	private Cab2bPanel filterPanel = new Cab2bPanel();

	private static Map<String, CaB2BFilterInterface> filterMap = new HashMap<String, CaB2BFilterInterface>();

	public ApplyFilter(ExperimentTableModel myTable) {
		this.table = myTable;
		this.filterMap = filterMap;

	}

	public Cab2bPanel applyingFilter() {
		Cab2bComboBox combo = new Cab2bComboBox();
		;
		int columnCount = table.getModel().getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			String colName = table.getModel().getColumnName(i);
			combo.addItem(colName);
			indexToName.put(colName, i);
		}
		combo.setPreferredSize(null);
		combo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				if (ie.getStateChange() == ItemEvent.SELECTED) {
					filterNotToBeApplied = false;
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
						filterPopup = new EnumeratedFilterPopUp(columnName, columnIndex,
								permissibleValueCollection, (CaB2BPatternFilter) oldFilter);
					} else {
						DataType dataType = Utility.getDataType(attribute
								.getAttributeTypeInformation());

						// If the clicked column is of type String.
						if (DataType.String == dataType || DataType.Boolean == dataType) {
							filterPopup = new PatternPopup((CaB2BPatternFilter) oldFilter,
									columnName, columnIndex);
							// If the clicked column is of type int/long
						} else if (DataType.Double == dataType || DataType.Float == dataType
								|| DataType.Long == dataType || DataType.Integer == dataType) {
							int len = table.getRowCount();
							double[] columnVal = null;
							if (oldFilter == null) {

								for (int i = 0; i < len; i++) {
									String val = (String) table.getValueAt(i, columnIndex);
									if (val != null && !val.equals("")) {
										elements.add(val);
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

							filterPopup = new FilterComponent("Range Filter", columnVal,
									columnName, columnIndex, (RangeFilter) oldFilter);
						}

					}
					if (filterNotToBeApplied == false) {
						filterPopup.showInDialog();
						applyFilter();
					}

				}
			}

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
		});

		filterPanel.add(new JLabel(""));
		filterPanel.add("tab ", new Cab2bLabel("Apply Filter"));
		filterPanel.add(combo);

		return filterPanel;
	}

	/**
	 * Adding a filter to the filter map
	 */
	public static void addFilter(String columnName, CaB2BFilterInterface filter) {
		filterMap.put(columnName, filter);
	}

	public static void clearMap() {
		filterMap.clear();
	}

	public static Vector<CaB2BFilterInterface> getFilterMap() {
		Vector<CaB2BFilterInterface> vector = new Vector<CaB2BFilterInterface>();
		for (CaB2BFilterInterface filter : filterMap.values()) {
			vector.add(filter);
		}
		return vector;
	}
}
