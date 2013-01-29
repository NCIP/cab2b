/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain;

/**
 * @author rahul_ner
 * @hibernate.class  table="DYEXTN_FILE_EXTENSIONS"
 */
public class FileExtension extends DynamicExtensionBaseDomainObject
{

	/**
	 * This method returns the unique identifier of the AbstractMetadata.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_FILE_EXTN_SEQ"
	 * @return the identifier of the AbstractMetadata.
	 */
	public Long getId()
	{
		return this.id;
	}


	/**
	 * allowed file extension for the attribute.
	 */
	String fileExtension;


	/**
	 * @return Returns the fileExtension.
	 * @hibernate.property name="fileExtension" column="FILE_EXTENSION" type="string"
	 */
	public String getFileExtension()
	{
		return fileExtension;
	}

	/**
	 * @param fileExtension The fileExtension to set.
	 */
	public void setFileExtension(String fileExtension)
	{
		this.fileExtension = fileExtension;
	}

}
