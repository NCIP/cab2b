package edu.wustl.cab2bwebapp.dvo;

import java.util.Date;

/**
 * @author chetan_pundhir
 *
 */
public class QueryStatusDVO {
    private String title;

    private String type;

    private String status;

    private long resultCount;

    private Date executedOn;

    private String conditions;

    private String fileName;

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
     * @return String
     */
    public String getConditions() {
        return conditions;
    }

    /**
     * @param conditions
     */
    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    /**
     * @return String
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}