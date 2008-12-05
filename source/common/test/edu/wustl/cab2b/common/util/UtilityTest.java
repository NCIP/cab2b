package edu.wustl.cab2b.common.util;

import static edu.wustl.cab2b.common.util.Constants.CAB2B_ENTITY_GROUP;
import static edu.wustl.cab2b.common.util.Constants.TYPE_CATEGORY;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.Cab2bQuery;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;
import edu.wustl.cab2b.server.util.TestUtil;
import edu.wustl.common.querysuite.metadata.associations.impl.IntraModelAssociation;
import edu.wustl.common.querysuite.metadata.path.Path;

/**
 * @author Chandrakant Talele 
 */
public class UtilityTest extends TestCase {

	/**
	 * Test method for {@link edu.wustl.cab2b.common.util.Utility#replaceAllWords(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public final void testReplaceAllWords() {
		//char replacement test 
		assertEquals("Test", Utility.replaceAllWords("Teasta", "a", ""));

		//String replacement test 
		assertEquals("What is this? this is this and is this Correct?",
					 Utility.replaceAllWords("What is that? that is that and is that Correct?", "that", "this"));
		//Null argument passing 
		assertEquals(null, Utility.replaceAllWords(null, "that", "this"));

		//No replacement found test  
		assertEquals("No replacement test", Utility.replaceAllWords("No replacement test", "Deepak", "Shingan"));
	}

	public void testCapitalizeString() {
		assertEquals("Gene", Utility.capitalizeFirstCharacter("gene"));
	}

	public void testCapitalizeStringAlreadyCapital() {
		assertEquals("Gene", Utility.capitalizeFirstCharacter("Gene"));
	}

	public void testHasAnySecureServiceforHttp() {
		List<String> list = new ArrayList<String>(2);
		list.add(" http://foo.bar/grid/service");
		Cab2bQuery query = new Cab2bQuery();
		query.setOutputUrls(list);
		assertFalse(Utility.hasAnySecureService(query));
	}

	public void testHasAnySecureServiceforHttps() {
		List<String> list = new ArrayList<String>(2);
		list.add(" https://foo.bar/grid/service");
		Cab2bQuery query = new Cab2bQuery();
		query.setOutputUrls(list);
		assertTrue(Utility.hasAnySecureService(query));
	}

	public void testGetPathDisplayStringJustAbove100() {

		EntityGroupInterface eg = TestUtil.getEntityGroup("caNanoLab v1.4", 120L);
		DynamicExtensionUtility.addTaggedValue(eg, CAB2B_ENTITY_GROUP, CAB2B_ENTITY_GROUP);
		EntityInterface nanoparticleSample = TestUtil.getEntity("NanoparticleSample", 134L);
		nanoparticleSample.addEntityGroupInterface(eg);
		EntityInterface sampleComposition = TestUtil.getEntity("SampleComposition", 133L);
		sampleComposition.addEntityGroupInterface(eg);
		AssociationInterface a = TestUtil.getAssociation(nanoparticleSample, sampleComposition);

		Path path = TestUtil.getPath(new IntraModelAssociation(a));
		String str = Utility.getPathDisplayString(path);
		String expected = "<HTML><B>Path</B>:NanoparticleSample (caNanoLab v1.4)<B>----></B>SampleComposition (caNanoLab v1.4)</HTML>";
		assertEquals(expected, str);
	}

	public void testGetPathDisplayStringLessThan100() {

		EntityGroupInterface eg = TestUtil.getEntityGroup("GeneConnect", 120L);
		DynamicExtensionUtility.addTaggedValue(eg, CAB2B_ENTITY_GROUP, CAB2B_ENTITY_GROUP);
		EntityInterface nanoparticleSample = TestUtil.getEntity("Gene", 134L);
		nanoparticleSample.addEntityGroupInterface(eg);
		EntityInterface sampleComposition = TestUtil.getEntity("Protein", 133L);
		sampleComposition.addEntityGroupInterface(eg);
		AssociationInterface a = TestUtil.getAssociation(nanoparticleSample, sampleComposition);

		Path path = TestUtil.getPath(new IntraModelAssociation(a));
		String str = Utility.getPathDisplayString(path);
		String expected = "<HTML><B>Path</B>:Gene (GeneConnect)<B>----></B>Protein (GeneConnect)</HTML>";
		assertEquals(expected, str);
	}

	public void testGetPathDisplayStringGreather100() {

		EntityGroupInterface eg = TestUtil.getEntityGroup("caTissue_Core_1_2 v1.2", 120L);
		DynamicExtensionUtility.addTaggedValue(eg, CAB2B_ENTITY_GROUP, CAB2B_ENTITY_GROUP);
		EntityInterface nanoparticleSample = TestUtil.getEntity("Participant", 134L);
		nanoparticleSample.addEntityGroupInterface(eg);
		EntityInterface sampleComposition = TestUtil.getEntity("ParticipantMedicalIdentifier", 133L);
		sampleComposition.addEntityGroupInterface(eg);
		AssociationInterface a = TestUtil.getAssociation(nanoparticleSample, sampleComposition);

		Path path = TestUtil.getPath(new IntraModelAssociation(a));
		String str = Utility.getPathDisplayString(path);
		String expected = "<HTML><B>Path</B>:Participant (caTissue_Core_1_2 v1.2)<B>----></B>ParticipantMedicalIdentifier (caTissue_Core_1_2 v1.2)<P></HTML>";
		assertEquals(expected, str);
	}

	public void testGetDisplayName() {
		String name = "entityName";
		EntityInterface entity = TestUtil.getEntityWithCaB2BGrp("groupName", name, "attrName");
		DynamicExtensionUtility.addTaggedValue(entity, TYPE_CATEGORY, TYPE_CATEGORY);
		String res = Utility.getDisplayName(entity);
		assertEquals(name, res);
	}

	public void testGetDisplayNameFE() {
		String name = "Gene";
		EntityInterface entity = TestUtil.getEntityWithCaB2BGrp("caFE Server 1.1", name, "attrName");

		entity.getEntityGroupCollection().iterator().next().setLongName("caFE Server 1.1");
		entity.getEntityGroupCollection().iterator().next().setVersion("1.1");
		String res = Utility.getDisplayName(entity);

		assertEquals("Gene (caFE Server v1.1)", res);
	}

	public void testGetOnlyEntityName() {
		String name = "edu.wustl.geneconnect.domain.Gene";
		EntityInterface entity = TestUtil.getEntityWithCaB2BGrp("groupName", name, "attrName");
		DynamicExtensionUtility.addTaggedValue(entity, TYPE_CATEGORY, TYPE_CATEGORY);
		String res = Utility.getOnlyEntityName(entity);
		assertEquals("Gene", res);
	}
	public void testGetEntityGroup() {
		EntityInterface entity = TestUtil.getEntity("Gene");
		boolean gotException = false;
		try {
			Utility.getEntityGroup(entity);
		} catch (RuntimeException e) {
			gotException = true;
		}
		assertTrue(gotException);
	}
}
