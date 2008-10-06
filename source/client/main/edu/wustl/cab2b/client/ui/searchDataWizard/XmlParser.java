/**
 * 
 */
package edu.wustl.cab2b.client.ui.searchDataWizard;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author gaurav_mehta
 * 
 */
public class XmlParser extends DefaultHandler {

    private static StringBuffer xmlText = new StringBuffer("<HTML><BODY>\n");

    private static int count = 0, startTag = 0;

    public XmlParser() {

    }

    public String parseXml(String dcqlQuery) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(dcqlQuery, this);

            xmlText.append("</BODY></HTML>");
        } catch (SAXException saxException) {

        } catch (ParserConfigurationException pce) {

        } catch (IOException ioException) {

        }

        return xmlText.toString();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        for (int i = 0; i < count; i++) {
            xmlText.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        }
        startTag++;

        xmlText.append(setColor("BLACK", "&lt"));
        xmlText.append(setColor("PURPLE", qName + " "));
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                if (i != 0 && (i % 2) == 0) {
                    xmlText.append("<br>");
                    for (int j = 0; j < count + 1; j++)
                        xmlText.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                }

                xmlText.append(setColor("OLIVE", attributes.getQName(i)));
                xmlText.append(setColor("BLACK", "="));
                xmlText.append(setColor("BLACK", String.valueOf('"')));
                xmlText.append(setColor("BLUE", attributes.getValue(i)));

                if (i == (attributes.getLength() - 1)) {
                    xmlText.append(setColor("BLACK", String.valueOf('"')));
                } else {
                    xmlText.append(setColor("BLACK", String.valueOf('"') + " "));
                }
            }
        }
        xmlText.append(setColor("BLACK", ">"));
        xmlText.append("<br>");

        count++;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        count--;
        for (int i = 0; i < count; i++) {
            xmlText.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        }
        xmlText.append(setColor("BLACK", "&lt/"));
        xmlText.append(setColor("PURPLE", qName));
        xmlText.append(setColor("BLACK", ">"));
        xmlText.append("<br>");
    }

    @Override
    public void characters(char[] character, int start, int length) throws SAXException {
        if (length > 10) {
            for (int i = 0; i < count; i++) {
                xmlText.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            }

            for (int i = start; i < (start + length); i++) {
                xmlText.append(setColor("BLUE", String.valueOf(character[i])));
            }
        }
    }

    private String setColor(String color, String tag) {
        StringBuffer htmlStart = new StringBuffer("<span style='font-size:14pt;"
                + "font-family:Courier New;font-weight:normal;color:");

        StringBuffer formattedText = new StringBuffer();
        String htmlEnd = "</span>";
        int length = htmlStart.length();

        if ("BLACK".equals(color)) {
            htmlStart = htmlStart.append("black'>");
            formattedText = formattedText.append(htmlStart).append(tag).append(htmlEnd);
            htmlStart.setLength(length);
        } else if ("PURPLE".equals(color)) {
            htmlStart = htmlStart.append("purple'>");
            formattedText = formattedText.append(htmlStart).append(tag).append(htmlEnd);
            htmlStart.setLength(length);
        } else if ("BLUE".equals(color)) {
            htmlStart = htmlStart.append("blue'>");
            formattedText = formattedText.append(htmlStart).append(tag).append(htmlEnd);
            htmlStart.setLength(length);
        } else if ("OLIVE".equals(color)) {
            htmlStart = htmlStart.append("olive'>");
            formattedText = formattedText.append(htmlStart).append(tag).append(htmlEnd);
            htmlStart.setLength(length);
        }

        return formattedText.toString();
    }

}
