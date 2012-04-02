package edu.wustl.common.querysuite.queryobject;

public interface IParameter<T extends IParameterizable> extends INameable {
    T getParameterizedObject();
}
