/*
 * Created on Aug 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.common.cde;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.common.bizlogic.CDEBizLogic;
import edu.wustl.common.cde.xml.XMLCDE;
import edu.wustl.common.cde.xml.XMLPermissibleValueType;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.EmailHandler;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CDECacheManager
{
    /**
     * @param cdeXMLMAP Map of xmlCDEs configured by the user in the CDEConfig.xml.
     * The refresh() method accepts the XMLCDEs map and iterates through the map. A CDE is downloaded from the server and 
     * depending on the XMLCDEs object configuration, it is stored in the database. An email is sent to the administrator regarding the download status
     * of the CDEs. The errors if generated are reported to the administrator by email.
     */
    public void refresh(Map cdeXMLMAP)
    {
    	Logger.out.info("Initializing CDE Cache Manager");
        CDEDownloader cdeDownloader = null;
        List downloadedCDEList = new ArrayList();
        List errorLogs = new ArrayList();
        
        try
        {
            cdeDownloader = new CDEDownloader();
            cdeDownloader.connect();
            Logger.out.info("Connected to the server successfully..." );
        }
        catch (Exception exp)
        {
            //Logging the error message.
            errorLogs.add(exp.getMessage());
            
            Logger.out.error(exp.getMessage(),exp);
            //Send the error logs to administrator.
            sendCDEDownloadStatusEmail(errorLogs);
            return;
        }
        
        Set xmlCDEMapKeySet = cdeXMLMAP.keySet();
        Iterator it = xmlCDEMapKeySet.iterator();
        while (it.hasNext())
        {
            Object key = it.next();
            XMLCDE xmlCDE = (XMLCDE) cdeXMLMAP.get(key);
            if (xmlCDE.isCache())
            {
                try
                {
                    CDE cde = cdeDownloader.downloadCDE(xmlCDE);
                    Logger.out.info(cde.getLongName() + " : CDE download successful ... ");
                    
                    //Sets the parent permissible values and the CDEs for all the permissible values.
                    configurePermissibleValues(cde, xmlCDE);
                    downloadedCDEList.add(cde);
                }
                catch(Exception exp)
                {
                    errorLogs.add(exp.getMessage());
                    Logger.out.error(exp.getMessage(),exp);
                }
            }
        }
        
        // Send the errors logs while downloading the CDEs to the administrator.
        sendCDEDownloadStatusEmail(errorLogs);
        
        //Inserting the downloaded CDEs into database.
        CDEBizLogic cdeBizLogic = new CDEBizLogic();
        Iterator iterator = downloadedCDEList.iterator();
        while (iterator.hasNext())
        {
            CDE cde = (CDE) iterator.next();
            try
            {
                cdeBizLogic.insert(cde, null, Constants.HIBERNATE_DAO);
                Logger.out.debug(cde.getLongName() + " : CDE inserted in database ... ");
            }
            catch (UserNotAuthorizedException userNotAuthExp)
            {
                errorLogs.add(userNotAuthExp.getMessage());
                Logger.out.error(userNotAuthExp.getMessage(),userNotAuthExp);
            }
            catch (BizLogicException bizLogicExp)
            {
                errorLogs.add(bizLogicExp.getMessage());
                Logger.out.error(bizLogicExp.getMessage(),bizLogicExp);
            }
        }
    }
    
    /**
     * Sends an email containing the error logs occured while downloading the CDEs to the administrator.
     * @param errorLogs The list of errors.
     */
    private void sendCDEDownloadStatusEmail(List errorLogs)
    {
        if (!errorLogs.isEmpty())
        {
            EmailHandler emailHandler = new EmailHandler();
            emailHandler.sendCDEDownloadStatusEmail(errorLogs);
        }
    }

    /**
     * Sets the parent permissible values for each of the permissible value of the CDE 
     * depending on the parent-child relationships present in the XMlCDE provided. 
     * @param cde The CDE whose permissible values are to be configured.
     * @param xmlCDE The XMLCDE object for the cde which contains the parent-child relationships between the 
     * 				 permissible values.  
     */
    private void configurePermissibleValues(CDE cde, XMLCDE xmlCDE)
    {
        Set configuredPermissibleValues = new HashSet();
        Set permissibleValues = cde.getPermissibleValues();
        
        Iterator iterator = xmlCDE.getXMLPermissibleValues().iterator();
        while (iterator.hasNext())
        {
            XMLPermissibleValueType xmlPermissibleValueType = (XMLPermissibleValueType) iterator.next();
            
            //The permissible value.
            PermissibleValueImpl permissibleValue = (PermissibleValueImpl)getPermissibleValueObject(permissibleValues,
                    								xmlPermissibleValueType.getConceptCode());
            
            if (permissibleValue != null)
            {
                // If the parent permissible value concept code is null set the cde value for the permissible value.
                if (xmlPermissibleValueType.getParentConceptCode() == null || 
                        xmlPermissibleValueType.getParentConceptCode().equals(""))
                {
                    permissibleValue.setCde(cde);
                    permissibleValue.setParentPermissibleValue(null);
                }
                else// If the parent permissible value concept code is not null, set the parent permissible value 
                {// and set the cde as null.
                    
                    //Parent permissible value.
                    PermissibleValue parentPermissibleValue = getPermissibleValueObject(permissibleValues,
                            									xmlPermissibleValueType.getParentConceptCode());
                    
                    //Set the parent permissible value of this permissible value.
                    permissibleValue.setParentPermissibleValue(parentPermissibleValue);
                    permissibleValue.setCde(null);
                }
                
                configuredPermissibleValues.add(permissibleValue);
            }
        }
        
        //Get the permissible values from the set whose relationship is not present in the xml file.
        permissibleValues.removeAll(configuredPermissibleValues);
        
        //Set the CDE for all the above permissible values.
        //i.e. put these permissible values under the cde in the tree structure.
        Iterator it = permissibleValues.iterator();
        while (it.hasNext())
        {
            PermissibleValueImpl permissibleValueImpl = (PermissibleValueImpl) it.next();
            permissibleValueImpl.setCde(cde);
            permissibleValueImpl.setParentPermissibleValue(null);
        }
        
        if (!permissibleValues.isEmpty())
        {
            configuredPermissibleValues.addAll(permissibleValues);
        }
        
        //Set the configured permissible value set to the cde.
        CDEImpl cdeImpl = (CDEImpl)cde;
        cdeImpl.setPermissibleValues(configuredPermissibleValues);
    }
    
    /**
     * Returns the permissible value object for the concept code from the Set of permissible values.
     * @param permissibleValues The Set of permissible values.
     * @param conceptCode The conceptCode whose permissible value object is required.
     * @return the permissible value object for the concept code from the Set of permissible values.
     */
    private PermissibleValue getPermissibleValueObject(Set permissibleValues, String conceptCode)
    {
        Iterator iterator = permissibleValues.iterator();
        while (iterator.hasNext())
        {
            PermissibleValue permissibleValue = (PermissibleValue)iterator.next();
            if (permissibleValue.getValue().equals(conceptCode))
            {
                return permissibleValue;
            }
        }
        
        return null;
    }
}