/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.dvo;

/**
 * @author lalit_chand
 *
 */
public class AnnotationElementDVO {

    String elementId;

    String description;
    
    String resourceURL;

    String fullDescription;
    
    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceURL() {
        return resourceURL;
    }

    public void setResourceURL(String resourceURL) {
        this.resourceURL = resourceURL;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((elementId == null) ? 0 : elementId.hashCode());
        result = prime * result + ((fullDescription == null) ? 0 : fullDescription.hashCode());
        result = prime * result + ((resourceURL == null) ? 0 : resourceURL.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final AnnotationElementDVO other = (AnnotationElementDVO) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (elementId == null) {
            if (other.elementId != null)
                return false;
        } else if (!elementId.equals(other.elementId))
            return false;
        if (fullDescription == null) {
            if (other.fullDescription != null)
                return false;
        } else if (!fullDescription.equals(other.fullDescription))
            return false;
        if (resourceURL == null) {
            if (other.resourceURL != null)
                return false;
        } else if (!resourceURL.equals(other.resourceURL))
            return false;
        return true;
    }

}
