/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 *
 */
package edu.wustl.cab2b.common.authentication;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.w3c.dom.Document;

import edu.wustl.cab2b.common.authentication.exception.AuthenticationException;
import edu.wustl.cab2b.common.authentication.util.CagridPropertyLoader;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.util.Utility;
import gov.nih.nci.cagrid.syncgts.bean.SyncDescription;
import gov.nih.nci.cagrid.syncgts.core.SyncGTS;

/**
 * @author chetan_patil
 *
 */
public class GTSSynchronizer {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(GTSSynchronizer.class);

    /**
     * This method generates the globus certificate in user.home folder
     * @param gridType
     */
    public static void generateGlobusCertificate() {
        URL signingPolicy = Utility.class.getClassLoader().getResource(CagridPropertyLoader.getSigningPolicy());
        URL certificate = Utility.class.getClassLoader().getResource(CagridPropertyLoader.getCertificate());

        if (signingPolicy != null || certificate != null) {
            copyCACertificates(signingPolicy);
            copyCACertificates(certificate);
        } else {
            logger.error("Could not find CA certificates");
            throw new AuthenticationException("Could not find CA certificates.", ErrorCodeConstants.CDS_016);
        }

        logger.debug("Getting sync-descriptor.xml file");
        try {
            logger.debug("Synchronizing with GTS service");
            URL syncDescFile = Utility.class.getClassLoader().getResource(CagridPropertyLoader.getSyncDesFile());

            Document doc = XMLUtils.newDocument(syncDescFile.openStream());
            Object obj = ObjectDeserializer.toObject(doc.getDocumentElement(), SyncDescription.class);
            SyncDescription description = (SyncDescription) obj;
            SyncGTS.getInstance().syncOnce(description);
            logger.debug("Successfully syncronized with GTS service. Globus certificates generated.");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AuthenticationException("Error occurred while generating globus certificates: "
                    + e.getMessage(), e, ErrorCodeConstants.CDS_004);
        }
    }

    private static void copyCACertificates(URL inFileURL) {
        int index = inFileURL.getPath().lastIndexOf('/');
        if (index > -1) {
            String fileName = inFileURL.getPath().substring(index + 1).trim();
            File destination = new File(gov.nih.nci.cagrid.common.Utils.getTrustedCerificatesDirectory()
                    + File.separator + fileName);

            try {
                FileUtils.copyURLToFile(inFileURL, destination);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new AuthenticationException("Unable to copy CA certificates to [user.home]/.globus: "
                        + e.getMessage(), e, ErrorCodeConstants.CDS_003);
            }
        }
    }

}
