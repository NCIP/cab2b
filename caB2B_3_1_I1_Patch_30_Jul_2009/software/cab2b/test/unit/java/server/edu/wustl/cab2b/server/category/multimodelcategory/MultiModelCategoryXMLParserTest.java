package edu.wustl.cab2b.server.category.multimodelcategory;

import java.io.File;
import java.io.FileInputStream;

import junit.framework.TestCase;

import org.dom4j.io.SAXReader;

import edu.wustl.cab2b.server.category.multimodelcategory.MultiModelCategoryXmlParser;

/**
 * JUnit test cases for MultiModelCategoryXMLParser class.
 * @author chetan_pundhir
 *
 */
public class MultiModelCategoryXMLParserTest extends TestCase {

    /**
     * Test method for getMultiModelCategory method.
     */
    public void testGetMultiModelCategory() {
        MultiModelCategoryXmlParser parser = new MultiModelCategoryXmlParser();
        assertNull(parser.getMultiModelCategory(null));
        assertNull(parser.getMultiModelCategory(new String()));
    }

    /**
     * Test method for populateMultiModelCategory method.
     */
    public void testPopulateMultiModelCategory() {
        MultiModelCategoryXmlParser parser = new MultiModelCategoryXmlParser();
        String xmlFileName = "MMC.xml";
        try {
            new SAXReader().read(new FileInputStream(new File(xmlFileName)));
        } catch (Exception e) {
            //throw new AssertionError(e);
        }
        assertNull(parser.populateMultiModelCategory(null));
    }

    /**
     * Test method for getArrtribAndPaths method.
     */
    public void testGetArrtribAndPaths() {
        MultiModelCategoryXmlParser parser = new MultiModelCategoryXmlParser();
        assertNull(parser.getArrtribAndPaths(null));
    }
    
    /**
     * Test method for populateMMCAttribs method.
     */
    public void testPopulateMMCAttribs() {
        MultiModelCategoryXmlParser parser = new MultiModelCategoryXmlParser();
        assertNull(parser.populateMMCAttribs(null));
    }    
    
    /**
     * Test method for populateMMCPaths method.
     */
    public void testpopulateMMCPaths() {
        MultiModelCategoryXmlParser parser = new MultiModelCategoryXmlParser();
        assertNull(parser.populateMMCPaths(null));
    }      
}