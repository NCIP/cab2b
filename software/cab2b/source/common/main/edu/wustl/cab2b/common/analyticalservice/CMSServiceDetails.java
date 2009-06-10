package edu.wustl.cab2b.common.analyticalservice;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.StringValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;

/**
 * @author Chandrakant Talele
 */
public class CMSServiceDetails implements ServiceDetailsInterface {

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.analyticalservice.ServiceDetailsInterface#getDisplayName()
     */
    public String getDisplayName() {
        return "Comparative Marker Selection";
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.analyticalservice.ServiceDetailsInterface#getRequiredEntities()
     */
    public List<EntityInterface> getRequiredEntities() {
        DomainObjectFactory fact = DomainObjectFactory.getInstance();
        EntityInterface bioAssay = fact.createEntity();
        bioAssay.setName("gov.nih.nci.mageom.domain.BioAssay.BioAssay");

        EntityInterface cmsParams = fact.createEntity();
        cmsParams.setName("ComparativeMarkerSelectionParameterSet");

        AttributeInterface testDirection = fact.createStringAttribute();
        testDirection.setName("testDirection");
        AttributeInterface testStatistic = fact.createStringAttribute();
        testStatistic.setName("testStatistic");
        AttributeInterface minStd = fact.createFloatAttribute();
        minStd.setName("minStd");
        AttributeInterface numberOfPermutations = fact.createIntegerAttribute();
        numberOfPermutations.setName("numberOfPermutations");
        AttributeInterface complete = fact.createBooleanAttribute();
        complete.setName("complete");
        AttributeInterface balanced = fact.createBooleanAttribute();
        balanced.setName("balanced");
        AttributeInterface randomSeed = fact.createLongAttribute();
        randomSeed.setName("randomSeed");
        AttributeInterface smoothPvalues = fact.createBooleanAttribute();
        smoothPvalues.setName("smoothPvalues");
        AttributeInterface phenotypeTest = fact.createStringAttribute();
        phenotypeTest.setName("phenotypeTest");
        AttributeInterface[] attributeArray = { testDirection, testStatistic, phenotypeTest };

        String[][] at1 = { { "two-sided", "class0", "class1" }, { "SNR", "SNR-median", "SNR-medianMinstd", "SNR-minstd" }, { "one-versus-all", "all-pairs" } };
        for (int i = 0; i < attributeArray.length; i++) {

            UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
            for (int j = 0; j < at1[i].length; j++) {
                StringValueInterface value = fact.createStringValue();
                value.setValue(at1[i][j]);
                userDefinedDE.addPermissibleValue(value);
            }
            attributeArray[i].getAttributeTypeInformation().setDataElement(userDefinedDE);
        }
        cmsParams.addAbstractAttribute(testDirection);
        cmsParams.addAbstractAttribute(testStatistic);
        cmsParams.addAbstractAttribute(minStd);
        cmsParams.addAbstractAttribute(numberOfPermutations);
        cmsParams.addAbstractAttribute(complete);
        cmsParams.addAbstractAttribute(balanced);
        cmsParams.addAbstractAttribute(randomSeed);
        cmsParams.addAbstractAttribute(smoothPvalues);
        cmsParams.addAbstractAttribute(phenotypeTest);

        List<EntityInterface> list = new ArrayList<EntityInterface>(2);
        list.add(bioAssay);
        list.add(cmsParams);
        return list;
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.analyticalservice.ServiceDetailsInterface#getServiceURL()
     */
    public String getServiceURL() {
        return "http://node255.broad.mit.edu:6060/wsrf/services/cagrid/ComparativeMarkerSelMAGESvc";
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.analyticalservice.ServiceDetailsInterface#getMethodName()
     */
    public String getMethodName() {
        return "invoke";
    }
}