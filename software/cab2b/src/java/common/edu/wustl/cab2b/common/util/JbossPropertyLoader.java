/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author gaurav_mehta
 */
public class JbossPropertyLoader {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(JbossPropertyLoader.class);

    /**
     * @param propertyfile
     * @return Property
     */
    public static Properties getPropertiesFromFile(String propertyfile) {
        Properties properties = null;
        String jbossConfLocation = System.getProperty("jboss.server.config.url");
        try {
            URL url = new URL(jbossConfLocation + File.separator + propertyfile);
            InputStream is = url.openStream();
            if (is == null) {
                logger.error("------------------------------------------------------");
                logger.error("Unable fo find property file : " + propertyfile
                        + "\n please put this file in JBoss Conf Folder");
                logger.error("------------------------------------------------------");
            }
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            logger.error("------------------------------------------------------");
            logger.error("Unable to load properties from : " + propertyfile);
            logger.error("------------------------------------------------------");
        }
        return properties;
    }

    /**
     * @return result size for UI
     */
    public static String getExportedResultsPath() {
        StringBuffer path = new StringBuffer();
        path.append(System.getProperty("jboss.server.base.dir"));
        path.append(File.separator);
        path.append(System.getProperty("jboss.server.name"));
        path.append(File.separator);
        path.append("cab2bExportFiles");
        path.append(File.separator);
        String dirPath = path.toString();
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dirPath;

    }
}
