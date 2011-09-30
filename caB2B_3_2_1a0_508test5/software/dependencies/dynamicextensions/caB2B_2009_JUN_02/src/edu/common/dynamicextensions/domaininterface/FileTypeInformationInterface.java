
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

import edu.common.dynamicextensions.domain.FileExtension;

/**
 * 
 * @author Rahul Ner
 */
public interface FileTypeInformationInterface extends AttributeTypeInformationInterface
{

	/**
	 * @return max file size
	 */
	Float getMaxFileSize();

	/**
	 * sets max file size
	 * @param maxFileSize
	 */
	void setMaxFileSize(Float maxFileSize);
	
	
	/**
	 * @param fileExtension
	 */
	void addFileExtension(FileExtension fileExtension);
	

	/**
	 * @param fileExtension
	 */
	void removeFileExtension(FileExtension fileExtension);
	
	
	/**
	 * 
	 */
	void removeAllFileExtensions();
	
	/**
	 * @return
	 */
	Collection<FileExtension> getFileExtensionCollection();
}
