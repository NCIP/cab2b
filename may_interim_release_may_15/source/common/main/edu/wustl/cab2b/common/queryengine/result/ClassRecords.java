package edu.wustl.cab2b.common.queryengine.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

public class ClassRecords implements IClassRecords {
    /**
     * 
     */
    private static final long serialVersionUID = 6884879809051274129L;

    private List<AttributeInterface> attributes;

    private Map<String, String[][]> urlToRecords;

    public ClassRecords() {
        urlToRecords = new HashMap<String, String[][]>();
        attributes = new ArrayList<AttributeInterface>();
    }

    /**
     * @param allRecords
     *            The allRecords to set.
     */
    public void putRecords(String url, String[][] allRecords) {
        urlToRecords.put(url, allRecords);
    }

    /**
     * @param attributes
     *            The attributes to set.
     */
    public void setAttributes(List<AttributeInterface> attributes) {
        this.attributes = attributes;
    }

    /**
     * @see cab2b.queryengine.result.IQueryResult#getAttributes()
     */
    public List<AttributeInterface> getAttributes() {
        return attributes;
    }

    /**
     * @see cab2b.queryengine.result.IQueryResult#getAllRecords()
     */
    public Object[][] getAllRecords(String url) {
        Object[][] res = urlToRecords.get(url);
        if (res == null) {
            res = new Object[0][0];
        }
        return res;
    }

    public Map<String, String[][]> getAllRecords() {
        return urlToRecords;
    }

    public boolean isCategoryResult() {
        return false;
    }
}