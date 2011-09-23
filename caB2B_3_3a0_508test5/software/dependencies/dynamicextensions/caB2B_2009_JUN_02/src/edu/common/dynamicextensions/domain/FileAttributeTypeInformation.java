
package edu.common.dynamicextensions.domain;

import java.util.Collection;
import java.util.HashSet;

import edu.common.dynamicextensions.domaininterface.FileTypeInformationInterface;
import edu.common.dynamicextensions.entitymanager.EntityManagerConstantsInterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:06 PM
 * @hibernate.joined-subclass table="DYEXTN_FILE_TYPE_INFO" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class FileAttributeTypeInformation extends AttributeTypeInformation
		implements
			FileTypeInformationInterface
{

	/**
	 * maximum file size (in MB)
	 */
	Float maxFileSize;

	/**
	 * allowed file types for this attribute
	 */
	Collection<FileExtension> fileExtensionCollection = new HashSet<FileExtension>();

	/**
	 * Empty Constructor.
	 */
	public FileAttributeTypeInformation()
	{

	}

	/**
	 * @hibernate.set name="fileExtensionCollection" table="DYEXTN_FILE_EXTENSIONS"
	 * cascade="all-delete-orphan" inverse="false" lazy="false"
	 * @hibernate.collection-key column="ATTRIBUTE_ID"
	 * @hibernate.cache  usage="read-write"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.FileExtension" 
	 * @return Returns the fileExtensionCollection.
	 */

	public Collection<FileExtension> getFileExtensionCollection()
	{
		return fileExtensionCollection;
	}

	/**
	 * @param fileExtensionCollection The fileExtensionCollection to set.
	 */
	public void setFileExtensionCollection(Collection<FileExtension> fileExtensionCollection)
	{
		this.fileExtensionCollection = fileExtensionCollection;
	}

	/**
	 * @return Returns the maxFileSize.
	 * @hibernate.property name="maxFileSize" column="MAX_FILE_SIZE" type="float"
	 */

	public Float getMaxFileSize()
	{
		return maxFileSize;
	}

	/**
	 * @param maxFileSize The maxFileSize to set.
	 */
	public void setMaxFileSize(Float maxFileSize)
	{
		this.maxFileSize = maxFileSize;
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.FileTypeInformationInterface#addFileExtension(edu.common.dynamicextensions.domain.FileExtension)
	 */
	public void addFileExtension(FileExtension fileExtension)
	{
		if (this.fileExtensionCollection == null)
		{
			this.fileExtensionCollection = new HashSet<FileExtension>();
		}
		this.fileExtensionCollection.add(fileExtension);
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.FileTypeInformationInterface#removeFileExtension(edu.common.dynamicextensions.domain.FileExtension)
	 */
	public void removeFileExtension(FileExtension fileExtension)
	{
		if (this.fileExtensionCollection != null)
		{
			this.fileExtensionCollection.remove(fileExtension);
		}

	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.FileTypeInformationInterface#removeAllFileExtensions()
	 */
	public void removeAllFileExtensions()
	{
		if (this.fileExtensionCollection != null)
		{
			this.fileExtensionCollection.clear();
		}
	}
	
	/** 
	 * @see edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface#getDataType()
	 */
	public String getDataType()
	{
		
		return EntityManagerConstantsInterface.FILE_ATTRIBUTE_TYPE;
	}
}