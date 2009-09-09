package edu.wustl.cab2b.client.ui.main;

import java.util.ArrayList;

/**
 * This contain methods to get/set UI component details for every attribute type.
 * It defines API to get selected condition, corresponding values and the attribute entity it represents. 
 * It is a common interface containing these APIs, which every data type specific UI component should implement. 
 * 
 * @author Deepak Shingan
 */
public interface IComponent {

    /**
     * @return the condition item associated with the implementing component.
     */
    public String getConditionItem();

    /**
     * @return the condition value(s) associated with the implementing component.
     */
    public ArrayList<String> getValues();

    /**
     * @param str sets the condition
     */
    public void setCondition(String str);

    /**
     * @return The display name of the attribute associated with the implementing component.
     */
    public String getAttributeDisplayName();

    /**
     * @param values sets values
     */
    public void setValues(ArrayList<String> values);

}