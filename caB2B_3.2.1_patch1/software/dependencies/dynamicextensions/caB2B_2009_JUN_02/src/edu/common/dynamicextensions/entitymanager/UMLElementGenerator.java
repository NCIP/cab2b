/**
 * 
 */

package edu.common.dynamicextensions.entitymanager;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.util.UniqueIDGenerator;

/**
 * @author chetan_patil
 *
 */
public class UMLElementGenerator implements XMIBuilderConstantsInterface
{

	public static Element generateEAModelElement(Document document)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("name", "EA Model");
		elementAttributeMap.put("xmi.id", MODEL_ID_PREFIX + UniqueIDGenerator.getId());

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_UML_MODEL,
				elementAttributeMap, null);
	}

	public static Element generateNamespace_OwnedElement(Document document, String parentId,
			boolean isUMLPackage) throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		if (isUMLPackage)
		{
			elementAttributeMap.put("xmi.id", (parentId + "_fix_1"));
		}
		else
		{
			elementAttributeMap.put("xmi.id", (parentId + "_fix_0"));
		}

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_UML_NAMESPACE_OWNEDELEMENT,
				elementAttributeMap, null);
	}

	public static Element generateUMLTagggedElement(Document document, String modelElement,
			int propertyNumber, String tagName, String value)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		if (tagName.equals("documentation"))
		{
			elementAttributeMap.put("xmi.id", CLASS_ID_PREFIX + UniqueIDGenerator.getId());
		}
		else
		{
			elementAttributeMap.put("xmlns:UML", "href://org.omg/UML");
			elementAttributeMap.put("xmi.id", modelElement + "_fix_" + propertyNumber);
		}
		elementAttributeMap.put("tag", tagName);
		elementAttributeMap.put("modelElement", modelElement);
		elementAttributeMap.put("value", value);

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_UML_TAGGEDVALUE,
				elementAttributeMap, null);
	}

	public static Element generateEARootClassElement(Document document)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("name", "EARootClass");
		elementAttributeMap.put("xmi.id", CLASS_ID_PREFIX + UniqueIDGenerator.getId());
		elementAttributeMap.put("isRoot", "true");
		elementAttributeMap.put("isLeaf", "false");
		elementAttributeMap.put("isAbstract", "false");

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_UML_CLASS,
				elementAttributeMap, null);
	}

	public static LinkedHashMap<String, String> createUMLPackageProperties()
	{
		String timeStamp = XMIBuilderUtil.getCurrentTimestamp();
		LinkedHashMap<String, String> propertyMap = new LinkedHashMap<String, String>();
		propertyMap.put("created", timeStamp);
		propertyMap.put("modified", timeStamp);
		propertyMap.put("iscontrolled", "FALSE");
		propertyMap.put("lastloaddate", timeStamp);
		propertyMap.put("lastsavedate", timeStamp);
		propertyMap.put("isprotected", "FALSE");
		propertyMap.put("usedtd", "FALSE");
		propertyMap.put("logxml", "FALSE");
		propertyMap.put("batchsave", "0");
		propertyMap.put("batchload", "0");
		propertyMap.put("phase", "1.0");
		propertyMap.put("status", "Proposed");
		propertyMap.put("complexity", "1");
		propertyMap.put("ea_stype", "Public");

		return propertyMap;
	}

	public static Element generateUMLPackageElement(Document document, String packageName,
			boolean createUMLTaggedValues) throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("name", packageName);
		elementAttributeMap.put("xmi.id", PACKAGE_ID_PREFIX + UniqueIDGenerator.getId());
		elementAttributeMap.put("isRoot", "false");
		elementAttributeMap.put("isLeaf", "false");
		elementAttributeMap.put("isAbstract", "false");
		elementAttributeMap.put("visibility", "public");

		Element umlPackageElement = XMIBuilderUtil.createElementNode(document,
				ELEMENT_TYPE_UML_PACKAGE, elementAttributeMap, null);
		if (createUMLTaggedValues)
		{
			LinkedHashMap<String, String> propertyMap = createUMLPackageProperties();
			String modelElement = umlPackageElement.getAttribute("xmi.id");
			XMIBuilderUtil.appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);
		}

		return umlPackageElement;
	}

	public static LinkedHashMap<String, String> createUMLClassProperties(String packageId,
			String packageName, boolean isDataModel) //, String genType // for data model
	{
		String timeStamp = XMIBuilderUtil.getCurrentTimestamp();
		LinkedHashMap<String, String> propertyMap = new LinkedHashMap<String, String>();
		propertyMap.put("isSpecification", "false");
		propertyMap.put("ea_stype", "Class");
		propertyMap.put("ea_ntype", "0");
		propertyMap.put("version", "1.0");
		propertyMap.put("package", packageId);
		propertyMap.put("date_created", timeStamp);
		propertyMap.put("date_modified", timeStamp);
		propertyMap.put("tagged", "0");
		propertyMap.put("package_name", packageName);
		propertyMap.put("phase", "1.0");
		if (isDataModel)
		{
			propertyMap.put("gentype", "Oracle"); // TODO Must be dynamic
			propertyMap.put("stereotype", "table");
			propertyMap.put("product_name", "Oracle");
		}
		else
		{
			propertyMap.put("gentype", "Java");
		}
		propertyMap.put("complexity", "1");
		propertyMap.put("ea_eleType", "element");
		propertyMap.put("status", "Proposed");
		//propertyMap.put("ea_localid", ""); //787
		//propertyMap.put("eventflag", ""); //ATT=5b55;LNK=1d05;
		propertyMap
				.put("style",
						"BackColor=-1;BorderColor=-1;BorderWidth=-1;FontColor=-1;VSwimLanes=0;HSwimLanes=0;BorderStyle=0;");

		return propertyMap;
	}

	public static Element generateUMLClassElement(Document document, String className,
			Element parentPackage, boolean createUMLTaggedValues, boolean isDataModel)
			throws DynamicExtensionsApplicationException
	{
		String packageId = parentPackage.getAttribute("xmi.id");
		String packageName = parentPackage.getAttribute("name");

		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("name", className);
		elementAttributeMap.put("xmi.id", CLASS_ID_PREFIX + UniqueIDGenerator.getId());
		elementAttributeMap.put("namespace", packageId);
		elementAttributeMap.put("isRoot", "false");
		elementAttributeMap.put("isLeaf", "false");
		elementAttributeMap.put("isAbstract", "false");
		elementAttributeMap.put("isActive", "false");
		elementAttributeMap.put("visibility", "public");

		Element umlClassElement = XMIBuilderUtil.createElementNode(document,
				ELEMENT_TYPE_UML_CLASS, elementAttributeMap, null);
		if (createUMLTaggedValues)
		{
			LinkedHashMap<String, String> propertyMap = createUMLClassProperties(packageId,
					packageName, isDataModel);
			String modelElement = umlClassElement.getAttribute("xmi.id");
			XMIBuilderUtil.appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);
		}

		return umlClassElement;
	}

	public static Element generateClassifierFeatureElement(Document document, String classId)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.id", classId + "_fix_1");

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_UML_CLASSIFIER_FEATURE,
				elementAttributeMap, null);
	}

	public static Element generateUMLInterfaceElement(Document document, String interfaceName,
			Element parentPackage, boolean createUMLTaggedValues)
			throws DynamicExtensionsApplicationException
	{
		String packageId = parentPackage.getAttribute("xmi.id");
		String packageName = parentPackage.getAttribute("name");

		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("name", interfaceName);
		elementAttributeMap.put("xmi.id", CLASS_ID_PREFIX + UniqueIDGenerator.getId());
		elementAttributeMap.put("namespace", packageId);
		elementAttributeMap.put("isRoot", "false");
		elementAttributeMap.put("isLeaf", "false");
		elementAttributeMap.put("isAbstract", "false");
		elementAttributeMap.put("isActive", "false");
		elementAttributeMap.put("visibility", "public");

		Element umlInterfaceElement = XMIBuilderUtil.createElementNode(document,
				ELEMENT_TYPE_UML_INTERFACE, elementAttributeMap, null);
		if (createUMLTaggedValues)
		{
			LinkedHashMap<String, String> propertyMap = createUMLClassProperties(packageId,
					packageName, false);
			String modelElement = umlInterfaceElement.getAttribute("xmi.id");
			XMIBuilderUtil.appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);
		}

		return umlInterfaceElement;
	}

	public static LinkedHashMap<String, String> createUMLAttributeProperties(String type,
			boolean isCollection, String description, String logicalModelAttributePath,
			boolean isDataModel)
	{
		LinkedHashMap<String, String> propertyMap = new LinkedHashMap<String, String>();
		propertyMap.put("type", type);
		if (isCollection)
		{
			propertyMap.put("collection", "true");
			propertyMap.put("upperBound", "*");
		}
		else
		{
			propertyMap.put("collection", "false");
			propertyMap.put("upperBound", "1");
		}
		propertyMap.put("position", "0");
		propertyMap.put("lowerBound", "1");
		if (isDataModel)
		{
			propertyMap.put("ordered", "1");
			propertyMap.put("precision", "1");
			propertyMap.put("scale", "0");
			propertyMap.put("stereotype", "column");
			propertyMap.put("duplicates", "1");
			if (logicalModelAttributePath != null && !logicalModelAttributePath.equals(""))
			{
				propertyMap.put("mapped_attribute", logicalModelAttributePath);
			}
		}
		else
		{
			propertyMap.put("derived", "0");
			propertyMap.put("containment", "Not Specified");
			propertyMap.put("ordered", "0");
			propertyMap.put("duplicates", "0");
			propertyMap.put("styleex", "volatile=0;");
			propertyMap.put("description", description);
		}
		//propertyMap.put("ea_guid", "");
		//propertyMap.put("ea_localid", ""); //787

		return propertyMap;
	}

	public static Element generateUMLAttributeElement(Document document, String attributeName,
			String classifierFeatureId, int attributeCount, String defaultValue,
			boolean isCollection, String deXmiId, String description, String type,
			String logicalModelAttributePath, boolean isDataModel)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("name", attributeName);
		elementAttributeMap.put("changeable", "none");
		if (isDataModel)
		{
			elementAttributeMap.put("visibility", VISIBILITY_PUBLIC);
		}
		else
		{
			elementAttributeMap.put("visibility", VISIBILITY_PROTECTED);
		}
		elementAttributeMap.put("ownerScope", "instance");
		elementAttributeMap.put("targetScope", "instance");
		elementAttributeMap.put("xmi.id", classifierFeatureId + "_fix_" + attributeCount);

		Element umlAttributeElement = XMIBuilderUtil.createElementNode(document,
				ELEMENT_TYPE_UML_ATTRIBUTE, elementAttributeMap, null);

		LinkedHashMap<String, String> propertyMap = createUMLAttributeProperties(type,
				isCollection, description, logicalModelAttributePath, isDataModel);
		String modelElement = umlAttributeElement.getAttribute("xmi.id");
		XMIBuilderUtil.appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);

		return umlAttributeElement;
	}

	public static Element generateUMLStructuralFeature_MultiplicityElement(Document document,
			String umlAttributeXMIId, boolean isCollection)
			throws DynamicExtensionsApplicationException
	{
		// Create and UML:StructuralFeature.multiplicity element
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.id", (umlAttributeXMIId + "_fix_0"));

		return XMIBuilderUtil.createElementNode(document,
				ELEMENT_TYPE_UML_STRUCTURALFEATURE_MULTIPLICITY, elementAttributeMap, null);
	}

	public static Element generateUMLMultiplicityElement(Document document, String parentId,
			String minCardinality, String maxCardinality)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.id", (parentId + "_fix_0"));

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_UML_MULTIPLICITY,
				elementAttributeMap, null);
	}

	public static Element generateUMLMultiplicity_RangeElement(Document document, String parentId)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.id", (parentId + "_fix_0"));

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_UML_MULTIPLICITY_RANGE,
				elementAttributeMap, null);
	}

	public static Element generateUMLMultiplicityRangeElement(Document document, String parentId,
			String minCardinality, String maxCardinality)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("lower", minCardinality);
		elementAttributeMap.put("upper", maxCardinality);
		elementAttributeMap.put("xmi.id", (parentId + "_fix_0"));

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_UML_MULTIPLICITYRANGE,
				elementAttributeMap, null);
	}

	public static Element generateUMLAttribute_InitialValueElement(Document document,
			String umlAttributeXmiId) throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.id", (umlAttributeXmiId + "_fix_1"));

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_UML_ATTRIBUTE_INITIALVALUE,
				elementAttributeMap, null);
	}

	public static Element generateUMLExpressionElement(Document document, String parentId,
			String defaultValue) throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		if (defaultValue != null && !defaultValue.equals(""))
		{
			elementAttributeMap.put("body", defaultValue);
		}
		elementAttributeMap.put("xmi.id", parentId + "_fix_0");

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_UML_EXPRESSION,
				elementAttributeMap, null);
	}

	public static Element generateUMLStructuralFeature_TypeElement(Document document,
			String umlAttributeXMIId, String deXmiId) throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.id", (umlAttributeXMIId + "_fix_2"));

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_UML_STRUCTURALFEATURE_TYPE,
				elementAttributeMap, null);
	}

	public static Element generateFoundation_Core_ClassifierElement(Document document,
			String parentId, String deXmiId) throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.idref", deXmiId);
		elementAttributeMap.put("xmi.id", (parentId + "_fix_0"));

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_FOUNDATION_CORE_CLASSIFIER,
				elementAttributeMap, null);
	}

	public static LinkedHashMap<String, String> createUMLAssociationProperties(String direction,
			String sourceName, String targetName, boolean isDataModel)
	{
		LinkedHashMap<String, String> propertyMap = new LinkedHashMap<String, String>();
		propertyMap.put("style", "2");
		propertyMap.put("ea_type", ELEMENT_ASSOCIATION);
		propertyMap.put("direction", direction);
		propertyMap.put("linemode", "2");
		propertyMap.put("linecolor", "-1");
		propertyMap.put("linewidth", "0");
		propertyMap.put("seqno", "0");
		propertyMap.put("headStyle", "0");
		propertyMap.put("lineStyle", "0");
		if (isDataModel)
		{
			propertyMap.put("stereotype", "FK");
			propertyMap.put("styleex", "FKINFO=SRC=" + sourceName + ":DST=" + targetName);
			propertyMap.put("conditional", "FK");
		}
		/*else
		 {
		 propertyMap.put("ea_sourceName", sourceName);
		 propertyMap.put("ea_targetName", targetName);
		 propertyMap.put("ea_sourceType", ELEMENT_CLASS);
		 propertyMap.put("ea_targetType", ELEMENT_CLASS);
		 }*/
		//propertyMap.put("privatedata5", "");
		propertyMap.put("virtualInheritance", "0");

		return propertyMap;
	}

	public static Element generateUMLAssociationElement(Document document, String sourceClassName,
			String targetClassName, String direction, boolean isDataModel)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.id", CLASS_ID_PREFIX + UniqueIDGenerator.getId());
		elementAttributeMap.put("visibility", VISIBILITY_PUBLIC);
		elementAttributeMap.put("isRoot", "false");
		elementAttributeMap.put("isLeaf", "false");
		elementAttributeMap.put("isAbstract", "false");

		Element umlAssociationElement = XMIBuilderUtil.createElementNode(document,
				ELEMENT_TYPE_UML_ASSOCIATION, elementAttributeMap, null);

		LinkedHashMap<String, String> propertyMap = createUMLAssociationProperties(direction,
				sourceClassName, targetClassName, isDataModel);
		String modelElement = umlAssociationElement.getAttribute("xmi.id");
		XMIBuilderUtil.appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);

		return umlAssociationElement;
	}

	public static Element generateUMLAssociation_ConnectionElement(Document document,
			String associationId) throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.id", associationId + "_fix_1");

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_UML_ASSOCIATION_CONNECTION,
				elementAttributeMap, null);
	}

	public static Element generateUMLAssociationEndElement(Document document, String name,
			String type, String parentId, int index) throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("visibility", VISIBILITY_PUBLIC);
		elementAttributeMap.put("name", name);
		elementAttributeMap.put("aggregation", "none");
		elementAttributeMap.put("isOrdered", "false");
		elementAttributeMap.put("isNavigable", "false");
		elementAttributeMap.put("type", type);
		elementAttributeMap.put("xmi.id", parentId + "_fix_" + index);

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_UML_ASSOCIATIONEND,
				elementAttributeMap, null);
	}

	public static Element generateUMLAssociationEnd_MultiplicityElement(Document document,
			String parentId) throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.id", parentId + "_fix_0");

		return XMIBuilderUtil.createElementNode(document,
				ELEMENT_TYPE_UML_ASSOCIATIONEND_MULTIPLICITY, elementAttributeMap, null);
	}

	public static Element generateUMLDataTypeElement(Document document, String deXmiId,
			String dataTypeName) throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.id", deXmiId);
		if (dataTypeName != null)
		{
			elementAttributeMap.put("name", dataTypeName);
		}
		elementAttributeMap.put("visibility", VISIBILITY_PRIVATE);
		elementAttributeMap.put("isRoot", "false");
		elementAttributeMap.put("isLeaf", "false");
		elementAttributeMap.put("isAbstract", "false");

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_UML_DATATYPE,
				elementAttributeMap, null);
	}

	public static Element generateUMLStereotypeElement(Document document, String deXmiId,
			String name, String baseClass, ArrayList<String> xmiIdList, boolean isxmlnsUML,
			boolean isDataModel) throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		if (isxmlnsUML)
		{
			elementAttributeMap.put("xmlns:UML", "href://org.omg/UML");
			elementAttributeMap.put("visibility", VISIBILITY_PUBLIC);
		}
		else
		{
			elementAttributeMap.put("visibility", VISIBILITY_PRIVATE);
		}
		elementAttributeMap.put("xmi.id", deXmiId);

		if (isDataModel)
		{
			elementAttributeMap.put("name", name);
		}
		elementAttributeMap.put("isSpecification", "false");
		elementAttributeMap.put("isRoot", "false");
		elementAttributeMap.put("isLeaf", "false");
		elementAttributeMap.put("isAbstract", "false");
		elementAttributeMap.put("baseClass", baseClass);

		StringBuffer extendedElements = new StringBuffer();
		for (String xmiId : xmiIdList)
		{
			extendedElements.append(" " + xmiId + " ");
		}
		elementAttributeMap.put("extendedElements", extendedElements.toString());

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_UML_STEREOTYPE,
				elementAttributeMap, null);
	}

	public static LinkedHashMap<String, String> createUMLDependencyProperties()
	{
		LinkedHashMap<String, String> propertyMap = new LinkedHashMap<String, String>();
		propertyMap.put("style", "3");
		propertyMap.put("ea_type", ELEMENT_DEPENDENCY);
		propertyMap.put("direction", DIRECTION_SRC_DEST);
		propertyMap.put("linemode", "3");
		propertyMap.put("linecolor", "-1");
		propertyMap.put("linewidth", "0");
		propertyMap.put("seqno", "0");
		propertyMap.put("stereotype", "DataSource");
		propertyMap.put("headStyle", "0");
		propertyMap.put("lineStyle", "0");
		propertyMap.put("conditional", "DataSource");
		propertyMap.put("src_visibility", VISIBILITY_PUBLIC);
		propertyMap.put("src_aggregation", "0");
		propertyMap.put("src_isOrdered", "false");
		propertyMap.put("src_isNavigable", "false");
		propertyMap.put("src_containment", "Unspecified");
		propertyMap.put("dst_visibility", VISIBILITY_PUBLIC);
		propertyMap.put("dst_aggregation", "0");
		propertyMap.put("dst_isOrdered", "false");
		propertyMap.put("dst_isNavigable", "true");
		propertyMap.put("dst_containment", "Unspecified");
		propertyMap.put("virtualInheritance", "0");

		return propertyMap;
	}

	public static Element generateUMLDependencyElement(Document document)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.id", CLASS_ID_PREFIX + UniqueIDGenerator.getId());
		elementAttributeMap.put("visibility", VISIBILITY_PUBLIC);
		elementAttributeMap.put("isSpecification", "false");

		Element umlDependencyElement = XMIBuilderUtil.createElementNode(document,
				ELEMENT_TYPE_UML_DEPENDENCY, elementAttributeMap, null);

		LinkedHashMap<String, String> propertyMap = createUMLDependencyProperties();
		String modelElement = umlDependencyElement.getAttribute("xmi.id");
		XMIBuilderUtil.appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);

		return umlDependencyElement;
	}

	public static Element generateUMLDependency_ClientElement(Document document, String parentId)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.id", parentId + "_fix_0");

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_UML_DEPENDENCY_CLIENT,
				elementAttributeMap, null);
	}

	public static Element generateUMLDependency_SupplierElement(Document document, String parentId)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.id", parentId + "_fix_1");

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_UML_DEPENDENCY_SUPPLIER,
				elementAttributeMap, null);
	}

	public static Element generateFoundation_Core_ModelElement(Document document, String parentId,
			String classId) throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.idref", classId);
		elementAttributeMap.put("xmi.id", parentId + "_fix_0");

		return XMIBuilderUtil.createElementNode(document,
				ELEMENT_TYPE_FOUNDATION_CORE_MODELELEMENT, elementAttributeMap, null);
	}

	public static LinkedHashMap<String, String> createUMLOperationProperties(String stereoType)
	{
		LinkedHashMap<String, String> propertyMap = new LinkedHashMap<String, String>();
		propertyMap.put("const", "false");
		propertyMap.put("stereotype", stereoType);
		propertyMap.put("synchronised", "0");
		propertyMap.put("position", "0");
		propertyMap.put("returnarray", "0");
		propertyMap.put("pure", "0");
		//propertyMap.put("ea_guid", "");
		propertyMap.put("ea_localid", "2");

		return propertyMap;
	}

	public static Element generateUMLOperationElement(Document document, String operationName,
			String parentId, int index, String stereoType)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("name", operationName);
		elementAttributeMap.put("visibility", VISIBILITY_PUBLIC);
		elementAttributeMap.put("ownerScope", "instance");
		elementAttributeMap.put("isQuery", "false");
		elementAttributeMap.put("concurrency", "sequential");
		elementAttributeMap.put("xmi.id", parentId + "_fix_" + index);

		Element umlOperationElement = XMIBuilderUtil.createElementNode(document,
				ELEMENT_TYPE_UML_OPERATION, elementAttributeMap, null);

		LinkedHashMap<String, String> propertyMap = createUMLOperationProperties(stereoType);
		String modelElement = umlOperationElement.getAttribute("xmi.id");
		XMIBuilderUtil.appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);

		return umlOperationElement;
	}

	public static Element generateUMLBehavioralFeature_ParameterElement(Document document,
			String parentId) throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.id", parentId + "_fix_0");

		return XMIBuilderUtil.createElementNode(document,
				ELEMENT_TYPE_UML_BEHAVIORALFEATURE_PARAMETER, elementAttributeMap, null);
	}

	public static Element generateUMLParameterElement(Document document, String parentId,
			String name, String kind, int parameterIndex)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		if (kind.equals("in"))
		{
			elementAttributeMap.put("name", name);
		}
		elementAttributeMap.put("kind", kind);
		elementAttributeMap.put("visibility", "public");
		elementAttributeMap.put("xmi.id", parentId + "_fix_" + parameterIndex);

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_UML_PARAMETER,
				elementAttributeMap, null);
	}

	public static Element generateUMLParameter_TypeElement(Document document, String parentId)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.id", parentId + "_fix_0");

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_UML_PARAMETER_TYPE,
				elementAttributeMap, null);
	}

	public static Element generateUMLParameter_DefaultValueElement(Document document,
			String parentId) throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.id", parentId + "_fix_2");

		return XMIBuilderUtil.createElementNode(document, ELEMENT_TYPE_UML_PARAMETER_DEFAULTVALUE,
				elementAttributeMap, null);
	}
}
