/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */

package edu.common.dynamicextensions.entitymanager;

import java.util.LinkedHashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;

/**
 * @author chetan_patil
 */
public class XMIElementsBuilder
{

	/**
	 * This method creates the XMI element along with its childrens
	 * XMI.header, XMI.content, and XMI.extensions.
	 * @param document - Holds the DOM Tree
	 * @return XMI element
	 * @throws XMIException
	 */
	public static Element createXMISkeleton(Document document)
			throws DynamicExtensionsApplicationException
	{
		// Create XMI root element
		LinkedHashMap<String, String> tagAttributeMap = new LinkedHashMap<String, String>();
		tagAttributeMap.put("xmlns:UML", "omg.org/UML1.3");
		tagAttributeMap.put("xmi.version", "1.3");
		String timeStamp = XMIBuilderUtil.getCurrentTimestamp();
		tagAttributeMap.put("timestamp", timeStamp);
		Element xmiRoot = XMIBuilderUtil.createElementNode(document, "XMI", tagAttributeMap, null);

		// Append XMI.header to XMI element
		Element xmiHeader = getXMIHeader(document);
		xmiRoot.appendChild(xmiHeader);

		// Append XMI.content to XMI element
		Element xmiContent = getXMIContent(document);
		xmiRoot.appendChild(xmiContent);

		// Append XMI.extensions to XMI element
		Element xmiExtensions = getXMIExtensions(document);
		xmiRoot.appendChild(xmiExtensions);

		return xmiRoot;
	}

	/**
	 * This method creates the XMI.header element along with its child XMI.documentation.
	 * @param document holds the DOM Tree
	 * @return XMI.header element
	 * @throws DynamicExtensionsApplicationException
	 */
	private static Element getXMIHeader(Document document)
			throws DynamicExtensionsApplicationException
	{
		// Create XMI.documentation element
		Element documentation = XMIBuilderUtil.createElementNode(document, "XMI.documentation",
				null, null);

		// Append XMI.exporter element
		//Element xmiExporter = XMIBuilderUtil.createElementNode(document, "XMI.exporter", null,
		//		ApplicationProperties.getValue("xmi.exporter"));
		Element xmiExporter = XMIBuilderUtil.createElementNode(document, "XMI.exporter", null,
				"Dynamic Extensions XMI Exporter");

		documentation.appendChild(xmiExporter);

		// Append XMI.exporterVersion element
		//Element xmiExporterVersion = XMIBuilderUtil.createElementNode(document,
		//		"XMI.exporterVersion", null, ApplicationProperties.getValue("xmi.exporterVersion"));
		Element xmiExporterVersion = XMIBuilderUtil.createElementNode(document,
				"XMI.exporterVersion", null, "1.0");
		documentation.appendChild(xmiExporterVersion);

		// Create XMI.header element
		Element header = XMIBuilderUtil.createElementNode(document, "XMI.header", null, null);
		// Append XMI.documentation element to XMI.header
		header.appendChild(documentation);

		return header;
	}

	/**
	 * This method creates the empty XMI.content element.
	 * @param document holds the DOM Tree
	 * @return XMI.content element
	 * @throws DynamicExtensionsApplicationException
	 */
	private static Element getXMIContent(Document document)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "XMI.content", null, null);
	}

	/**
	 * This method creates the empty XMI.extensions element.
	 * @param document holds the DOM Tree
	 * @return XMI.extensions element
	 * @throws DynamicExtensionsApplicationException
	 */
	private static Element getXMIExtensions(Document document)
			throws DynamicExtensionsApplicationException
	{
		// Create attribute list of XMI.extensions element
		LinkedHashMap<String, String> tagAttributeMap = new LinkedHashMap<String, String>();
		//tagAttributeMap.put("xmi.extender", ApplicationProperties.getValue("xmi.exporter") + " "
		//		+ ApplicationProperties.getValue("xmi.exporterVersion"));
		tagAttributeMap.put("xmi.extender", "Dynamic Extensions XMI Exporter 1.0");

		return XMIBuilderUtil.createElementNode(document, "XMI.extensions", tagAttributeMap, null);
	}

}
