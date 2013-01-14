/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.util.caObr;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.axis.types.URI.MalformedURIException;
import org.apache.log4j.Logger;
import org.cagrid.caobr.client.CaObrClient;

import edu.wustl.caobr.Annotation;
import edu.wustl.caobr.Ontology;
import edu.wustl.caobr.Resource;

/**
 * @author chandrakant_talele
 * @author lalit_chand
 */
public class ServiceInvoker {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ServiceInvoker.class);

    private static final String SERVICE_URL = PropertyLoader.getCaObrServiceURL();

    private CaObrClient client = null;

    public ServiceInvoker() {

        try {
            client = new CaObrClient(SERVICE_URL);
        } catch (MalformedURIException e) {
            logger.error("CaObr service url is malformed ", e);
        } catch (RemoteException e) {
            logger.error("Remote exception while initiating caObr client ", e);
        }
    }

    public Ontology[] getOntologies() {
        Ontology[] ontologies = null;
        try {
            ontologies = client.getAllOntologies();
        } catch (RemoteException e) {
            logger.error("Remote exception while getting ontologies ", e);
        }
        Arrays.sort(ontologies, new Comparator<Ontology>() {
            public int compare(Ontology o1, Ontology o2) {
                return o1.getDisplayLable().compareToIgnoreCase(o2.getDisplayLable());
            }
        });
        logger.info("------------------------\n\tOntologies\n------------------------");
        return ontologies;
    }

    public Resource[] getResources() {
        Resource[] resources = null;
        try {
            resources = client.getAllResources();
        } catch (RemoteException e) {
            logger.error("Remote exception while getting resources ", e);
        }
        Arrays.sort(resources, new Comparator<Resource>() {
            public int compare(Resource r1, Resource r2) {
                return r1.getName().compareToIgnoreCase(r2.getName());
            }
        });
        logger.info("------------------------\n\tResources\n------------------------");
        return resources;
    }

    public Annotation[] getAnnotations(Ontology[] ontologies, Resource[] resources, String token) {
        Annotation[] annotations = new Annotation[0];
        try {
            annotations = client.getAnnotations(ontologies, resources, token);
        } catch (RemoteException e) {
            logger.error("Remote exception while getting annotation ", e);
            e.printStackTrace();
        }
        return annotations;
    }
}
