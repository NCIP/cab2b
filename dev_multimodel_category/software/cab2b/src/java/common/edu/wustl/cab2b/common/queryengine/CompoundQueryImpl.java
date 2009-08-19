/**
 * 
 */
package edu.wustl.cab2b.common.queryengine;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
 * @author chetan_patil
 *
 */
public abstract class CompoundQueryImpl extends Cab2bQuery implements CompoundQuery {
    private static final long serialVersionUID = 8692891177510236676L;

    private Collection<ICab2bQuery> subQueries = null;

    /**
     * 
     */
    public CompoundQueryImpl() {
        super();
    }

    /**
     * @param query
     */
    public CompoundQueryImpl(ICab2bQuery query) {
        super(query);
    }

    /**
     * @param id
     * @param name
     * @param description
     * @param createdDate
     */
    public CompoundQueryImpl(Long id, String name, String description, Date createdDate) {
        super(id, name, description, createdDate);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.queryengine.MultiModelCategoryQuery#getSubQueries()
     */
    public Collection<ICab2bQuery> getSubQueries() {
        if (subQueries == null) {
            subQueries = new HashSet<ICab2bQuery>();
        }
        return subQueries;
    }

    /*
     * (non-Javadoc)
     * @see edu.wustl.cab2b.common.queryengine.MultiModelCategoryQuery#addSubQueries(edu.wustl.cab2b.common.queryengine.ICab2bQuery)
     */
    public void addSubQuery(ICab2bQuery query) {
        getSubQueries().add(query);
    }

    @Override
    public void setSubQueries(Collection<ICab2bQuery> queries) {
        subQueries = queries;
    }

}
