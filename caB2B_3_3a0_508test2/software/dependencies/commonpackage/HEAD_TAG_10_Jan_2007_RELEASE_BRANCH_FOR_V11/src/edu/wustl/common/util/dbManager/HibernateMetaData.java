/*
 * Created on Sep 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.util.dbManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.ManyToOne;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Subclass;
import org.hibernate.mapping.Table;
import org.hibernate.proxy.HibernateProxy;

import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;


/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernateMetaData
{
	private static Configuration cfg;
	private static HashSet mappings=new HashSet();
	public static void initHibernateMetaData(Configuration configuration)
	{
		cfg = configuration;
//		This function finds all the relations and keeps in mappings set.
		//This Function is commented because it is unused code.
		//findRelations();
	}

	/**
	 * This method returns the list of subclasses of the className
	 * @author aarti_sharma
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static List getSubClassList(String className) throws ClassNotFoundException
	{
		List list = new ArrayList();
		//System.out.println("className :::"+ className);
		Class classObj = Class.forName(className);
		//System.out.println("classObj :::"+ classObj);
		PersistentClass classobj1 = cfg.getClassMapping(classObj.getName());
		Iterator it  =classobj1.getDirectSubclasses();
		//Iterator it = cfg.getClassMapping(classObj).getDirectSubclasses();
		while(it.hasNext())
		{
			Subclass subClass = (Subclass)it.next();

			System.out.println(subClass.getClass().getName());
			//System.out.println("Name "+subClass.getName());
			list.add(subClass.getClassName());
		}
		return list;
	}

	/**
	 * This method returns the super class of the obj passed
	 * @author aarti_sharma
	 * @param objClass
	 * @return
	 */
	public static Class getSuperClass(Object  obj)
	{
		Class objClass = obj.getClass();
		PersistentClass persistentClass = cfg.getClassMapping(objClass.getName());
		PersistentClass superClass = persistentClass.getSuperclass();
		return superClass.getClass();
	}

	/**
	 * This method returns the supermost class
	 * of the class passed that is in the same package as class
	 * @author aarti_sharma
	 * @param objClass
	 * @return
	 */
	public static Class getSupermostClassInPackage(Object obj) {
		Class objClass = obj.getClass();
		Package objPackage = objClass.getPackage();
		//Logger.out.debug("Input Class: " + objClass.getName()+" Package:"+objPackage.getName());

		PersistentClass persistentClass = cfg.getClassMapping(objClass.getName());
		if (persistentClass != null && persistentClass.getSuperclass()!=null) {

			//Logger.out.debug(objPackage.getName()+" "+persistentClass.getClassName()+"*********"+persistentClass.getSuperclass().getMappedClass().getPackage().getName());
//			Logger.out.debug("!!!!!!!!!!! "+persistentClass.getSuperclass().getMappedClass().getPackage().getName()
//					.equals(objPackage.getName()));
			do {
				persistentClass = persistentClass.getSuperclass();
			}while(persistentClass !=null);
//			Logger.out.debug("Supermost class in the same package:"
//					+ persistentClass.getMappedClass().getName());
		} else {
			return objClass;
		}
		return persistentClass.getMappedClass();
	}


	public static String getTableName(Class classObj)
	{
		
		Table tbl = cfg.getClassMapping(classObj.getName()).getTable();
		if(tbl!=null)
			return tbl.getName();
		return "";

	}
	public static String getRootTableName(Class classObj)
	{
		Table tbl = cfg.getClassMapping(classObj.getName()).getRootTable();
		if(tbl!=null)
			return tbl.getName();
		return "";

	}
	public static  String getClassName(String tableName)
	{
		Iterator it = cfg.getClassMappings();
		PersistentClass persistentClass;
		while(it.hasNext())
		{
			persistentClass = (PersistentClass) it.next();
			if(tableName.equalsIgnoreCase(persistentClass.getTable().getName()))
			{
				return persistentClass.getClassName();
			}
		}

		return "";
	}

	public static String getColumnName(Class classObj, String attributeName)
	{
		//Logger.out.debug("classObj, String attributeName "+classObj+" "+attributeName);
		Iterator it = cfg.getClassMapping(classObj.getName()).getPropertyClosureIterator();
		while(it.hasNext())
		{
			Property property = (Property)it.next();

			//Logger.out.debug("property.getName() "+property.getName());
			//System.out.println();
			//System.out.print("property.getName() "+property.getName()+" ");
			if(property!=null && property.getName().equals(attributeName))
			{
				//System.out.println("property.getColumnSpan() "+property.getColumnSpan());
				Iterator colIt = property.getColumnIterator();
				while(colIt.hasNext())
				{
					Column col = (Column)colIt.next();
					//System.out.println("col "+col.getName());
					return col.getName();
				}
			}
		}


		Property property = cfg.getClassMapping(classObj.getName()).getIdentifierProperty();
		//Logger.out.debug("property.getName() "+property.getName());
		if(property.getName().equals(attributeName))
		{
			Iterator colIt = property.getColumnIterator();//y("id").getColumnIterator();
			while(colIt.hasNext())
			{
				Column col = (Column)colIt.next();
				return col.getName();
				//System.out.println(col.getName());
			}
		}

		return "";
	}
	public static String getDataBaseName()
	{
		String dbName="";
		String dialect = cfg.getProperty("hibernate.dialect");
		if(dialect.toLowerCase().indexOf("oracle")!=-1)
		{
			dbName=Constants.ORACLE_DATABASE;
		}
		else if(dialect.toLowerCase().indexOf("mysql")!=-1)
		{
			dbName=Constants.MYSQL_DATABASE;
		}
		else if (dialect.toLowerCase().indexOf("postgresql") != -1)
		{
			dbName = Constants.POSTGRESQL_DATABASE;
		}
		else if (dialect.toLowerCase().indexOf("db2") != -1)
		{
			dbName = Constants.DB2_DATABASE;
		}
		return dbName;
	}
	public static void getDATA(Class classObj)
	{
		org.hibernate.mapping.Collection coll = cfg.getCollectionMapping("edu.wustl.catissuecore.domain.CollectionProtocolEvent.specimenRequirementCollection");
		//System.out.println(map);

		System.out.println(coll.getCollectionTable().getName());
		System.out.println(coll.getTable().getName());
		//System.out.println();

		Iterator it = coll.getColumnIterator();

		while(it.hasNext())
		{
			//org.hibernate.mapping.Set set = (org.hibernate.mapping.Set)it.next();
			System.out.println(it.next());

		}
	}

	/**
	 * This Function finds all the relations in i.e Many-To-Many and One-To-Many
	 * All the relations are kept in HashMap where key is formed as table1@table2@table_name@attributeName
	 * and value is Many-To-Many or One-To-Many
	 *
	 * @return Map
	 */
	private static void findRelations()
	{
		try
		{
			Iterator itr1=cfg.getCollectionMappings();

			while(itr1.hasNext())
			{
				Collection col=(Collection)itr1.next();

				if(col.getElement().getClass().getName().equals(ManyToOne.class.getName()))
				{
					saveRelations(col,"ManyToMany");
				}
				else
				{
					saveRelations(col,"OneToMany");
				}
			}
		}
		catch (Exception e)
		{
			//This line is commented because logger when not initialized properly throws NullPointerException
			//Logger.out.info("Error occured in fildAllRelations Function:"+e);
		}

	}

	/**This function saves the relation data in HashSet.
	 * @param col this is the collection which contains all data
	 * @param rel_type this is Many-To-Many ot Many-To-One
	 * @throws Exception
	 */
	private static void saveRelations(Collection col,String rel_type) throws Exception
	{
		String className=col.getOwner().getClassName();
		String relatedClassName=col.getElement().getType().getName();
		String roleAttribute=col.getRole();
		String relationType=rel_type;
		String relationTable=col.getElement().getTable().getName();
		String keyId=getKeyId(roleAttribute);
		String roleId=getRoleKeyId(roleAttribute);

		ClassRelationshipData hmc=new ClassRelationshipData(className,relatedClassName,roleAttribute,relationType,
											relationTable,keyId,roleId);
		mappings.add(hmc);

		List list1=HibernateMetaData.getSubClassList(col.getOwner().getClassName());
		for(int i=0;i<list1.size();i++)
		{
			hmc=new ClassRelationshipData(list1.get(i).toString(),relatedClassName,roleAttribute,relationType,
					relationTable,keyId,roleId);
			mappings.add(hmc);
		}

		List list2=HibernateMetaData.getSubClassList(col.getElement().getType().getName());
		for(int i=0;i<list2.size();i++)
		{
			hmc=new ClassRelationshipData(className,list2.get(i).toString(),roleAttribute,relationType,
					relationTable,keyId,roleId);
			mappings.add(hmc);
		}
	}


	/** This function checks weather relation is Many-Many
	 *
	 * @param classObj1
	 * @param classObj2
	 * @return true is relation is Many-Many otherwise false
	 */
	public static boolean isRelationManyToMany(Class classObj1,Class classObj2,String roleAttributeName)
	{
		ClassRelationshipData crd=new ClassRelationshipData(classObj1.getName(),classObj2.getName(),roleAttributeName);
		Iterator itr=mappings.iterator();

		while(itr.hasNext())
		{
			ClassRelationshipData crd1=(ClassRelationshipData)itr.next();
			if(crd1.equals(crd)&& crd1.getRelationType().equals("ManyToMany"))
			{
				return true;
			}
		}
		return false;
	}

	public static ClassRelationshipData getClassRelationshipData(Class classObj1,Class classObj2, String roleAttributeName)
	{
		ClassRelationshipData crd=new ClassRelationshipData(classObj1.getName(),classObj2.getName(),roleAttributeName);

		Iterator itr=mappings.iterator();

		while(itr.hasNext())
		{
			ClassRelationshipData crd1=(ClassRelationshipData)itr.next();
			if(crd1.equals(crd))
			{
				return crd1;

			}
		}
		return null;

	}
	/**This function returns the RoleClass for given attName
	 * @param attName
	 * @return RoleClass
	 */
	public static Class getRoleClass(String attName)
	{
		Iterator itr=mappings.iterator();

		while(itr.hasNext())
		{
			ClassRelationshipData crd=(ClassRelationshipData)itr.next();
			if(crd.getRoleAttribute().indexOf(attName)!=-1)
			{
				return Utility.getClassObject(crd.getRelatedClassName());

			}
		}
		return null;
	}

	/** This function returns the attributeName related to classObj1 and classObj2
	 *
	 * @param classObj1
	 * @param classObj2
	 * @return attName

		getFullyQulifiedRoleAttrName
	 */
	public static String getFullyQualifiedRoleAttName(Class classObj1,Class classObj2,String attName)
	{
		Iterator itr=mappings.iterator();
		while(itr.hasNext())
		{
			ClassRelationshipData crd=(ClassRelationshipData)itr.next();
			if(classObj1.getName().equals(crd.getClassName())&&classObj2.getName().equals(crd.getRelatedClassName())&& crd.getRoleAttribute().indexOf(attName)!=-1)
			{
				return crd.getRoleAttribute();
			}
		}
		return null;
	}


	/**
	 * This function gets the key Id
	 * from hibernate mappings and returns the value
	 * @param attributeName
	 * @return key Id
	 *
	 */
	public static String getKeyId(String attributeName)
	{
		org.hibernate.mapping.Collection col1 = cfg.getCollectionMapping(attributeName);
		Iterator keyIt = col1.getKey().getColumnIterator();
		while (keyIt.hasNext())
		{
			Column col = (Column) keyIt.next();
			return(col.getName());
		}

		return "";
	}
	/** This function returns the role Id
	 * from hibernate mapping and returns the value
	 * @param attributeName
	 * @return roleKeyId
	 *
	 */
	public static String getRoleKeyId(String attributeName)
	{
		org.hibernate.mapping.Collection col1 = cfg.getCollectionMapping(attributeName);
		Iterator colIt = col1.getElement().getColumnIterator();
		while (colIt.hasNext())
		{
			Column col = (Column) colIt.next();
			return(col.getName());
		}
		return "";
	}

	//Mandar:26-apr-06 start
//	Mandar : 26-Apr-06 : 872 : Column width
	/**
	 * @param classObj Name of the class.
	 * @param attributeName Name of the attribute.
	 * @return The width of the column. Returns width of the column or zero.
	 */
	public static int getColumnWidth(Class classObj, String attributeName)
	{
		Iterator it = cfg.getClassMapping(classObj.getName()).getPropertyClosureIterator();
		while(it.hasNext())
		{
			Property property = (Property)it.next();

			if(property!=null && property.getName().equals(attributeName))
			{
				Iterator colIt = property.getColumnIterator();
				while(colIt.hasNext())
				{
					Column col = (Column)colIt.next();
					return col.getLength();
				}
			}
		}
		// if attribute is not found than the default width will be 50.
		return 50;
	} // getColumnWidth


	/**
	 * This method will return domain object from proxy Object
	 * @param domainObject
	 * @return domain Object
	 */
	public static Object getProxyObjectImpl(Object domainObject)
	{
		if (domainObject instanceof HibernateProxy)
		{
			HibernateProxy hp  = (HibernateProxy)domainObject;
			Object obj = hp.getHibernateLazyInitializer().getImplementation();
			//Logger.out.debug(obj+" : obj");
			return (AbstractDomainObject)obj;
		}
        return domainObject;
	}

	//	Mandar:26-apr-06 end

	public static void main(String[] args) throws Exception
	{
		Variables.applicationHome = System.getProperty("user.dir");
		Logger.out = org.apache.log4j.Logger.getLogger("");
		PropertyConfigurator.configure(Variables.applicationHome+"\\WEB-INF\\src\\"+"ApplicationResources.properties");

		Logger.out.debug("here");

		DBUtil.currentSession();
		System.out.println(mappings.size());

	}
}