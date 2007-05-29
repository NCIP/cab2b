package edu.wustl.cab2b.client.ui.util;

/**
 * This class is used to wrap the user object.
 * 
 * @author Rahul Ner
 *
 */
public class UserObjectWrapper {

    /**
     * 
     */
    private String displayName;

    /**
     * 
     */
    private Object userObject;

    public UserObjectWrapper(Object userObject, String displayName) {
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
    public Object getUserObject() {
        return userObject;
    }

    /**
     * @param userObject The userObject to set.
     */
    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }
}
