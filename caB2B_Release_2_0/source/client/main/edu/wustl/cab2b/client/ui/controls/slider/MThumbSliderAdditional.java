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

    public Rectangle getTrackRect();

    public Dimension getThumbSize();

    public int xPositionForValue(int value);

    public int yPositionForValue(int value);

}
