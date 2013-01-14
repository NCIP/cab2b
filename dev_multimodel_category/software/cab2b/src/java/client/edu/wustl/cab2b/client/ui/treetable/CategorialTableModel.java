/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.treetable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;

public class CategorialTableModel extends DefaultTableModel {
    private Vector<String> attributeNameColumnList = null;

    private Map<String, Object> columnValueMap = null;

    private Vector<Vector> newDataVector = null;

    public CategorialTableModel(ICategorialClassRecord record) {
        super();
        initData(record);
    }

    /**
     *  
     * @param record
     */
    private void initData(ICategorialClassRecord record) {
        attributeNameColumnList = new Vector<String>();
        columnValueMap = new HashMap<String, Object>();
        newDataVector = new Vector<Vector>();
        getAllAttributes(record);
        this.setDataVector(newDataVector, attributeNameColumnList);

    }

    /**
     * This method creates a vector object(Row) which contains the column values 
     * @return vector
     */
    private Vector getRowVector() {
        int size = attributeNameColumnList.size();
        Vector rowDataVector = new Vector(attributeNameColumnList.size());
        for (int i = 0; i < size; i++) {
            Object value = "";
            String attributeName = null;
            attributeName = attributeNameColumnList.get(i);
            if (columnValueMap.containsKey(attributeName)) {
                value = columnValueMap.get(attributeName);
            }
            rowDataVector.add(value);
        }
        return rowDataVector;
    }

    /**
     * This method retrives all the sub categorialclassrecords and their corresponding attribute values
     * 
     * @param record
     */
    private void getAllAttributes(ICategorialClassRecord record) {
        String attributeName = null;
        boolean isLeafCategoryClassRecord = true;
        Set<AttributeInterface> attributeList = record.getAttributes();
        for (AttributeInterface attribute : attributeList) {
            attributeName = attribute.getName();
            if (!attributeNameColumnList.contains(attributeName)) {
                attributeNameColumnList.add(attributeName);
            }
            Object attributeValue = record.getValueForAttribute(attribute);
            columnValueMap.put(attributeName, attributeValue);
        }
        Map<CategorialClass, List<ICategorialClassRecord>> childrenCategorialClassRecord = record.getChildrenCategorialClassRecords();
        if (childrenCategorialClassRecord.size() > 0) {
            isLeafCategoryClassRecord = false;
        }
        for (CategorialClass categorialClass : childrenCategorialClassRecord.keySet()) {
            List<ICategorialClassRecord> categorialClassRecordList = childrenCategorialClassRecord.get(categorialClass);
            for (ICategorialClassRecord categorialClassRecord : categorialClassRecordList) {
                getAllAttributes(categorialClassRecord);
            }
        }
        if (isLeafCategoryClassRecord) {
            newDataVector.add(getRowVector());
        }
        for (AttributeInterface attribute : attributeList) {
            attributeName = attribute.getName();
            columnValueMap.remove(attributeName);
        }

        return;
    }

}
