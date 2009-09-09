package edu.wustl.cab2b.client.ui.util;

/**
 * This class is used to wrap the user object.
 * 
 * @author Rahul Ner
 *
 */
public class UserObjectWrapper<E extends Object> {

    /**
     * 
     */
    private String displayName;

    /**
     * 
     */
    private E userObject;

    public UserObjectWrapper(E userObject, String displayName) {
        this.displayName = displayName;
        this.userObject = userObject;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return displayName;
    }

    /**
     * @return Returns the displayName.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName The displayName to set.
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return Returns the userObject.
     */
    public E getUserObject() {
        return userObject;
    }

    /**
     * @param userObject The userObject to set.
     */
    public void setUserObject(E userObject) {
        this.userObject = userObject;
    }
}
