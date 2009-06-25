/**
 * 
 */
package edu.wustl.cab2b.common.multimodelcategory.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.common.querysuite.metadata.path.IPath;

/**
 * @author chetan_patil
 *
 */
public class MultiModelCategoryBean {

    private String name;

    private String description;

    private ModelGroupInterface applicationGroup;

    private Collection<IPath> paths = new ArrayList<IPath>();

    private Collection<MultiModelAttributeBean> multiModelAttributes = new ArrayList<MultiModelAttributeBean>();

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
        return paths;
    }

    /**
     * This method returns the path present at the given index.
     * @param index position in the collection
     * @return IPath at the given index; otherwise, if no IPath is present at given index
     */
    public IPath getPath(int index) {
        IPath path = null;
        if (paths.size() > index) {
            path = ((List<IPath>) paths).get(index);
        }
        return path;
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
        paths.add(path);
    }

    /**
     * @return the multiModelAttributes
     */
    public Collection<MultiModelAttributeBean> getMultiModelAttributes() {
        return multiModelAttributes;
    }

    /**
     * 
     * @param index
     * @return
     */
    public MultiModelAttributeBean getMultiModelAttribute(int index) {
        MultiModelAttributeBean multiModelAttributebean = null;
        if (multiModelAttributes.size() > index) {
            multiModelAttributebean = ((List<MultiModelAttributeBean>) multiModelAttributes).get(index);
        }
        return multiModelAttributebean;
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
        this.multiModelAttributes.add(multiModelAttributeBean);
    }

}
