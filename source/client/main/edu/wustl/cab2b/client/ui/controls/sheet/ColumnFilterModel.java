package edu.wustl.cab2b.client.ui.controls.sheet;

/*
 * ColumnFilterModel.java
 *
 * Created on October 22, 2007, 11:08 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.table.TableModel;

import edu.wustl.cab2b.client.ui.controls.slider.BiSlider;

/**
 * 
 * Important NOTE: 
 * 1)   All attributes are bounded properties.
 * 2)   As a  workaround to immensly badly designed component - BiSlider (Range Selector), this model needs to keep 
 *      a refernce to an actual instance of this visual component. Because, this BiSlide do has any model associated to 
 *      that may contain set input values, and selected max & min bound. Moreover, in constructoe is forced to provide the 
 *      set range values.
 * 
 *T refers to the column type which is associated to this column.
 * @param T This Model only works on values that are natrually comparable.
 * @author jasbir_sachdeva
 */
public class ColumnFilterModel<T extends Comparable> {

    public static final String PROPERTY_RANGE_FILTER_CHANGED = "PROPERTY_RANGE_FILTER_CHANGED";

    public static final String PROPERTY_ENUM_LIST_FILTER_CHANGED = "PROPERTY_ENUM_LIST_FILTER_CHANGED";

    public static final String PROPERTY_PATTERN_FILTER_CHANGED = "PROPERTY_PATTERN_FILTER_CHANGED";

    public static final String PROPERTY_NO_FILTER_APPLIED = "PROPERTY_NO_FILTER_APPLIED";

    /**     Active filter should be any of the following...*/
    public static final String FILTER_TYPE_NONE = "No";

    public static final String FILTER_TYPE_PATTERN = "Pattern";

    public static final String FILTER_TYPE_ENUMERATION = "List";

    public static final String FILTER_TYPE_RANGE = "Range";

    /**     Specifies which kind of filter is active.   */
    private String activeFilterType = FILTER_TYPE_NONE;

    private PropertyChangeSupport pcs;

    /**     Name of  the associated Column.   */
    /** Values that may be used by Range Selection Slider   */
    private TreeSet<T> sampleSortedValues = new TreeSet();

    /** Optional Sorter for sample values...*/
    private Comparator valuesSorter;

    /**     Values used by Range filter to determine, if any cell value for this column qualifies filter criteria. */
    private T minBound;

    private T maxBound;

    /**     Keeps refernce to active Pattern Filter, if any.    */
    public String pattern;

    private int[] userSelectedItems;

    private boolean isRangeFilterActive = false;

    /** Reference to actual BiSlider Visual Component. It reference is created whenever, possible allowed values are set.
     NOTE: there is no provision o set min & max bounds, and set of possible values when instance of this compoenent is created.
     Therefore, design of this class takes care of this limitation of one way communication. Because, it is only possible
     to get min & max bounds.*/
    private BiSlider bsRanger;

    /** Samples values are also required in the form of LIst for use with components like JList */
    private ArrayList sampleSortedValuesAL;

    /** Keeps user selected Item in hash for fast filtering */
    private HashSet userEnumeratedValues;

    /** Creates a new instance of ColumnFilterModel */
    public ColumnFilterModel() {
        pcs = new PropertyChangeSupport(this);
    }

    public String getActiveFilterType() {
        return activeFilterType;
    }

    protected void setRangerBounds(T minBound, T maxBound) {
        this.minBound = minBound;
        this.maxBound = maxBound;
        isRangeFilterActive = (null != minBound || null != maxBound);
    }

    public void setRangeFilterBounds(T minBound, T maxBound) {
        setRangerBounds(minBound, maxBound);
        activeFilterType = FILTER_TYPE_RANGE;
        pcs.firePropertyChange(ColumnFilterModel.PROPERTY_RANGE_FILTER_CHANGED, 0, 1);
    }

    public JComponent getNewRangerComponent() {
        if (null == sampleSortedValues) {
            throw new IllegalStateException("Technical Limitation: Cannot create an instance of "
                    + "Range-Selector (BiSlider) if setPossibleValues(...) is NOT set. ");
        }

        if (minBound == null || maxBound == null) {
            bsRanger = new BiSlider();
        } else {
            bsRanger = new BiSlider(new Vector(sampleSortedValues), minBound, maxBound);
        }
        Common.setBackgroundWhite(bsRanger);

        return bsRanger;
    }

    public JComponent getRangerComponent() {
        if (null == sampleSortedValues) {
            throw new IllegalStateException("Technical Limitation: Cannot create an instance of "
                    + "Range-Selector (BiSlider) if setPossibleValues(...) is NOT set. ");
        }

        if (null == bsRanger) {
            getNewRangerComponent();
        }
        return bsRanger;
    }

    public void setFilterValues(int[] selectedItemsIndex) {
        userSelectedItems = selectedItemsIndex;
        activeFilterType = FILTER_TYPE_ENUMERATION;
        pcs.firePropertyChange(ColumnFilterModel.PROPERTY_ENUM_LIST_FILTER_CHANGED, null, selectedItemsIndex);
        userEnumeratedValues = new HashSet(selectedItemsIndex.length);
        for (int idx : selectedItemsIndex) {
            userEnumeratedValues.add(sampleSortedValuesAL.get(idx));
        }
    }

    /**
     * @return  set of Enumerated values allowed by filter, if this FILTER_TYPE is active.
     */
    public int[] getFilterValues() {
        return userSelectedItems;
    }

    /**     Checks if provided value is in Enumerated List
     * @param value 
     *  Value that should be searched in Enumerated List.
     * @return 
     * true if found. false otherwise.
     */
    public boolean includeFilterValue(Comparable value) {
        return userEnumeratedValues.contains(value);
    }

    public void setPatternFilter(String newPattern) {
        pattern = newPattern.toLowerCase();
        activeFilterType = FILTER_TYPE_PATTERN;
        pcs.firePropertyChange(ColumnFilterModel.PROPERTY_PATTERN_FILTER_CHANGED, null, pattern);
        //        pcs.firePropertyChange(ColumnFilterModel.PROPERTY_PATTERN_FILTER_CHANGED, null, pattern);
    }

    /** Returns an instance of PatternFilterModel, that is associated to this Column.   
     * Lazy creation of PatternFilterModel insatnce is employed.
     * @return Applicable patern filter, if FILTER_TYPE is set to pattern.
     */
    public String getPatternFilter() {
        return pattern;
    }

    public void setNoFilter() {
        activeFilterType = FILTER_TYPE_NONE;
        pcs.firePropertyChange(ColumnFilterModel.PROPERTY_NO_FILTER_APPLIED, 0, 1);
    }

    /** Return set of sample values used in enumeration/list filter and also in BiSlider.
     * @return 
     */
    public TreeSet<T> getSampleSortedValuesSet() {
        return new TreeSet(sampleSortedValues);
    }

    /** Return set of sample values used in enumeration/list filter and also in BiSlider.
     * @return 
     */
    public ArrayList<T> getSampleSortedValues() {
        return sampleSortedValuesAL;
    }

    /** Maximun known value for this column...
     * @return 
     */
    public T getMaxValue() {
        return sampleSortedValues.last();
    }

    /** Minimum known value for this column...
     * @return 
     */
    public T getMinValue() {
        return sampleSortedValues.first();
    }

    /**     Creates a collection of unique and sorted values, based on full data set.
     * If Given a set of empty values, returns a empty non-null Set.
     * @param allPossibleValues - list of all possibles that E may have.
     * @return  Sorted set made of uniques values from provided input: allPossibleValues.
     */
    public static <E> TreeSet<E> createSampleSortedValues(ArrayList<E> allPossibleValues) {
        TreeSet<E> sortedSet = new TreeSet<E>();
        if (null != allPossibleValues) {
            sortedSet.addAll(allPossibleValues);
        }
        return sortedSet;
    }

    /**  Given full data set it creates a collection of unique and sorted values.
     * If Given a set of empty values, returns a empty non-null Set.
     * Further, underlying Sorted Set is initalised to this computed set.
     * @param allPossibleValues - list of all possibles that E may have. Redendencies may be present.
     */
    public void setSampleValues(ArrayList<T> allPossibleValues) {
        this.sampleSortedValues = createSampleSortedValues(allPossibleValues);
        setSampleValues$Post();
    }

    /**  From a Given table model, i.e. full data set, it creates a collection of unique and sorted values.
     * If Given a set of empty values, returns a empty non-null Set.
     * Further, underlying Sorted Set is initalised to this computed set.
     * This is a convinience method.
     * @param tblModel Table model that is contains al the possible values.
     * @param colIndex Which column of the above table Model should be used incomputing set of unique values.
     */
    public void setSampleValues(TableModel tblModel, int colIndex) {
        this.sampleSortedValues.clear();
        for (int r = 0; r < tblModel.getRowCount(); r++) {
            T sampleVal = (T) tblModel.getValueAt(r, colIndex);
            if (null != sampleVal) //  Nulls are NOT kept as sample values...
            {
                sampleSortedValues.add(sampleVal);
            }
        }
        setSampleValues$Post();
    }

    /** Set the sample sorted values - data model.  
     * @param sampleSortedValues - list of all possibles that E may have. NO-Redendencies are present.  */
    public void setSampleValues(TreeSet<T> sampleSortedValues) {
        this.sampleSortedValues.clear();
        if (null == sampleSortedValues) {
            //  we  want empty set...
            return;
        }

        this.sampleSortedValues.addAll(sampleSortedValues);
        setSampleValues$Post();
    }

    /**     Checks inclusion of value due to existing FIlter condition.
     * @param value 
     * filter condition is evaluated on this <code>value</code>.
     * @return 
     * method returns true, if value qulifies filter condition.
     */
    public boolean includeCell(Comparable value) {
        if (FILTER_TYPE_NONE.equals(activeFilterType)) {
            //  No Filter ser for this column: Skip this column, assume filter condition passed...
            return true;
        }

        if (FILTER_TYPE_PATTERN.equals(activeFilterType)) {
            String strVal = "";
            if (null != value) {
                strVal = value.toString().toLowerCase();
            }
            //  patrial include check...
            return strVal.indexOf(pattern) >= 0;
        }

        if (FILTER_TYPE_RANGE.equals(activeFilterType)) {
            if (null == value)
                return false;

            //  null bounds means, infinity at that end...
            return (null == minBound || minBound.compareTo(value) <= 0)
                    && (null == maxBound || maxBound.compareTo(value) >= 0);
        }

        if (FILTER_TYPE_ENUMERATION.equals(activeFilterType)) {
            // enumeration check...
            return userEnumeratedValues.contains(value);
        }

        //  Reached Here, unknown filter condition, Incluide uncondiionally...
        return true;
    }

    private void setSampleValues$Post() {
        if( sampleSortedValues.size() > 0)
            setRangerBounds(sampleSortedValues.first(), sampleSortedValues.last());
        sampleSortedValuesAL = new ArrayList(sampleSortedValues);
    }

    public boolean isFilterActive() {
        return !activeFilterType.equals(FILTER_TYPE_NONE);
    }

    @Override
    public String toString() {
        return "ColumnFilterModel [ " + getFilterDescription() + " ] ";
        //        return "ColumnExtraState [ No of Values in TreeSet =" + sampleSortedValues.size() + ",  maxBound=" + maxBound + ",  minBound=" + minBound + " ]; ";
    }

    /**     get Active min Bounds for the filter.
     * @return 
     */
    public T getMinBound() {
        return minBound;
    }

    /**     get Active max Bounds for the filter.
     * @return 
     */
    public T getMaxBound() {
        return maxBound;
    }

    /**     Filter representation String . by default it is"No Filter" String.
     * @return 
     */
    public String getFilterDescription() {
        String filterDescription = activeFilterType;

        if (activeFilterType.equals(FILTER_TYPE_NONE)) {
            return filterDescription;
        }

        if (activeFilterType.equals(FILTER_TYPE_RANGE)) {
            return getRangeFilerDescription();
        }

        if (activeFilterType.equals(FILTER_TYPE_PATTERN)) {
            return filterDescription + " matching: " + pattern;
        }

        if (activeFilterType.equals(FILTER_TYPE_ENUMERATION)) {
            return filterDescription + ", " + userSelectedItems.length + " values specified";
        }

        return filterDescription;
    }

    /**     Gives user understandable version of range filter.
     * @return 
     */
    protected String getRangeFilerDescription() {
        if (null == minBound && null == minBound) {
            return "";
        }

        if (null != minBound && null != minBound) {
            return "Between " + minBound + " and " + maxBound;
        }

        if (null != minBound) {
            return "greater/eq than " + minBound;
        }

        //  the remaining case:
        //        if( null != maxBound)
        return "less/eq than " + minBound;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }
}
