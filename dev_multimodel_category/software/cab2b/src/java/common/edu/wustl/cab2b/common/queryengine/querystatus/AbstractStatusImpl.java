package edu.wustl.cab2b.common.queryengine.querystatus;

import java.io.Serializable;

public class AbstractStatusImpl implements Serializable {

    private static final long serialVersionUID = 1L;

    //TODO SET THE MESSAGES

    /*public static final String Waiting_To_Begin = "Waiting To Begin";

    public static final String Processing = "Processing";

    public static final String Complete = "Complete";

    public static final String Complete_With_Error = "Complete With Error";*/

    /** This is the Identifier for Query Status and URL Status */
    private Long id;

    /** This is the status which will be implemented by Query and URL */
    private String status;

    /** This is the count of entire Query and per URL also */
    private Integer resultCount;

    /** This message will be implemented b Query and URL accordingly */
    private String message;

    /** The description will be customized for Query and URL */
    private String description;

    /**
     * Gets the Id
     * @return the id
     * @hibernate.id name="id" column="ID" type="long" length="30" 
     * generator-class="increment" unsaved-value="null" 
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the status depending upon the caller
     * @return the status
     * @hibernate.property name="status" column="STATUS" type="string" length="100" not-null="true"
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns the result count of Query or per URL
     * @return the resultCount
     * @hibernate.property name="resultCount" column="RESULT_COUNT" type="integer" not-null="true"
     */
    public Integer getResultCount() {
        return resultCount;
    }

    /**
     * Sets the result count of Query or per URL
     * @param resultCount the resultCount to set
     */
    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    /**
     * Returns the message of Query or URL
     * @return the message
     * @hibernate.property name="message" column="MESSAGE" type="string" length="255" not-null="true"
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message of Query or URL
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the description of Query or URL
     * @return the description
     * @hibernate.property name="description" column="DESCRIPTION" type="string" length="255" not-null="true"
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of Query or URL
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
