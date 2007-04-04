/**
 * <p>Title: AdvancedSearch Class>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Hrishikesh Rajpathak
 * @version 1.4
 */
package edu.wustl.cab2b.server.advancedsearch;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.wustl.cab2b.common.beans.Attribute;
import edu.wustl.cab2b.common.beans.Entity;
import edu.wustl.cab2b.common.beans.IAttribute;
import edu.wustl.cab2b.common.beans.IStringPermissibleValue;
import edu.wustl.cab2b.common.beans.StringPermissibleValue;
import edu.wustl.cab2b.common.beans.IEntity;
import edu.wustl.cab2b.common.beans.ISemanticProperty;
import edu.wustl.cab2b.common.beans.MatchedClass;
import edu.wustl.cab2b.common.beans.SemanticProperty;
import edu.wustl.cab2b.common.entityCache.IEntityCache;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.util.Constants;

/**
 * @author hrishikesh_rajpathak
 * @author gautam_shetty
 */
public class AdvancedSearch implements IAdvancedSearch {
	/**
	 * List of all Entity objects.
	 */
	private static IEntityCache entityCache;

	/**
	 * Default constructor.
	 * 
	 */
	public AdvancedSearch() {
	}

	/**
	 * Constructor initializing the EntityCache.
	 * 
	 * @param entityCache
	 *            The EntityCache object.
	 */
	public AdvancedSearch(IEntityCache entityCache) {
		AdvancedSearch.entityCache = entityCache;
	}

	/**
	 * Searches the array of search strings on the search targets specified.
	 * 
	 * @param searchTarget
	 *            The search targets to search on.
	 * @param searchString
	 *            The search strings.
	 * @param basedOn
	 *            The based on fields.
	 * @return The Matched class object containing all the entities found in the search.
	 * @throws RemoteException
	 */
	public MatchedClass search(int[] searchTarget, String[] searchString, int basedOn) throws CheckedException {
		if (searchTarget == null) {
			throw new CheckedException("Search target cannot be null");
		}
		if (searchString == null) {
			throw new CheckedException("Search string cannot be null");
		}
		MatchedClass resultMatchedClass = new MatchedClass();
		MatchedClass matchedClass = null;
		matchedClass = searchSplit(searchTarget, searchString, basedOn);
		if (matchedClass.getEntityCollection().isEmpty() == false) {
			resultMatchedClass.getEntityCollection().addAll(matchedClass.getEntityCollection());
		}
		if (matchedClass.getMatchedAttributeCollection().isEmpty() == false) {
			resultMatchedClass.getMatchedAttributeCollection()
					.addAll(matchedClass.getMatchedAttributeCollection());
		}
		return resultMatchedClass;
	}

	/**
	 * Searches the array of strings passed on the target based on the TEXT/CONCEPT CODE parameter. Returns the
	 * MatchedClass instance containing the matched entities in the search. Splits the search on the basis of
	 * "basedOn" parameter.
	 * 
	 * @param searchTarget
	 *            The target on which the search is to be performed.
	 * @param searchString
	 *            The string to be searched.
	 * @param basedOn
	 *            The search to be based on.
	 * @return the MatchedClass instance containing the matched entities in the search.
	 * @throws RemoteException
	 */
	public MatchedClass searchSplit(int[] searchTarget, String[] searchString, int basedOn)
			throws CheckedException {
		MatchedClass matchedClass = null;
		switch (basedOn) {
		case Constants.BASED_ON_TEXT:
			matchedClass = searchText(searchString, searchTarget);
			break;
		case Constants.BASED_ON_CONCEPT_CODE:
			matchedClass = searchConceptCode(searchString, searchTarget);
			break;
		default:
			throw new CheckedException("Search target does not exist : " + searchTarget);
		}
		return matchedClass;
	}

	/**
	 * Searches the TEXT for array of strings passed. Returns the MatchedClass instance containing the matched
	 * entities in the search. Splits the search on the basis of "searchTarget" parameter.
	 * 
	 * @param searchTarget
	 *            The target on which the search is to be performed.
	 * @param searchString
	 *            The string to be searched.
	 * @return the MatchedClass instance containing the matched entities in the search.
	 * @throws RemoteException
	 */
	private MatchedClass searchText(String[] searchString, int[] searchTarget) throws CheckedException {
		MatchedClass matchedClass = null;
		MatchedClass matchedClassForClasses = null;
		Collection entityCollection = null;
		Collection attributeCollection = null;
		MatchedClass resultantMatchedClass = new MatchedClass();
		for (int i = 0; i < searchTarget.length; i++) {
			switch (searchTarget[i]) {
			case Constants.CLASS:
				entityCollection = createSearchEntity(searchString);
				matchedClass = entityCache.getEntityOnEntityParameters(entityCollection);
				resultantMatchedClass = createResultClass(resultantMatchedClass, matchedClass);
				matchedClassForClasses = entityCache.getCategories(entityCollection);
				resultantMatchedClass = createResultClass(resultantMatchedClass, matchedClassForClasses);
				break;
			case Constants.CLASS_WITH_DESCRIPTION:
				entityCollection = createSearchEntityWithDesc(searchString);
				matchedClass = entityCache.getEntityOnEntityParameters(entityCollection);
				resultantMatchedClass = createResultClass(resultantMatchedClass, matchedClass);
				matchedClassForClasses = entityCache.getCategories(entityCollection);
				resultantMatchedClass = createResultClass(resultantMatchedClass, matchedClassForClasses);
				break;
			case Constants.ATTRIBUTE:
				attributeCollection = createSearchAttribute(searchString);
				matchedClass = entityCache.getEntityOnAttributeParameters(attributeCollection);
				resultantMatchedClass = createResultClass(resultantMatchedClass, matchedClass);
				matchedClassForClasses = entityCache.getCategoriesAttributes(attributeCollection);
				resultantMatchedClass = createResultClass(resultantMatchedClass, matchedClassForClasses);
				break;
			case Constants.ATTRIBUTE_WITH_DESCRIPTION:
				attributeCollection = createSearchAttributeWithDesc(searchString);
				matchedClass = entityCache.getEntityOnAttributeParameters(attributeCollection);
				resultantMatchedClass = createResultClass(resultantMatchedClass, matchedClass);
				matchedClassForClasses = entityCache.getCategoriesAttributes(attributeCollection);
				resultantMatchedClass = createResultClass(resultantMatchedClass, matchedClassForClasses);
				break;
			case Constants.PV:
				Collection PVCollection = createSearchPermissibleValue(searchString);
				matchedClass = entityCache.getEntityOnPermissibleValueParameters(PVCollection);
				resultantMatchedClass = createResultClass(resultantMatchedClass, matchedClass);
				break;
			default:
				throw new CheckedException("Search target does not exist : " + searchTarget[i]);
			}
		}
		return resultantMatchedClass;
	}

	/**
	 * Collects the results from individual searches and adds it to the resultClass to show all the results
	 * together
	 * 
	 * @param resultClass
	 *            Instance of MatchedClass to which all the results are added.
	 * @param matchClass
	 *            Instance of MatchedClass which holds the results of current execution.
	 * @return the MatchedClass instance containing the matched entities in the search.
	 */
	private MatchedClass createResultClass(MatchedClass resultClass, MatchedClass matchClass) {
		if (matchClass.getEntityCollection().isEmpty() == false) {
			resultClass.getEntityCollection().addAll(matchClass.getEntityCollection());
		}
		if (matchClass.getMatchedAttributeCollection().isEmpty() == false) {
			resultClass.getMatchedAttributeCollection().addAll(matchClass.getMatchedAttributeCollection());
		}
		return resultClass;
	}

	/**
	 * Creates and returns the Entity instance with entity name set as each search string entered for search.
	 * 
	 * @param searchString
	 *            the search string.
	 * @return the Entity instance which is to be searched.
	 */
	private Collection createSearchEntity(String[] searchString) throws CheckedException {
		Collection<IEntity> entityCollection = new HashSet<IEntity>();
		for (int i = 0; i < searchString.length; i++) {
			IEntity entity = getEntity(searchString[i], null);
			entityCollection.add(entity);
		}
		return entityCollection;
	}

	/**
	 * Creates and returns the Entity instance with entity name and entity description set as each search string
	 * entered for search.
	 * 
	 * @param searchString
	 *            the search string.
	 * @return the Entity instance which is to be searched.
	 */
	private Collection createSearchEntityWithDesc(String[] searchString) throws CheckedException {
		Collection<IEntity> entityCollection = new HashSet<IEntity>();
		for (int i = 0; i < searchString.length; i++) {
			IEntity entity = getEntity(searchString[i], searchString[i]);
			entityCollection.add(entity);
		}
		return entityCollection;
	}

	/**
	 * Adds name and description to the entity
	 * 
	 * @param name
	 *            the name of entity string.
	 * @param desc
	 *            the description of entity string.
	 * @return the Entity instance which is to be searched.
	 */
	private IEntity getEntity(String name, String desc) {
		IEntity entity = new Entity();
		entity.setName(name);
		entity.setDescription(desc);
		return entity;
	}

	/**
	 * Creates and returns the Attribute instance with Attribute name set as each search string entered for search.
	 * 
	 * @param searchString
	 *            the search string.
	 * @return the Entity instance which is to be searched.
	 */
	private Collection createSearchAttribute(String[] searchString) throws CheckedException {
		Collection<IAttribute> attributeCollection = new HashSet<IAttribute>();
		for (int i = 0; i < searchString.length; i++) {
			IAttribute attribute = getAttribute(searchString[i], null);
			attributeCollection.add(attribute);
		}
		return attributeCollection;
	}

	/**
	 * Creates and returns the Attribute instance with Attribute name and Attribute description set as each search
	 * string entered for search.
	 * 
	 * @param searchString
	 *            the search string.
	 * @return the Entity instance which is to be searched.
	 */
	private Collection createSearchAttributeWithDesc(String[] searchString) throws CheckedException {
		Collection<IAttribute> attributeCollection = new HashSet<IAttribute>();
		for (int i = 0; i < searchString.length; i++) {
			IAttribute attribute = getAttribute(searchString[i], searchString[i]);
			attributeCollection.add(attribute);
		}
		return attributeCollection;
	}

	/**
	 * Adds name and description to the attribute
	 * 
	 * @param name
	 *            the name of attribute string.
	 * @param name
	 *            the description of attribute string.
	 * @return the Entity instance which is to be searched.
	 */
	private IAttribute getAttribute(String name, String desc) {
		IAttribute attrib = createAttribute();
		attrib.setName(name);
		attrib.setDescription(desc);
		return attrib;
	}

	/**
	 * Creates and returns the Attribute instance with Permissible Value of an attribute set as each search string
	 * entered for search.
	 * 
	 * @param searchString
	 *            the search string.
	 * @return the Entity instance which is to be searched.
	 */
	private Collection createSearchPermissibleValue(String[] searchString) throws CheckedException {
		Collection<IStringPermissibleValue> pVCollection = new HashSet<IStringPermissibleValue>();
		for (int i = 0; i < searchString.length; i++) {
			StringPermissibleValue pV = new StringPermissibleValue();
			pV.setValue("*" + searchString[i] + "*");
			pVCollection.add(pV);
		}
		return pVCollection;
	}

	/**
	 * Searches the CONCEPT CODE for array of strings passed. Returns the MatchedClass instance containing the
	 * matched entities in the search. Splits the search on the basis of "searchTarget" parameter.
	 * 
	 * @param searchTarget
	 *            The target on which the search is to be performed.
	 * @param searchString
	 *            The string to be searched.
	 * @return the MatchedClass instance containing the matched entities in the search.
	 * @throws RemoteException
	 */
	private MatchedClass searchConceptCode(String[] searchString, int[] searchTarget) throws CheckedException {
		MatchedClass matchedClass = null;
		Collection entityCollection = null;
		Collection attributeCollection = null;
		MatchedClass resultantMatchedClass = new MatchedClass();
		for (int i = 0; i < searchTarget.length; i++) {
			switch (searchTarget[i]) {
			case Constants.CLASS:
				entityCollection = createSearchEntityConceptCode(searchString);
				matchedClass = entityCache.getEntityOnEntityParameters(entityCollection);
				resultantMatchedClass = createResultClass(resultantMatchedClass, matchedClass);
				break;
			case Constants.ATTRIBUTE:
				attributeCollection = createSearchAttributeConceptCode(searchString);
				matchedClass = entityCache.getEntityOnAttributeParameters(attributeCollection);
				resultantMatchedClass = createResultClass(resultantMatchedClass, matchedClass);
				break;
			case Constants.PV:
				Collection PVCollection = createSearchPermissibleValueConceptCode(searchString);
				matchedClass = entityCache.getEntityOnPermissibleValueParameters(PVCollection);
				resultantMatchedClass = createResultClass(resultantMatchedClass, matchedClass);
				break;
			default:
				throw new CheckedException("Search target does not exist : " + searchTarget);
			}
		}
		return resultantMatchedClass;
	}

	/**
	 * Creates and returns the Entity instance collection with concept code set as each search string entered for
	 * search.
	 * 
	 * @param searchString
	 *            the search string.
	 * @return the Entity instance which is to be searched.
	 */
	private Collection createSearchEntityConceptCode(String[] searchString) throws CheckedException {
		Collection<IEntity> entityCollection = new HashSet<IEntity>();
		for (int i = 0; i < searchString.length; i++) {
			IEntity entity = new Entity();
			ISemanticProperty semanticProperty = new SemanticProperty();
			semanticProperty.setConceptCode(searchString[i]);
			Set<ISemanticProperty> semanticPropCollection = new HashSet<ISemanticProperty>();
			semanticPropCollection.add(semanticProperty);
			entity.setSemanticPropertyCollection(semanticPropCollection);
			entityCollection.add(entity);
		}
		return entityCollection;
	}

	/**
	 * Creates and returns the Attibute instance collection with concept code set as each search string entered for
	 * search.
	 * 
	 * @param searchString
	 *            the search string.
	 * @return the Entity instance which is to be searched.
	 */
	private Collection createSearchAttributeConceptCode(String[] searchString) throws CheckedException {
		Collection<IAttribute> attributeCollection = new HashSet<IAttribute>();
		for (int i = 0; i < searchString.length; i++) {
			IAttribute attribute = createAttribute();
			ISemanticProperty semanticProperty = new SemanticProperty();
			semanticProperty.setConceptCode(searchString[i]);
			Collection<ISemanticProperty> semanticPropCollection = new HashSet<ISemanticProperty>();
			semanticPropCollection.add(semanticProperty);
			attribute.setSemanticPropertyCollection(semanticPropCollection);
			attributeCollection.add(attribute);
		}
		return attributeCollection;
	}

	/**
	 * Creates and returns the Permissible value instance collection with concept code set as each search string
	 * entered for search.
	 * 
	 * @param searchString
	 *            the search string.
	 * @return the collection of StringPermissibleValue instances which is to be searched.
	 */
	private Collection createSearchPermissibleValueConceptCode(String[] searchString) throws CheckedException {
		Collection<IStringPermissibleValue> pVCollection = new HashSet<IStringPermissibleValue>();
		for (int i = 0; i < searchString.length; i++) {
			StringPermissibleValue pV = new StringPermissibleValue();
			ISemanticProperty semanticProperty = new SemanticProperty();
			semanticProperty.setConceptCode(searchString[i]);
			Collection<ISemanticProperty> semanticPropCollection = new HashSet<ISemanticProperty>();
			semanticPropCollection.add(semanticProperty);
			pV.setSemanticPropertyCollection(semanticPropCollection);
			pVCollection.add(pV);
		}
		return pVCollection;
	}

	/**
	 * Creates an empty attribute
	 * 
	 * @return the Attribute instance which is to be searched.
	 */
	private IAttribute createAttribute() {
		AttributeTypeInformationInterface attributeTypeInformation = DomainObjectFactory.getInstance()
				.createStringAttributeTypeInformation();
		IAttribute attribute = new Attribute();
		attribute.setAttributeTypeInformation(attributeTypeInformation);
		return attribute;
	}
}
