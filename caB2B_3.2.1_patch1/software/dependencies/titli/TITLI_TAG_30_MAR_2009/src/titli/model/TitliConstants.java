/**
 * 
 */
package titli.model;

/**
 * The constants used by TiTLi 
 * @author Juber Patel
 *
 */
public interface TitliConstants 
{
	/**
	 * the name of the properties file containing configurations for TiTLi 
	 */
	String PROPERTIES_FILE="titli.properties";
	
	/**
	 *the delimiter regular expression used to read a property that has a list of values.
	 *eg. JDBC Drivers 
	 */
	String PROPERTIES_FILE_DELIMITER_PATTERN="\\s*,\\s*";
	
	
	/**
	 * the name of the property that holds a comma separated list of JDBC drivers
	 */
	String JDBC_DRIVERS="jdbc.drivers";
	
	
	/**
	 * the name of the property that holds the path to the properties file
	 */
	String TITLI_INDEX_LOCATION="titli.index.location";
	
	
	/**
	 * the name of the property that holds the comma separated list of databases to be indexed and searched
	 */
	String JDBC_DATABASES="jdbc.databases";
	
	
	/**
	 * the name of the field in the Lucene Document that holds the name of the database
	 */
	String DOCUMENT_DATABASE_FIELD="database";
	
	
	/**
	 * the name of the field in the Lucene Document that holds the name of the table
	 */
	String DOCUMENT_TABLE_FIELD="table";
	
	
	/**
	 * the name of the field in the Lucene Document that holds the content of the record
	 */
	String DOCUMENT_CONTENT_FIELD="content";
	
	
	/**
	 * the suffix to be added to the table name to derive the name of the directory that holds its index 
	 */
	String INDEX_DIRECTORY_SUFFIX="_index";
	
	/**
	 * the merge factor for indexing
	 */
	int INDEX_MERGE_FACTOR=100;
	
	
	/**
	 * the max buffered docs for indexing 
	 */
	int INDEX_MAX_BUFFERED_DOCS=100;
	
	String DBTYPE_ORACLE="oracle";
	
	String DBTYPE_MYSQL="mysql";
	
	String DBTYPE_POSTGRESQL="postgresql";
	
		
	
}
