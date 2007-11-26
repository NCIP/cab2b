/*
 * ColumnExtraState.java
 *
 * Created on October 22, 2007, 11:08 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.wustl.cab2b.client.ui.controls.sheet;

import java.util.*;
import javax.swing.table.TableModel;

/**
 *T refers to the column type which is associated to this column.
 * @author jasbir_sachdeva
 */
class ColumnExtraState<T extends Comparable> {

    /** If Column should be shown in Display. By default it is visible.  */
    private boolean isVisible = true;
    /** Values that may be used by Range Selection Slider   */
    private TreeSet<T> sampleSortedValues = new TreeSet();
    /** Optional Sorter for sample values...*/
    private Comparator valuesSorter;
    //    private String filterDescription = "No Filter";

    int modelIdx = -1;
    /**     Values used by Range filter to determine, if any cell value for this column qualifies filter criteria. */
    private Comparable minBound;
    private Comparable maxBound;
    private boolean isRangeFilterActive = false;
    //    Object minBound;
//    Object maxBound;

    /**     Keeps refernce to active Pattern Filter, if any.    */
    DefaultPatternFilter patternFilterModel;
    
    /**     At present only two filters are present, Range and Pattern based., 
                These can be joined either on "AND" or on "OR" conjunction. */
    boolean joinFilterByAnd = true;
    
    /**     Work around to a badly designed UI Cmoponent. ( A UI component without suport for Model)    */
    Object cargoRef_1;
    private String columnNickName;
    /**     Keeps tag, if contents of this column could be edited.  */
    private boolean isReadOnlyColumn = false;

    /** Creates a new instance of ColumnExtraState */
    public ColumnExtraState() {
    }

    //
//    FACTORY ...
//

    public static ColumnExtraState getInstanceFor(Class type) {
        //        if( type.equals( String.class))
//            return new ColumnExtraState<String>();
//        if( type.equals( Integer.class))
//            return new ColumnExtraState<Integer>();
//        if( type.equals( Long.class))
//            return new ColumnExtraState<Long>();
//        if( type.equals( Date.class))
//            return new ColumnExtraState<Date>();
//        if( type.equals( Double.class))
//            return new ColumnExtraState<Double>();
//        if( type.equals( Float.class))
//            return new ColumnExtraState<Float>();
//        if( type.equals( Short.class))
//            return new ColumnExtraState<Short>();
//        if( type.equals( Character.class))
//            return new ColumnExtraState<Character>();

        return new ColumnExtraState();
    }

    //
//      SETTERS & GETTERS ...
//

    public boolean isColVisible() {
        return isVisible;
    }

    public void setColVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    /** Maximun known value for this column...  */
    public T getMaxValue() {
        return sampleSortedValues.last();
    }

    /** Minimum known value for this column...  */
    public T getMinValue() {
        return sampleSortedValues.first();
    }

    public TreeSet<T> getSampleSortedValues() {
        return new TreeSet(sampleSortedValues);
    }

    /**     Creates a collection of unique and sorted values, based on full data set.
     * If Given a set of empty values, returns a empty non-null Set.   */
    public static <E> TreeSet<E> createSampleSortedValues(ArrayList<E> allPossibleValues) {
        TreeSet<E> sortedSet = new TreeSet<E>();
        if (null != allPossibleValues) {
            sortedSet.addAll(allPossibleValues);
        }
        return sortedSet;
    }

    /**  Given full data set it creates a collection of unique and sorted values.
     * If Given a set of empty values, returns a empty non-null Set.
     * Further, underlying Sorted Set is initalised to this computed set.              */
    public void setSampleSortedValues(ArrayList<T> allPossibleValues) {
        this.sampleSortedValues = createSampleSortedValues(allPossibleValues);
    }

    /**  From a Given table model, i.e. full data set, it creates a collection of unique and sorted values.
     * If Given a set of empty values, returns a empty non-null Set.
     * Further, underlying Sorted Set is initalised to this computed set.              */
    public void setSampleSortedValues(TableModel tblModel, int colIndex) {
        this.sampleSortedValues.clear();
        for (int r = 0; r < tblModel.getRowCount(); r++) {
            T sampleVal = (T) tblModel.getValueAt(r, colIndex);
            if( null != sampleVal)
                //  Nulls are NOT kept as sample values...
                sampleSortedValues.add( sampleVal);
        }
    }

    /** Set the sample sorted values - data model.  */
    public void setSampleSortedValues(TreeSet<T> sampleSortedValues) {
        this.sampleSortedValues.clear();
        if (null == sampleSortedValues) {
            //  we  want empty set...
            return;
        }

        this.sampleSortedValues.addAll(sampleSortedValues);
    }

    /**   Setting by min and max bounds for filteration.
     *          If any one is NOT null, filter is considered to be active on this column.   */
    public void setBounds(Comparable minBound, Comparable maxBound) {
        this.minBound = minBound;
        this.maxBound = maxBound;

        isRangeFilterActive = (null != minBound || null != maxBound);
    }

//    public void setBounds( Object minBound, Object maxBound) {
//        this.minBound = minBound;
//        this.maxBound = maxBound;
//
//        isFilterActive = ( null != minBound || null != maxBound);
//    }

    public boolean isFilterActive() {
        return isRangeFilterActive || (null != patternFilterModel);
    }

//    public Comparator getValuesComparator() {
//        return valuesSorter;
//    }
//
//    public void setValuesComparator(Comparator valuesComparator) {
//        this.valuesSorter = valuesComparator;
//    }
//
//    private void sortSampleValues(ArrayList<T> sampleSortedValues) {
//        if( sampleSortedValues.size() <= 0)
//            return;
//}

    @Override
    public String toString() {
        return "ColumnExtraState [ isVisible=" + isVisible 
                +",  No of Values in TreeSet =" + sampleSortedValues.size() 
                +",  maxBound=" + maxBound + ",  minBound=" + minBound + " ]; ";
    }

    /**     get Active min Bounds for the filter.   */
    public Comparable getMinBound() {
        return minBound;
    }

    /**     get Active max Bounds for the filter.   */
    public Comparable getMaxBound() {
        return maxBound;
    }

    /**     Filter representation String . by default it is"No Filter" String.  */
    public String getFilterDescription() {
        String filterDescription = "";
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


    void setColumnNickName(String name) {
        columnNickName = name;
    }

    String getColumnNickName() {
        return columnNickName;
    }

    void setReadOnlyColumn(boolean readOnlyColumn) {
        isReadOnlyColumn = readOnlyColumn;
    }

    boolean isReadOnlyColumn() {
        return isReadOnlyColumn;
    }

    /** Returns an instance of PatternFilterModel, that is associated to this Column.   
                Lazy creation of PatternFilterModel insatnce is employed. */
    DefaultPatternFilter getPatternFilterModel(){
        if( null == patternFilterModel)
            patternFilterModel = new DefaultPatternFilter();
        return patternFilterModel;
    }
    
    boolean isPatternFilterModelExists() {
        return null != patternFilterModel;
    }
    
            
    
}
