/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.entitymanager;

public class AttributeInformationObject implements NameInformationInterface {
	
	/**
	 * 
	 */
	String name;
	
	/**
	 * 
	 */
	Long identifier;
	

	public AttributeInformationObject(String name, Long identifier) {
		this.name = name;
		this.identifier = identifier;
	}


	public Long getIdentifier() {
		return identifier;
	}


	public void setIdentifier(Long identifier) {
		this.identifier = identifier;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

}
