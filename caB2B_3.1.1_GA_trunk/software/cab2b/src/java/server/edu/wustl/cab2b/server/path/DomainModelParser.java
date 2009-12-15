package edu.wustl.cab2b.server.path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLGeneralization;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLAssociationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLClassCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelUmlGeneralizationCollection;

/**
 * This class parses the domain model for an application.<br>
 * It uses caGrid metadata classes it convert the XML to java object (DomainModel).
 * An instance of this class is tied to a particular DomainModel instance.
 * Additionally, this class uses the caGRID's definition of a DomainModel and their respective API(s) 
 * to return list of UMLClass and UMLAssociations. 
 * 
 * @author Chandrakant Talele
 * @author munesh
 * @version 1.0
 */
public class DomainModelParser {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(DomainModelParser.class);
    
    /**
     * The DomainModel java object of given XML file.
     */
    protected DomainModel domainModel;

    /**
     * This is map with key as ID of parent UML Class and value as list of IDs of all its children 
     */
    protected Map<String, List<String>> parentIdVsChildrenIds;

    /**
     * This constructor ties instance of this class to a single particular domain model.<br>
     * It takes the domain model object as parameter<br>
     * @param model The DomainModel Object.
     * Throws the exception occurred during getting / parsing the domain model XML as RuntimeException. 
     */
    public DomainModelParser(DomainModel model) {
        init(model);
    }

    /**
     * This constructor ties instance of this class to a single particular domain model.<br>
     * It takes the URL as parameter where the DomainModel.xml is available.<br>
     * @param xmlFilePath The URL from where to retrieve the DomainModel.xml
     *            and create a DomainModel Object.
     * Throws the exception occurred during getting / parsing the domain model XML as RuntimeException. 
     */
    public DomainModelParser(String xmlFilePath) {
        logger.info("Parsing model at location : " + xmlFilePath);
        try {
            domainModel = (DomainModel) Utils.deserializeDocument(xmlFilePath, DomainModel.class);
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse domain model XML file.", e, ErrorCodeConstants.GR_0001);
        }
        init(domainModel);
    }

    /**
     * Gets the DomainModel associated with this instance.
     * @return model - The entire domain model.
     */
    public DomainModel getDomainModel() {
        return domainModel;
    }

    /**
     * Returns all the UML associations present in the DomainModel
     * @return UMLAssociation[] - Array of UMLAssociation.
     */
    public UMLAssociation[] getUmlAssociations() {
        DomainModelExposedUMLAssociationCollection collection = domainModel.getExposedUMLAssociationCollection();
        if (collection != null && collection.getUMLAssociation() != null) {
            return collection.getUMLAssociation();
        }
        return new UMLAssociation[0];
    }

    /**
     * Returns all the UML classes in the DomainModel
     * @return UMLClass[]- Array of UMLClass.
     */
    public UMLClass[] getUmlClasses() {
        DomainModelExposedUMLClassCollection collection = domainModel.getExposedUMLClassCollection();
        if (collection != null && collection.getUMLClass() != null) {
            return collection.getUMLClass();
        }
        return new UMLClass[0];
    }

    /**
     * Gives a map having parent child information.
     * @return Map with key as UML-id of parent class and value as list of UML-id of all children classes.
     */
    public Map<String, List<String>> getParentVsChildrenMap() {
        return parentIdVsChildrenIds;
    }

    /**
     * This method finds our parent child relationship and populates {@link DomainModelParser#parentIdVsChildrenIds}
     */
    private Map<String, List<String>> initializeParentChildMap() {
        DomainModelUmlGeneralizationCollection collection = domainModel.getUmlGeneralizationCollection();
        if (collection != null && collection.getUMLGeneralization() != null) {
            UMLGeneralization[] generalizations = collection.getUMLGeneralization();
            Map<String, List<String>> map = new HashMap<String, List<String>>(generalizations.length);
            for (UMLGeneralization umlGeneralization : generalizations) {
                String childClass = umlGeneralization.getSubClassReference().getRefid();
                String parentClass = umlGeneralization.getSuperClassReference().getRefid();
                List<String> children = map.get(parentClass);
                if (children == null) {
                    children = new ArrayList<String>();
                    map.put(parentClass, children);
                }
                children.add(childClass);
            }
            return map;
        }
        return new HashMap<String, List<String>>(0);
    }

    private void init(DomainModel model) {
        if (model == null) {
            throw new RuntimeException("Unable to parse domain model XML file.", ErrorCodeConstants.GR_0001);
        }
        domainModel = model;
        parentIdVsChildrenIds = initializeParentChildMap();
    }
}