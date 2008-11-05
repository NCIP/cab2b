package edu.wustl.cab2b.client.ui.controls.slider;

import java.awt.Dimension;
import java.awt.Rectangle;

/**
 * @author Hrishikesh Rajpathak
 * @author Deepak Shingan
 * @author Atul Jawale
 *
 */
public interface MThumbSliderAdditional {

    /**
     * @return Rectangle
     */
    public Rectangle getTrackRect();

    /**
     * @return Dimension
     */
    public Dimension getThumbSize();

    /**
     * @param value
     * @return integer
     */
    public int xPositionForValue(int value);

    /**
     * @param value
     * @return integer 
     */
    public int yPositionForValue(int value);

}
