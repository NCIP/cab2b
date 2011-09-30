package edu.wustl.common.querysuite.queryobject;

/**
 * Represents the time intervals that are completely deterministic. The name is
 * borrowed from the corresponding Oracle data type.
 * 
 * @author srinath_k
 * @see YMInterval
 */
public enum DSInterval implements ITimeIntervalEnum {
    Second(1), Minute(60), Hour(60 * 60), Day(Hour.numSeconds * 24), Week(Day.numSeconds * 7);

    private int numSeconds;

    private DSInterval(int numSeconds) {
        this.numSeconds = numSeconds;
    }

    public int numSeconds() {
        return numSeconds;
    }
}
