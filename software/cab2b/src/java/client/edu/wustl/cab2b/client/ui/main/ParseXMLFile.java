/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 *<p>Copyright: (c) Washington University, School of Medicine 2006.</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *<p>ClassName: edu.wustl.cab2b.client.ui.main.ParseXMLFile </p> 
 */
package edu.wustl.cab2b.client.ui.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.common.util.logger.Logger;

/**
 * This class is used for parsing the XML file and put the parsed elements to HashMaps 
 * @author Kaushal Kumar
 * @version 1.0
 */
public class ParseXMLFile {

    private static ParseXMLFile instance = null;

    /**
     * XML file (to be parsed) with complete path.
     */
    private final String xmlFileName = "dynamicUI.xml";

    /**
     * Variables declared by Pratibha
     */
    private Map<String, ArrayList<String>> nonEnumDataTypeToConditionMap = new HashMap<String, ArrayList<String>>();

    private Map<String, String> nonEnumDataTypeToComponentMap = new HashMap<String, String>();

    private Map<String, ArrayList<String>> enumDataTypeToConditionMap = new HashMap<String, ArrayList<String>>();

    private Map<String, String> enumDataTypeToComponentMap = new HashMap<String, String>();

	private static final org.apache.log4j.Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ParseXMLFile.class);

    /**
     * Constructor for parsing the XML file. 
     * @throws CheckedException
     */
    protected ParseXMLFile() throws CheckedException {

        Document doc = parseFile(xmlFileName);
        Node root = doc.getDocumentElement();
        readDynamicUIComponents(root);
    }

    /**
     * @return Object Reference of ParseXMLFile
     * @throws CheckedException
     */
    public static ParseXMLFile getInstance() throws CheckedException {
        if (instance == null) {
            instance = new ParseXMLFile();
        }
        return instance;
    }

    /**
     * This method return the Element value of any particular node
     * @param elem
     * @return string
     */
    private String getElementValue(Node elem) {
        Node child;
        if (elem != null) {
            if (elem.hasChildNodes()) {
                for (child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    /**
     * Method to read dynamic UI component details from xml and populate UI accordingly
     * @param node root node to parse
     */
    private void readDynamicUIComponents(Node node) {

        // This node list will return two childrens 
        // 1. non-enumerated
        // 2. enumerated
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            // Check if node is non-enumberated
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (child.getNodeName().equalsIgnoreCase("non-enumerated")) {
                    // Get all its children nodes and store corresponding details into the appropriate data-strutures
                    NodeList dataTypeNodes = child.getChildNodes();
                    readDataTypeDetailsForAllNode(dataTypeNodes, nonEnumDataTypeToConditionMap,
                                                  nonEnumDataTypeToComponentMap);
                } else {
                    //	Get all its children nodes and store corresponding details into the appropriate data-strutures
                    NodeList dataTypeNodes = child.getChildNodes();
                    readDataTypeDetailsForAllNode(dataTypeNodes, enumDataTypeToConditionMap,
                                                  enumDataTypeToComponentMap);
                }
            }
        }
    }

    /**
     * Method to get condition list and component associated with it
     * @param dataTypeNodes
     * @param dataTypeToConditionMap
     * @param dataTypeToComponentMap
     */
    private void readDataTypeDetailsForAllNode(NodeList dataTypeNodes,
                                               Map<String, ArrayList<String>> dataTypeToConditionMap,
                                               Map<String, String> dataTypeToComponentMap) {
        for (int i = 0; i < dataTypeNodes.getLength(); i++) {
            Node dataTypeNode = dataTypeNodes.item(i);
            if (dataTypeNode.getNodeType() == Node.ELEMENT_NODE) {
                // Node name like String, number, date, boolean
                String nodeType = dataTypeNode.getNodeName();
                // Get all the nodes under nodeType
                NodeList childNodes = dataTypeNode.getChildNodes();
                for (int childCnt = 0; childCnt < childNodes.getLength(); childCnt++) {
                    // Read the condition node and populate the condition list
                    Node node = childNodes.item(childCnt);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        if (true == node.getNodeName().equalsIgnoreCase("conditions")) {
                            ArrayList<String> conditionList = new ArrayList<String>();
                            NodeList nodeConditions = node.getChildNodes();
                            for (int conditionCnt = 0; conditionCnt < nodeConditions.getLength(); conditionCnt++) {
                                Node conditionNode = nodeConditions.item(conditionCnt);
                                logger.info("JJJ NODEINFO for cc="+conditionCnt+" nodeName="+conditionNode.getNodeName()+" added"+getElementValue(conditionNode));

                                if (conditionNode.getNodeType() == Node.ELEMENT_NODE) {
                                    NodeList displayNodes = conditionNode.getChildNodes();
                                    for (int j = 0; j < displayNodes.getLength(); j++) {
                                        logger.info("JJJ CHILDREN for cc="+j+" nodeName="+displayNodes.item(j).getNodeName()+" added"+getElementValue(displayNodes.item(j)));

                                        if (displayNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                            conditionList.add(getElementValue(displayNodes.item(j)));
                                            break;
                                        }                                  
                                    }
                                }
                            }
                            dataTypeToConditionMap.put(nodeType, conditionList);
                        } else if (true == node.getNodeName().equalsIgnoreCase("components"))// Read component details
                        {
                            dataTypeToComponentMap.put(nodeType, getElementValue(node));
                        }
                    }
                }
            }
        }
    }

    /**
     * This method parses the nodes of XML file and returns the Document object
     * @param fileName
     * @return doc
     */
    private Document parseFile(String fileName) throws CheckedException {
        DocumentBuilder docBuilder;
        Document doc = null;
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setIgnoringElementContentWhitespace(true);
        try {
            docBuilderFactory.setIgnoringComments(true);
            docBuilderFactory.setIgnoringElementContentWhitespace(true);
            docBuilder = docBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new CheckedException(e.getMessage(), e, ErrorCodeConstants.UN_XXXX); // TODO put proper error code.
        }

        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
            doc = docBuilder.parse(in);
        } catch (SAXException e) {
            throw new CheckedException(e.getMessage(), e, ErrorCodeConstants.IO_0003);
        } catch (IOException e) {
            throw new CheckedException(e.getMessage(), e, ErrorCodeConstants.IO_0001);
        }
        return doc;
    }

    /**
     * @param dataTypeString
     * @return
     */
    public String getNonEnumClassName(String dataTypeString) {
        return nonEnumDataTypeToComponentMap.get(dataTypeString);
    }

    /**
     * @param dataTypeString
     * @return
     */
    public String getEnumClassName(String dataTypeString) {
        return enumDataTypeToComponentMap.get(dataTypeString);
    }

    /**
     * @param dataTypeString
     * @return
     */
    public ArrayList<String> getEnumConditionList(String dataTypeString) {
        return enumDataTypeToConditionMap.get(dataTypeString);
    }

    /**
     * @param dataTypeString
     * @return
     */
    public ArrayList<String> getNonEnumConditionList(String dataTypeString) {
        return nonEnumDataTypeToConditionMap.get(dataTypeString);
    }

}