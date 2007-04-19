package edu.wustl.cab2b.common.queryengine;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.queryobject.impl.Query;

public class Cab2bQuery extends Query implements ICab2bQuery {

    private static final long serialVersionUID = -3676549385071170949L;

    private List<String> outputClassUrls;

    private EntityInterface outputEntity;

    /**
     * @return the outputClassUrls.
     */
    public List<String> getOutputUrls() {
        return outputClassUrls;
    }

    /**
     * @param outputClassUrls
     *            the outputClassUrls to set.
     */
    public void setOutputUrls(List<String> outputClassUrls) {
        this.outputClassUrls = outputClassUrls;
    }

    public EntityInterface getOutputEntity() {
        return outputEntity;
    }

    public void setOutputEntity(EntityInterface outputEntity) {
        this.outputEntity = outputEntity;
    }

}
