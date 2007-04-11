package edu.wustl.cab2b.common.util;


/**
 * @author Chandrakant Talele
 */
public class IdGenerator {

    /**
     * 
     */
    static long id;

    /**
     * @param value
     */
    public static void setInitialValue(long value) {
        id = value;
    }

    /**
     * @return
     */
    public static synchronized long getNextId() {
        return id++;
    }
}