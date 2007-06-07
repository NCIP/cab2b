package edu.wustl.cab2b.common.queryengine.result;

import java.io.Serializable;


public class RecordId implements Serializable {
    /**
     * The "identifier" is the value for the id attribute as obtained from
     * {@link edu.wustl.cab2b.common.util.Utility#getIdAttribute(EntityInterface)}.
     */
    private String id;

    private String url;

    public RecordId(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
