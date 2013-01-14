/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.dvo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lalit_chand
 */

public class AnnotationDVO {

    String resourceLogoURl;

    String resourceName;

    String resourceDescription;

    String resourceId;

    List<AnnotationElementDVO> list = new ArrayList<AnnotationElementDVO>();

    public String getResourceLogoURl() {
        return resourceLogoURl;
    }

    public void setResourceLogoURl(String resourceLogoURl) {
        this.resourceLogoURl = resourceLogoURl;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public List<AnnotationElementDVO> getList() {
        return list;
    }

    public void setList(List<AnnotationElementDVO> dvo) {
        this.list = dvo;
    }

    public String getResourceDescription() {
        return resourceDescription;
    }

    public void setResourceDescription(String resourceDescription) {
        this.resourceDescription = resourceDescription;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
