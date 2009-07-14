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

    private static final long serialVersionUID = 1L;

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
    @Override
    public int hashCode() {
        return targetUrl.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (obj.getClass() != targetUrl.getClass())) {
            return false;
        }
        String url = (String) obj;
        return targetUrl.equals(url);
    }
}
