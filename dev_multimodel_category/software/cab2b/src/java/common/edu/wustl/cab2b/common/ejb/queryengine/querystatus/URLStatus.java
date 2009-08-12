package edu.wustl.cab2b.common.ejb.queryengine.querystatus;

/**
 * @author chetan_pundhir
 *
 */
public class URLStatus {

    private long queryStatusId;

    private long queryId;

    private long urlId;

    private char status;

    private long resultCount;

    private String message;

    private String description;
    
    private String url;

    /**
     * @return long
     */
    public long getId() {
        return queryStatusId;
    }

    /**
     * @return long
     */
    public long getUrlId() {
        return urlId;
    }

    /**
     * @return long
     */
    public long getQueryId() {
        return queryId;
    }

    /**
     * @param queryId
     */
    public void setQueryId(long queryId) {
        this.queryId = queryId;
    }

    /**
     * @param urlId
     */
    public void setUrlId(long urlId) {
        this.urlId = urlId;
    }

    /**
     * @return char
     */
    public char getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(char status) {
        this.status = status;
    }

    /**
     * @return long
     */
    public long getResultCount() {
        return resultCount;
    }

    /**
     * @param resultCount
     */
    public void setResultCount(long resultCount) {
        this.resultCount = resultCount;
    }

    /**
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return String
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param details
     */
    public void setDescription(String details) {
        this.description = details;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
}