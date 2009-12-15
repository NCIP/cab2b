package edu.wustl.cab2b.common.errorcodes;

/**
 * This interface contains the constants for the error codes to be used in the code throughout the application. Any
 * error code constant added here should have a corresponding entry in the errorcodes.properties file.
 * 
 * @author gautam_shetty
 */
public interface ErrorCodeConstants {
    /** User fetched with incomplete data. */
    // static final String UR_0001 = "UR.0001";
    /** User already exists */
    static final String UR_0002 = "UR.0002";

    /** Error while fetching user from database */
    static final String UR_0003 = "UR.0003";

    /** Error while inserting user in database */
    static final String UR_0004 = "UR.0004";

    /** Error while updating user information in database */
    static final String UR_0005 = "UR.0005";

    /** Please recheck identity provider url */
    static final String UR_0006 = "UR.0006";

    /** Unable to authenticate: Invalid credentials */
    static final String UR_0007 = "UR.0007";

    /** Please recheck dorian url */
    static final String UR_0008 = "UR.0008";

    /** Please check the credentials again (User name is case sensitive) */
    static final String UR_0009 = "UR.0009";

    //---------------------------------------------------------------------
    /** Unable to parse domain model XML file. */
    static final String GR_0001 = "GR.0001";

    //---------------------------------------------------------------------
    /** Unable to persist Entity Group in Dynamic Extension. */
    static final String DE_0001 = "DE.0001";

    /** Unable to persist Entity in Dynamic Extension. */
    static final String DE_0002 = "DE.0002";

    /** Inconsistent data in database */
    static final String DE_0003 = "DE.0003";

    /** Unable to retrive Dynamic Extension objects */
    static final String DE_0004 = "DE.0004";

    //---------------------------------------------------------------------
    /** Database down. */
    static final String DB_0001 = "DB.0001";

    /** Unable to create a connection from datasource */
    static final String DB_0002 = "DB.0002";

    /** Exception while firing Parameterized query.* */
    static final String DB_0003 = "DB.0003";

    /** Exception while firing Update query.* */
    static final String DB_0004 = "DB.0004";

    /** Exception while editing saved query.* */
    static final String DB_0005 = "DB.0005";

    /** Error while loading path information into database.* */
    static final String DB_0006 = "DB.0006";

    /** Error while no service url available into database.* */
    static final String DB_0007 = "DB.0007";

    // ---------------------------------------------------------------------
    /** Cab2b server down. */
    static final String SR_0001 = "SR.0001";

    //---------------------------------------------------------------------
    /** File operation failed */
    static final String IO_0001 = "IO.0001";

    /** Can't find resource bundle. */
    static final String IO_0002 = "IO.0002";

    /** XML parse error. */
    static final String IO_0003 = "IO.0003";

    //---------------------------------------------------------------------
    /** Java Reflection API Error. */
    static final String RF_0001 = "RF.0001";

    //---------------------------------------------------------------------
    /** Unknown Error in the Application (Can be used for app. development). */
    static final String UN_XXXX = "UN.XXXX";

    //---------------------------------------------------------------------
    /** Unable to look up resource from JNDI */
    static final String JN_0001 = "JN.0001";

    /** Invalid input query object */
    static final String QUERY_INVALID_INPUT = "QM.0001";

    /** Critical error encountered when accessing the caGrid infrastructure.\nPlease report this to the administrator */
    //static final String QUERY_EXECUTION_ERROR = "QM.0002";
    /** Error occurred while saving query.\nPlease report this to the administrator */
    //static final String QUERY_SAVE_ERROR = "QM.0005";
    /** Error occurred while retrieving query.\nPlease report this to the administrator */
    //static final String QUERY_RETRIEVE_ERROR = "QM.0006";
    /** Error while saving category */
    static final String CATEGORY_SAVE_ERROR = "CT.0001";

    /** Error while retrieving category */
    static final String CATEGORY_RETRIEVE_ERROR = "CT.0002";

    /** Can not create Custom Data Categories as Data List contains Admin defined categories */
    static final String CUSTOM_CATEGORY_ERROR = "CT.0003";

    /** Error while saving data list */
    static final String DATALIST_SAVE_ERROR = "DL.0001";

    /** Error while retrieving data list */
    static final String DATALIST_RETRIEVE_ERROR = "DL.0002";

    /** Error while saving data category */
    static final String DATACATEGORY_SAVE_ERROR = "DC.001";

    /** Please connect all nodes before proceed */
    static final String QM_0003 = "QM.0003";

    /** Error occurred while querying URL: */
    static final String QM_0004 = "QM.0004";

    /** Fatal error occurred while launching caB2B client.\nPlease contact administrator */
    //     static final String CA_0001 = "CA.0001";
    /** Search string cannot be null */
    //     static final String CA_0007 = "CA.0007";
    /**
     * Error Regarding CDS service
     */
    /** Credential delegation failed */
    //static final String CDS_001 = "CDS.001";
    /** Delegated Credential's serialization failed */
    //     static final String CDS_002 = "CDS.002";
    /** Unable to copy CA certificates to [user.home]/.globus */
    static final String CDS_003 = "CDS.003";

    /** Error occurred while generating globus certificates */
    static final String CDS_004 = "CDS.004";

    /** Unable to serialize the delegated credentials */
    static final String CDS_005 = "CDS.005";

    /** An unknown internal error occurred at CDS while delegating the credentials */
    static final String CDS_006 = "CDS.006";

    /** Error occurred while delegating the credentials */
    static final String CDS_007 = "CDS.007";

    /** The server doesn't have permission to acess the client's credentials */
    static final String CDS_008 = "CDS.008";

    /** Incorrect CDS URL. Please check the CDS URL in conf/client.properties */
    static final String CDS_009 = "CDS.009";

    /** Please check the dorian URL */
    static final String CDS_010 = "CDS.010";

    /** Error occurred at Dorian while obtaining GlobusCredential */
    static final String CDS_011 = "CDS.011";

    /** Invalid SAMLAssertion. Please check the Dorian URL and user's credentials. */
    static final String CDS_012 = "CDS.012";

    /** Error occurred due to invalid proxy. */
    static final String CDS_013 = "CDS.013";

    /** Incorrect user policy set for the proxy. */
    static final String CDS_014 = "CDS.014";

    /** You have insufficient permissions. Please contact Dorian Administrator. */
    static final String CDS_015 = "CDS.015";

    /** Could not find CA certificates */
    static final String CDS_016 = "CDS.016";

    /** Unable to delegate the credential to CDS */
    static final String CDS_017 = "CDS.017";

    /** Unable to generate GlobusCredential */
    static final String CDS_018 = "CDS.018";

    /** Unable to authenticate the user */
    static final String CDS_019 = "CDS.019";

    /** Unable to create the authentication client */
    static final String CDS_020 = "CDS.020";

    /* Experiment related error codes */
    /** Error occurred while saving Experiment */
    //static final String EX_001 = "EX.001";
    /** Error occurred while retrieving Experiment */
    static final String EX_002 = "EX.002";

    /** Error occurred while saving ExperimentGroup */
    //static final String EX_003 = "EX.003";
    /** Error occurred while retrieving ExperimentGroup */
    static final String EX_004 = "EX.004";

    /** Error occurred while retrieving ModelGroup */
    static final String MG_001 = "MG.001";
    
    /** Unauthorized user trying to save Model Group */
    static final String MG_002 = "MG.002";
    
    /** Unauthorized user trying to delete Model Group */
    static final String MG_003 = "MG.003";
    
    /** Unauthorized user trying to modify Model Group */
    static final String MG_004 = "MG.004";
    
    /** Error occur while saving Model Group*/
    static final String MG_005 = "MG.005";
    
    /** Error occur while deleting Model Group*/
    static final String MG_006 = "MG.006";
    
    /** Error occur while modifying Model Group*/
    static final String MG_007 = "MG.007";
}
