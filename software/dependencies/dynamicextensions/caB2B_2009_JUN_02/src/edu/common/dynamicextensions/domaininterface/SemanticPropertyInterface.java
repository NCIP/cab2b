/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domaininterface;

/**
 * For every abstract metadata object semantic properties are associated.
 * @author sujay_narkar
 */
public interface SemanticPropertyInterface extends DynamicExtensionBaseDomainObjectInterface, Comparable
{

	/**
	 * This method returns the Unique identifier.
	 * @return the Unique identifier.
	 */
	Long getId();

	/**
	 * This method returns the concept code.
	 * @return the concept code.
	 */
	String getConceptCode();

	/**
	 * This method sets the concept code.
	 * @param conceptCode the concept code to be set.
	 */
	void setConceptCode(String conceptCode);

	/**
	 * This method returns the tem i.e. concept name. 
	 * @return Returns the term i.e. concept name
	 */
	String getTerm();

	/**
	 * This method sets the term.
	 * @param term the term to be set.
	 */
	void setTerm(String term);

	/**
	 * This method returns the thesaurus name.
	 * @return the thesaurus name.
	 */
	String getThesaurasName();

	/**
	 * This method sets the thesauras name.
	 * @param thesaurasName the thesauras name to be set.
	 */
	void setThesaurasName(String thesaurasName);
	
	/**
	 * @return int
	 */
	int getSequenceNumber();

	/**
	 * @param sequenceNumber int
	 */
	void setSequenceNumber(int sequenceNumber);
	
	/**
	 * This method returns the tem i.e. concept name. 
	 * @return the conceptDefinition
	 */
	public String getConceptDefinition();
		
	/**
	 * @param conceptDefinition the conceptDefinition to set
	 */
	public void setConceptDefinition(String conceptDefinition);

}
