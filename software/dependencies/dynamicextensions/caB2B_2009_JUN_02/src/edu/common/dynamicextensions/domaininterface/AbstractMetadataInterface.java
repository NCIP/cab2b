/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;
import java.util.Date;

import edu.common.dynamicextensions.domain.SemanticAnnotatableInterface;

/**
 * This is an interface extended by EntityInterface,EntityGroupInterface,AttributeInterface.This interface contains
 * basic information needed for each metadata objects. 
 * @author sujay_narkar
 *
 */
public interface AbstractMetadataInterface extends SemanticAnnotatableInterface
{

	/**
	 * This method returns the Created Date of the AbstractMetadata.
	 * @return the createdDate of the AbstractMetadata.
	 */
	Date getCreatedDate();

	/**
	 * This method sets the Created Date of the AbstractMetadata.
	 * @param createdDate The createdDate to set.
	 */
	void setCreatedDate(Date createdDate);

	/**
	 * This method returns the description of the AbstractMetadata.
	 * @return the description of the AbstractMetadata.
	 */
	String getDescription();

	/**
	 * This method sets the description of the AbstractMetadata.
	 * @param description The description to set.
	 */
	void setDescription(String description);

	/**
	 * This method returns the unique identifier of the AbstractMetadata.
	 * @return the identifier of the AbstractMetadata.
	 */
	Long getId();

	/**
	 * This method sets the unique identifier of the AbstractMetadata.
	 * @param id The identifier to set.
	 */
	void setId(Long id);

	/**
	 * The last updated date of metadata object.
	 * @return Returns the lastUpdated.
	 */
	Date getLastUpdated();

	/**
	 * The method sets the date of last updation of the meta data to the given date.
	 * @param lastUpdated the date to be set as last updation date.
	 */
	void setLastUpdated(Date lastUpdated);

	/**
	 * This method returns the name of the AbstractMetadata.
	 * @return the name of the AbstractMetadata.
	 */
	String getName();

	/**
	 * This method sets the name of the AbstractMetadata to the given name.
	 * @param name the name to be set.
	 */
	void setName(String name);

	/**
	 * 
	 * @return
	 */
	Collection<TaggedValueInterface> getTaggedValueCollection();

	/**
	 * Setter method for taggedValueCollection
	 * @param taggedValueCollection Collection of tagged values.
	 */
	void setTaggedValueCollection(Collection<TaggedValueInterface> taggedValueCollection);

	/**
	 * 
	 * @param taggedValueInterface
	 */
	void addTaggedValue(TaggedValueInterface taggedValueInterface);
	
	/**
	 * @return the publicId
	 */
	public String getPublicId();
		
	/**
	 * This method stores public id of metadata object 
	 * @param publicId the publicId to set
	 */
	public void setPublicId(String publicId);
	
}
