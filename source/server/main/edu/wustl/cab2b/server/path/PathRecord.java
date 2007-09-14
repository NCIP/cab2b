package edu.wustl.cab2b.server.path;

import edu.wustl.cab2b.common.util.Constants;


/**
 * This class is object representation of the table PATH. <br>
 * One instance of this class is created per row.
 * It avoids passing the result set across methods.
 * @author Chandrakant Talele
 */
public class PathRecord {
    private long pathId;

    private long firstEntityId;

    private String intermediateAssociations;

    private long lastEntityId;

    /**
     * The only public constructor.
     * @param pathId Path id
     * @param firstEntityId Id of the very first entity in the path.
     * @param intermediateAssociations String made by connecting all intermediate association IDs by {@link PathConstants#CONNECTOR}.
     * @param lastEntityId Id of the very last entity in the path.
     */
    public PathRecord(long pathId, long firstEntityId, String intermediateAssociations, long lastEntityId) {
        this.pathId = pathId;
        this.firstEntityId = firstEntityId;
        this.intermediateAssociations = intermediateAssociations;
        this.lastEntityId = lastEntityId;
    }

    /**
     * This method parses the intermediate path and returns all Ids present in it.<br>
     * It parses intermediate path string based on {@link Constants#CONNECTOR} and 
     * then converts it to Association ids.
     * @return Returns the List of Ids of all Associations present in the path in sequential order.
     */
    public Long[] getAssociationSequence() {
        String[] associationIds = this.intermediateAssociations.split(Constants.CONNECTOR);
        Long[] ids = new Long[associationIds.length];
        for (int i = 0; i < associationIds.length; i++) {
            ids[i] = Long.decode(associationIds[i]);
        }
        return ids;
    }

    /**
     * @return Returns the pathId.
     */
    public long getPathId() {
        return pathId;
    }

    /**
     * @return Returns the firstEntityId.
     */
    public long getFirstEntityId() {
        return firstEntityId;
    }

    /**
     * @return Returns the lastEntityId.
     */
    public long getLastEntityId() {
        return lastEntityId;
    }

    /**
     * @return Returns the intermediateAssociations.
     */
    public String getIntermediateAssociations() {
        return intermediateAssociations;
    }

}
