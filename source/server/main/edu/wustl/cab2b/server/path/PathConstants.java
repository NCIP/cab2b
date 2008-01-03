package edu.wustl.cab2b.server.path;

import java.io.File;

/**
 * These are the constants used by path building and path finding classes.
 * @author Chandrakant Talele
 */
public interface PathConstants {
    /**
     * This represents whether a table is to be crteated in Dynamic extension when entity is persisted.
     * If TRUE then a storage table will be created.
     * If FLASE then storage table will not be created.
     * Set this constant externally. This is by default FALSE
     */
    public static boolean CREATE_TABLE_FOR_ENTITY = false;

    /**
     * Represents the location where path file is generated.Used to populate PATH table
     */
    public static final String PATH_FILE_NAME = System.getProperty("user.home") + File.separator + "Paths.txt";

    /**
     * Represents the location where association file is generated.Used to populate ASSOCIATION table
     */
    public static final String ASSOCIATION_FILE_NAME = System.getProperty("user.home") + File.separator
            + "associations.txt";

    /**
     * Represents the location where InterModelAssociations file is generated.Used to populate INTER_MODEL_ASSOCIATION table
     */
    public static final String INTER_MODEL_ASSOCIATION_FILE_NAME = System.getProperty("user.home")
            + File.separator + "InterModelAssociations.txt";

    /**
     * Represents the location where IntraModelAssociations file is generated.Used to populate INTRA_MODEL_ASSOCIATION table
     */
    public static final String INTRA_MODEL_ASSOCIATION_FILE_NAME = System.getProperty("user.home")
            + File.separator + "IntraModelAssociations.txt";

    /**
     * Represents the String used as field separator while generating the file
     */
    public static final String FIELD_SEPARATOR = "###";

    /**
     * Represents the String used as record separator while generating the file
     */
    public static final String LINE_SEPERATOR = System.getProperty("line.separator");

    /**
     * Represents the String used as field connector to connect intermediate path Ids, while generating the file.
     */
    public static final String ID_CONNECTOR = "-";

    //Local URL : "http://localhost:8443/wsrf/services/cagrid/CaDSRService"; 
    /**
     * Represents the URL of caDSR service. This is location from which models are retrieved.
     * TODO It will be removed from here and it will be externally configurable.
     */
    public static final String CA_DSR_URL = "http://cagrid04.bmi.ohio-state.edu:7080/wsrf/services/cagrid/CaDSRService";

    /**
     * Enum to represents possible value which column "ASSOCIATION.ASSOCIATION_TYPE" can take.  
     * @author Chandrakant Talele
     */
    public enum AssociationType {
        INTER_MODEL_ASSOCIATION(1), INTRA_MODEL_ASSOCIATION(2);
        private int value;

        AssociationType(int value) {
            this.value = value;
        }

        /**
         * @return int value associated with enum
         */
        public int getValue() {
            return value;
        }

        /**
         * @param value Get type based in int value
         * @return
         */
        public static AssociationType getType(int value) {
            if (value == 1) {
                return AssociationType.INTER_MODEL_ASSOCIATION;
            }
            if (value == 2){
                return AssociationType.INTRA_MODEL_ASSOCIATION;
            }
            throw new IllegalArgumentException();
        }
    }
}