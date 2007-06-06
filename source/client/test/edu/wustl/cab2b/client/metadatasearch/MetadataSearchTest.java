package edu.wustl.cab2b.client.metadatasearch;

import java.util.Set;

import org.apache.cactus.ServletTestCase;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.beans.MatchedClass;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.util.logger.Logger;

public class MetadataSearchTest extends ServletTestCase {
    MatchedClass resultMatchedClass;

	MetadataSearch metadataSearch;

	@Override
	protected void setUp() throws Exception {
        Logger.configure();
		EntityCache entityCache = EntityCache.getInstance();
		resultMatchedClass = new MatchedClass();
		metadataSearch = new MetadataSearch(entityCache);
	}

	public void testsearchPv() {

		int[] searchTargetStatus = { Constants.PV };
		String[] searchString = { "C25228", "C62637" };
		int basedOn = Constants.BASED_ON_CONCEPT_CODE;
		try {
			resultMatchedClass = metadataSearch.search(searchTargetStatus,
					searchString, basedOn);
		} catch (CheckedException e) {
			e.printStackTrace();
			fail();
		}
		Set<EntityInterface> entities = resultMatchedClass
				.getEntityCollection();
		boolean b = false;
		for (EntityInterface eI : entities) {
			String result = eI.getName();
			b = b || result.contains("ArrayGroup");
		}
		assertTrue(b);
	}

	public void testsearchNullTargetString() {

		int[] searchTargetStatus = { Constants.PV };
		int basedOn = Constants.BASED_ON_CONCEPT_CODE;
		try {
			resultMatchedClass = metadataSearch.search(searchTargetStatus, null,
					basedOn);
			fail();
		} catch (CheckedException e) {
			assertTrue(true);
		}
	}

	public void testsearchNullSearchTargetStatus() {

		String[] searchString = { "C25228", "C62637" };
		int basedOn = Constants.BASED_ON_CONCEPT_CODE;
		try {
			resultMatchedClass = metadataSearch.search(null, searchString,
					basedOn);
			fail();
		} catch (CheckedException e) {
			assertTrue(true);
		}
	}

	public void testsearchBasedOnOutOfBounds() {

		int[] searchTargetStatus = { Constants.PV };
		String[] searchString = { "C25228", "C62637" };
		try {
			resultMatchedClass = metadataSearch.search(searchTargetStatus,
					searchString, 3);
			fail();
		} catch (CheckedException e) {
			assertTrue(true);
		}
	}

	public void testsearchCategory() {

		int[] searchTargetStatus = { Constants.CLASS };
		String[] searchString = { "chromosome" };
		try {
			resultMatchedClass = metadataSearch.search(searchTargetStatus,
					searchString, Constants.BASED_ON_TEXT);
		} catch (CheckedException e) {
			e.printStackTrace();
			fail();
		}
		Set<EntityInterface> entities = resultMatchedClass
				.getEntityCollection();
		boolean b = false;
		for (EntityInterface eI : entities) {
			String result = eI.getName();
			b = b || result.contains("Chromosome");
		}
		assertTrue(b);
	}

	public void testsearchCategoruWithDescription() {

		int[] searchTargetStatus = { Constants.CLASS_WITH_DESCRIPTION };
		String[] searchString = { "chromosome" };
		try {
			resultMatchedClass = metadataSearch.search(searchTargetStatus,
					searchString, Constants.BASED_ON_TEXT);
		} catch (CheckedException e) {
			e.printStackTrace();
			fail();
		}
		Set<EntityInterface> entities = resultMatchedClass
				.getEntityCollection();
		boolean b = false;
		for (EntityInterface eI : entities) {
			String result = eI.getName();
			b = b || result.contains("ReporterCompositeMap");
		}
		assertTrue(b);
	}

	public void testsearchAttribute() {
		int[] searchTargetStatus = { Constants.ATTRIBUTE };
		String[] searchString = { "chromosome" };
		try {
			resultMatchedClass = metadataSearch.search(searchTargetStatus,
					searchString, Constants.BASED_ON_TEXT);
		} catch (CheckedException e) {
			e.printStackTrace();
			fail();
		}
		Set<EntityInterface> entities = resultMatchedClass
				.getEntityCollection();
		boolean b = false;
		for (EntityInterface eI : entities) {
			String result = eI.getName();
			b = b || result.contains("Gene Annotation");
		}
		assertTrue(b);
	}

	public void testsearchAttributeWithDescription() {
		int[] searchTargetStatus = { Constants.ATTRIBUTE_WITH_DESCRIPTION };
		String[] searchString = { "chromosome" };
		try {
			resultMatchedClass = metadataSearch.search(searchTargetStatus,
					searchString, Constants.BASED_ON_TEXT);
		} catch (CheckedException e) {
			e.printStackTrace();
			fail();
		}
		Set<EntityInterface> entities = resultMatchedClass
				.getEntityCollection();
		boolean b = false;
		for (EntityInterface eI : entities) {
			String result = eI.getName();
			b = b || result.contains("Literature-based Gene Association");
		}
		assertTrue(b);
	}

}