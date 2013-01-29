/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */

package edu.common.dynamicextensions.entitymanager;

/**
 * @author chetan_patil
 *
 */
public interface XMIBuilderConstantsInterface
{

	final static String MODEL_ID_PREFIX = "DEMX_";
	final static String PACKAGE_ID_PREFIX = "DEPK_";
	final static String CLASS_ID_PREFIX = "DEID_";

	final static String VISIBILITY_PUBLIC = "public";
	final static String VISIBILITY_PRIVATE = "private";
	final static String VISIBILITY_PROTECTED = "protected";

	final static String DIRECTION_SRC_DEST = "Source -> Destination";
	final static String DIRECTION_BI = "Bi-Directional";

	final static String ELEMENT_PACKAGE = "Package";
	final static String ELEMENT_CLASS = "Class";
	final static String ELEMENT_ATTRIBUTE = "Attribute";
	final static String ELEMENT_ASSOCIATION = "Association";
	final static String ELEMENT_DEPENDENCY = "Dependency";
	final static String ELEMENT_OPERATION = "Operation";
	final static String ELEMENT_STEREOTYPE = "Stereotype";

	final static boolean ISNOT_DOCUMENTATION_TAG = false;
	final static boolean IS_DOCUMENTATION_TAG = true;

	final static String ELEMENT_TYPE_XMI = "XMI";
	final static String ELEMENT_TYPE_XMI_HEADER = "XMI.header";
	final static String ELEMENT_TYPE_XMI_DOCUMENTATION = "XMI.documentation";
	final static String ELEMENT_TYPE_XMI_EXPORTER = "XMI.exporter";
	final static String ELEMENT_TYPE_XMI_CONTENT = "XMI.content";
	final static String ELEMENT_TYPE_XMI_EXTENSIONS = "XMI.extensions";

	final static String ELEMENT_TYPE_FOUNDATION_CORE_CLASSIFIER = "Foundation.Core.Classifier";
	final static String ELEMENT_TYPE_FOUNDATION_CORE_MODELELEMENT = "Foundation.Core.ModelElement";
	final static String ELEMENT_TYPE_FOUNDATION_CORE_GENERALIZATION = "Foundation.Core.Generalization";

	final static String ELEMENT_TYPE_UML_MODEL = "UML:Model";
	final static String ELEMENT_TYPE_UML_NAMESPACE_OWNEDELEMENT = "UML:Namespace.ownedElement";
	final static String ELEMENT_TYPE_UML_CLASS = "UML:Class";
	final static String ELEMENT_TYPE_UML_CLASSIFIER_FEATURE = "UML:Classifier.feature";
	final static String ELEMENT_TYPE_UML_ATTRIBUTE = "UML:Attribute";
	final static String ELEMENT_TYPE_UML_ATTRIBUTE_INITIALVALUE = "UML:Attribute.initialValue";
	final static String ELEMENT_TYPE_UML_STRUCTURALFEATURE_MULTIPLICITY = "UML:StructuralFeature.Multiplicity";
	final static String ELEMENT_TYPE_UML_STRUCTURALFEATURE_TYPE = "UML:StructuralFeature.type";
	final static String ELEMENT_TYPE_UML_MULTIPLICITY = "UML:Multiplicity";
	final static String ELEMENT_TYPE_UML_MULTIPLICITY_RANGE = "UML:Multiplicity.range";
	final static String ELEMENT_TYPE_UML_MULTIPLICITYRANGE = "UML:MultiplicityRange";
	final static String ELEMENT_TYPE_UML_EXPRESSION = "UML:Expression";
	final static String ELEMENT_TYPE_UML_CLASSIFIER = "UML:Classifier";
	final static String ELEMENT_TYPE_UML_OPERATION = "UML:Operation";
	final static String ELEMENT_TYPE_UML_BEHAVIORALFEATURE_PARAMETER = "UML:BehavioralFeature.parameter";
	final static String ELEMENT_TYPE_UML_PARAMETER = "UML:Parameter";
	final static String ELEMENT_TYPE_UML_PARAMETER_TYPE = "UML:Parameter.type";
	final static String ELEMENT_TYPE_UML_PARAMETER_DEFAULTVALUE = "UML:Parameter.defaultValue";
	final static String ELEMENT_TYPE_UML_PACKAGE = "UML:Package";
	final static String ELEMENT_TYPE_UML_DATATYPE = "UML:DataType";
	final static String ELEMENT_TYPE_UML_TAGGEDVALUE = "UML:TaggedValue";
	final static String ELEMENT_TYPE_UML_INTERFACE = "UML:Interface";
	final static String ELEMENT_TYPE_UML_GENERALIZATION = "UML:Generalization";
	final static String ELEMENT_TYPE_UML_GENERALIZABLEENELEMENT_GENERALIZATION = "UML:GeneralizableElement.generalization";
	final static String ELEMENT_TYPE_UML_ASSOCIATION = "UML:Association";
	final static String ELEMENT_TYPE_UML_ASSOCIATION_CONNECTION = "UML:Association.connection";
	final static String ELEMENT_TYPE_UML_ASSOCIATIONEND = "UML:AssociationEnd";
	final static String ELEMENT_TYPE_UML_ASSOCIATIONEND_MULTIPLICITY = "UML:AssociationEnd.multiplicity";
	final static String ELEMENT_TYPE_UML_STEREOTYPE = "UML:Stereotype";
	final static String ELEMENT_TYPE_UML_DEPENDENCY = "UML:Dependency";
	final static String ELEMENT_TYPE_UML_DEPENDENCY_CLIENT = "UML:Dependency.client";
	final static String ELEMENT_TYPE_UML_DEPENDENCY_SUPPLIER = "UML:Dependency.supplier";

}
