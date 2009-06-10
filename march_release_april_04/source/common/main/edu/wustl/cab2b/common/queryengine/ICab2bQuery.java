package edu.wustl.cab2b.common.queryengine;

import java.util.List;

import edu.wustl.common.querysuite.queryobject.IQuery;

public interface ICab2bQuery extends IQuery {
    // TODO need to be generalized for multiple outputs
    List<String> getOutputClassUrls();

    void setOutputClassUrls(List<String> url);
}
