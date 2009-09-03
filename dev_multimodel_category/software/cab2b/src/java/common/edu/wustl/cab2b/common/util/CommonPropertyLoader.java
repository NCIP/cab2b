package edu.wustl.cab2b.common.util;

import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * This class handles fetching properties from cab2b.properties file
 *
 * @author Chandrakant_Talele
 * @author lalit_chand
 */
public class CommonPropertyLoader {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(CommonPropertyLoader.class);

    public static final String propertyFileName = "cab2b.properties";
    
    private static Properties properties = Utility.getPropertiesFromFile(propertyFileName);
    

    /**
     * @return The URL of JNDI service running on caB2B server
     */
    public static String getJndiUrl() {
        String serverIP = properties.getProperty("caB2B.server.ip");
        String jndiPort = properties.getProperty("caB2B.server.port");
        String jndiProtocol = properties.getProperty("caB2B.server.jndi.protocol");
        String invokerURLSuffix = properties.getProperty("caB2B.server.jndi.invoker.url.suffix");
        return jndiProtocol+"://" + serverIP + ":" + jndiPort+invokerURLSuffix;
    }
    
    public static String getSSLTruststoreFileName(){
        return properties.getProperty("caB2B.ssl.truststore.filename");
    }
    
    public static String isIgnoreHttpHost(){
        return properties.getProperty("org.jboss.security.ignoreHttpsHost");
    }
    
    
    //https://localhost:8443/invoker/JNDIFactorySSL
    //"jnp://" + serverIP + ":" + jndiPort;
}
