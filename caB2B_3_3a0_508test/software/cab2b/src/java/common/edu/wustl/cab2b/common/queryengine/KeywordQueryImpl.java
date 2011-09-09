package edu.wustl.cab2b.common.queryengine;

import java.util.Date;

import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;

public class KeywordQueryImpl extends CompoundQueryImpl implements KeywordQuery {
    private static final long serialVersionUID = 6588432626831499139L;

    private ModelGroupInterface applicationGroup;

    /**
     * Default Constructor
     */
    public KeywordQueryImpl() {
        super();
    }

    /**
     * @param query
     */
    public KeywordQueryImpl(ICab2bQuery query) {
        super(query);
    }

    /**
     * @param id
     * @param name
     * @param description
     * @param createdDate
     */
    public KeywordQueryImpl(Long id, String name, String description, Date createdDate) {
        super(id, name, description, createdDate);
    }

    @Override
    public ModelGroupInterface getApplicationGroup() {
        return applicationGroup;
    }

    @Override
    public void setApplicationGroup(ModelGroupInterface applicationGroup) {
        this.applicationGroup = applicationGroup;
    }

}
