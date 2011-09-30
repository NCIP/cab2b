/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */

package edu.common.dynamicextensions.entitymanager;

public interface EntityManagerConstantsInterface
{


	String DIRECTION_SRC_DESTINATION = "SRC_DESTINATION";
	String DIRECTION_BI_DIRECTIONAL = "BI_DIRECTIONAL";

	static final int DATA_TABLE_STATE_CREATED = 1;
	static final int DATA_TABLE_STATE_NOT_CREATED = 2;
	static final int DATA_TABLE_STATE_ALREADY_PRESENT = 3;

	String ID_ATTRIBUTE_NAME = "id";
	String STRING_ATTRIBUTE_TYPE = "String";
	String FLOAT_ATTRIBUTE_TYPE = "Float";
	String SHORT_ATTRIBUTE_TYPE = "Short";
	String BOOLEAN_ATTRIBUTE_TYPE = "Boolean";
	String FILE_ATTRIBUTE_TYPE = "File";
	String DATE_ATTRIBUTE_TYPE = "Date";
	String DATE_TIME_ATTRIBUTE_TYPE = "DateTime";
	String DOUBLE_ATTRIBUTE_TYPE = "Double";
	String LONG_ATTRIBUTE_TYPE = "Long";
    String INTEGER_ATTRIBUTE_TYPE = "Integer";
    String OBJECT_ATTRIBUTE_TYPE = "Object";
    String BYTE_ARRAY_ATTRIBUTE_TYPE = "ByteArray";
    String ACTIVITY_STATUS_ATTRIBUTE_NAME = "activityStatus";

    String CAB2B_ENTITY_GROUP = "caB2BEntityGroup";
    String PACKAGE_NAME = "PackageName";
}
