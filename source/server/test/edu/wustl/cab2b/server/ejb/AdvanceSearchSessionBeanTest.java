package edu.wustl.cab2b.server.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.cactus.ServletTestCase;

import edu.common.dynamicextensions.domain.Entity;
import edu.wustl.cab2b.common.advancedSearch.AdvancedSearchBusinessInterface;
import edu.wustl.cab2b.common.advancedSearch.AdvancedSearchHome;
import edu.wustl.cab2b.common.beans.MatchedClass;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.locator.Locator;
import edu.wustl.cab2b.common.locator.LocatorException;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author gautam_shetty
 */
public class AdvanceSearchSessionBeanTest extends ServletTestCase {

    /**
     * AdvancedSearchBean reference. 
     */
    private AdvancedSearchBusinessInterface advancedSearchBusinessInterface = null;

    /**
     * Override setUp() method from ServletTestCase. 
     */
    protected void setUp() throws Exception {
        Logger.configure();
        try {
            advancedSearchBusinessInterface = (AdvancedSearchBusinessInterface) Locator.getInstance().locate(EjbNamesConstants.ADVANCED_SEARCH_BEAN,AdvancedSearchHome.class);
        } catch (LocatorException locExp) {
            fail("Cannot locate AdvancedSearchBean");
        }
    }

    public void testSearchForClassOnText() {
        int[] searchTarget = new int[1];
        searchTarget[0] = Constants.CLASS;
        int basedOn = Constants.BASED_ON_TEXT;
        String[] searchString = { "ParticipantMedicalIdentifier" };

        try {
            MatchedClass matchedClass = search(searchTarget, searchString, basedOn);
            Collection entityCollection = matchedClass.getEntityCollection();
            Iterator iterator = entityCollection.iterator();
            Entity entity = (Entity) iterator.next();

            assertTrue(entity.getName().contains("ParticipantMedicalIdentifier"));
        } catch (RemoteException remExp) {
            fail("RemoteException : " + remExp.getMessage());
        }
    }

    public void testSearchForClassOnConceptCode() {
        int[] searchTarget = new int[1];
        searchTarget[0] = Constants.CLASS;
        int basedOn = Constants.BASED_ON_CONCEPT_CODE;
        String[] searchString = { "C45258" };

        try {
            MatchedClass matchedClass = search(searchTarget, searchString, basedOn);
            Collection entityCollection = matchedClass.getEntityCollection();
            Iterator iterator = entityCollection.iterator();
            Entity entity = (Entity) iterator.next();
            assertTrue(entity.getName().contains("ParticipantMedicalIdentifier"));
        } catch (RemoteException remExp) {
            fail("RemoteException : " + remExp.getMessage());
        }
    }

    public void testSearchForAttributeOnText() {
        int[] searchTarget = new int[1];
        searchTarget[0] = Constants.ATTRIBUTE;
        int basedOn = Constants.BASED_ON_TEXT;
        String[] searchString = { "medicalRecordNumber" };
        MatchedClass matchedClass = null;

        try {
            matchedClass = search(searchTarget, searchString, basedOn);
            Collection entityCollection = matchedClass.getEntityCollection();
            Iterator iterator = entityCollection.iterator();
            Entity entity = (Entity) iterator.next();
            assertTrue(entity.getName().contains("ParticipantMedicalIdentifier"));
        } catch (RemoteException remExp) {
            fail("RemoteException : " + remExp.getMessage());
        }
    }

    public void testSearchForAttributeOnConceptCode() {
        int[] searchTarget = new int[1];
        searchTarget[0] = Constants.ATTRIBUTE;
        int basedOn = Constants.BASED_ON_CONCEPT_CODE;
        String[] searchString = { "C45258" };
        MatchedClass matchedClass = null;

        try {
            matchedClass = search(searchTarget, searchString, basedOn);
            Collection entityCollection = matchedClass.getEntityCollection();
            Iterator iterator = entityCollection.iterator();
            Entity entity = (Entity) iterator.next();
            assertTrue(entity.getName().contains("ParticipantMedicalIdentifier"));
        } catch (RemoteException remExp) {
            fail("RemoteException : " + remExp.getMessage());
        }
    }

    public void testSearchForNullSearchTarget() {
        int[] searchTarget = null;
        int basedOn = Constants.BASED_ON_CONCEPT_CODE;
        String[] searchString = { "C40974" };
        MatchedClass matchedClass = null;

        try {
            matchedClass = search(searchTarget, searchString, basedOn);
            fail();//test fails here if exception is not thrown
        } catch (RemoteException remExp) {
            assertNull(matchedClass);
        }
    }

    public void testSearchForNullSearchString() {
        int[] searchTarget = new int[1];
        searchTarget[0] = Constants.ATTRIBUTE;
        int basedOn = Constants.BASED_ON_CONCEPT_CODE;
        String[] searchString = null;
        MatchedClass matchedClass = null;

        try {
            matchedClass = search(searchTarget, searchString, basedOn);
            fail();//test fails here if exception is not thrown
        } catch (RemoteException remExp) {
            assertNull(matchedClass);
        }
    }

    public void testSearchonNotExistingSearchTarget() {
        int[] searchTarget = new int[1];
        searchTarget[0] = 9;
        int basedOn = Constants.BASED_ON_CONCEPT_CODE;
        String[] searchString = { "medicalRecordNumber" };
        MatchedClass matchedClass = null;

        try {
            matchedClass = search(searchTarget, searchString, basedOn);
            fail();//test fails here if exception is not thrown
        } catch (RemoteException remExp) {
            assertNull(matchedClass);
        }
    }

    public void testSearchonNotExistingBasedOn() {
        int[] searchTarget = new int[1];
        searchTarget[0] = Constants.CLASS;
        int basedOn = 2;
        String[] searchString = { "medicalRecordNumber" };
        MatchedClass matchedClass = null;

        try {
            matchedClass = search(searchTarget, searchString, basedOn);
            fail();//test fails here if exception is not thrown
        } catch (RemoteException remExp) {
            assertNull(matchedClass);
        }
    }

    /**
     * @param searchTarget
     * @param basedOn
     * @return
     * @throws RemoteException
     */
    private MatchedClass search(int[] searchTarget, String[] searchString, int basedOn) throws RemoteException {
        MatchedClass matchedClass = advancedSearchBusinessInterface.search(searchTarget, searchString, basedOn);
        return matchedClass;
    }
}