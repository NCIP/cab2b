package edu.wustl.cab2bwebapp.dvo;

/**
 * @author chetan_pundhir
 *
 */
public class SavedQueryDVO {
    String name = null;
    String description = null;

    boolean selected = false;

    int resultCount = 0;

    /**
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        if (name.lastIndexOf("#") == name.length() - 1)
            name = name.substring(0, name.length() - 1);
        this.name = name;
    }


    /**
     * @return String
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param name
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * @return boolean
     */
    public boolean getSelected() {
        return selected;
    }

    /**
     * @param selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @return int
     */
    public int getResultCount() {
        return resultCount;
    }

    /**
     * @param resultCount
     */
    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }
}
