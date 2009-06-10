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

    public FailedTargetURL(String targetUrl, String errorMessage, String description) {
        this.targetUrl = targetUrl;
        this.errorMessage = errorMessage;
        this.description = description;
    }

    /**
     * @return the targetUrl
     */
    public String getTargetUrl() {
        return targetUrl;
    }

    /**
     * @param targetUrl
     *            the targetUrl to set
     */
    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage
     *            the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
