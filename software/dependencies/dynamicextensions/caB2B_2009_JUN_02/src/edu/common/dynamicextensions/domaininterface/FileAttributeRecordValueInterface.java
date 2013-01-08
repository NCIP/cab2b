/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domaininterface;

public interface FileAttributeRecordValueInterface extends DynamicExtensionBaseDomainObjectInterface
{
	/**
	 * 
	 * @return
	 */
	public Long getId();
	
	/**
	 * @return Returns the contentType.
	 */
	public String getContentType();
	
	/**
	 * @param contentType The contentType to set.
	 */
	public void setContentType(String contentType);
	
	/**
	 * @return Returns the fileContent.
	 */
	public byte[] getFileContent();
	
	/**
	 * @param fileContent The fileContent to set.
	 */
	public void setFileContent(byte[] fileContent);
	
	/**
	 * @return Returns the fileName.
	 */
	public String getFileName();
		
	/**
	 * @param fileName The fileName to set.
	 */
	public void setFileName(String fileName);
	
	
}
