/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.path;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.metadata.common.Enumeration;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.common.UMLClassUmlAttributeCollection;
import gov.nih.nci.cagrid.metadata.common.ValueDomain;
import gov.nih.nci.cagrid.metadata.common.ValueDomainEnumerationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLAssociationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLClassCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelUmlGeneralizationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationSourceUMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationTargetUMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference;
import gov.nih.nci.cagrid.metadata.dataservice.UMLGeneralization;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.jaxen.JaxenException;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Test class for {@link edu.wustl.cab2b.server.path.DomainModelParser}
 * @author Chandrakant Talele
 */
public class DomainModelParserTest extends TestCase {
    private String path = System.getProperty("user.home") + "/FromDomainModelParserTest.xml";

    /**
     * Test case which creates a DomainModelParser object and calls all available methods on it.
     */
    public void testDomainModelParser() {
        DomainModel model = getModel();
        DomainModelParser parser = null;
        try {
            Utils.serializeDocument(path, model, DomainModel.getTypeDesc().getXmlType());
            parser = new DomainModelParser(path);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception while writing domain model to file / constructing DomainModelParser using : " + path);
        }
      
        Document doc = null;
        try {
            doc = new SAXBuilder().build(path);
        } catch (JDOMException e) {
            e.printStackTrace();
            fail("Got JDOMException while parsing the domain Model XML at location : " + path);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unable to load file : " + path);
        }
        Element root = doc.getRootElement();

        List<?> allClasses = executeXpath("//*[local-name()='exposedUMLClassCollection']//*[local-name()='UMLClass']", root);
        List<?> allAssociation = executeXpath("//*[local-name()='exposedUMLAssociationCollection']//*[local-name()='UMLAssociation']", root);

        assertNotNull(parser);
        assertNotNull(parser.getDomainModel());
        assertEquals(allClasses.size(), parser.getUmlClasses().length);
        assertEquals(allAssociation.size(), parser.getUmlAssociations().length);
    }

    public void testDomainModelParserWithObject() {
        DomainModelParser p = new DomainModelParser(getModel());
        Map<String, List<String>> map = p.getParentVsChildrenMap();
        assertEquals(3, map.size());
        assertEquals(1, map.get("ID_Shape").size());
        assertEquals("ID_Triangle", map.get("ID_Shape").get(0));

        assertEquals(1, map.get("ID_Triangle").size());
        assertEquals("ID_EquilateralTriangle", map.get("ID_Triangle").get(0));
        assertNull(map.get("ID_ConsolePainter"));
    }

    public void testDomainModelParserWithObjectNoGeneralization() {
        DomainModelParser p = new DomainModelParser(getModelNoGeneralization());
        Map<String, List<String>> map = p.getParentVsChildrenMap();
        assertEquals(0, map.size());
    }

    public void testDomainModelParserWithXML() {
        DomainModel model = getModel();
        try {
            Utils.serializeDocument(path, model, DomainModel.getTypeDesc().getXmlType());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception while writing domain model to file" + path);
        }

        DomainModelParser p = new DomainModelParser(path);
        Map<String, List<String>> map = p.getParentVsChildrenMap();
        assertEquals(3, map.size());
        assertEquals(1, map.get("ID_Shape").size());
        assertEquals("ID_Triangle", map.get("ID_Shape").get(0));

        assertEquals(1, map.get("ID_Triangle").size());
        assertEquals("ID_EquilateralTriangle", map.get("ID_Triangle").get(0));
        assertNull(map.get("ID_ConsolePainter"));
    }

    private static UMLClass get(String name) {
        UMLClass umlClass = new UMLClass(true);
        umlClass.setId("ID_" + name);
        umlClass.setPackageName("edu");
        umlClass.setClassName(name);
        umlClass.setUmlAttributeCollection(new UMLClassUmlAttributeCollection(null));
        return umlClass;
    }

    public static DomainModel getModel() {
        UMLClass shape = get("Shape");
        UMLClass triangle = get("Triangle");
        UMLClass equilateralTriangle = get("EquilateralTriangle");
        UMLClass painter = get("Painter");
        UMLClass consolePainter = get("ConsolePainter");
        UMLClass[] umlClasses = new UMLClass[] { shape, triangle, equilateralTriangle, painter, consolePainter };
        shape.setUmlAttributeCollection(new UMLClassUmlAttributeCollection(new UMLAttribute[] {getAttribute("id","int")}));
        UMLGeneralization[] generalization = new UMLGeneralization[3];
        generalization[0] = getGeneralization(shape, triangle);
        generalization[1] = getGeneralization(triangle, equilateralTriangle);
        generalization[2] = getGeneralization(painter, consolePainter);
        UMLAssociation assoc = new UMLAssociation();
        UMLAssociationEdge srcAssocEdge = new UMLAssociationEdge(getClassRef(painter), 1, 0, "associatedPainter");
        UMLAssociationEdge tgtAssocEdge = new UMLAssociationEdge(getClassRef(shape), -1, 0, "shapeCollection");
        assoc.setBidirectional(true);
        assoc.setSourceUMLAssociationEdge(new UMLAssociationSourceUMLAssociationEdge(srcAssocEdge));
        assoc.setTargetUMLAssociationEdge(new UMLAssociationTargetUMLAssociationEdge(tgtAssocEdge));

        DomainModel model = new DomainModel();
        model.setExposedUMLClassCollection(new DomainModelExposedUMLClassCollection(umlClasses));
        model.setUmlGeneralizationCollection(new DomainModelUmlGeneralizationCollection(generalization));
        model.setExposedUMLAssociationCollection(new DomainModelExposedUMLAssociationCollection(new UMLAssociation[] { assoc }));
        return model;
    }
private static UMLAttribute getAttribute(String name,String type) {
    UMLAttribute a = new UMLAttribute();
    a.setName(name);
    a.setDataTypeName(type);
    ValueDomain vd = new ValueDomain();
    
    vd.setEnumerationCollection(new ValueDomainEnumerationCollection(new Enumeration[]{}));
    a.setValueDomain(vd);
    return a;
}
    public static DomainModel getModelNoGeneralization() {
        UMLClass shape = get("Shape");
        UMLClass triangle = get("Triangle");
        UMLClass equilateralTriangle = get("EquilateralTriangle");
        UMLClass painter = get("Painter");
        UMLClass consolePainter = get("ConsolePainter");
        UMLClass[] umlClasses = new UMLClass[] { shape, triangle, equilateralTriangle, painter, consolePainter };

        UMLAssociation assoc = new UMLAssociation();
        UMLAssociationEdge srcAssocEdge = new UMLAssociationEdge(getClassRef(painter), 1, 0, "associatedPainter");
        UMLAssociationEdge tgtAssocEdge = new UMLAssociationEdge(getClassRef(shape), -1, 0, "shapeCollection");
        assoc.setBidirectional(true);
        assoc.setSourceUMLAssociationEdge(new UMLAssociationSourceUMLAssociationEdge(srcAssocEdge));
        assoc.setTargetUMLAssociationEdge(new UMLAssociationTargetUMLAssociationEdge(tgtAssocEdge));

        DomainModel model = new DomainModel();
        model.setExposedUMLClassCollection(new DomainModelExposedUMLClassCollection(umlClasses));
        model.setUmlGeneralizationCollection(new DomainModelUmlGeneralizationCollection(new UMLGeneralization[0]));
        model.setExposedUMLAssociationCollection(new DomainModelExposedUMLAssociationCollection(new UMLAssociation[] { assoc }));
        return model;
    }

    private static UMLGeneralization getGeneralization(UMLClass parent, UMLClass child) {
        UMLGeneralization umlGeneralization = new UMLGeneralization();
        umlGeneralization.setSuperClassReference(getClassRef(parent));
        umlGeneralization.setSubClassReference(getClassRef(child));
        return umlGeneralization;
    }

    private static UMLClassReference getClassRef(UMLClass clazz) {
        return new UMLClassReference(clazz.getId());
    }
    private List<?> executeXpath(String xpathExpression, Element modelElement) {
        List<?> list = null;
        try {
            JDOMXPath path = new JDOMXPath(xpathExpression);
            list = path.selectNodes(modelElement);
        } catch (JaxenException e) {
            e.printStackTrace();
            fail("Execption while firing a xPath Query");
        }
        return list;
    }
}
