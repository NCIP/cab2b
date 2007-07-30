package edu.wustl.cab2b.client.ui.main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bComboBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.util.ClientConstants;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.util.Utility;

/**
 * An abstract class which provides the skeletal implementation of the
 * IComponent interface and defines some more abstract method like
 * getFirstComponent, getSecondComponent that needs to be implemented by the
 * subclasses like NumberTypePanel, StringTypePanel, etc.
 * 
 * @author chetan_bh
 */
public abstract class AbstractTypePanel extends Cab2bPanel implements IComponent {

    public static final int CAB2B_FORMATTED_TEXT_FIELD_COLUMN_SIZE = 9;

    /**
     * Label for displaying the attribute name.
     */
    protected Cab2bLabel m_Name;

    /**
     * ComboBox for displaying the conditions based on the data-type
     */
    protected Cab2bComboBox m_Conditions;

    /**
     * TextField for entering the alphanumeric text.
     */
    protected JComponent m_NameEdit;

    /**
     * Another TextField for entering the alphanumeric text.
     */
    protected JComponent m_OtherEdit;

    /**
     * Parsed xml file's data structure.
     */
    protected ArrayList<String> conditionList;

    /**
     * Entity representing attribute.
     */
    protected AttributeInterface attributeEntity;

    /**
     * Attribute name represented by the panel.
     */
    protected String attributeName;

    /**
     * 
     */
    protected Boolean showCondition;

    public AbstractTypePanel(
            ArrayList<String> conditionList,
            AttributeInterface attributeEntity,
            Dimension maxLabelDimension) {
        this(conditionList, attributeEntity, true, maxLabelDimension);
    }

    public AbstractTypePanel(
            ArrayList<String> conditionList,
            AttributeInterface attributeEntity,
            Boolean showCondition,
            Dimension maxLabelDimension) {
        this.setLayout(new RiverLayout(10, 0));
        this.attributeEntity = attributeEntity;
        this.attributeName = attributeEntity.getName();
        this.showCondition = showCondition;

        String formattedString = attributeEntity.getName();
        if (!Utility.isCategory(attributeEntity.getEntity())) {
            formattedString = CommonUtils.getFormattedString(formattedString);
        }

        m_Name = new Cab2bLabel(formattedString + " : ");
        m_Name.setPreferredSize(maxLabelDimension);// new Dimension(235,20)
        String toolTipText = getAttributeToolTip(attributeEntity);
        m_Name.setToolTipText(toolTipText);
        m_NameEdit = getFirstComponent();

        m_OtherEdit = getSecondComponent();
        m_OtherEdit.setEnabled(false);
        m_OtherEdit.setVisible(false);
        m_OtherEdit.setOpaque(false);

        final Border border = m_OtherEdit.getBorder();
        final EmptyBorder emptyBorder = new EmptyBorder(2, 2, 2, 2);
        m_OtherEdit.setBorder(emptyBorder);

        add(m_Name);
        //add("tab", new Cab2bLabel());

        if (showCondition) {
            setCondtionControl(conditionList, border, emptyBorder);
        }

        //	add("tab", new Cab2bLabel());
        add(m_NameEdit);
        //	add("tab", new Cab2bLabel());
        add(m_OtherEdit);

        setSize(new Dimension(300, 100));
    }

    public String getCondition() {
        return (String) m_Conditions.getSelectedItem();
    }

    public abstract JComponent getFirstComponent();

    public abstract JComponent getSecondComponent();

    public void setCondition(String str) {
        int itemCount = m_Conditions.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            if (m_Conditions.getItemAt(i).toString().compareToIgnoreCase(str) == 0) {
                m_Conditions.setSelectedIndex(i);
            }
        }
    }

    public abstract void setComponentPreference(String condition);

    public abstract void resetPanel();

    public String getAttributeName() {
        return attributeName;
    }

    private String getAttributeToolTip(AttributeInterface attribute) {
        StringBuffer tooltip = new StringBuffer();
        tooltip.append("<HTML><P>" + getWrappedDescription(attribute.getDescription()) + "</P>");

        if (attribute.getPublicId() != null) {
            tooltip.append("<P><B>Public Id : </B>" + attribute.getPublicId() + " ");
        }

        tooltip.append("<B>Concept Code : </B>");
        boolean isFirst = true;
        for (SemanticPropertyInterface semanticProperty : attribute.getSemanticPropertyCollection()) {
            String conceptCode = semanticProperty.getConceptCode();

            if (conceptCode != null) {
                if (isFirst) {
                    tooltip.append(conceptCode);
                    isFirst = false;
                } else {
                    tooltip.append(", " + conceptCode);
                }
            }
        }
        tooltip.append("</P></HTML>");

        return tooltip.toString();
    }

    /**
     * Method to wrap the text and send it accross
     * 
     * @return
     */
    private String getWrappedDescription(String text) {
        StringBuffer wrappedText = new StringBuffer();
        String currentString = null;
        int currentStart = 0;
        int offset = 75;
        int strLen = 0;
        int len = 0;

        while (currentStart < text.length() && text.length() > offset) {
            currentString = text.substring(currentStart, (currentStart + offset));
            strLen += currentString.length() + len;
            wrappedText.append(currentString);

            int index = text.indexOf(" ", (currentStart + offset));
            if (index == -1) {
                index = text.indexOf(".", (currentStart + offset));
            }
            if (index == -1) {
                index = text.indexOf(",", (currentStart + offset));
            }
            if (index != -1) {
                len = index - strLen;
                currentString = text.substring((currentStart + offset), (currentStart + offset + len));
                wrappedText.append(currentString);
                wrappedText.append("<BR>");
            } else {
                if (currentStart == 0) {
                    currentStart = offset;
                }
                wrappedText.append(text.substring(currentStart));
                return wrappedText.toString();
            }

            currentStart += offset + len;
            if ((currentStart + offset + len) > text.length()) {
                break;
            }
        }
        wrappedText.append(text.substring(currentStart));
        wrappedText.append("</P>");
        return wrappedText.toString();
    }

    private void setCondtionControl(ArrayList<String> conditionList, final Border border,
                                    final EmptyBorder emptyBorder) {
        this.conditionList = conditionList;

        /*
         * Initializing conditions can't be abstracted, since it varies from
         * string type to number to date
         */
        m_Conditions = new Cab2bComboBox();
        m_Conditions.setBorder(ClientConstants.border);
        m_Conditions.setPreferredSize(new Dimension(125, 20));

        Collections.sort(conditionList);
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (int i = 0; i < conditionList.size(); i++) {
            model.addElement(conditionList.get(i));
        }

        m_Conditions.setModel(model);
        m_Conditions.setMaximumRowCount(conditionList.size());

        m_Conditions.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                conditionListenerAction(border, emptyBorder);
            }
        });

        m_Conditions.setSelectedIndex(0);
        add(m_Conditions);
    }

    private void conditionListenerAction(final Border border, final EmptyBorder emptyBorder) {
        if (m_Conditions.getSelectedItem().equals("Between")) {
            setNameEdit(true, border);
            setOtherEdit(true, border);
        } else if ((m_Conditions.getSelectedItem().equals("Is Null"))
                || (m_Conditions.getSelectedItem().equals("Is Not Null"))) {
            setNameEdit(false, emptyBorder);
            setOtherEdit(false, emptyBorder);

            ArrayList<String> values = new ArrayList<String>();
            values.add("");
            values.add("");
            setValues(values);
        } else {
            setNameEdit(true, border);

            // If previously selected condition was 'Between' then clear the
            // second text box
            ArrayList<String> oldValues = getValues();
            if (m_OtherEdit.isEnabled() && oldValues.size() == 2) {
                ArrayList<String> values = new ArrayList<String>();
                values.add(oldValues.get(0));
                values.add("");
                setValues(values);
            } else {
                setValues(oldValues);
            }
            setOtherEdit(false, emptyBorder);
        }
        setComponentPreference(getCondition());
    }

    private void setNameEdit(boolean value, Border border) {
        m_NameEdit.setOpaque(value);
        m_NameEdit.setEnabled(value);
        m_NameEdit.setVisible(value);
        m_NameEdit.setBorder(border);
    }

    private void setOtherEdit(boolean value, Border border) {
        m_OtherEdit.setOpaque(value);
        m_OtherEdit.setEnabled(value);
        m_OtherEdit.setVisible(value);
        m_OtherEdit.setBorder(border);
    }

}
