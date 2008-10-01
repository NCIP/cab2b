package edu.wustl.cab2b.client.ui.pagination;

/**
 * Implmentation class for PageElement. 
 * @author chetan_bh
 * @author atul_jawale
 */
public class PageElementImpl implements PageElement{

	/**
	 * Display name for the hyperlink.
	 */
	private String displayName;
	
	/**
	 * This text will be displayed next to display text but as a normal label & not as a hyperlink. 
	 */
	private String extraDisplayText;
	
	/**
	 * URL associated with the hyperlink.
	 */
	private String linkURL;
	
	/**
	 * User Object associated with this hyperlink.
	 */
	private Object userObject;
	
	/**
	 * Description of the page element.
	 */
	private String description;
	
	/**
	 * Image location associated with this page element.
	 */
	private String imageLocation;
	
	/**
	 * Holds selection status of this element.
	 */
	private boolean selected;
	
	
	public PageElementImpl()
	{
		this("");
	}

	public PageElementImpl(String displayName)
	{
		this.displayName = displayName;
	}
	
	/**
	 * Returns description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets Description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets display name.
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Sets display name.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * Returns image location.
	 */
	public String getImageLocation() {
		return imageLocation;
	}
	
	/**
	 * Sets image location.
	 */
	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}
	
	/**
	 * Rteturns url.
	 */
	public String getLinkURL() {
		return linkURL;
	}
	
	/**
	 * Sets url.
	 */
	public void setLinkURL(String linkURL) {
		this.linkURL = linkURL;
	}
	
	/**
	 * Returns user object.
	 */
	public Object getUserObject() {
		return userObject;
	}
	
	/**
	 * Sets user object.
	 */
	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	public String toString()
	{
		return displayName;
	}

	
	public boolean isSelected()
	{
		return selected;
	}

	
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

    /**
     * @return the extraDisplayText
     */
	public String getExtraDisplayText() {
        return extraDisplayText;
    }

    /**
     * @param extraDisplayText the extraDisplayText to set
     */
	public void setExtraDisplayText(String extraDisplayText) {
        this.extraDisplayText = extraDisplayText;
    }
	
	
}
