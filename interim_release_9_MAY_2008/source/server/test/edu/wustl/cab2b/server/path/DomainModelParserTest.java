package edu.wustl.cab2b.server.path;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.cagrid.metadata.common.UMLClassUmlAttributeCollection;
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

/**
 * Test class for {@link edu.wustl.cab2b.server.path.DomainModelParser}
 * @author Chandrakant Talele
 */
public class DomainModelParserTest extends TestCase {

//    /**
//     * Test case which creates a DomainModelParser object and calls all available methods on it.
//     */
//        public void testDomainModelParser() {
//
//        String name = PropertyLoader.getAllApplications()[0];
//        String path = PropertyLoader.getModelPath(name);
//        DomainModelParser parser = null;
//        try {
//            parser = new DomainModelParser(path);
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail("unable to initialise");
//        }
//
//        Document doc = null;
//        try {
//            doc = new SAXBuilder().build(path);
//        } catch (JDOMException e) {
//            e.printStackTrace();
//            fail("Got JDOMException while parsing the domain Model XML at location : " + path);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            fail("Unable to load file : " + path);
//        }
//        Element root = doc.getRootElement();
//
//        Collection allClasses = executeXpath(
//                                             "//*[local-name()='exposedUMLClassCollection']//*[local-name()='UMLClass']",
//                                             root);
//        Collection allAssociation = executeXpath(
//                                                 "//*[local-name()='exposedUMLAssociationCollection']//*[local-name()='UMLAssociation']",
//                                                 root);
//
//        assertNotNull(parser);
//        assertNotNull(parser.getDomainModel());
//        assertEquals(allClasses.size(), parser.getUmlClasses().length);
//        assertEquals(allAssociation.size(), parser.getUmlAssociations().length);
//
//    }
//    private Collection executeXpath(String xpathExpression, Element modelElement) {
//        Collection collection = null;
//        try {
//            JDOMXPath path = new JDOMXPath(xpathExpression);
//            collection = path.selectNodes(modelElement);
//        } catch (JaxenException e) {
//            e.printStackTrace();
//            fail("Execption while firing a xPath Query");
//        }
//        return collection;
//    }

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

    @Override
    protected void setUp() throws Exception {
        Logger.configure();
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
        UMLClass[] umlClasses = new UMLClass[] { shape, triangle, equilateralTriangle, painter ,consolePainter};

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
        model.setExposedUMLAssociationCollection(new DomainModelExposedUMLAssociationCollection(
                new UMLAssociation[] { assoc }));
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
}
