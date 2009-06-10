/**
 * 
 */
package edu.wustl.cab2b.common.queryengine.result;

import java.io.Serializable;

/**
 * Class describing failed target urls for query. 
 * @author deepak_shingan
 */
public class FailedTargetURL implements Serializable {
    private String targetUrl;

    private String errorMessage;

    private String description;

    private Object targetObject;

    public FailedTargetURL(String targetUrl, String errorMessage, String description, Object targetObject) {
        this.targetUrl = targetUrl;
        this.errorMessage = errorMessage;
        this.description = description;
        this.targetObject = targetObject;
    }

    /**
     * @return the targetUrl
     */
    public String getTargetUrl() {
        return targetUrl;
    }
  
    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }
  

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the targetObject
     */
    public Object getTargetObject() {
        return targetObject;
    }
}
