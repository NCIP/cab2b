package edu.wustl.cab2b.admin.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.wustl.cab2b.admin.util.AttributePair.MatchFactor;
import edu.wustl.cab2b.server.path.InterModelConnectionBizLogic;
import edu.wustl.cab2b.server.path.PathFinder;

/**
 * Collection of methods to support creation of new connections across models
 * given a pair of classes.
 * 
 * @author srinath_k
 */
public class InterModelConnectionsUtil {
	/**
	 * Saves the specified connection and its mirror to the database.
	 * 
	 * @param attributePair
	 *            the connection to save.
	 * @throws IllegalArgumentException
	 *             if the specified connection already exists.
	 */
	public static void saveInterModelConnection(AttributePair attributePair) {
		if (isInterModelConnectionExist(attributePair)) {
			throw new IllegalArgumentException(
					"Specified intermodel connection already exists.");
		}
		new InterModelConnectionBizLogic().saveInterModelConnection(
				attributePair.getAttribute1(), attributePair.getAttribute2());
	}

	/**
	 * @param attributePair
	 * @return true if the specified connection already exists.
	 */
	public static boolean isInterModelConnectionExist(
			AttributePair attributePair) {
		return PathFinder.getInstance().doesInterModelConnectionExist(
				attributePair.getAttribute1(), attributePair.getAttribute2());
	}

	/**
	 * Determines eligible connections across specified pair of classes.
	 * <p>
	 * The restrictions on what is meant by
	 * "eligible connections between two classes from different models" are
	 * gradually reduced. Each successive step in the following is reached only
	 * if no results are found in the previous step.
	 * <ol>
	 * <li>if there exist attribute-pairs with exact publicID match, those are
	 * returned.</li>
	 * <li>if there exist attribute-pairs such that concept codes of the
	 * attributes and the classes match, then those are returned.</li>
	 * <li>if there exist attribute-pairs such that concept codes of the
	 * attributes match, then those attributes are returned.</li>
	 * <li>an empty set is returned</li>
	 * </ol>
	 * 
	 * @param class1
	 * @param class2
	 * @return the (possibly empty) set of eligible connections across the
	 *         specified classes; in each of the returned <tt>AttributePair</tt>
	 *         's, <tt>attribute1</tt> belongs to <tt>class1</tt> and
	 *         <tt>attribute2</tt> belongs to <tt>class2</tt>.
	 * @throws IllegalArgumentException
	 *             if the two classes belong to same model i.e. are from the
	 *             same entity group.
	 */
	public static Set<AttributePair> determineConnections(
			EntityInterface class1, EntityInterface class2) {
		if (entityGroup(class1).equals(entityGroup(class2))) {
			throw new IllegalArgumentException(
					"The two classes are from same model.");
		}
		Set<AttributePair> step1Res = new HashSet<AttributePair>();
		Set<AttributePair> step2Res = new HashSet<AttributePair>();
		Set<AttributePair> step3Res = new HashSet<AttributePair>();

		boolean classesConceptsMatch = areAllConceptCodesMatch(class1
				.getOrderedSemanticPropertyCollection(), class2
				.getOrderedSemanticPropertyCollection());
		for (AttributeInterface attr1 : class1.getAttributeCollection()) {
			for (AttributeInterface attr2 : class2.getAttributeCollection()) {
				AttributePair imc = new AttributePair(attr1, attr2);
				if (isInterModelConnectionExist(imc)) {
					continue;
				}

				if (attr1.getPublicId().equals(attr2.getPublicId())) {
					imc.setMatchFactor(MatchFactor.PUBLIC_ID);
					step1Res.add(imc);
					continue;
				}

				if (!step1Res.isEmpty()) {
					continue;
				}

				boolean attributesConceptsMatch = areAllConceptCodesMatch(attr1
						.getOrderedSemanticPropertyCollection(), attr2
						.getOrderedSemanticPropertyCollection());
				if (attributesConceptsMatch) {
					if (classesConceptsMatch) {
						imc.setMatchFactor(MatchFactor.CLASS_CONCEPT_CODE);
						step2Res.add(imc);
					} else {
						imc.setMatchFactor(MatchFactor.ATTRIBUTE_CONCEPT_CODE);
						step3Res.add(imc);
					}
				}
			}
		}

		if (!step1Res.isEmpty()) {
			return step1Res;
		}

		if (!step2Res.isEmpty()) {
			return step2Res;
		}

		return step3Res;
	}

	/**
	 * Checks whether all the concept codes from source are matching to that of
	 * destination in order
	 * 
	 * @param collectionSrc
	 *            Source side SemanticProperty collection
	 * @param collectionDes
	 *            Destination side SemanticProperty collection
	 * @return TRUE if match is found.
	 */
	private static boolean areAllConceptCodesMatch(
			Collection<SemanticPropertyInterface> collectionSrc,
			Collection<SemanticPropertyInterface> collectionDes) {
		return !collectionSrc.isEmpty() && collectionSrc.equals(collectionDes);
	}

	private static EntityGroupInterface entityGroup(EntityInterface entity) {
		return entity.getEntityGroupCollection().iterator().next();
	}
}