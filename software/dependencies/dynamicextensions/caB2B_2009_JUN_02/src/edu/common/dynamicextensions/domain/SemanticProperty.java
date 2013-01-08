/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;

/**
 * For every abstract metadata object semantic properties are associated.
 * This Class represents the Semantic Properties of a Metadata.
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.class table="DYEXTN_SEMANTIC_PROPERTY"
 * @hibernate.cache  usage="read-write"
 */
public class SemanticProperty extends DynamicExtensionBaseDomainObject implements SemanticPropertyInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = -1955066885211283279L;

	/**
	 * The concept code.
	 */
	protected String conceptCode;

	/**
	 * Term
	 */
	protected String term;

	/**
	 * Thesauras Name
	 */
	protected String thesaurasName;
	
	/**
	 * 
	 */
	protected String conceptDefinition;
	
	/**
	 * 
	 */
	protected int sequenceNumber = 0;
	

	/**
	 * Empty Constructor.
	 */
	public SemanticProperty()
	{
	}

	/**
	 * This method returns the Unique identifier.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_SEMANTIC_PROPERTY_SEQ"
	 * @return the Unique identifier.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * This method returns the concept code.
	 * @hibernate.property name="conceptCode" type="string" column="CONCEPT_CODE" 
	 * @return the concept code.
	 */
	public String getConceptCode()
	{
		return conceptCode;
	}

	/**
	 * This method sets the concept code.
	 * @param conceptCode the concept code to be set.
	 */
	public void setConceptCode(String conceptCode)
	{
		this.conceptCode = conceptCode;
	}

	/**
	 * This method returns the tem i.e. concept name. 
	 * @hibernate.property name="term" type="string" column="TERM" 
	 * @return Returns the term i.e. concept name
	 */
	public String getTerm()
	{
		return term;
	}

	/**
	 * This method sets the term.
	 * @param term the term to be set.
	 */
	public void setTerm(String term)
	{
		this.term = term;
	}

	/**
	 * This method returns the thesaurus name.
	 * @hibernate.property name="thesaurasName" type="string" column="THESAURAS_NAME" 
	 * @return the thesaurus name.
	 */
	public String getThesaurasName()
	{
		return thesaurasName;
	}

	/**
	 * This method sets the thesauras name.
	 * @param thesaurasName the thesauras name to be set.
	 */
	public void setThesaurasName(String thesaurasName)
	{
		this.thesaurasName = thesaurasName;
	}

	
	/**
	 * @hibernate.property name="sequenceNumber" type="int" column="SEQUENCE_NUMBER" 
	 * @return sequence number
	 */
	public int getSequenceNumber()
	{
		return sequenceNumber;
	}

	
	/**
	 * @param sequenceNumber sequence number
	 */
	public void setSequenceNumber(int sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}

	/** 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object object)
	{
		SemanticProperty semanticProperty = (SemanticProperty) object;
		Integer thisSequenceNumber = this.sequenceNumber;
		Integer otherSequenceNumber = semanticProperty.getSequenceNumber();
		return thisSequenceNumber.compareTo(otherSequenceNumber);
	}

	
	/**
	 * This method returns the tem i.e. concept name. 
	 * @hibernate.property name="conceptDefinition" type="string" column="CONCEPT_DEFINITION" 
	 * @return the conceptDefinition
	 */
	public String getConceptDefinition()
	{
		return conceptDefinition;
	}

	
	/**
	 * @param conceptDefinition the conceptDefinition to set
	 */
	public void setConceptDefinition(String conceptDefinition)
	{
		this.conceptDefinition = conceptDefinition;
	}

}