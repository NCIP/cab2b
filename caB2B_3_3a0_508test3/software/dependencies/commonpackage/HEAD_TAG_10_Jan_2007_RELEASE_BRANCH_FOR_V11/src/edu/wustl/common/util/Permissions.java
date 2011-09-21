
package edu.wustl.common.util;

/**
 * Interface for storing the names of Permissions that are defined
 * in the authorization database.
 * 
 * 
 * @author Brian Husted
 */
public interface Permissions {

	public static final String READ = "READ";
	public static final String READ_DENIED = "READ_DENIED";
	public static final String UPDATE = "UPDATE";
	public static final String DELETE = "DELETE";
	public static final String CREATE = "CREATE";
	public static final String EXECUTE = "EXECUTE";
	public static final String USE = "USE";
	public static final String ASSIGN_READ = "ASSIGN_READ";
	public static final String ASSIGN_USE = "ASSIGN_USE";
	public static final String IDENTIFIED_DATA_ACCESS = "IDENTIFIED_DATA_ACCESS";
	public static final String USER_PROVISIONING = "USER_PROVISIONING";
	public static final String REPOSITORY_ADMINISTRATION = "REPOSITORY_ADMINISTRATION";
	public static final String STORAGE_ADMINISTRATION = "STORAGE_ADMINISTRATION";
	public static final String PROTOCOL_ADMINISTRATION = "PROTOCOL_ADMINISTRATION";
	public static final String DEFINE_ANNOTATION = "DEFINE_ANNOTATION";
	public static final String REGISTRATION = "REGISTRATION";
	public static final String SPECIMEN_ACCESSION = "SPECIMEN_ACCESSION";
	public static final String DISTRIBUTION = "DISTRIBUTION";
	public static final String QUERY = "QUERY";
	// public static final String PHI = "PHI";
	public static final String PHI = "PHI_ACCESS";
	public static final String PARTICIPANT_SCG_ANNOTATION = "PARTICIPANT_SCG_ANNOTATION";
	public static final String SPECIMEN_ANNOTATION = "SPECIMEN_ANNOTATION";
	public static final String SPECIMEN_PROCESSING = "SPECIMEN_PROCESSING";
	public static final String SPECIMEN_STORAGE = "SPECIMEN_STORAGE";
	public static final String GENERAL_SITE_ADMINISTRATION = "GENERAL_SITE_ADMINISTRATION";
	public static final String GENERAL_ADMINISTRATION = "GENERAL_ADMINISTRATION";
	public static final String SHIPMENT_PROCESSING = "SHIPMENT_PROCESSING";
	public static final String EXECUTE_QUERY="EXECUTE_QUERY";
}
