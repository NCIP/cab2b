package edu.wustl.cab2bwebapp.util.caObr;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author chandrakant_talele
 */
public class PropertyLoader {
    private static final Logger logger = Logger.getLogger(PropertyLoader.class);

    private static final Properties properties;

    static {
        properties = getPropertiesFromFile("caObr-service.properties");
    }


    public static String getCaObrServiceURL() {
        return properties.getProperty("caobr.service.url");
    }

    
    /**
     * Loads properties from a property file present in classpath to java objects.
     * 
     * @param propertyfile Name of property file. 
     * @return Properties loaded from given file.
     */
    private static Properties getPropertiesFromFile(String propertyfile) {
        Properties properties = null;
        try {
            ClassLoader loader = PropertyLoader.class.getClassLoader();
            URL url = loader.getResource(propertyfile);

            InputStream is = url.openStream();
            if (is == null) {
                logger.info("Unable fo find property file in class path : " + propertyfile);
            }
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            logger.error("Unable to load properties from : " + propertyfile);
        }
        return properties;
    }
}