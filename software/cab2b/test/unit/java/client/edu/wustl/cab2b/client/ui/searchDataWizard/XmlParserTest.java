/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.searchDataWizard;

import junit.framework.TestCase;

public class XmlParserTest extends TestCase {

    public void testSetColor() {
        String color = "OLIVE";
        String tag = "<CQLQuery>";
        String res = new XmlParser().setColor(color, tag);
        StringBuffer buff = new StringBuffer(
                "<span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:olive'>");
        buff.append(tag).append("</span>");
        assertEquals(buff.toString(), res);
    }

    public void testParseXml() {
        String xmlString = "<CQLQuery xmlns=\"http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery\"> <Target name=\"edu.wustl.catissuecore.domain.Address\"> <Attribute name=\"id\" predicate=\"EQUAL_TO\" value=\"1\"/> </Target> </CQLQuery>";
        String xmlTestText = new XmlParser().parseXml(xmlString);
        System.out.println(xmlTestText);
        String output = "<HTML><BODY>\n<span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>&lt</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:purple'>CQLQuery </span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:olive'>xmlns</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>=</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>\"</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:blue'>http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>\"</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>></span><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>&lt</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:purple'>Target </span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:olive'>name</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>=</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>\"</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:blue'>edu.wustl.catissuecore.domain.Address</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>\"</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>></span><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>&lt</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:purple'>Attribute </span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:olive'>name</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>=</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>\"</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:blue'>id</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>\" </span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:olive'>predicate</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>=</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>\"</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:blue'>EQUAL_TO</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>\" </span><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:olive'>value</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>=</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>\"</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:blue'>1</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>\"</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>></span><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>&lt/</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:purple'>Attribute</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>></span><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>&lt/</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:purple'>Target</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>></span><br><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>&lt/</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:purple'>CQLQuery</span><span style='font-size:14pt;font-family:Courier New;font-weight:normal;color:black'>></span><br></BODY></HTML>";
        System.out.println(output);
        assertEquals(output, xmlTestText);
    }

}
