/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.List;

import edu.wustl.common.util.CompoundEnum;

public class TimeInterval<T extends Enum<?> & ITimeIntervalEnum> extends CompoundEnum<TimeInterval<T>, T> {
    private static final long serialVersionUID = -5638354869641955221L;

    // list of the primitive enums.
    private static final List<ITimeIntervalEnum> primitiveValues = new ArrayList<ITimeIntervalEnum>();

    // list of compound enums; ordering is same as the "values" list
    private static final List<TimeInterval<?>> values = new ArrayList<TimeInterval<?>>();

    // ordinal tracker
    private static int nextOrdinal = 0;

    private final int numSeconds;

    // private constructor because this is an enum
    private TimeInterval(T primitiveEnum) {
        super(primitiveEnum, nextOrdinal++, primitiveEnum.name());
        primitiveValues.add(primitiveEnum);
        values.add(this);
        this.numSeconds = ((ITimeIntervalEnum)primitiveEnum).numSeconds();
    }

    // the compound enum values
    public static final TimeInterval<DSInterval> Second = new TimeInterval<DSInterval>(DSInterval.Second);

    public static final TimeInterval<DSInterval> Minute = new TimeInterval<DSInterval>(DSInterval.Minute);

    public static final TimeInterval<DSInterval> Hour = new TimeInterval<DSInterval>(DSInterval.Hour);

    public static final TimeInterval<DSInterval> Day = new TimeInterval<DSInterval>(DSInterval.Day);

    public static final TimeInterval<DSInterval> Week = new TimeInterval<DSInterval>(DSInterval.Week);

    public static final TimeInterval<YMInterval> Month = new TimeInterval<YMInterval>(YMInterval.Month);

    public static final TimeInterval<YMInterval> Quarter = new TimeInterval<YMInterval>(YMInterval.Quarter);

    public static final TimeInterval<YMInterval> Year = new TimeInterval<YMInterval>(YMInterval.Year);

    public int numSeconds() {
        return numSeconds;
    }
    // all the compound enum values in this compound enum
    // this method is mandated by the CompoundEnum contract
    public static TimeInterval<?>[] values() {
        return values.toArray(new TimeInterval<?>[0]);
    }

    public static TimeInterval<?> valueOf(String name) {
        if (name == null) {
            throw new NullPointerException("name is null.");
        }
        for (TimeInterval<?> timeInterval : values) {
            if (timeInterval.name().equals(name)) {
                return timeInterval;
            }
        }
        throw new IllegalArgumentException("no compound enum AllDataTypes." + name);
    }

    // returns all the primitive enum values in this compound enum.
    public static ITimeIntervalEnum[] primitiveValues() {
        return primitiveValues.toArray(new ITimeIntervalEnum[0]);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<?> & ITimeIntervalEnum> TimeInterval<T> compoundEnum(T primitiveEnum) {
        if (primitiveEnum == null) {
            throw new IllegalArgumentException();
        }
        int index = primitiveValues.indexOf(primitiveEnum);
        return (TimeInterval<T>) values.get(index);
    }

    // serialization; ensure unique instance
    private Object readResolve() throws ObjectStreamException {
        return values.get(ordinal());
    }
}
