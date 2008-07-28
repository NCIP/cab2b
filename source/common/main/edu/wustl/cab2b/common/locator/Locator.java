package edu.wustl.cab2b.common.locator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.Logger;
import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.util.PropertyLoader;

/**
 * This class is responsible for all bean look ups. <br>
 * This is a singleton class.Cloning is not supported for this class. The server
 * and JNDI used for lookup, can be configured by changing values in the file
 * "jndi.properties"
 * 
 * @author Chandrakant Talele
 * 
 */
public class Locator {
    private static Locator locator;

    /**
     * This is to enforce that Locator is a singleton class
     */
    protected Locator() {
    }

    /**
     * This method is thread safe
     * 
     * @return instance of Locator
     */
    public static synchronized Locator getInstance() {
        if (locator == null) {
            locator = new Locator();
        }
        return locator;
    }

    /**
     * @param ejbName EJB Name 
     * @return Returns the BusinessInterface
     * @throws LocatorException Any exception, occured during look up operation.
     *             The exception will be wrapped in LocatorException
     */
    public BusinessInterface locate(String ejbName) throws LocatorException {

        BusinessInterface businessInterface = null;
        try {
            final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(Locator.class);

            logger.debug("Contacting to :" + PropertyLoader.getJndiUrl());
            System.setProperty("java.naming.provider.url", PropertyLoader.getJndiUrl());
            System.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
            System.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
            Context ctx = new InitialContext();
            logger.debug("Looking For :" + ejbName);
            businessInterface = (BusinessInterface) ctx.lookup(ejbName);
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            throw new LocatorException(e.getMessage(), e, ErrorCodeConstants.SR_0001);
        } catch (ClassCastException e) {
            throw new LocatorException("There is no annotation of Remote Interface on bean", e,
                    ErrorCodeConstants.UN_XXXX);
        }

        return businessInterface;
    }

    /**
     * Cloning of locator is invalid
     * 
     * @see java.lang.Object#clone()
     */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
