
package edu.common.dynamicextensions.domain;

import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;

/**
 * @author Rahul J Ner
 *
 */
public interface SemanticAnnotatableInterface extends DynamicExtensionBaseDomainObjectInterface
{

	/**
	 * This method returns the Collection of SemanticProperties of the AbstractMetadata.
	 * @return the Collection of SemanticProperties of the AbstractMetadata.
	 */
	Collection<SemanticPropertyInterface> getSemanticPropertyCollection();

	/**
	 * This method adds a SemanticProperty to the AbstractMetadata.
	 * @param semanticPropertyInterface A SemanticProperty to be added.
	 */
	void addSemanticProperty(SemanticPropertyInterface semanticProperty);

	/**
	 * This method removes a SemanticProperty from the AbstractMetadata.
	 * @param semanticPropertyInterface A SemanticProperty to be removed.
	 */
	void removeSemanticProperty(SemanticPropertyInterface semanticPropertyInterface);

	/**
	 * This method removes all SemanticProperties from AbstractMetadata.
	 */
	void removeAllSemanticProperties();

	/**
	 * Method returns sorted semantic properties based on the sequence number.
	 * @return
	 */
	List<SemanticPropertyInterface> getOrderedSemanticPropertyCollection();
}
