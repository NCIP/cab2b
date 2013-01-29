/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.services;

import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;

import java.rmi.RemoteException;

import junit.framework.TestCase;

import org.apache.axis.types.URI.MalformedURIException;

/**
 * This tests whether all services related to cab2B are running or not daily.
 * It is not a test case for source code.
 * @author Chandrakant Talele
 */
public class DataServicesTest extends TestCase {
    public void testCaFE() {
        String fe = "<ns2:Gene id=\"1\" symbol=\"A1BG\" name=\"alpha-1-B glycoprotein\" summary=\"The protein encoded by this gene is a plasma glycoprotein of unknown function. The protein shows sequence similarity to the variable regions of some immunoglobulin supergene family member proteins.\" pubmedCount=\"1232\" chromosomeMap=\"19q13.4\" xmlns:ns2=\"gme://caCORE.cabig/3.0/edu.wustl.fe\"/>";

        CQLQuery cql = getSimpleQuery();
        cql.getTarget().setName("edu.wustl.fe.Gene");
        execCQL(cql, "http://128.252.227.94:9094/wsrf/services/cagrid/CaFE", fe);
    }

    public void testGeneConnect() {
        String gene = "<ns2:Gene id=\"1\" ensemblGeneId=\"ENSG00000007080\" entrezGeneId=\"115098\" unigeneClusterId=\"Hs.100043\" ensemblGeneAsOutput=\"true\" entrezGeneAsOutput=\"true\" unigeneAsOutput=\"true\" xmlns:ns2=\"gme://caCORE.cabig/3.0/edu.wustl.geneconnect.domain\"/>";
        CQLQuery cql = getSimpleQuery();
        cql.getTarget().setName("edu.wustl.geneconnect.domain.Gene");
        execCQL(cql, "http://128.252.227.94:9092/wsrf/services/cagrid/GeneConnect", gene);
    }

    public void testCaArray() {
        String array = "<ns2:ArrayDesign id=\"1\" lsidAuthority=\"Affymetrix.com\" lsidNamespace=\"PhysicalArrayDesign\" lsidObjectId=\"GenomeWideSNP_6.Full\" name=\"GenomeWideSNP_6.Full\" assayType=\"snp\" version=\"rev2\" xmlns:ns2=\"gme://caArray.caBIG/2.1/gov.nih.nci.caarray.domain.array\"><ns2:technologyType><ns3:Term id=\"624\" accession=\"MO_514\" url=\"http://mged.sourceforge.net/ontologies/MGEDontology.php#in_situ_oligo_features\" value=\"in_situ_oligo_features\" xmlns:ns3=\"gme://caArray.caBIG/2.1/gov.nih.nci.caarray.domain.vocabulary\"><ns3:categories/></ns3:Term></ns2:technologyType><ns2:organism><ns4:Organism commonName=\"human\" scientificName=\"Homo sapiens\" taxonomyRank=\"species\" ncbiTaxonomyId=\"9606\" id=\"1\" xmlns:ns4=\"gme://caArray.caBIG/2.1/edu.georgetown.pir\"><ns4:additionalOrganismNameCollection/></ns4:Organism></ns2:organism><ns2:provider><ns5:Organization id=\"1\" name=\"Affymetrix\" xmlns:ns5=\"gme://caArray.caBIG/2.1/gov.nih.nci.caarray.domain.contact\"/></ns2:provider><ns2:designFiles><ns6:CaArrayFile id=\"1\" name=\"GenomeWideSNP_6.Full.cdf\" status=\"IMPORTED\" type=\"AFFYMETRIX_CDF\" xmlns:ns6=\"gme://caArray.caBIG/2.1/gov.nih.nci.caarray.domain.file\"/></ns2:designFiles></ns2:ArrayDesign>";
        CQLQuery cql = getSimpleQuery();
        cql.getTarget().setName("gov.nih.nci.caarray.domain.array.ArrayDesign");
        execCQL(cql, "http://array.nci.nih.gov/wsrf/services/cagrid/CaArraySvc", array);
    }

    public void testCaTissueDeidentified() {
        CQLQuery cql = getSimpleQuery();
        cql.getTarget().setName("edu.wustl.catissuecore.domain.Participant");
        try {
            DataServiceClient client = new DataServiceClient(
                    "https://caarray.wustl.edu:58443/wsrf/services/cagrid/CaTissueCore");
            client.query(cql);
        } catch (MalformedURIException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            assertTrue("Expecting an RemoteException as this case call secure caTissue without ", true);
        }
    }

    private CQLQuery getSimpleQuery() {
        CQLQuery cql = new CQLQuery();
        Object target = new Object();
        cql.setTarget(target);

        Attribute idCond = new Attribute();
        target.setAttribute(idCond);

        idCond.setName("id");
        idCond.setPredicate(Predicate.EQUAL_TO);
        idCond.setValue("1");

        return cql;
    }

    private void execCQL(CQLQuery query, String url, String expected) {
        CQLQueryResults result = null;
        try {
            DataServiceClient client = new DataServiceClient(url);
            result = client.query(query);
        } catch (MalformedURIException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        CQLQueryResultsIterator itr = new CQLQueryResultsIterator(result, true);
        while (itr.hasNext()) {
            String singleRecordXml = (String) itr.next();
            assertEquals(expected, singleRecordXml);
        }
    }

}
