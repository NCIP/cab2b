/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.IParameter;
import edu.wustl.common.querysuite.queryobject.IParameterizable;

public class Parameter<T extends IParameterizable> extends BaseQueryObject implements IParameter<T> {
    private static final long serialVersionUID = 626816287100504757L;

    private T parameterizedObject;

    private String name;

    private Parameter() {
    // for hibernate
    }

    public Parameter(T parameterizedObject, String name) {
        setParameterizedObject(parameterizedObject);
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getParameterizedObject() {
        return parameterizedObject;
    }

    public void setParameterizedObject(T parameterizedObject) {
        this.parameterizedObject = parameterizedObject;
    }
}
