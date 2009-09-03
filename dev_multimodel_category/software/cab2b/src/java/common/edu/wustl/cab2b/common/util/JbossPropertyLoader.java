/**
 * 
 */
package edu.wustl.cab2b.common.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author gaurav_mehta
 *
 */
public class JbossPropertyLoader {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(JbossPropertyLoader.class);

    private static String jbossConfLocation = null;

    static {
        Map<String, String> environmentVariablesVsValues = System.getenv();
        String jbossHome = environmentVariablesVsValues.get("JBOSS_HOME");
        int endIndex = jbossHome.indexOf("bin");
        StringBuffer confPropertyLocation = new StringBuffer(jbossHome.subSequence(0, endIndex));
        confPropertyLocation.append("server").append(File.separator).append("default");
        confPropertyLocation.append(File.separator).append("conf");
        jbossConfLocation = confPropertyLocation.toString();
    }

    /**
     * @param propertyfile
     * @return Property
     */
    public static Properties getPropertiesFromFile(String propertyfile) {
        Properties properties = null;
        try {
            File file = new File(jbossConfLocation + File.separator + propertyfile);
            URL url = file.toURI().toURL();
            InputStream is = url.openStream();
            if (is == null) {
                logger.error("Unable fo find property file : " + propertyfile
                        + "\n please put this file in JBoss Conf Folder");
            }
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            logger.error("Unable to load properties from : " + propertyfile);
        }
        return properties;
    }
}
