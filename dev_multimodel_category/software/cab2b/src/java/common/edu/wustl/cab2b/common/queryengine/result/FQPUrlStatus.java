/**
 * 
 */
package edu.wustl.cab2b.common.queryengine.result;

import java.io.Serializable;

import edu.wustl.cab2b.common.queryengine.querystatus.AbstractStatus;

/**
 * Class describing failed target urls for query. 
 * @author deepak_shingan
 */
public class FQPUrlStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    private String targetUrl;

    private String message;

    private String description;

    private String status = AbstractStatus.Processing;

    public FQPUrlStatus(String targetUrl, String errorMessage, String description) {
        this.targetUrl = targetUrl;
        this.message = errorMessage;
        this.description = description;
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
    public String getMessage() {
        return message;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
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

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
