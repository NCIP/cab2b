package edu.wustl.common.querysuite.queryobject;

/**
 * Used to offset a date operand. Such an offset has a time interval associated
 * to it e.g. if the offset is 3 Months, then the time interval is "Month".
 * 
 * @author srinath_k
 */
public interface IDateOffset extends IBaseQueryObject {
    TimeInterval<?> getTimeInterval();
    
    void setTimeInterval(TimeInterval<?> timeInterval);
}
