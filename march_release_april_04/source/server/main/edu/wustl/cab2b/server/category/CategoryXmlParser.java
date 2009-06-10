package edu.wustl.cab2b.server.category;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;

/**
 * This class provides methods to parse Category XML and convert them to java object form.
 * These java objects will be used in actual category creation and saving. 
 * @author Chandrakant Talele
 */
public class CategoryXmlParser {
    /**
     * Reads the XML file and creates a {@link InputCategory} object for the same.
     * This method does not perform any check for "well-formedness" of the XML.
     * It is callers responsibility to do that.
     * @param xmlFileName The full path of Category XML file.
     * @return The InputCategory for given Category XML.
     */
    public InputCategory getInputCategory(String xmlFileName) {
        File file = new File(xmlFileName);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException");
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = new SAXReader().read(fileInputStream);
        } catch (DocumentException e) {
            throw new RuntimeException("Unable to parse category XML file : " + xmlFileName, e,
                    ErrorCodeConstants.IO_0001);
        }
        
        Element category = document.getRootElement();
        return getInputCategory(category);
    }

    /**
     * @param category The Document element which represents a Category 
     * @return InputCategory Object for given Category Element.
     */
    private InputCategory getInputCategory(Element category) {
        InputCategory inputCategory = new InputCategory();
        List<InputCategory> subCategories = new ArrayList<InputCategory>();
        InputCategorialClass rootClass = null;
        Element rootCategorialClass = null;
        List<Element> elements = category.elements();

        for (Element e : elements) {
            if (e.getName().equals("CategorialClass")) {
                rootCategorialClass = (Element) category.elementIterator().next();
                rootClass = getInputCategorialClass(rootCategorialClass);
            } else if (e.getName().equals("Category")) {
                subCategories.add(getInputCategory(e));
            }
        }

        String name = category.attribute("name").getValue();
        inputCategory.setName(name);
        
        String description = category.attribute("description").getValue();
        inputCategory.setDescription(description);
        inputCategory.setRootCategorialClass(rootClass);
        inputCategory.setSubCategories(subCategories);
        return inputCategory;
    }

    /**
     * @param categorialClass The Document element which represents a CategorialClass
     * @return InputCategorialClass Object for given CategorialClass Element.
     */
    private InputCategorialClass getInputCategorialClass(Element categorialClass) {
        List<InputCategorialAttribute> attributeList = new ArrayList<InputCategorialAttribute>();
        List<InputCategorialClass> children = new ArrayList<InputCategorialClass>();

        String pathFromParent = categorialClass.attribute("IdOfPathFromParentToThis").getValue();
        InputCategorialClass inputCategorialClass = new InputCategorialClass();
        inputCategorialClass.setPathFromParent(Long.parseLong(pathFromParent));
        String entityName = categorialClass.attribute("name").getValue();

        List<Element> elements = categorialClass.elements();
        for (Element element : elements) {
            if (element.getName().equals("Attribute")) {
                String attrName = element.attribute("name").getValue();
                Attribute attribute = element.attribute("displayName");
                String displayName = (attribute == null ? attrName : attribute.getValue());

                
                AttributeInterface attr = DynamicExtensionUtility.getAttribute(entityName, attrName);
                if(!attrName.equals(attr.getName())) {
                    throw new RuntimeException("Expected attribute : " + attrName + " Returned Attribute : "+ attr.getName());
                }
                InputCategorialAttribute inputCategorialAttribute = new InputCategorialAttribute();
                inputCategorialAttribute.setDynamicExtAttribute(attr);
                inputCategorialAttribute.setDisplayName(displayName);
                attributeList.add(inputCategorialAttribute);
                
            } else if (element.getName().equals("CategorialClass")) {
                children.add(getInputCategorialClass(element));
            }
        }

        inputCategorialClass.setAttributeList(attributeList);
        inputCategorialClass.setChildren(children);
        
        return inputCategorialClass;
    }

//    public static void main(String[] args) throws Exception {
//        Logger.configure();
//        String xmlFileName = "Genomic Identifiers.xml";
//        //String xmlFileName = "Literature-based Gene Association.xml";
//
//        String fullPath = "C:/Documents and Settings/chandrakant_talele/Desktop/category/sample/" + xmlFileName;
//        CategoryXmlParser xmlParsing = new CategoryXmlParser();
//        InputCategory cat = xmlParsing.getInputCategory(fullPath, ConnectionUtil.getConnection());
//        System.out.println("foooo");
//    }
}