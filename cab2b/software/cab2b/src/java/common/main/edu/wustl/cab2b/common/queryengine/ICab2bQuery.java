/**
 *
 */
package edu.wustl.cab2b.common.queryengine;

import java.io.Serializable;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;

/**
 * @author chetan_patil
 *
 */
public interface ICab2bQuery extends IParameterizedQuery, Serializable {
    enum QUERY_TYPE {
        ANDed("ANDed"), ORed("ORed");

        private String type;

        private QUERY_TYPE(String type) {
            this.type = type;
        }

        public String toString() {
            return type;
        }
    };

    // TODO need to be generalized for multiple outputs
    // TODO urls for intermodel categories
    /**
     * @return list of output URLs
     */
    List<String> getOutputUrls();

    /**
     * @param url
     */
    void setOutputUrls(List<String> url);

    /**
     * @return output entity
     */
    EntityInterface getOutputEntity();

    /**
     * @param outputEntity
     */
    void setOutputEntity(EntityInterface outputEntity);

    /**
     * This method returns true if the query is marked for keyword search; false otherwise.
     * @return
     */
    Boolean isKeywordSearch();

    /**
     * This method marks whether query is for keyword search or not.
     * @param isKeywordSearch true if marked for keyword search; false if not.
     */
    void setIsKeywordSearch(Boolean isKeywordSearch);
}
