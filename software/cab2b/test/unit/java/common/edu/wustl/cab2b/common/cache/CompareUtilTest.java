/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.cache;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.domaininterface.StringValueInterface;

/**
 * @author Chandrakant Talele
 */
public class CompareUtilTest extends TestCase {
    private static DomainObjectFactory fact = DomainObjectFactory.getInstance();
    
    public void testCompareEntityByName() {
        EntityInterface cacheEn = fact.createEntity();
        cacheEn.setName("edu.wustl.catissuecore.domain.Specimen");

        EntityInterface patternEn = fact.createEntity();
        patternEn.setName("Speci");
        assertNotNull(CompareUtil.compare(cacheEn, patternEn));
    }

    public void testCompareEntityByDescription() {
        EntityInterface cacheEn = fact.createEntity();
        cacheEn.setDescription("A single unit of tissue, body fluid, or derivative biological macromolecule that is collected or created from a Participant");

        EntityInterface patternEn = fact.createEntity();
        patternEn.setDescription("macro");
        assertNotNull(CompareUtil.compare(cacheEn, patternEn));
    }

    public void testCompareEntityBySemanticProperty() {
        EntityInterface cacheEn = fact.createEntity();
        cacheEn.addSemanticProperty(getSP("C00000"));
        cacheEn.addSemanticProperty(getSP("C23456"));

        EntityInterface patternEn = fact.createEntity();
        patternEn.addSemanticProperty(getSP("23"));
        assertNotNull(CompareUtil.compare(cacheEn, patternEn));
    }

    public void testCompareEntityByNameNotMatching() {
        EntityInterface cacheEn = fact.createEntity();
        cacheEn.setName("edu.wustl.catissuecore.domain.Specimen");

        EntityInterface patternEn = fact.createEntity();
        patternEn.setName("domain");
        assertNull(CompareUtil.compare(cacheEn, patternEn));
    }

    public void testCompareAttributeNameMatching() {
        AttributeInterface cachedAttribute = getStrAttr("GeneAttribute");
        AttributeInterface patternAttribute = getStrAttr("gene");
        assertNotNull(CompareUtil.compare(cachedAttribute, patternAttribute));
    }

    public void testCompareAttributeDescriptionMatching() {
        AttributeInterface cachedAttribute = fact.createStringAttribute();
        cachedAttribute.setDescription("GeneAttribute");
        AttributeInterface patternAttribute = fact.createStringAttribute();
        patternAttribute.setDescription("gene");
        assertNotNull(CompareUtil.compare(cachedAttribute, patternAttribute));
    }

    public void testCompareAttributeDifferentType() {
        AttributeInterface cachedAttribute = getStrAttr("GeneAttribute");
        AttributeInterface patternAttribute = getStrAttr("gene");
        assertNotNull(CompareUtil.compare(cachedAttribute, patternAttribute));
    }

    public void testCompareAttributeSemanticPropertyMatched() {
        SemanticPropertyInterface cachedSP = getSP("C23456");

        AttributeInterface cachedAttribute = getStrAttr("GeneAttribute");
        cachedAttribute.addSemanticProperty(cachedSP);
        SemanticPropertyInterface patternSP = getSP("45");

        AttributeInterface patternAttribute = getStrAttr("foobar");
        patternAttribute.addSemanticProperty(patternSP);
        assertNotNull(CompareUtil.compare(cachedAttribute, patternAttribute));
    }

    public void testComparePVsValueMatched() {
        StringValueInterface cachedPV = getStrVal("top");
        StringValueInterface patternPV = getStrVal("t");
        assertNotNull(CompareUtil.compare(cachedPV, patternPV,null));
    }

    public void testComparePVsNothingMatched() {
        StringValueInterface cachedPV = getStrVal("top");
        StringValueInterface patternPV = getStrVal("r");
        assertNull(CompareUtil.compare(cachedPV, patternPV,null));
    }

    public void testComparePVsSemanticPropertyMatched() {
        StringValueInterface cachedPV = fact.createStringValue();
        cachedPV.addSemanticProperty(getSP("C23456"));

        StringValueInterface patternPV = fact.createStringValue();
        patternPV.addSemanticProperty(getSP("45"));
        assertNotNull(CompareUtil.compare(cachedPV, patternPV,null));
    }

    public void testCompareSemanticPropertyNullConceptCode() {
        SemanticPropertyInterface cachedSP = getSP(null);
        SemanticPropertyInterface patternSP = getSP(null);
        assertEquals(-1,CompareUtil.SemanticPropertyCollectionComparator.compare(patternSP,cachedSP));
    }

    public void testCompareSemanticPropertyWithConceptCode() {
        SemanticPropertyInterface cachedSP = getSP("C23456");
        SemanticPropertyInterface patternSP = getSP("45");
        assertEquals(3,CompareUtil.SemanticPropertyCollectionComparator.compare(patternSP,cachedSP));
    }

    public void testCompareSemanticPropertyWithConceptCodeNotMatch() {
        SemanticPropertyInterface cachedSP = getSP("C23456");
        SemanticPropertyInterface patternSP = getSP("88");
        assertEquals(-1,CompareUtil.SemanticPropertyCollectionComparator.compare(patternSP,cachedSP));
    }

    public void testCompareSemanticPropertyNoConceptCodeForPattern() {
        SemanticPropertyInterface cachedSP = getSP("C23456");
        SemanticPropertyInterface patternSP = getSP(null);
        assertEquals(-1,CompareUtil.SemanticPropertyCollectionComparator.compare(patternSP,cachedSP));
    }

    private static SemanticPropertyInterface getSP(String conceptCode) {
        SemanticPropertyInterface sp = fact.createSemanticProperty();
        sp.setConceptCode(conceptCode);
        return sp;
    }

    private static AttributeInterface getStrAttr(String name) {
        AttributeInterface attr = fact.createStringAttribute();
        attr.setName(name);
        return attr;
    }

    private static StringValueInterface getStrVal(String val) {
        StringValueInterface strValue = fact.createStringValue();
        strValue.setValue(val);
        return strValue;
    }
}
