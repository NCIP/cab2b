package edu.wustl.cab2b.client.ui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;

/**
 * This class wraps all the CDE related details for a entity.
 * It is essentialy a table like structure where columns are a particualr details and each row represents one 
 * attribute.
 *  
 * @author rahul_ner
 */
public class CDEDetails {

    /**
     * 
     */
    private String[] columnNames = new String[] { " Attribute Name", " Public ID", " Concept Codes", " Description" };

    /**
     * 
     */
    private EntityInterface entity;

    /**
     * 
     */
    private List<CDEDetail> cdeDetailList = new ArrayList<CDEDetail>();

    /**
     * @param entity
     */
    public CDEDetails(EntityInterface entity) {
        this.entity = entity;
        for (AttributeInterface attribute : entity.getAttributeCollection()) {
            cdeDetailList.add(new CDEDetail(attribute));
        }
    }

    /**
     * @return
     */
    public int getRowCount() {
        return cdeDetailList.size();
    }

    /**
     * @return
     */
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * @param i
     * @return
     */
    public String getColumnName(int i) {
        return columnNames[i];
    }

    /**
     * @param row
     * @param column
     * @return
     */
    public Object getValueAt(int row, int column) {
        CDEDetail detail = cdeDetailList.get(row);
        String value = "";
        switch (column) {
            case 0:
                value = detail.getAttributeInterface().getName();
                break;

            case 1:
                value = detail.getAttributeInterface().getPublicId();
                break;

            case 2:
                value = detail.getConceptCodes();
                break;
            case 3:
                value = detail.getAttributeInterface().getDescription();
                break;
        }

        return value;
    }

    /**
     * represent details for a particular attribute
     *
     */
    private class CDEDetail {

        /**
         * 
         */
        private AttributeInterface attribute;

        /**
         * 
         */
        private String conceptCodeString;

        /**
         * @param attribute
         */
        private CDEDetail(AttributeInterface attribute) {
            this.attribute = attribute;
            setConceptCode();
        }

        /**
         * @return
         */
        private AttributeInterface getAttributeInterface() {
            return attribute;
        }

        /**
         * @return
         */
        private String getConceptCodes() {
            return conceptCodeString;
        }

        /**
         * 
         */
        private void setConceptCode() {
            Collection<SemanticPropertyInterface> semanticPropertyCollection = attribute.getSemanticPropertyCollection();
            StringBuffer conceptCodeList = new StringBuffer();
            for (SemanticPropertyInterface semanticProperty : semanticPropertyCollection) {
                if (conceptCodeList.toString().equals("")) {
                    conceptCodeList.append(semanticProperty.getConceptCode());
                } else {
                    conceptCodeList.append(", ").append(semanticProperty.getConceptCode());
                }
            }
            conceptCodeString = conceptCodeList.toString();
        }
    }

}
