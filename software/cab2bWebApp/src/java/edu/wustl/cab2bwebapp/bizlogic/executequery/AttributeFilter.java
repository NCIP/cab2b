/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.bizlogic.executequery;



import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * Class that takes attributes names to be shown on spreadsheet.
 * Takes input from <FileName>attribute_filtering.properties</FileName>. 
 * @author chandrakant_talele
 */
public class AttributeFilter {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(AttributeFilter.class);
    private static Properties properties = null;

    static {
        URL url = AttributeFilter.class.getClassLoader().getResource("attribute_filtering.properties");
        InputStream is;
        try {
            is = url.openStream();
            if (is == null) {
                logger.error("Unable fo find attribute_filtering.properties");
                logger.error("Please put this file in classpath");
            }
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            logger.error("Unable fo load attribute_filtering.properties");
            e.printStackTrace();
        }
    }

    /**
     * @param attribute
     * @return TRUE if the attribute is to be shown.
     */
    public static boolean isVisible(AttributeInterface attribute) {
        String entityName = attribute.getEntity().getName();
        String attributeName = attribute.getName();
        String attributesToShow = properties.getProperty(entityName);
        //if no entry found, it means everything is to be shown
        if (attributesToShow == null) {
            return true;
        }
        String[] allowesAttributes = attributesToShow.split(",");
        for (String allowesAttribute : allowesAttributes) {
            if (attributeName.equals(allowesAttribute)) {
                return true;
            }
        }
        return false;
    }
}