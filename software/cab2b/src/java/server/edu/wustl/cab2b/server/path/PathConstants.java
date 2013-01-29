/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.path;

import java.io.File;

/**
 * These are the constants used by path building and path finding classes.
 * @author Chandrakant Talele
 */
public interface PathConstants {
    /**
     * This represents whether a table is to be created in Dynamic extension when entity is persisted.
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
     * Represents the URL of caDSR service. This is location from which models are retrieved.
     * TODO It will be removed from here and it will be externally configurable.
     */
    public static final String CA_DSR_URL = "http://cagrid04.bmi.ohio-state.edu:7080/wsrf/services/cagrid/CaDSRService";
    
    /**
     * Name of metadata entity group
     */
    public static final String METADATA_ENTITY_GROUP = "MetadataEntityGroup";
}