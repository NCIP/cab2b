package edu.wustl.cab2b.common.locator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ejb.EJBHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.util.PropertyLoader;
import edu.wustl.common.util.logger.Logger;

/**
 * This class is responsible for all bean look ups. <br>
 * This is a singleton class.Cloning is not supported for this class.
 * The server and JNDI used for lookup, can be configured by changing
 * values in the file "jndi.properties" 
 * @author Chandrakant Talele
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
     * @return instance of Locator
     */
    public static synchronized Locator getInstance() {
        if (locator == null) {
            locator = new Locator();
        }
        return locator;
    }

    /**
     * @param ejbName
     *            EJB Name defined in "ejb-jar.xml".
     *            It is defined at <ejb-name>HelloHome</ejb-name>
     * @param homeClassForEJB
     *            This is home class for the EJB which caller want to locate.
     *            It is defined in "ejb-jar.xml" at <home>learn.ejb.HelloHome</home>
     * 
     * @return Returns the BusinessInterface
     * @throws LocatorException
     *             Any exception, occured during look up operation.
     *             The exception will be wrapped in LocatorException
     */
    public BusinessInterface locate(String ejbName, Class homeClassForEJB) throws LocatorException {
        Object obj = null;
        Logger.out.debug("Finding Bean : " + ejbName + "\n Home Interface is : " + homeClassForEJB.getName());
        try {
            //set system properties for NJDI
            System.setProperty("java.naming.factory.initial",
                               PropertyLoader.getProperty("java.naming.factory.initial"));
            System.setProperty("java.naming.factory.url.pkgs",
                               PropertyLoader.getProperty("java.naming.factory.url.pkgs"));
            System.setProperty("java.naming.provider.url", PropertyLoader.getProperty("java.naming.provider.url"));


            Context ctx = new InitialContext();
            obj = ctx.lookup(ejbName);
        } catch (NamingException e) {
            throw new LocatorException(e.getMessage(), e, ErrorCodeConstants.SR_0001);
        }
        
        EJBHome homeObject = (EJBHome) javax.rmi.PortableRemoteObject.narrow(obj, homeClassForEJB);
        
        Method method = null;
        try {
            method = homeObject.getClass().getDeclaredMethod("create", new Class[0]);
        } catch (SecurityException e) {
            throw new LocatorException(e);
        } catch (NoSuchMethodException e) {
            throw new LocatorException(e);
        }
        // invoke the create method
        BusinessInterface businessInterface = null;
        try {
            Object bean = method.invoke(homeObject, new Object[0]);
            businessInterface = (BusinessInterface) bean;
        } catch (IllegalArgumentException e) {
            throw new LocatorException(e);
        } catch (IllegalAccessException e) {
            throw new LocatorException(e);
        } catch (InvocationTargetException e) {
            throw new LocatorException(e);
        } catch (ClassCastException e) {
            throw new LocatorException("Required beans remote interface does not extends from business interface OR its businessinterface does not extend edu.wustl.cab2b.common.BusinessInterface", e, ErrorCodeConstants.UN_XXXX);
        }

        return businessInterface;
    }

    /**
     * Cloning of locator is invalid
     * @see java.lang.Object#clone()
     */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
