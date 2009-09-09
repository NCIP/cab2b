package edu.wustl.cab2b.server.category;

import java.util.List;

/**
 * This is private class to this package.
 * It is used to reprents the category XML's Category tag.
 * @author Chandrakant Talele
 */
public class InputCategory {
    /**
     * Name of the category
     */
    private String name;

    /**
     * Description of the category
     */
    private String description;

    /**
     * All the suncategories of this category.
     */
    private List<InputCategory> subCategories;

    /**
     * The root categorial class of this category.
     */
    private InputCategorialClass rootCategorialClass;

    /**
     * @return Returns the rootCategorialClass.
     */
    public InputCategorialClass getRootCategorialClass() {
        return rootCategorialClass;
    }

    /**
     * @param rootCategorialClass The rootCategorialClass to set.
     */
    public void setRootCategorialClass(InputCategorialClass rootCategorialClass) {
        this.rootCategorialClass = rootCategorialClass;
    }

    /**
     * @return Returns the subCategories.
     */
    public List<InputCategory> getSubCategories() {
        return subCategories;
    }

    /**
     * @param subCategories The subCategories to set.
     */
    public void setSubCategories(List<InputCategory> subCategories) {
        this.subCategories = subCategories;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}