/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.common.multimodelcategory.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.common.querysuite.metadata.path.IPath;

/**
 * @author chetan_patil
 *
 */
public class MultiModelCategoryBean implements Serializable {

    private static final long serialVersionUID = -8886703360963442705L;

    private String name;

    private String description;

    private ModelGroupInterface applicationGroup;

    private Collection<IPath> paths;

    private Collection<MultiModelAttributeBean> multiModelAttributes;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the applicationGroup
     */
    public ModelGroupInterface getApplicationGroup() {
        return applicationGroup;
    }

    /**
     * @param applicationGroup the applicationGroup to set
     */
    public void setApplicationGroup(ModelGroupInterface applicationGroup) {
        this.applicationGroup = applicationGroup;
    }

    /**
     * @return the paths
     */
    public Collection<IPath> getPaths() {
        if (paths == null) {
            paths = new ArrayList<IPath>();
        }
        return paths;
    }

    /**
     * @param paths the paths to set
     */
    public void setPaths(Collection<IPath> paths) {
        this.paths = paths;
    }

    /**
     * This method adds the given IPath to the paths collection
     * @param path
     */
    public void addPath(IPath path) {
        getPaths().add(path);
    }

    /**
     * @return the multiModelAttributes
     */
    public Collection<MultiModelAttributeBean> getMultiModelAttributes() {
        if (multiModelAttributes == null) {
            multiModelAttributes = new ArrayList<MultiModelAttributeBean>();
        }
        return multiModelAttributes;
    }

    /**
     * @param multiModelAttributes the multiModelAttributes to set
     */
    public void setMultiModelAttributes(Collection<MultiModelAttributeBean> multiModelAttributes) {
        this.multiModelAttributes = multiModelAttributes;
    }

    /**
     * This method adds the given MultiModelAttributeBean to the MultiModelAttributeBean collection
     * @param multiModelAttributeBean
     */
    public void addMultiModelAttribute(MultiModelAttributeBean multiModelAttributeBean) {
        getMultiModelAttributes().add(multiModelAttributeBean);
    }
}
