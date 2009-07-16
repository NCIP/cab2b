/**
 * 
 */
package edu.wustl.cab2b.common.queryengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @author chetan_patil
 *
 */
public class MultiModelCategoryQueryImpl extends Cab2bQuery implements MultiModelCategoryQuery {
    private static final long serialVersionUID = -7969272734716913783L;

    private Collection<ICab2bQuery> subQueries = new ArrayList<ICab2bQuery>();

    /**
     * Default constructor
     */
    public MultiModelCategoryQueryImpl() {
        super();
    }

    /**
     * Parameterized constructor
     * @param id
     * @param name
     * @param description
     * @param createdDate
     */
    public MultiModelCategoryQueryImpl(Long id, String name, String description, Date createdDate) {
        super(id, name, description, createdDate);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.queryengine.MultiModelCategoryQuery#getSubQueries()
     */

    public Collection<ICab2bQuery> getSubQueries() {
        return subQueries;
    }

    /*
     * (non-Javadoc)
     * @see edu.wustl.cab2b.common.queryengine.MultiModelCategoryQuery#addSubQueries(edu.wustl.cab2b.common.queryengine.ICab2bQuery)
     */
    public void addSubQueries(ICab2bQuery query) {
        subQueries.add(query);
    }

}
