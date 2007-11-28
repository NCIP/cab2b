package edu.wustl.cab2b.server.services;
import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;

import edu.wustl.cab2b.common.util.Utility;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import junit.framework.TestCase;

public class DataServicesTest extends TestCase {
    // APP names
    // caFE,caTissueCore,geneConnect,caArray
    public void testCaFE() {
        CQLQuery cql = getSimpleQuery();
        cql.getTarget().setName("edu.wustl.fe.Gene");
        execCQL(cql, Utility.getServiceURLs("caFE")[0]);
    }

    public void testGeneConnect() {
        CQLQuery cql = getSimpleQuery();
        cql.getTarget().setName("edu.wustl.geneconnect.domain.Gene");
        execCQL(cql, Utility.getServiceURLs("geneConnect")[0]);
    }

    public void testCaTissueCore1() {
        CQLQuery cql = getSimpleQuery();
        cql.getTarget().setName("edu.wustl.catissuecore.domain.Participant");
        execCQL(cql, Utility.getServiceURLs("caTissueCore")[0]);
    }

    public void testCaTissueCore2() {
        CQLQuery cql = getSimpleQuery();
        cql.getTarget().setName("edu.wustl.catissuecore.domain.Participant");
        execCQL(cql, Utility.getServiceURLs("caTissueCore")[1]);
    }

    public void testCaArray() {
        CQLQuery cql = getSimpleQuery();
        cql.getTarget().setName("gov.nih.nci.mageom.domain.Experiment.Experiment");
        cql.getTarget().getAttribute().setName("name");
        cql.getTarget().getAttribute().setPredicate(Predicate.LIKE);
        cql.getTarget().getAttribute().setValue("%asbestos%");
        execCQL(cql, Utility.getServiceURLs("caArray")[0]);
    }

    private CQLQuery getSimpleQuery() {
        CQLQuery cql = new CQLQuery();
        Object target = new Object();
        cql.setTarget(target);

        Attribute idCond = new Attribute();
        target.setAttribute(idCond);

        idCond.setName("id");
        idCond.setPredicate(Predicate.LESS_THAN);
        idCond.setValue("5");

        return cql;
    }

    private void execCQL(CQLQuery query, String url) {
        try {
            DataServiceClient client = new DataServiceClient(url);
            client.query(query);
        } catch (MalformedURIException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

}
