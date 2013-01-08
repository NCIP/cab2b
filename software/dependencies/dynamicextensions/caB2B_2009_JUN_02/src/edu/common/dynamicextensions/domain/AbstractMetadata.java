/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.entitymanager.NameInformationInterface;

/**
 * This is an abstract class extended by Entity, Entity group, Attribute.
 * This class stores basic information needed for metadata objects.  
 * @version 1.0
 * @created 28-Sep-2006 12:20:06 PM
 * @hibernate.class table="DYEXTN_ABSTRACT_METADATA" 
 * @hibernate.cache  usage="read-write"
 */
public abstract class AbstractMetadata extends DynamicExtensionBaseDomainObject
		implements
			AbstractMetadataInterface, NameInformationInterface
			
{

	/**
	 * Serial Version Unique Identifief
	 */
	protected static final long serialVersionUID = 1234567890L;

	/**
	 * This method returns the unique identifier of the AbstractMetadata.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_ABSTRACT_METADATA_SEQ"
	 * @return the identifier of the AbstractMetadata.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Name of metadata object
	 */
	protected String name;

	/**
	 * Description of metadata object
	 */
	protected String description;

	/**
	 * Last updated date for metadata object
	 */
	protected Date lastUpdated;

	/**
	 * Created date for metadata object
	 */
	protected Date createdDate;
	
	/**
	 * Public Identifier from CaDSR.
	 */
	protected String publicId;

	/**
	 * Semantic property collection.
	 */
	protected Collection<TaggedValueInterface> taggedValueCollection = new HashSet<TaggedValueInterface>();
	
	/**
	 * Semantic property collection.
	 */
	protected Collection<SemanticPropertyInterface> semanticPropertyCollection = new HashSet<SemanticPropertyInterface>();

	
	

	/**
	 * Empty Constructor
	 */
	public AbstractMetadata()
	{
	}

	/**
	 * This method returns the Created Date of the AbstractMetadata.
	 * @hibernate.property name="createdDate" type="date" column="CREATED_DATE" 
	 * @return the createdDate of the AbstractMetadata.
	 */
	public Date getCreatedDate()
	{
		return createdDate;
	}

	/**
	 * This method sets the Created Date of the AbstractMetadata.
	 * @param createdDate The createdDate to set.
	 */
	public void setCreatedDate(Date createdDate)
	{
		this.createdDate = createdDate;
	}

	/**
	 * This method returns the description of the AbstractMetadata.
	 * @hibernate.property name="description" type="string" column="DESCRIPTION" length="1000"
	 * @return the description of the AbstractMetadata.
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * This method sets the description of the AbstractMetadata.
	 * @param description The description to set.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * This method returns the date of last updation of the meta data.
	 * @hibernate.property name="lastUpdated" type="date" column="LAST_UPDATED" 
	 * @return the date of last updation of the meta data.
	 */
	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	/**
	 * The method sets the date of last updation of the meta data to the given date.
	 * @param lastUpdated the date to be set as last updation date.
	 */
	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	/**
	 * This method returns the name of the AbstractMetadata.
	 * @hibernate.property name="name" type="string" column="NAME" length="1000" 
	 * @return the name of the AbstractMetadata.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * This method sets the name of the AbstractMetadata to the given name.
	 * @param name the name to be set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * This method returns the Collection of TaggedValue of the AbstractMetadata.
	 * @hibernate.set name="taggedValueCollection" cascade="all-delete-orphan"
	 * inverse="false" lazy="false"
	 * @hibernate.collection-key column="ABSTRACT_METADATA_ID"
	 * @hibernate.cache  usage="read-write"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.TaggedValue"
	 * @return the Collection of TaggedValue of the AbstractMetadata.
	 */
	public Collection<TaggedValueInterface> getTaggedValueCollection()
	{
		return taggedValueCollection;
	}

	/**
	 * Setter method for taggedValueCollection
	 * @param taggedValueCollection Collection of tagged values.
	 */
	public void setTaggedValueCollection(Collection<TaggedValueInterface> taggedValueCollection)
	{
		this.taggedValueCollection = taggedValueCollection;
	}

	/**
	 * 
	 * @param taggedValueInterface
	 */
	public void addTaggedValue(TaggedValueInterface taggedValueInterface)
	{
		if (taggedValueInterface == null)
		{
			return;
		}

		if (taggedValueCollection == null)
		{
			taggedValueCollection = new HashSet<TaggedValueInterface>();
		}
		taggedValueCollection.add(taggedValueInterface);

	}
	
	/**
	 * This method sets the semanticPropertyCollection to the given Collection of SemanticProperties.
	 * @param semanticPropertyCollection the Collection of SemanticProperties to be set.
	 */
	public void setSemanticPropertyCollection(
			Collection<SemanticPropertyInterface> semanticPropertyCollection)
	{
		this.semanticPropertyCollection = semanticPropertyCollection;
	}

	/**
	 * This method returns the Collection of SemanticProperties of the AbstractMetadata.
	 * @hibernate.set name="semanticPropertyCollection" cascade="save-update"
	 * inverse="false" lazy="false"
	 * @hibernate.collection-key column="ABSTRACT_METADATA_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.SemanticProperty"
	 * @hibernate.cache  usage="read-write"
	 * @return the Collection of SemanticProperties of the AbstractMetadata.
	 */
	public Collection<SemanticPropertyInterface> getSemanticPropertyCollection()
	{
		return semanticPropertyCollection;
	}
	/**
	 * This method adds a SemanticProperty to the AbstractMetadata.
	 * @param semanticPropertyInterface A SemanticProperty to be added.
	 */
	public void addSemanticProperty(SemanticPropertyInterface semanticPropertyInterface)
	{
		if (semanticPropertyCollection == null)
		{
			semanticPropertyCollection = new HashSet<SemanticPropertyInterface>();
		}
		semanticPropertyCollection.add(semanticPropertyInterface);
	}

	/**
	 * This method removes a SemanticProperty from the AbstractMetadata.
	 * @param semanticPropertyInterface A SemanticProperty to be removed.
	 */
	public void removeSemanticProperty(SemanticPropertyInterface semanticPropertyInterface)
	{
		if ((semanticPropertyCollection != null)
				&& (semanticPropertyCollection.contains(semanticPropertyInterface)))
		{
			semanticPropertyCollection.remove(semanticPropertyInterface);
		}
	}

	/**
	 * This method removes all SemanticProperties from AbstractMetadata.
	 */
	public void removeAllSemanticProperties()
	{
		if (semanticPropertyCollection != null)
		{
			semanticPropertyCollection.clear();
		}
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface#getOrderedSemanticPropertyCollection()
	 */
	public List<SemanticPropertyInterface> getOrderedSemanticPropertyCollection()
	{
		List<SemanticPropertyInterface> semanticPropertyList = new ArrayList<SemanticPropertyInterface>();

		if (this.semanticPropertyCollection != null && !this.semanticPropertyCollection.isEmpty())
		{
			semanticPropertyList.addAll(this.semanticPropertyCollection);
			Collections.sort(semanticPropertyList);
		}
		return semanticPropertyList;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "Name: " + name + "  Id :  " + id;
	}

	
	
	/**
	 * @hibernate.property name="publicId" type="string" column="PUBLIC_ID"  
	 * @return the publicId
	 */
	public String getPublicId()
	{
		return publicId;
	}

	
	/**
	 * @param publicId the publicId to set
	 */
	public void setPublicId(String publicId)
	{
		this.publicId = publicId;
	}
}