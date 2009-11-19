package edu.wustl.cab2bwebapp.util.caObr;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.caObr.client.CaObrClient;
import org.cagrid.caObr.service.CaObrImpl;

import per.edu.wustl.Annotation;
import per.edu.wustl.Ontology;
import per.edu.wustl.Resource;

public class AnnotationService implements Runnable {

    CaObrClient client;

    Ontology[] ontologies;

    Resource[] resources;

    String token;

    public static List<Annotation> annotations = new ArrayList<Annotation>();

    public AnnotationService(Ontology[] fromOntologies, Resource[] fromResources, String term) {
        ontologies = fromOntologies;
        resources = fromResources;
        token = term;

        try {
            client = new CaObrClient("http://ps4266:8080/wsrf/services/cagrid/CaObr");
        } catch (MalformedURIException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public CaObrClient getClient() {
        return client;
    }

    public void setClient(CaObrClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
           // Annotation[] ann = client.getAnnotations(ontologies, resources, token);
            Annotation[] ann = new CaObrImpl().getAnnotations(ontologies, resources, token);
            if (ann != null) {
                annotations.addAll(Arrays.asList(ann));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
