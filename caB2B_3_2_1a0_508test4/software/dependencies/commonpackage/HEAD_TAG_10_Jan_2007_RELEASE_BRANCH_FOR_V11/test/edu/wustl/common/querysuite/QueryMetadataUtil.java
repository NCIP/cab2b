package edu.wustl.common.querysuite;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.exceptions.SqlException;
import edu.wustl.common.util.dbManager.DBUtil;

/**
 * This class generates metadata for CP enhancements.
 * 
 * @author deepti_shelar
 * 
 */
public class QueryMetadataUtil {
	private static Connection connection = null;

	private static Statement stmt = null;

	private static HashMap<String, List<String>> entityNameAttributeNameMap = new HashMap<String, List<String>>();

	private static HashMap<String, String> attributeColumnNameMap = new HashMap<String, String>();

	private static HashMap<String, String> attributeDatatypeMap = new HashMap<String, String>();

	public static void main(String[] args)
			throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, MultipleRootsException,
			SqlException, HibernateException, SQLException, IOException {
		populateEntityAttributeMap();
		populateAttributeColumnNameMap();
		populateAttributeDatatypeMap();
		connection = DBUtil.getConnection();
		connection.setAutoCommit(true);
		stmt = connection.createStatement();

		Set<String> keySet = entityNameAttributeNameMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String entityName = (String) iterator.next();
			List<String> attributes = entityNameAttributeNameMap
					.get(entityName);
			for (String attr : attributes) {
				/*System.out.println("Adding attribute  " + attr + "--"
						+ entityName);
				*/String sql = "select max(identifier) from dyextn_abstract_metadata";
				ResultSet rs = stmt.executeQuery(sql);
				int nextIdOfAbstractMetadata = 0;
				if (rs.next()) {
					int maxId = rs.getInt(1);
					nextIdOfAbstractMetadata = maxId + 1;
				}
				int nextIdAttrTypeInfo = 0;
				sql = "select max(identifier) from dyextn_attribute_type_info";
				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					int maxId = rs.getInt(1);
					nextIdAttrTypeInfo = maxId + 1;
				}
				int nextIdDatabaseproperties = 0;
				sql = "select max(identifier) from dyextn_database_properties";
				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					int maxId = rs.getInt(1);
					nextIdDatabaseproperties = maxId + 1;
				}
				/*System.out.println(" generated ids");
				System.out.println("dyextn_abstract_metadata   : "
						+ nextIdOfAbstractMetadata);
				System.out.println("dyextn_attribute_type_info :"
						+ nextIdAttrTypeInfo);
				System.out.println("dyextn_database_properties  :"
						+ nextIdDatabaseproperties);*/

				sql = "INSERT INTO dyextn_abstract_metadata values("
						+ nextIdOfAbstractMetadata + ",NULL,NULL,NULL,'" + attr
						+ "',null)";
				executeInsertSQL(sql);
				int entityId = getEntityIdByName(entityName);
				sql = "INSERT INTO dyextn_attribute values ("
						+ nextIdOfAbstractMetadata + "," + entityId + ")";
				executeInsertSQL(sql);

				sql = "insert into `dyextn_primitive_attribute` (`IDENTIFIER`,`IS_COLLECTION`,`IS_IDENTIFIED`,`IS_PRIMARY_KEY`,`IS_NULLABLE`)"
						+ " values ("
						+ nextIdOfAbstractMetadata
						+ ",0,NULL,0,1)";
				executeInsertSQL(sql);

				sql = "insert into `dyextn_attribute_type_info` (`IDENTIFIER`,`PRIMITIVE_ATTRIBUTE_ID`) values ("
						+ nextIdAttrTypeInfo
						+ ","
						+ nextIdOfAbstractMetadata
						+ ")";
				executeInsertSQL(sql);

				String dataType = getDataTypeOfAttribute(attr);
				if (!dataType.equalsIgnoreCase("String")) {
					sql = "insert into `dyextn_numeric_type_info` (`IDENTIFIER`,`MEASUREMENT_UNITS`,`DECIMAL_PLACES`,`NO_DIGITS`) values ("
							+ nextIdAttrTypeInfo + ",NULL,0,NULL)";
					executeInsertSQL(sql);
				}

				if (dataType.equalsIgnoreCase("string")) {
					sql = "insert into `dyextn_string_type_info`  (`IDENTIFIER`) values ("
							+ nextIdAttrTypeInfo + ")";
				} else if (dataType.equalsIgnoreCase("double")) {
					sql = "insert into dyextn_double_type_info (`IDENTIFIER`) values ("
							+ nextIdAttrTypeInfo + ")";
				} else if (dataType.equalsIgnoreCase("int")) {
					sql = "insert into dyextn_integer_type_info (`IDENTIFIER`) values ("
							+ nextIdAttrTypeInfo + ")";
				}
				executeInsertSQL(sql);
				String columnName = getColumnNameOfAttribue(attr);
				sql = "insert into `dyextn_database_properties` (`IDENTIFIER`,`NAME`) values ("
						+ nextIdDatabaseproperties + ",'" + columnName + "')";
				executeInsertSQL(sql);
				sql = "insert into `dyextn_column_properties` (`IDENTIFIER`,`PRIMITIVE_ATTRIBUTE_ID`) values ("
						+ nextIdDatabaseproperties
						+ ","
						+ nextIdOfAbstractMetadata + ")";
				executeInsertSQL(sql);
			}
		}
		addAssociation(false);
		addAssociation(true);
	}

	private static void addAssociation(boolean isSwap) throws SQLException {
		String sql = "select max(identifier) from dyextn_abstract_metadata";
		ResultSet rs = stmt.executeQuery(sql);

		int nextIdOfAbstractMetadata = 0;
		if (rs.next()) {
			int maxId = rs.getInt(1);
			nextIdOfAbstractMetadata = maxId + 1;
		}
		int nextIdOfDERole = 0;
		sql = "select max(identifier) from dyextn_role";
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
			int maxId = rs.getInt(1);
			nextIdOfDERole = maxId + 1;
		}
		int nextIdOfDBProperties = 0;

		sql = "select max(identifier) from dyextn_database_properties";
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
			int maxId = rs.getInt(1);
			nextIdOfDBProperties = maxId + 1;
		}
		int nextIDintraModelAssociation = 0;
		sql = "select max(ASSOCIATION_ID) from intra_model_association";
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
			int maxId = rs.getInt(1);
			nextIDintraModelAssociation = maxId + 1;
		}
		
		int nextIdPath = 0;
		sql = "select max(PATH_ID) from path";
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
			int maxId = rs.getInt(1);
			nextIdPath = maxId + 1;
		}

		int entityId = 0;
		String entityName = "edu.wustl.catissuecore.domain.CollectionProtocol";
		sql = "select identifier from dyextn_abstract_metadata where name like '"
				+ entityName + "'";
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
			entityId = rs.getInt(1);

		}
		if (entityId == 0) {
			System.out.println("Entity not found of name ");
		}
		/*System.out.println(" generated ids");
		System.out.println("Fount entity id is         :" + entityId);
		System.out.println("dyextn_abstract_metadata   : "
				+ nextIdOfAbstractMetadata);
		System.out.println("nextIdOfDERole             : " + nextIdOfDERole);
		System.out.println("nextIdOfDBProperties       : "
				+ nextIdOfDBProperties);
		System.out.println("nextIDintraModelAssociatio : "
				+ nextIDintraModelAssociation);*/
	/*	System.out.println("nextIdDEAssociation        : "
				+ nextIdDEAssociation);*/
		
		sql = "insert into dyextn_abstract_metadata values ("
				+ nextIdOfAbstractMetadata
				+ ",null,null,null,'collectionProtocolSelfAssociation',null)";
		executeInsertSQL(sql);
		sql = "insert into dyextn_attribute values ("
				+ nextIdOfAbstractMetadata + "," + entityId + ")";
		executeInsertSQL(sql);
		sql = "insert into dyextn_role values (" + nextIdOfDERole
				+ ",'ASSOCIATION',2,0,'childCollectionProtocolCollection')";
		executeInsertSQL(sql);
		int roleId = nextIdOfDERole + 1;
		sql = "insert into dyextn_role values (" + roleId
				+ ",'ASSOCIATION',1,0,'parentCollectionProtocol')";
		executeInsertSQL(sql);
		if (!isSwap) {
			sql = "insert into dyextn_association values ("
					+ nextIdOfAbstractMetadata + ",'BI_DIRECTIONAL',"
					+ entityId + "," + roleId + "," + nextIdOfDERole + ",1)";
		} else {
			sql = "insert into dyextn_association values ("
					+ nextIdOfAbstractMetadata + ",'BI_DIRECTIONAL',"
					+ entityId + "," + nextIdOfDERole + "," + roleId + ",1)";
		}
		executeInsertSQL(sql);
		sql = "insert into dyextn_database_properties values ("
				+ nextIdOfDBProperties
				+ ",'collectionProtocolSelfAssociation')";
		executeInsertSQL(sql);
		sql = "insert into dyextn_constraint_properties values("
				+ nextIdOfDBProperties + ",'PARENT_CP_ID',null,"
				+ nextIdOfAbstractMetadata + ")";
		executeInsertSQL(sql);
		sql = "insert into association values("
			+ nextIDintraModelAssociation + ",2)";
		executeInsertSQL(sql);
		sql = "insert into intra_model_association values("
				+ nextIDintraModelAssociation + "," + nextIdOfAbstractMetadata + ")";
		executeInsertSQL(sql);
		sql = "insert into path values (" + nextIdPath + "," + entityId + ","
				+ nextIDintraModelAssociation + "," + entityId + ")";
		executeInsertSQL(sql);
	}

	private static String getColumnNameOfAttribue(String attr) {
		return attributeColumnNameMap.get(attr);
	}

	private static String getDataTypeOfAttribute(String attr) {
		return attributeDatatypeMap.get(attr);
	}

	private static void populateEntityAttributeMap() {
		List<String> attributes = new ArrayList<String>();
		attributes.add("offset");
		entityNameAttributeNameMap.put(
				"edu.wustl.catissuecore.domain.SpecimenCollectionGroup",
				attributes);
		attributes = new ArrayList<String>();
		attributes.add("offset");
		entityNameAttributeNameMap.put(
				"edu.wustl.catissuecore.domain.CollectionProtocolRegistration",
				attributes);
		attributes = new ArrayList<String>();
		attributes.add("type");
		attributes.add("sequenceNumber");
		attributes.add("studyCalendarEventPoint");
		entityNameAttributeNameMap.put(
				"edu.wustl.catissuecore.domain.CollectionProtocol", attributes);

	}

	private static void populateAttributeColumnNameMap() {
		attributeColumnNameMap.put("offset", "DATE_OFFSET");
		attributeColumnNameMap.put("type", "CP_TYPE");
		attributeColumnNameMap.put("sequenceNumber", "SEQUENCE_NUMBER");
		attributeColumnNameMap.put("studyCalendarEventPoint",
				"STUDY_CALENDAR_EVENT_POINT");
	}

	private static void populateAttributeDatatypeMap() {
		attributeDatatypeMap.put("offset", "int");
		attributeDatatypeMap.put("type", "string");
		attributeDatatypeMap.put("sequenceNumber", "int");
		attributeDatatypeMap.put("studyCalendarEventPoint", "double");
	}

	private static int executeInsertSQL(String sql) throws SQLException {
		int b;
		System.out.println(sql+";");
		b = stmt.executeUpdate(sql);
		return b;
	}

	private static int getEntityIdByName(String entityName) throws SQLException {
		ResultSet rs;
		int entityId = 0;
		String sql = "select identifier from dyextn_abstract_metadata where name like '"
				+ entityName + "'";
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
			entityId = rs.getInt(1);
		}
		if (entityId == 0) {
			System.out.println("Entity not found of name ");
		}
		return entityId;
	}
}
/*
 * insert into dyextn_abstract_metadata values
 * (1224,null,null,null,'collectionProtocolSelfAssociation',null) insert
 * into dyextn_attribute values (1224,177); insert into dyextn_role
 * values (897,'ASSOCIATION',2,0,'childCollectionProtocolCollection');
 * insert into dyextn_role values
 * (898,'ASSOCIATION',1,0,'parentCollectionProtocol'); insert into
 * dyextn_association values (1224,'BI_DIRECTIONAL',177,898,897,1)
 * insert into dyextn_database_properties values
 * (1222,'collectionProtocolSelfAssociation'); insert into
 * dyextn_constraint_properties values(1222,'PARENT_CP_ID',null,1224);
 * insert into intra_model_association values(664,1224) insert into path
 * values (985340,177,664,177);
 * 
 * insert into dyextn_abstract_metadata values
 * (1225,null,null,null,'collectionProtocolSelfAssociation',null) insert
 * into dyextn_attribute values (1225,177); insert into dyextn_role
 * values (899,'ASSOCIATION',2,0,'childCollectionProtocolCollection');
 * insert into dyextn_role values
 * (900,'ASSOCIATION',1,0,'parentCollectionProtocol'); insert into
 * dyextn_association values (1225,'BI_DIRECTIONAL',177,900,899,1)
 * insert into dyextn_database_properties values
 * (1223,'collectionProtocolSelfAssociation'); insert into
 * dyextn_constraint_properties values(1223,'PARENT_CP_ID',null,1225);
 * insert into intra_model_association values(665,1225) insert into path
 * values (985340,177,666,177);
 */