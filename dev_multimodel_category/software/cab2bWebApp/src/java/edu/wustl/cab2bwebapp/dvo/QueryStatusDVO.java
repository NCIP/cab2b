package edu.wustl.cab2bwebapp.dvo;

import java.util.Date;

/**
 * @author chetan_pundhir
 *
 */
public class QueryStatusDVO {
    private String title = null;
    
    private String type = null;

    private String status = null;

    long resultCount;
    
    int failedHostingInstitutions; 

    Date executedOn = null;
    
    String queryConditions = null;
    
    String resultsFilePath= null;

    /**
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return String
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
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
     * @return Date
     */    
    public Date getExecutedOn() {
        return executedOn;
    }
    
    /**
     * @param executedOn
     */
    public void setExecutedOn(Date executedOn) {
        this.executedOn = executedOn;
    }
    
    /**
     * @return String
     */
    public String getType() {
        return type;
    }
    
    /**
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return int
     */    
    public int getFailedHostingInstitutions() {
        return failedHostingInstitutions;
    }

    /**
     * @param failedHostingInstitutions
     */    
    public void setFailedHostingInstitutions(int failedHostingInstitutions) {
        this.failedHostingInstitutions = failedHostingInstitutions;
    }

    /**
     * @return String
     */    
    public String getQueryConditions() {
        return queryConditions;
    }

    /**
     * @param failedHostingInstitutions
     */     
    public void setQueryConditions(String queryConditions) {
        this.queryConditions = queryConditions;
    }

    /**
     * @return String
     */     
    public String getResultsFilePath() {
        return resultsFilePath;
    }

    /**
     * @param resultsFilePath
     */    
    public void setResultsFilePath(String resultsFilePath) {
        this.resultsFilePath = resultsFilePath;
    }    
}