/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.path;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.util.dbManager.DBUtil;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

/**
 * Loads all domain model XMLs present in given folder as specified in model.dir property.
 * This is used to load model from back end. It is not invoked during normal application execution. 
 * To use this, create a new file named "server.properties" having following properties
 * ---------------------------------------------- 
            model.dir=C:/caDSR_Models/2009_aug_14
            database.loader=edu.wustl.cab2b.server.util.DataFileLoader
            max.path.length=6
 * ----------------------------------------------
 *  Update hibernate.cfg.xml with database information
 *  Ensure hibernate.cfg.xml and above "server.properties" are present in classpath
 *      
 * @author chandrakant_talele
 */
public class LoadModelUtil {
    /**
     * Main method to load load model
     * @param args command line args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Started at : " + new Date());
        Properties p = Utility.getPropertiesFromFile("server.properties");
        int maxPathlength = Integer.parseInt(p.getProperty("max.path.length"));
        File dir = new File(p.getProperty("model.dir"));
        List<String> list = new ArrayList<String>();
        for (File f : dir.listFiles()) {
            if (isXML(f)) {
                list.add(f.getAbsolutePath());
            }
        }
        Connection con = null;
        try {
            con = DBUtil.getConnection();
            for (String path : list) {
                DomainModel domainModel = (DomainModel) Utils.deserializeDocument(path, DomainModel.class);
                System.out.println("Loading model : " + path);
                PathBuilder.loadSingleModel(con, path, domainModel.getProjectLongName(), maxPathlength);
                System.out.println("Finished at : " + new Date());
            }
        } finally {
            System.out.println("Finished at : " + new Date());
            if (con != null) {
                con.close();
            }
        }
    }

    private static boolean isXML(File f) {
        String path = f.getAbsolutePath();
        String onlyName = path.substring(path.lastIndexOf("\\") + 1, path.length());
        int idx = onlyName.lastIndexOf(".");
        String ext = "";
        if (idx > 0) {
            ext = onlyName.substring(idx + 1, onlyName.length());
        }
        return ext.equalsIgnoreCase("xml");
    }

}