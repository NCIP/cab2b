/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.utils;

import org.jmock.Expectations;
import org.jmock.Mockery;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.DateTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.NumericTypeInformationInterface;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.TermType;

public class DynExtnMockUtil {
    private static Mockery context = new Mockery();

    private static int i = 0;

    private static synchronized String getI() {
        return String.valueOf(i++);
    }

    public static AttributeInterface createAttribute(final String name, final EntityInterface entity) {
        final AttributeInterface attribute = context.mock(AttributeInterface.class, getI());

        context.checking(new Expectations() {
            {
                allowing(attribute).getName();
                will(returnValue(name));
                allowing(attribute).getEntity();
                will(returnValue(entity));
                allowing(attribute).getAttributeTypeInformation();
                will(returnValue(context.mock(NumericTypeInformationInterface.class, getI())));
            }
        });
        return attribute;
    }

    public static AttributeInterface createAttribute(final String name, final EntityInterface entity,
            final TermType termType) {
        final AttributeTypeInformationInterface attrInfo;
        switch (termType) {
            case Numeric :
                attrInfo = context.mock(NumericTypeInformationInterface.class, getI());
                break;
            case Date :
                attrInfo = context.mock(DateTypeInformationInterface.class, getI());
                context.checking(new Expectations() {
                    {
                        allowing((DateTypeInformationInterface) attrInfo).getFormat();
                        will(returnValue("foo"));
                    }
                });
                break;
            case Timestamp :
                attrInfo = context.mock(DateTypeInformationInterface.class, getI());
                context.checking(new Expectations() {
                    {
                        allowing((DateTypeInformationInterface) attrInfo).getFormat();
                        will(returnValue("MM-dd-yyyy HH:mm"));
                    }
                });
                break;
            default :
                throw new IllegalArgumentException();
        }
        final AttributeInterface attribute = context.mock(AttributeInterface.class, getI());

        context.checking(new Expectations() {
            {
                allowing(attribute).getName();
                will(returnValue(name));
                allowing(attribute).getEntity();
                will(returnValue(entity));
                allowing(attribute).getAttributeTypeInformation();
                will(returnValue(attrInfo));

            }
        });
        return attribute;
    }

    public static EntityInterface createEntity(final String name) {
        final EntityInterface entity = context.mock(EntityInterface.class, getI());
        context.checking(new Expectations() {
            {
                allowing(entity).getName();
                will(returnValue(name));
            }
        });
        return entity;
    }
    
    public static IExpression createExpression(final int id) {
        final IExpression expr = context.mock(IExpression.class, getI());
        context.checking(new Expectations() {
            {
                allowing(expr).getExpressionId();
                will(returnValue(id));
            }
        });
        return expr;
    }
}
