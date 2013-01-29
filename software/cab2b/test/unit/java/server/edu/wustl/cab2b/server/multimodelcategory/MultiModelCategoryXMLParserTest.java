/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.multimodelcategory;

import java.io.File;
import java.io.FileInputStream;

import junit.framework.TestCase;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

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
        try {
            assertNull(parser.getMultiModelCategory(""));
            fail("Exception should have been thrown");
        } catch (RuntimeException e) {

        }
        try {
            assertNull(parser.getMultiModelCategory(new String()));
            fail("Exception should have been thrown");
        } catch (RuntimeException e) {

        }
    }

//    /**
//     * Test method for populateMultiModelCategory method.
//     */
//    public void testPopulateMultiModelCategory() {
//        MultiModelCategoryXmlParser parser = new MultiModelCategoryXmlParser();
//        String xmlFileName = "C:\\Documents and Settings\\chandrakant_talele\\My Documents\\My Received Files\\MMC.xml";
//        try {
//            Document doc = new SAXReader().read(new FileInputStream(new File(xmlFileName)));
//            assertNull(parser.populateMultiModelCategory(doc));
//        } catch (Exception e) {
//            //throw new AssertionError(e);
//        }
//        
//    }
//
//    /**
//     * Test method for getArrtribAndPaths method.
//     */
//    public void testGetArrtribAndPaths() {
//        MultiModelCategoryXmlParser parser = new MultiModelCategoryXmlParser();
//        assertNull(parser.getArrtribAndPaths(null));
//    }
//    
//    /**
//     * Test method for populateMMCAttribs method.
//     */
//    public void testPopulateMMCAttribs() {
//        MultiModelCategoryXmlParser parser = new MultiModelCategoryXmlParser();
//        assertNull(parser.populateMMCAttribs(null));
//    }    
//    
//    /**
//     * Test method for populateMMCPaths method.
//     */
//    public void testpopulateMMCPaths() {
//        MultiModelCategoryXmlParser parser = new MultiModelCategoryXmlParser();
//        assertNull(parser.populateMMCPaths(null));
//    }     
    
    /*
<MultiModelCategory name="Participant Information" description="Participant Basic Information" modelGroup="Biospecimen data">
    <MultiModelCategoryAttributes>
        <MultiModelCategoryAttribute name="First Name" description="First Name">
            <Attribute name="firstName" model="caTissue_Core_1_2_v1.2" class="edu.wustl.catissuecore.domain.Participant"/>
        </MultiModelCategoryAttribute>
        <MultiModelCategoryAttribute name="Last Name" description="Last Name">
            <Attribute name="lastName" model="caTissue_Core_1_2_v1.2" class="edu.wustl.catissuecore.domain.Participant"/>
        </MultiModelCategoryAttribute>
        <MultiModelCategoryAttribute name="Gender" description="Gender">
            <Attribute name="gender" model="caTissue_Core_1_2_v1.2" class="edu.wustl.catissuecore.domain.Participant"/>
        </MultiModelCategoryAttribute>
        <MultiModelCategoryAttribute name="Birth Date" description="Birth Date">
            <Attribute name="birthDate" model="caTissue_Core_1_2_v1.2" class="edu.wustl.catissuecore.domain.Participant"/>
        </MultiModelCategoryAttribute>
    </MultiModelCategoryAttributes>
    <Paths>
        <Path id="4"/>
        <Path id="2"/>
        <Path id="7"/>
    </Paths>
</MultiModelCategory> 
      
      */
}