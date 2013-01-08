/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.ui.interfaces;

/**
 * 
 * @author deepti_shelar
 *
 */
 public interface EntityUIBeanInterface
 {
	/**
	 * @return Returns the name.
	 */
	 String getFormName();

	/**
	 * @param formName The name to set to the form.
	 */
	 void setFormName(String formName);

	/**
	 * @return Returns the description.
	 */
	 String getFormDescription();

	/**
	 * @param description The description to set.
	 */
	 void setFormDescription(String description);

	/**
	 * 
	 * @return String CreateAs
	 */
	 String getCreateAs();

	/**
	 * @param createAs The createAs to set.
	 */
	 void setCreateAs(String createAs);

	/**
	 * @return String conceptCode .
	 */
	 String getConceptCode();

	/**
	 * @param conceptCode The ConceptCode to set.
	 */
	 void setConceptCode(String conceptCode);
	 
	 /**
	  * 
	  * @return
	  */
	 String getIsAbstract();
	
	 /**
	  * 
	  * @param isAbstract
	  */
	 void setIsAbstract(String isAbstract);
}
