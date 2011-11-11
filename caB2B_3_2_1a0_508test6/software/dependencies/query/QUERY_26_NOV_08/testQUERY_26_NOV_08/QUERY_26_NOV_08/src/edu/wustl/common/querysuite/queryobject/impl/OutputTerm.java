package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.IOutputTerm;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.TimeInterval;

public class OutputTerm extends BaseQueryObject implements IOutputTerm {
    private static final long serialVersionUID = 5063978137285352552L;

    private String name;

    private ITerm term;

    private TimeInterval<?> timeInterval;

    public OutputTerm() {

    }

    public OutputTerm(String name, ITerm term) {
        this.name = name;
        this.term = term;
    }

    public String getName() {
        if (name == null) {
            name = "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ITerm getTerm() {
        if (term == null) {
            term = QueryObjectFactory.createTerm();
        }
        return term;
    }

    public void setTerm(ITerm term) {
        this.term = term;
    }

    @Override
    public String toString() {
        return "term : " + term + "; name : " + name;
    }

    public TimeInterval<?> getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(TimeInterval<?> timeInterval) {
        this.timeInterval = timeInterval;
    }
}
