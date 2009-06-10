package edu.wustl.cab2b.common.queryengine;

import java.util.List;

import edu.wustl.common.querysuite.queryobject.impl.Query;

public class Cab2bQuery extends Query implements ICab2bQuery {

    private static final long serialVersionUID = -3676549385071170949L;

    List<String> outputClassUrls;

    /**
     * @return the outputClassUrls.
     */
    public List<String> getOutputClassUrls() {
        return outputClassUrls;
    }

    /**
     * @param outputClassUrls
     *            the outputClassUrls to set.
     */
    public void setOutputClassUrls(List<String> outputClassUrls) {
        this.outputClassUrls = outputClassUrls;
    }

}
