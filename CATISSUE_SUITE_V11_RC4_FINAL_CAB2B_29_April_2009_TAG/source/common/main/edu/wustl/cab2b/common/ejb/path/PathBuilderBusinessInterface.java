package edu.wustl.cab2b.common.ejb.path;

import java.rmi.RemoteException;

import edu.wustl.cab2b.common.BusinessInterface;

/**
 * @author Chandrakant Talele
 */
public interface PathBuilderBusinessInterface extends BusinessInterface {
    /**
     * This method is to be called at server startup.
     * It builds all non-redundent paths for traversal between classes of all configured models.
     * It writes all paths to a datafile first for all the supported models then stores all paths to database.
     * @throws RemoteException EJB specific exception
     */
    public void init() throws RemoteException;
}
