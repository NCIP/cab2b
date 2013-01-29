/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.dvo;

/**
 * @author chetan_pundhir
 *
 */
public class ServiceInstanceDVO {
    private String name;

    private String status;

    private long resultCount;

    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
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
     * @param long
     */
    public void setResultCount(long resultCount) {
        this.resultCount = resultCount;
    }
}