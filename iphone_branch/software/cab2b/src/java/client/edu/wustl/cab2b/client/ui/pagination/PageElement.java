/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.pagination;

/**
 *
 * @author chetan_bh
 * @author atul_jawale
 */
public interface PageElement {

	/**
	 * Returns description
	 * @return 
	 */
	public String getDescription();
	
	/**
	 * Sets description
	 * @param description
	 */
	public void setDescription(String description);
	
	/**
	 * Returns display name
	 * @return
	 */
	public String getDisplayName();
	
	/**
	 * Sets display name
	 * @param displayName
	 */
	public void setDisplayName(String displayName);
	
	/**
	 * Returns image Location.
	 * @return
	 */
	public String getImageLocation();
	
	/**
	 * Sets image location
	 * @param imageLocation
	 */
	public void setImageLocation(String imageLocation);
	
	/**
	 * Returns link URL
	 * @return
	 */
	public String getLinkURL();
	
	/**
	 * Sets link URL.
	 * @param linkURL
	 */
	public void setLinkURL(String linkURL);
	
	/**
	 * Returns User object 
	 * @return
	 */
	public Object getUserObject();
	
	/**
	 * Sets user object
	 * @param userObject
	 */
	public void setUserObject(Object userObject);
	
	/**
	 * Returns whether this is selected or not
	 * @return
	 */
	public boolean isSelected();
	
	/**
	 * Sets boolean for selection.
	 * @param value
	 */
	public void setSelected(boolean value);
	
	/**
	 * Sets extra display text.
	 * @param extraDisplayText
	 */
	public void setExtraDisplayText(String extraDisplayText);
	
	/**
	 * Returns extra display text.
	 * @return
	 */
	public String getExtraDisplayText();

	/**
	 * Returns action command
	 * @return
	 */
    public String getActionCommand();
}
