/*
 * Created on Aug 31, 2007
 * @author
 *
 */
package edu.common.dynamicextensions.xmi.exporter;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.entitymanager.DataTypeFactory;
import edu.common.dynamicextensions.entitymanager.EntityManagerConstantsInterface;
import edu.common.dynamicextensions.exception.DataTypeFactoryInitializationException;
import edu.common.dynamicextensions.util.global.Variables;


/**
 * @author preeti_lodha
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public enum DatatypeMappings {

	STRING(EntityManagerConstantsInterface.STRING_ATTRIBUTE_TYPE){
		public String getJavaClassMapping()
		{
			return "java.lang.String";
		}
		
	}
	, INTEGER(EntityManagerConstantsInterface.INTEGER_ATTRIBUTE_TYPE){
		public String getJavaClassMapping()
		{
			return "java.lang.Integer";
		}
	},
	BOOLEAN(EntityManagerConstantsInterface.BOOLEAN_ATTRIBUTE_TYPE){
		public String getJavaClassMapping()
		{
			return "java.lang.Boolean";
		}
	},
	LONG(EntityManagerConstantsInterface.LONG_ATTRIBUTE_TYPE){
		public String getJavaClassMapping()
		{
			return "java.lang.Long";
		}
	},
	SHORT(EntityManagerConstantsInterface.SHORT_ATTRIBUTE_TYPE){
		public String getJavaClassMapping()
		{
			return "java.lang.Short";
		}
	},
	DATE(EntityManagerConstantsInterface.DATE_ATTRIBUTE_TYPE){
		public String getJavaClassMapping()
		{
			return "java.util.Date";
		}
	},
	FLOAT(EntityManagerConstantsInterface.FLOAT_ATTRIBUTE_TYPE){
		public String getJavaClassMapping()
		{
			return "java.lang.Float";
		}
	},
	DOUBLE(EntityManagerConstantsInterface.DOUBLE_ATTRIBUTE_TYPE){
		public String getJavaClassMapping()
		{
			return "java.lang.Double";
		}
	};

	String value;

	DatatypeMappings(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}

	public static DatatypeMappings get(String value)
	{
		DatatypeMappings[] datatypeMappings = DatatypeMappings.values();

		for (DatatypeMappings datatypeMapping : datatypeMappings)
		{
			if (datatypeMapping.getValue().equalsIgnoreCase(value))
			{
				return datatypeMapping;
			}
		}
		return null;
	}
	public String getJavaClassMapping()
	{
		System.out.println("Java Mapping for datatype not found");
		return null;
	}
	public String getSQLClassMapping()   throws DataTypeFactoryInitializationException
	{
		return DataTypeFactory.getInstance().getDatabaseDataType(value);
	}
	
	public static void main(String[] args) throws DataTypeFactoryInitializationException
	{
		AttributeInterface a = DomainObjectFactory.getInstance().createFloatAttribute();
		Variables.databaseName="MYSQL";
		DatatypeMappings d = DatatypeMappings.get(a.getDataType());
//		System.out.println(d.getJavaClassMapping());
//		System.out.println(d.getSQLClassMapping());
	}
}
