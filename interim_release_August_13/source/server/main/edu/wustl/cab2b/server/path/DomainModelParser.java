package edu.wustl.cab2b.server.path;

import java.util.HashMap;
import java.util.Map;

import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLGeneralization;
import java.util.ArrayList;
import java.util.List;
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
    /**
     * The DomainModel java object of given XML file.
     */
    protected DomainModel domainModel;

    /**
     * This constructor ties instance of this class to a single particular domain model.<br>
     * It takes the URL as parameter where the DomainModel.xml is available.<br>
     * @param xmlFilePath The URL from where to retrieve the DomainModel.xml
     *            and create a DomainModel Object.
     * Throws the exception occured during getting / parsing the domain model XML as RuntimeException. 
     */
    public DomainModelParser(String xmlFilePath) {
        Logger.out.info("Parsing model at location : " + xmlFilePath);
        try {
            domainModel = (DomainModel) Utils.deserializeDocument(xmlFilePath, DomainModel.class);
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse domain model XML file.",e,ErrorCodeConstants.GR_0001);
        }
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
        return domainModel.getExposedUMLAssociationCollection().getUMLAssociation();
    }

    /**
     * Returns all the UML classes in the DomainModel
     * @return UMLClass[]- Array of UMLClass.
     */
    public UMLClass[] getUmlClasses() {
        return domainModel.getExposedUMLClassCollection().getUMLClass();
    }
    /**
     * Gives a map having parent child information.
     * @return Map with key as UML-id of parent class and value as list of UML-id of all children classes.
     */
    public Map<String,List<String>> getParentVsChildrenMap() {
        UMLGeneralization[] umlGeneralizations = domainModel.getUmlGeneralizationCollection().getUMLGeneralization();
        if (umlGeneralizations != null) {
           
            HashMap<String, List<String>> parentIdVsChildrenIds = new HashMap<String, List<String>>(umlGeneralizations.length);
            for (UMLGeneralization umlGeneralization : umlGeneralizations) {
                String childClass = umlGeneralization.getSubClassReference().getRefid();
                String parentClass = umlGeneralization.getSuperClassReference().getRefid();
                List<String> children = parentIdVsChildrenIds.get(parentClass);
                if (children == null) {
                    children = new ArrayList<String>();
                    parentIdVsChildrenIds.put(parentClass, children);
                }
                children.add(childClass);
            }
            return parentIdVsChildrenIds;
        }
        
        return new HashMap<String, List<String>>(0);
    }
}