package edu.wustl.cab2b.server.path;

import java.io.IOException;
import java.util.Collection;

import junit.framework.TestCase;

import org.jaxen.JaxenException;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import edu.wustl.cab2b.common.util.PropertyLoader;
import edu.wustl.common.util.logger.Logger;

/**
 * Test class for {@link edu.wustl.cab2b.server.path.DomainModelParser}
 * @author Chandrakant Talele
 */
public class DomainModelParserTest extends TestCase {

    /**
     * Test case which creates a DomainModelParser object and calls all available methods on it.
     */
    public void testDomainModelParser() {

        String name = PropertyLoader.getAllApplications()[0];
        String path = PropertyLoader.getModelPath(name);
        DomainModelParser parser = null;
        try {
            parser = new DomainModelParser(path);
        } catch (Exception e) {
            e.printStackTrace();
            fail("unable to initialise");
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

        Collection allClasses = executeXpath("//*[local-name()='exposedUMLClassCollection']//*[local-name()='UMLClass']",root);
        Collection allAssociation = executeXpath("//*[local-name()='exposedUMLAssociationCollection']//*[local-name()='UMLAssociation']",root);
        
        assertNotNull(parser);
        assertNotNull(parser.getDomainModel());
        assertEquals(allClasses.size(), parser.getUmlClasses().length);
        assertEquals(allAssociation.size(), parser.getUmlAssociations().length);
        
    }

    private Collection executeXpath(String xpathExpression, Element modelElement) {
        Collection collection = null;
        try {
            JDOMXPath path = new JDOMXPath(xpathExpression);
            collection = path.selectNodes(modelElement);
        } catch (JaxenException e) {
            e.printStackTrace();
            fail("Execption while firing a xPath Query");
        }
        return collection;
    }
    @Override
    protected void setUp() throws Exception {
        Logger.configure();
    }
}
