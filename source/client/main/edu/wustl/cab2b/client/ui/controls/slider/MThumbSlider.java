package edu.wustl.cab2b.client.ui.controls.slider;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.Icon;
import javax.swing.JSlider;

/**
 * @author Hrishikesh Rajpathak
 * @author Deepak Shingan
 * @author Atul Jawale
 *
 */
public class MThumbSlider extends JSlider {
    /**
     * Number of thumbs on slider
     */
    protected int thumbNum = 2;

    /**
     * Range data model
     */
    protected BoundedRangeModel[] sliderModels;

    /**
     * thumb Icons
     */
    protected Icon[] thumbRenderers;

    /**
     * Filling color 
     */
    protected Color[] fillColors;

    /**
     * 
     */
    protected Color trackFillColor;

    /**
     * Class ID
     */
    private final static String uiClassID = "MThumbSliderUI";

    /**
     * Input data vector for slider
     */
    private Vector inputData = new Vector();

    /**
     * currentInputValueIndex
     */
    public int currentInputValueIndex;

    /**
     * Thumbslider model class
     */
    private BiSlider cab2bSliderUI;

    /**
     * Constructor
     */
    public MThumbSlider() {

    }

    /**
     * Constructor
     * @param objectInput
     * @param cab2bSliderUI
     */
    public MThumbSlider(Vector objectInput, BiSlider cab2bSliderUI) {
        this.inputData = objectInput;
        this.cab2bSliderUI = cab2bSliderUI;

        initUI();
        updateUI();
    }

    /**
     * Constructor
     * @param objectInput
     * @param slider
     * @param initialMin
     * @param initialMax
     */
    public MThumbSlider(Vector objectInput, BiSlider slider, Object initialMin, Object initialMax) {
        this.inputData = objectInput;
        this.cab2bSliderUI = slider;

        initUI(initialMin, initialMax);
        updateUI();
    }

    /**
     * Method for Initilizing UI 
     * @param initialMin
     * @param initialMax
     */
    protected void initUI(Object initialMin, Object initialMax) {
        initUI();
        this.setValueAt(inputData.indexOf(initialMin), 0);
        this.setValueAt(inputData.indexOf(initialMax), 1);

    }

    /**
     * Method for Initilizing UI
     */
    protected void initUI() {

        sliderModels = new BoundedRangeModel[2];
        thumbRenderers = new Icon[2];
        fillColors = new Color[2];
        for (int i = 0; i < 2; i++) {
            if (inputData != null || inputData.size() > 0)
                sliderModels[i] = new DefaultBoundedRangeModel(0, 0, 0, inputData.size() - 1);
            thumbRenderers[i] = null;
            fillColors[i] = null;
        }

        this.setValueAt(0, 0);
        this.setValueAt(inputData.size(), 1);
        this.setFillColorAt(Color.gray, 0);
        this.setFillColorAt(Color.yellow, 1);
        this.setTrackFillColor(Color.gray);
        this.setToolTipText("");
        this.setPaintTicks(true);
        this.setSnapToTicks(true);
        this.putClientProperty("JSlider.isFilled", Boolean.TRUE);

    }

    /**
     * Sets Object Input data 
     * @param objectInput Vector
     */
    public void setObjectInput(Vector objectInput) {
        this.inputData = objectInput;
    }

    /**
     * Updates UI
     * @see javax.swing.JSlider#updateUI()
     */
    public void updateUI() {
        AssistantUIManager.setUIName(this);
        super.updateUI();
    }

    /**
     * Gets the UI Class
     * @see javax.swing.JSlider#getUIClassID()
     * @return returns UI Class
     */
    public String getUIClassID() {
        return uiClassID;
    }

    /** 
     * Slide Thumb Number count 
     * @return int
     */
    public int getThumbNum() {
        return thumbNum;
    }

    /**
     * gets thumb value from thumb   
     * @param index
     * @return int
     */
    public int getValueAt(int index) {
        return getModelAt(index).getValue();
    }

    /**
     * Sets value "n" for thumb having index-value as index  
     * @param n
     * @param index
     */
    public void setValueAt(int n, int index) {
        getModelAt(index).setValue(n);
    }

    /**
     * Get minimum bar value from data model
     * @return Object
     */
    public Object getMinimumBarValue() {
        return inputData.get(getValueAt(0));
    }

    /**
     * Get maximum bar value from data model
     * @return
     */
    public Object getMaximumBarValue() {
        return inputData.get(getValueAt(1));
    }

    /**
     * @see javax.swing.JSlider#getMinimum()
     * @return minimum
     */
    public int getMinimum() {
        return getModelAt(0).getMinimum();
    }

    /**
     * @see javax.swing.JSlider#getMaximum()
     * @return maximum
     */
    public int getMaximum() {
        return getModelAt(0).getMaximum();
    }

    /**
     * Gets BoundedRangeModel for a given class
     * @param index
     * @return BoundedRangeModel
     */
    public BoundedRangeModel getModelAt(int index) {
        // System.out.println("model at index ="+index);
        return sliderModels[index];
    }

    /**
     * Get thumb renderer icon at index
     * @param index
     * @return Icon
     */
    public Icon getThumbRendererAt(int index) {
        return thumbRenderers[index];
    }

    /**
     * Set thumb renderer icon for index position 
     * @param icon
     * @param index
     */
    public void setThumbRendererAt(Icon icon, int index) {
        System.out.println();
        thumbRenderers[index] = icon;
    }

    /**
     * Get thumb travelled area color 
     * @param index
     * @return Color
     */
    public Color getFillColorAt(int index) {
        return fillColors[index];
    }

    /**
     * Sets thumb travelled area color
     * @param color
     * @param index
     */
    public void setFillColorAt(Color color, int index) {
        fillColors[index] = color;
    }

    /**
     * Get track fill color
     * @return Color
     */
    public Color getTrackFillColor() {
        return trackFillColor;
    }

    /**
     * Set track fill color
     * @param color
     */
    public void setTrackFillColor(Color color) {
        trackFillColor = color;
    }

    /** Gets the ToolTipText
     * @see javax.swing.JComponent#getToolTipText(java.awt.event.MouseEvent)
     * @param e MouseEvent
     * @return String which is null
     * 
     */
    public String getToolTipText(MouseEvent e) {
        return null;
    }

    /**
     * Set filter values
     * @param min
     * @param max
     */
    public void setFilterValues(Object min, Object max) {
        cab2bSliderUI.setFilterValues(min, max);
    }

    /**
     * Method to change min value on thumb slider UI  
     * @param min
     */
    public void displayMinValue(String min) {
        cab2bSliderUI.lowVal.setText(min);
    }

    /**
     * Method to change max value on thumb slider UI
     * @param max
     */
    public void displayMaxValue(String max) {
        cab2bSliderUI.hiVal.setText(max);
    }

    /**
     * Method to set min and max bar values 
     * 
     * @param minBound
     * @param maxBound
     */
    public void setRangeBounds(Comparable minBound, Comparable maxBound) {
        setValueAt(inputData.indexOf(minBound), 0);
        setValueAt(inputData.indexOf(maxBound), 1);
        if (minBound instanceof Date) {
            DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
            displayMinValue(dateFormatter.format(minBound));
            displayMaxValue(dateFormatter.format(maxBound));
        } else {
            displayMinValue((String) minBound);
            displayMaxValue((String) maxBound);
        }
    }
}
