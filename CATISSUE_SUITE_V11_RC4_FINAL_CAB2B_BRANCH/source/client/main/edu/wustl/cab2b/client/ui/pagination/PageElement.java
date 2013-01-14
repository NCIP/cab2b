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

	public String getDescription();
	
	public void setDescription(String description);
	
	public String getDisplayName();
	
	public void setDisplayName(String displayName);
	
	public String getImageLocation();
	
	public void setImageLocation(String imageLocation);
	
	public String getLinkURL();
	
	public void setLinkURL(String linkURL);
	
	public Object getUserObject();
	
	public void setUserObject(Object userObject);
	
	public boolean isSelected();
	
	public void setSelected(boolean value);
	
	public void setExtraDisplayText(String extraDisplayText);
	
	public String getExtraDisplayText();
}
