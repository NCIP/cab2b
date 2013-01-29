/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.multimodelcategory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.common.multimodelcategory.bean.MultiModelAttributeBean;
import edu.wustl.cab2b.common.multimodelcategory.bean.MultiModelCategoryBean;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.modelgroup.ModelGroupOperations;
import edu.wustl.cab2b.server.path.PathFinder;
import edu.wustl.common.querysuite.metadata.path.IPath;

/**
 * This class provides methods to parse MultiModel Category XML and convert it to java object form.
 * These java objects will be used in actual MultiModel category creation and saving.
 * @author Chetan_Pundhir
 */
public class MultiModelCategoryXmlParser {

    /**
     * Reads the XML file and creates an object of MultiModelCategory for the same.
     * This method does not perform any check for "well-form" of the XML.
     * It is callers responsibility to do that.
     * @param xmlFileName The full path of Category XML file.
     * @return The MultiModelCategory for given MultiModel Category XML.
     */
    public MultiModelCategoryBean getMultiModelCategory(String xmlFileName) {
        if (!isXMLFile(xmlFileName)) {
            throw new IllegalArgumentException("The parameter file must be a XML file");
        }

        File xmlFile = new File(xmlFileName);
        if (xmlFileName == null || !xmlFile.exists()) {
            throw new RuntimeException(xmlFileName + "doesn't exists.");
        }

        return getMultiModelCategory(xmlFile);
    }

    /**
     * This method reads the XML file and creates an object of MultiModelCategory for the same.
     * This method does not perform any check for "well-form" of the XML.
     * It is callers responsibility to do that.
     * @param xmlFileName The full path of Category XML file.
     * @return The MultiModelCategory for given MultiModel Category XML.
     */
    public MultiModelCategoryBean getMultiModelCategory(File xmlFile) {
        if (!isXMLFile(xmlFile.getName())) {
            throw new IllegalArgumentException("The parameter file must be a XML file");
        }

        Document document = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(xmlFile);
            document = new SAXReader().read(fileInputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Given MMC XML file not found: " + xmlFile.getName(), e);
        } catch (DocumentException e) {
            throw new RuntimeException("Unable to parse multi model category XML file: " + xmlFile.getName(), e);
        }
        return populateMultiModelCategory(document);
    }

    private boolean isXMLFile(String xmlFileName) {
        String extension = xmlFileName.substring(xmlFileName.lastIndexOf('.') + 1);
        return "xml".equalsIgnoreCase(extension);
    }

    /**
     * @param category The Document element which represents a MultiModel Category 
     * @return MultiModelCategory Object.
     */
    MultiModelCategoryBean populateMultiModelCategory(Document document) {
        Element mmcRoot = document.getRootElement();
        MultiModelCategoryBean mmc = new MultiModelCategoryBean();

        String mmcName = mmcRoot.attribute("name").getValue();
        mmc.setName(mmcName);

        String mmcDescription = mmcRoot.attribute("description").getValue();
        mmc.setDescription(mmcDescription);

        List<ModelGroupInterface> modelGroups = new ModelGroupOperations().getAllModelGroups();
        String modelGroupName = mmcRoot.attribute("applicationGroup").getValue();
        for (ModelGroupInterface modelGroup : modelGroups) {
            if (modelGroup.getModelGroupName().equals(modelGroupName)) {
                mmc.setApplicationGroup(modelGroup);
                break;
            }
        }

        Map<String, List<Element>> arrtribAndPaths = getArrtribAndPaths(mmcRoot);

        List<Element> attributes = arrtribAndPaths.get("MultiModelCategoryAttributes");
        Collection<MultiModelAttributeBean> mmcAttribs = populateMMCAttribs(attributes);
        mmc.setMultiModelAttributes(mmcAttribs);

        List<Element> paths = arrtribAndPaths.get("Paths");
        Collection<IPath> mmcPaths = populateMMCPaths(paths);
        mmc.setPaths(mmcPaths);

        return mmc;
    }

    Map<String, List<Element>> getArrtribAndPaths(Element mmcRoot) {
        List<Element> arrtribAndPathsElements = mmcRoot.elements();
        Map<String, List<Element>> arrtribAndPaths = new HashMap<String, List<Element>>(2);
        arrtribAndPaths.put("MultiModelCategoryAttributes", new ArrayList<Element>());
        arrtribAndPaths.put("Paths", new ArrayList<Element>());
        for (Element e : arrtribAndPathsElements) {
            if (e.getName().equals("MultiModelCategoryAttributes")) {
                List<Element> attributes = e.elements();
                arrtribAndPaths.get("MultiModelCategoryAttributes").addAll(attributes);
            } else if (e.getName().equals("Paths")) {
                List<Element> paths = e.elements();
                arrtribAndPaths.get("Paths").addAll(paths);
            }
        }
        return arrtribAndPaths;
    }

    Collection<MultiModelAttributeBean> populateMMCAttribs(List<Element> attributes) {
        Collection<MultiModelAttributeBean> mmcAttribs = new ArrayList<MultiModelAttributeBean>();
        for (Element a : attributes) {
            MultiModelAttributeBean multiModelCategoryAttribute = new MultiModelAttributeBean();

            String mmcAttribName = a.attribute("name").getValue();
            multiModelCategoryAttribute.setName(mmcAttribName);

            String mmcAttribDesc = a.attribute("description").getValue();
            multiModelCategoryAttribute.setDescription(mmcAttribDesc);

            Collection<AttributeInterface> mappedClassAttributes = new ArrayList<AttributeInterface>();
            List<Element> mappedClasseAttribs = a.elements();
            for (Element mappedClassAttrib : mappedClasseAttribs) {
                String entityGroupName = mappedClassAttrib.attribute("model").getValue();
                EntityGroupInterface entityGroup = EntityCache.getInstance().getEntityGroupByName(entityGroupName);

                String entityName = mappedClassAttrib.attribute("class").getValue();
                EntityInterface entity = entityGroup.getEntityByName(entityName);

                Collection<AttributeInterface> attribs = entity.getAllAttributes();

                String attribName = mappedClassAttrib.attribute("name").getValue();
                for (AttributeInterface ai : attribs) {
                    if (attribName.equals(ai.getName())) {
                        mappedClassAttributes.add(ai);
                        break;
                    }
                }
            }
            multiModelCategoryAttribute.setSelectedAttributes(mappedClassAttributes);
            mmcAttribs.add(multiModelCategoryAttribute);
        }
        return mmcAttribs;
    }

    Collection<IPath> populateMMCPaths(List<Element> paths) {
        Collection<IPath> iPaths = new ArrayList<IPath>();
        for (Element path : paths) {
            IPath iPath = PathFinder.getInstance().getPathById(Long.parseLong(path.attribute("id").getValue()));
            iPaths.add(iPath);
        }
        return iPaths;
    }
}