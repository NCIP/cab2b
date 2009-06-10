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
	 * Action Command
	 */
	private String actionCommand;
	
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
	
	
	/**
	 * Default Constructor 
	 */
	public PageElementImpl()
	{
		this("","");
	}

	/**
	 * Instantiates with display name
	 * @param displayName
	 */
	public PageElementImpl(String displayName)
	{
		this(displayName, displayName);
		
	}
	
	/**
	 * Instantiates with displayName and actionCommand
	 * @param displayName
	 * @param actionCommand
	 */
	public PageElementImpl(String displayName, String actionCommand)
    {
        this.displayName = displayName;
        this.actionCommand = actionCommand;
    }
	
	/**
	 * Returns description.
	 * @return 
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets Description.
	 * @param description 
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets display name.
	 * @return 
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Sets display name.
	 * @param displayName 
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * Returns image location.
	 * @return 
	 */
	public String getImageLocation() {
		return imageLocation;
	}
	
	/**
	 * Sets image location.
	 * @param imageLocation 
	 */
	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}
	
	/**
	 * Rteturns url.
	 * @return 
	 */
	public String getLinkURL() {
		return linkURL;
	}
	
	/**
	 * Sets url.
	 * @param linkURL 
	 */
	public void setLinkURL(String linkURL) {
		this.linkURL = linkURL;
	}
	
	/**
	 * Returns user object.
	 * @return 
	 */
	public Object getUserObject() {
		return userObject;
	}
	
	/**
	 * Sets user object.
	 * @param userObject 
	 */
	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	/**
	 * Overriding {@link Object#toString()}}
	 * @return 
	 * 
	 */
	public String toString()
	{
		return displayName;
	}

	
	/* (non-Javadoc)
	 * @see edu.wustl.cab2b.client.ui.pagination.PageElement#isSelected()
	 */
	/**
	 * Returns whether this is selected or not
	 * @return
	 */
	public boolean isSelected()
	{
		return selected;
	}

	
	/* (non-Javadoc)
	 * @see edu.wustl.cab2b.client.ui.pagination.PageElement#setSelected(boolean)
	 */
	/**
	 * Sets boolean for selection.
	 * @param selected
	 */
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

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.pagination.PageElement#getActionCommand()
     */
	/**
	 * Returns action command
	 * @return
	 */
    public String getActionCommand() {
        return actionCommand;
    }

    /**
     * @param actionCommand
     */
    public void setActionCommand(String actionCommand) {
        this.actionCommand = actionCommand;
    }
}