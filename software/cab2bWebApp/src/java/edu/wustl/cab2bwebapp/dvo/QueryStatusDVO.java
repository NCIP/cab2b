/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.dvo;

import java.util.Date;
import java.util.List;

/**
 * @author chetan_pundhir
 *
 */
public class QueryStatusDVO {

    private Long id;

    private String title;

    private String type;

    private String status;

    private long resultCount;

    private Date executedOn;

    private List<QueryConditionDVO> conditions;

    private String fileName;

    private List<ServiceInstanceDVO> serviceInstances;

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
     * @return List<QueryConditionDVO>
     */
    public List<QueryConditionDVO> getConditions() {
        return conditions;
    }

    /**
     * @param conditions
     */
    public void setConditions(List<QueryConditionDVO> conditions) {
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

    /**
     * @return List<ServiceInstanceDVO>
     */
    public List<ServiceInstanceDVO> getServiceInstances() {
        return serviceInstances;
    }

    /**
     * @param serviceInstances
     */
    public void setServiceInstances(List<ServiceInstanceDVO> serviceInstances) {
        this.serviceInstances = serviceInstances;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

}