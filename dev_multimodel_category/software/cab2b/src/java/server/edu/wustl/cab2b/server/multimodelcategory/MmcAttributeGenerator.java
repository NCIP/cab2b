/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.multimodelcategory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.UserDefinedDE;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.BooleanValueInterface;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.DateValueInterface;
import edu.common.dynamicextensions.domaininterface.DoubleValueInterface;
import edu.common.dynamicextensions.domaininterface.FloatValueInterface;
import edu.common.dynamicextensions.domaininterface.IntegerValueInterface;
import edu.common.dynamicextensions.domaininterface.LongValueInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.StringValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.wustl.cab2b.common.multimodelcategory.bean.MultiModelAttributeBean;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.queryobject.DataType;

/**
 * @author chandrakant_talele
 * @author gaurav_mehta
 */
public class MmcAttributeGenerator {

    /**
     * @param mmaBean
     * @return AttributeInterface
     */
    public static AttributeInterface getAttribute(MultiModelAttributeBean mmaBean) {
        Collection<AttributeInterface> allAttributes = mmaBean.getSelectedAttributes();
        Set<DataType> types = new HashSet<DataType>();
        for (AttributeInterface attribute : allAttributes) {
            types.add(Utility.getDataType(attribute.getAttributeTypeInformation()));
        }

        AttributeInterface attribute = createAttributeBasedOnRules(types);
        //if all attributes are of same type then create PV union
        if (types.size() == 1) {
            UserDefinedDE userDefinedDE = getPermissibleValueUnion(allAttributes, types.iterator().next());
            if(! userDefinedDE.getPermissibleValueCollection().isEmpty()) {
                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
            }
        }
        return attribute;
    }

    private static AttributeInterface createAttributeBasedOnRules(Set<DataType> dataTypes) {
        List<DataType> types = new ArrayList<DataType>(dataTypes);
        DataType type = null;
        for (int i = 0; i < types.size(); i++) {
            type = getDataType(type, types.get(i));
        }
        return createAttribute(type);
    }

    /**
     * @param first
     * @param second
     * @return Data Type
     */
    public static DataType getDataType(DataType first, DataType second) {
        if (first == null || first == second) {
            return second;
        }
        if (first == DataType.String || second == DataType.String) {
            return DataType.String;
        }
        if (first == DataType.Boolean || second == DataType.Boolean) {
            return DataType.String;
        }
        if (first == DataType.Date || second == DataType.Date) {
            return DataType.String;
        }
        if (first == DataType.Double || second == DataType.Double) {
            return DataType.Double;
        }
        if (first == DataType.Float || second == DataType.Float) {
            return DataType.Float;
        }
        if (first == DataType.Long || second == DataType.Long) {
            return DataType.Long;
        }
        return DataType.String;
    }

    private static UserDefinedDE getPermissibleValueUnion(Collection<AttributeInterface> allAttributes,
                                                          DataType dataType) {
        Set<Object> setOfValues = new HashSet<Object>();

        for (AttributeInterface attr : allAttributes) {
            DataElementInterface dataElement = attr.getAttributeTypeInformation().getDataElement();
            UserDefinedDEInterface userDefined = (UserDefinedDEInterface) dataElement;

            if (userDefined != null) {
                Collection<PermissibleValueInterface> allValues = userDefined.getPermissibleValueCollection();
                for (PermissibleValueInterface pv : allValues) {
                    setOfValues.add(pv.getValueAsObject());
                }
            }
        }
        UserDefinedDE userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
        for (Object pv : setOfValues) {
            PermissibleValueInterface permisssibleValue = createPermissibleValue(dataType, pv);
            userDefinedDE.addPermissibleValue(permisssibleValue);
        }
        return userDefinedDE;
    }

    private static PermissibleValueInterface createPermissibleValue(DataType dataType, Object value) {
        DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
        switch (dataType) {
            case String:
                StringValueInterface p1 = domainObjectFactory.createStringValue();
                p1.setValue((String) value);
                return p1;

            case Double:
                DoubleValueInterface p2 = domainObjectFactory.createDoubleValue();
                p2.setValue((Double) value);
                return p2;

            case Integer:
                IntegerValueInterface p3 = domainObjectFactory.createIntegerValue();
                p3.setValue((Integer) value);
                return p3;

            case Date:
                DateValueInterface p4 = domainObjectFactory.createDateValue();
                p4.setValue((Date) value);
                return p4;

            case Float:
                FloatValueInterface p5 = domainObjectFactory.createFloatValue();
                p5.setValue((Float) value);
                return p5;

            case Boolean:
                BooleanValueInterface p6 = domainObjectFactory.createBooleanValue();
                p6.setValue((Boolean) value);
                return p6;

            case Long:
                LongValueInterface p7 = domainObjectFactory.createLongValue();
                p7.setValue((Long) value);
                return p7;
        }
        return null;
    }

    private static AttributeInterface createAttribute(DataType dataType) {
        AttributeInterface attribute = null;
        DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
        switch (dataType) {
            case String:
                attribute = domainObjectFactory.createStringAttribute();
                break;

            case Double:
                attribute = domainObjectFactory.createDoubleAttribute();
                break;

            case Integer:
                attribute = domainObjectFactory.createIntegerAttribute();
                break;

            case Date:
                attribute = domainObjectFactory.createDateAttribute();
                break;

            case Float:
                attribute = domainObjectFactory.createFloatAttribute();
                break;

            case Boolean:
                attribute = domainObjectFactory.createBooleanAttribute();
                break;

            case Long:
                attribute = domainObjectFactory.createLongAttribute();
                break;

        }
        return attribute;
    }
}