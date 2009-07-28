package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.QueryResultFactory;
import edu.wustl.cab2b.common.queryengine.result.RecordId;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;

/**
 * JUnitTestcases for <code>CategoryToSpreadsheetTransformer</code> class. 
 * @author deepak_shingan
 *
 */
public class CategoryToSpreadsheetTransformerTest extends TestCase {
    private int counter = 0;

    private long recordId = 1;

    private List<AttributeInterface> allowedAttributes = new ArrayList<AttributeInterface>();

    /**
     * Default constructor
     */
    public CategoryToSpreadsheetTransformerTest() {
        counter = 0;
        recordId = 1;
        allowedAttributes = new ArrayList<AttributeInterface>();
    }

    /**
     *  
     * @throws Exception
     */
    public void testConvertFlat1() throws Exception {
        List<ICategorialClassRecord> list = createInput1();

        CategoryToSpreadsheetTransformer foo = new CategoryToSpreadsheetTransformer();
        List<Map<AttributeInterface, Object>> masterList = foo.convert(list,100);
        assertEquals(7, masterList.size());
        write(masterList, 1);

    }

    /**
     * @throws Exception
     */
    public void testConvertFlat2() throws Exception {
        List<ICategorialClassRecord> list = createInput2();

        CategoryToSpreadsheetTransformer foo = new CategoryToSpreadsheetTransformer();
        List<Map<AttributeInterface, Object>> masterList = foo.convert(list,100);
        assertEquals(16, masterList.size());
        write(masterList, 2);

    }

    /**
     * @throws Exception
     */
    public void testConvertFlat3() throws Exception {
        List<ICategorialClassRecord> list = createInput3();

        CategoryToSpreadsheetTransformer foo = new CategoryToSpreadsheetTransformer();
        List<Map<AttributeInterface, Object>> masterList = foo.convert(list,100);
        assertEquals(0, masterList.size());
        write(masterList, 3);

    }

    /**
     * Writes output into csv file.
     * @param masterList
     * @param num
     * @throws Exception
     */
    private void write(List<Map<AttributeInterface, Object>> masterList, int num) throws Exception {
        BufferedWriter bos = new BufferedWriter(new FileWriter(new File("c:/test" + num + ".csv")));
        for (AttributeInterface a : allowedAttributes) {
            bos.write(a.getName());
            bos.write(",");
        }
        bos.write("\n");
        for (Map<AttributeInterface, Object> map : masterList) {
            for (AttributeInterface a : allowedAttributes) {
                System.out.println(a.getName());
                Object obj = map.get(a);
                String str = "-";
                if (obj != null) {
                    str = obj.toString();
                }
                bos.write(str);
                bos.write(",");
            }
            bos.write("\n");
        }
        bos.flush();
    }

    /**
     * Returns <code>EntityInterface</code> object for given entity name and attributes.
     * @param entityName
     * @param attributes
     * @return EntityInterface
     */
    private EntityInterface createEntity(String entityName, String... attributes) {
        EntityInterface entity = DomainObjectFactory.getInstance().createEntity();
        entity.setName(entityName);

        for (String name : attributes) {
            AttributeInterface attr = DomainObjectFactory.getInstance().createStringAttribute();
            attr.setName(name);
            attr.setId(recordId++);
            entity.addAttribute(attr);
            allowedAttributes.add(attr);
        }
        return entity;
    }

    /**
     * Mock method for creating category records.
     * @param catClass
     * @param howMany
     * @return
     */
    private List<ICategorialClassRecord> createRecord(CategorialClass catClass, int howMany) {
        List<ICategorialClassRecord> records = new ArrayList<ICategorialClassRecord>(howMany);
        Collection<AttributeInterface> collection = catClass.getCategorialClassEntity().getAttributeCollection();
        Set<AttributeInterface> set = new HashSet<AttributeInterface>(collection);

        for (int i = 0; i < howMany; i++) {
            ICategorialClassRecord record = QueryResultFactory.createCategorialClassRecord(catClass, set,
                                                                                           new RecordId(recordId++
                                                                                                   + "", ""));
            for (AttributeInterface attr : record.getAttributes()) {
                record.putStringValueForAttribute(attr, attr.getName() + "_R_" + counter);
            }
            records.add(record);
            counter++;
        }
        return records;
    }

    /*
     * Creating record structure like
     * 
     * ts0 | |-->rep0 |-->scg0 | |-->p0 |-->p1
     * 
     * ts1 | |-->rep1 |-->scg1 | | | |-->p2 | |-->p3 | |-->scg2 | |-->p4 |-->p5
     * |-->p6
     * 
     * ts0 rep0 scg0 p0 ts0 rep0 scg0 p1 ts1 rep1 scg1 p2 ts1 rep1 scg1 p3 ts1
     * rep1 scg2 p4 ts1 rep1 scg2 p5 ts1 rep1 scg2 p6
     * 
     */

    private List<ICategorialClassRecord> createInput1() {
        CategorialClass ts = getTissueSpecimen();
        CategorialClass reviewEventParams = getReviewEventParameters();
        CategorialClass scg = getSCG();
        CategorialClass participant = getParticipant();

        ts.addChildCategorialClass(reviewEventParams);
        ts.addChildCategorialClass(scg);
        scg.addChildCategorialClass(participant);

        List<ICategorialClassRecord> scgRecs = createRecord(scg, 1);
        scgRecs.get(0).addCategorialClassRecords(participant, createRecord(participant, 2));

        List<ICategorialClassRecord> tsRecs = createRecord(ts, 2);
        tsRecs.get(0).addCategorialClassRecords(scg, scgRecs);
        tsRecs.get(0).addCategorialClassRecords(reviewEventParams, createRecord(reviewEventParams, 1));

        List<ICategorialClassRecord> scgRecsMore = createRecord(scg, 2);
        scgRecsMore.get(1).addCategorialClassRecords(participant, createRecord(participant, 3));

        scgRecsMore.get(0).addCategorialClassRecords(participant, createRecord(participant, 2));

        tsRecs.get(1).addCategorialClassRecords(scg, scgRecsMore);
        tsRecs.get(1).addCategorialClassRecords(reviewEventParams, createRecord(reviewEventParams, 1));
        return tsRecs;
    }

    /**
     * @return List<ICategorialClassRecord>
     */
    private List<ICategorialClassRecord> createInput3() {
        return null;
    }

    /*
     * Creating record structure like
     * 
     * p0 | |-->cpr0-->cp0 |-->cpr1-->cp1 |-->scg0 |-->ts0 |-->ts1 | |-->ms0
     * |-->ms1 |-->ms2
     * 
     * p1 | |-->cpr2-->cp2 |-->scg1 |-->ts2 |-->ts3 | |-->ms3 |-->ms4
     * 
     * p0 cpr0 cp0 scg0 ts0 ms0 p0 cpr0 cp0 scg0 ts0 ms1 p0 cpr0 cp0 scg0 ts0
     * ms2 p0 cpr0 cp0 scg0 ts1 ms0 p0 cpr0 cp0 scg0 ts1 ms1 p0 cpr0 cp0 scg0
     * ts1 ms2
     * 
     * p0 cpr1 cp1 scg0 ts0 ms0 p0 cpr1 cp1 scg0 ts0 ms1 p0 cpr1 cp1 scg0 ts0
     * ms2 p0 cpr1 cp1 scg0 ts1 ms0 p0 cpr1 cp1 scg0 ts1 ms1 p0 cpr1 cp1 scg0
     * ts1 ms2
     * 
     * p1 cpr2 cp2 scg1 ts2 ms3 p1 cpr2 cp2 scg1 ts2 ms4 p1 cpr2 cp2 scg1 ts3
     * ms3 p1 cpr2 cp2 scg1 ts3 ms4
     * 
     *    
     * @return List<ICategorialClassRecord> 
     */
    private List<ICategorialClassRecord> createInput2() {
        CategorialClass participant = getParticipant();
        CategorialClass cpr = getCPR();
        CategorialClass cp = getCP();
        CategorialClass scg = getSCG();
        CategorialClass ts = getTissueSpecimen();
        CategorialClass ms = getMs();

        participant.addChildCategorialClass(cpr);
        participant.addChildCategorialClass(scg);
        cpr.addChildCategorialClass(cp);
        scg.addChildCategorialClass(ts);
        scg.addChildCategorialClass(ms);

        List<ICategorialClassRecord> cprRecs1 = createRecord(cpr, 1);
        cprRecs1.get(0).addCategorialClassRecords(cp, createRecord(cp, 1));

        List<ICategorialClassRecord> cprRecs = createRecord(cpr, 2);
        cprRecs.get(0).addCategorialClassRecords(cp, createRecord(cp, 1));
        cprRecs.get(1).addCategorialClassRecords(cp, createRecord(cp, 1));

        List<ICategorialClassRecord> scgRec = createRecord(scg, 1);
        scgRec.get(0).addCategorialClassRecords(ts, createRecord(ts, 2));
        scgRec.get(0).addCategorialClassRecords(ms, createRecord(ms, 3));

        List<ICategorialClassRecord> scgRec1 = createRecord(scg, 1);
        scgRec1.get(0).addCategorialClassRecords(ts, createRecord(ts, 2));
        scgRec1.get(0).addCategorialClassRecords(ms, createRecord(ms, 2));

        List<ICategorialClassRecord> participantRecs = createRecord(participant, 2);
        participantRecs.get(0).addCategorialClassRecords(cpr, cprRecs);
        participantRecs.get(0).addCategorialClassRecords(scg, scgRec);

        participantRecs.get(1).addCategorialClassRecords(cpr, cprRecs1);
        participantRecs.get(1).addCategorialClassRecords(scg, scgRec1);

        return participantRecs;
    }

    /**     
     * Mock Method for getting object for <code>edu.wustl.catissuecore.domain.CollectionProtocol</code> class.
     * @return CategorialClass
     */
    private CategorialClass getCP() {
        return get("edu.wustl.catissuecore.domain.CollectionProtocol", "CP_title");
    }

    /**     
     * Mock Method for getting object for <code>edu.wustl.catissuecore.domain.CollectionProtocolRegistration</code> class.
     * @return CategorialClass
     */
    private CategorialClass getCPR() {
        return get("edu.wustl.catissuecore.domain.CollectionProtocolRegistration", "CPR_PPI");
    }

    /**     
     * Mock Method for getting object for <code>edu.wustl.catissuecore.domain.MolecularSpecimen</code> class.
     * @return CategorialClass
     */
    private CategorialClass getMs() {
        return get("edu.wustl.catissuecore.domain.MolecularSpecimen", "MS_label");
    }

    /**     
     * Mock Method for getting object for <code>edu.wustl.catissuecore.domain.Participant</code> class.
     * @return CategorialClass
     */
    private CategorialClass getParticipant() {
        return get("edu.wustl.catissuecore.domain.Participant", "Participant_gender");
    }

    /**     
     * Mock Method for getting object for <code>edu.wustl.catissuecore.domain.SpecimenCollectionGroup</code> class.
     * @return CategorialClass
     */
    private CategorialClass getSCG() {
        return get("edu.wustl.catissuecore.domain.SpecimenCollectionGroup", "SCG_clinicalDiagnosis");
    }

    /**     
     * Mock Method for getting object for <code>edu.wustl.catissuecore.domain.ReviewEventParameters</code> class.
     * @return CategorialClass
     */
    private CategorialClass getReviewEventParameters() {
        return get("edu.wustl.catissuecore.domain.ReviewEventParameters", "ReviewEventParameters_comments");
    }

    /**     
     * Mock Method for getting object for <code>edu.wustl.catissuecore.domain.TissueSpecimen</code> class.
     * @return CategorialClass
     */
    private CategorialClass getTissueSpecimen() {
        return get("edu.wustl.catissuecore.domain.TissueSpecimen", "TS_label");
    }

    /**
     * Mock Method for getting object for given class name.
     * @param className
     * @param attributes
     * @return CategorialClass
     */
    private CategorialClass get(String className, String... attributes) {
        CategorialClass categorialClass = new CategorialClass();
        EntityInterface entity = createEntity(className, attributes);
        categorialClass.setCategorialClassEntity(entity);
        return categorialClass;
    }

    /**
     * @param allowedAttributes
     *            the allowedAttributes to set
     */
    public void setAllowedAttributes(List<AttributeInterface> allowedAttributes) {
        this.allowedAttributes = allowedAttributes;
    }
}
