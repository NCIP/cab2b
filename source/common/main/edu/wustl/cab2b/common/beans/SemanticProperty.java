/**
 * <p>Title: SemanticProperty class>
 * <p>Description:	SemanticProperty extends the SemanticProperty class of dynamic extension.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */
package edu.wustl.cab2b.common.beans;

import java.io.Serializable;
import edu.wustl.cab2b.common.util.Utility;

/**
 * SemanticProperty extends the SemanticProperty class of dynamic extension.
 * 
 * @author gautam_shetty
 */
public class SemanticProperty extends edu.common.dynamicextensions.domain.SemanticProperty implements
		ISemanticProperty, Serializable {
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Default constructor.
	 */
	public SemanticProperty() {
		setConceptCode(null);
		setTerm(null);
		setThesaurasName(null);
	}

	public boolean newEquals(Object obj) {
		boolean matchStatus = false;
		if (conceptCode != null && obj instanceof SemanticProperty) {
			// TODO this null check is bcoz caTissue
			// has some permissible values without
			// concept codes. This will never be the case with models from cDSR
			SemanticProperty patternSemanticProperty = (SemanticProperty) obj;
			if (patternSemanticProperty.conceptCode != null) {
				String patternConceptCode = "*" + patternSemanticProperty.conceptCode + "*";
				matchStatus = Utility.compareRegEx(patternConceptCode, conceptCode);
			}
		}
		return matchStatus;
	}
}
