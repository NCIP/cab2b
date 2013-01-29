/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls.slider;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 * @author Hrishikesh Rajpathak
 * @author Deepak Shingan
 * @author Atul Jawale
 *
 */
public class MThumbSliderAdditionalUI {

    MThumbSlider mSlider;

    BasicSliderUI ui;

    Rectangle[] thumbRects;

    int thumbNum;

    private transient boolean isDragging;

    Icon thumbRenderer;

    Rectangle trackRect;

    ChangeHandler changeHandler;

    TrackListener trackListener;

    /** Constructor
     * @param ui
     */
    public MThumbSliderAdditionalUI(BasicSliderUI ui) {
        this.ui = ui;
    }

    /** 
     * Sets UI for given Component
     * @param c
     */
    public void installUI(JComponent c) {
        mSlider = (MThumbSlider) c;
        JToolTip toolTip = mSlider.createToolTip();

        thumbNum = mSlider.getThumbNum();
        thumbRects = new Rectangle[thumbNum];
        for (int i = 0; i < thumbNum; i++) {
            thumbRects[i] = new Rectangle();
        }
        isDragging = false;
        trackListener = new MThumbSliderAdditionalUI.TrackListener(mSlider);

        changeHandler = new ChangeHandler();
    }

    /**
     * Removes UI for given Component
     * @param c
     */
    public void uninstallUI(JComponent c) {
        thumbRects = null;
        trackListener = null;
        changeHandler = null;
    }

    /**
     * Calculates Thumb Size
     */
    protected void calculateThumbsSize() {
        Dimension size = ((MThumbSliderAdditional) ui).getThumbSize();
        for (int i = 0; i < thumbNum; i++) {
            thumbRects[i].setSize(size.width, size.height);
        }
    }

    /**
     * calculates Thumb Location
     */
    protected void calculateThumbsLocation() {
        for (int i = 0; i < thumbNum; i++) {
            if (mSlider.getSnapToTicks()) {
                int tickSpacing = mSlider.getMinorTickSpacing();
                if (tickSpacing == 0) {
                    tickSpacing = mSlider.getMajorTickSpacing();

                }
                if (tickSpacing != 0) {
                    int sliderValue = mSlider.getValueAt(i);
                    int snappedValue = sliderValue;
                    int min = mSlider.getMinimum();
                    if ((sliderValue - min) % tickSpacing != 0) {
                        float temp = (float) (sliderValue - min) / (float) tickSpacing;

                        int whichTick = Math.round(temp);
                        snappedValue = min + (whichTick * tickSpacing);
                        mSlider.setValueAt(snappedValue, i);
                    }
                }
            }
            trackRect = getTrackRect();
            if (mSlider.getOrientation() == JSlider.HORIZONTAL) {
                int value = mSlider.getValueAt(i);
                int valuePosition = ((MThumbSliderAdditional) ui).xPositionForValue(value);

                if (i == 0) {
                    valuePosition = valuePosition - 5;
                }

                thumbRects[i].x = valuePosition - (thumbRects[i].width / 2);
                thumbRects[i].y = trackRect.y;

            } else {
                int valuePosition = ((MThumbSliderAdditional) ui).yPositionForValue(mSlider.getValueAt(i)); // need
                thumbRects[i].x = trackRect.x;
                thumbRects[i].y = valuePosition - (thumbRects[i].height / 2);
            }
        }
    }

    /**
     * @return Thumb Number
     */
    public int getThumbNum() {
        return thumbNum;
    }

    /**
     * @return Thumb Rectangles
     */
    public Rectangle[] getThumbRects() {
        return thumbRects;
    }

    private static Rectangle unionRect = new Rectangle();

    /**
     * Sets Thumb Location for given location
     * @param x
     * @param y
     * @param index
     */
    public void setThumbLocationAt(int x, int y, int index) {

        Rectangle rect = thumbRects[index];
        unionRect.setBounds(rect);

        if (index == 0) {
            rect.setLocation(x, y);
        }

        else {
            rect.setLocation(x + 5, y);
        }
        SwingUtilities.computeUnion(rect.x, rect.y, rect.width, rect.height, unionRect);
        mSlider.repaint(unionRect.x, unionRect.y, unionRect.width, unionRect.height);
    }

    /**
     * @return
     */
    public Rectangle getTrackRect() {
        return ((MThumbSliderAdditional) ui).getTrackRect();
    }

    /**
     * @author 
     *
     */
    public class ChangeHandler implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            if (!isDragging) {
                calculateThumbsLocation();
                mSlider.repaint();
            }
        }
    }

    /**
     * @author 
     *
     */
    public class TrackListener extends MouseInputAdapter {
        protected transient int offset;

        protected transient int currentMouseX;

        protected transient int currentMouseY;

        protected Rectangle adjustingThumbRect = null;

        protected int adjustingThumbIndex;

        protected MThumbSlider slider;

        protected Rectangle trackRect;

        TrackListener(MThumbSlider slider) {
            this.slider = slider;
        }

        public void mouseClicked(MouseEvent e) {

            setBarPosition(e);
        }

        public void mousePressed(MouseEvent e) {
            setBarPosition(e);
        }

        private void setBarPosition(MouseEvent e) {

            if (!slider.isEnabled()) {
                return;
            }
            currentMouseX = e.getX();
            currentMouseY = e.getY();
            slider.requestFocus();

            for (int i = 0; i < thumbNum; i++) {
                Rectangle rect = thumbRects[i];

                if (rect.contains(currentMouseX, currentMouseY)) {

                    switch (slider.getOrientation()) {
                        case JSlider.VERTICAL:
                            offset = currentMouseY - rect.y;
                            break;
                        case JSlider.HORIZONTAL:
                            offset = currentMouseX - rect.x;
                            break;
                    }
                    isDragging = true;
                    slider.setValueIsAdjusting(true);
                    adjustingThumbRect = rect;
                    adjustingThumbIndex = i;
                    return;
                }
            }

        }

        private void setVerticaleBarPosition(int y) {
            Rectangle rect = thumbRects[adjustingThumbIndex];
            int thumbMiddle = 0;
            int halfThumbHeight = rect.height / 2;
            int thumbTop = y - offset;
            int trackTop = trackRect.y;
            int trackBottom = trackRect.y + (trackRect.height - 1);

            thumbTop = Math.max(thumbTop, trackTop - halfThumbHeight);
            thumbTop = Math.min(thumbTop, trackBottom - halfThumbHeight);
            if (isValidPosition(rect.x, thumbTop)) {
                setThumbLocationAt(rect.x, thumbTop, adjustingThumbIndex);
                thumbMiddle = thumbTop + halfThumbHeight;
                mSlider.setValueAt(ui.valueForYPosition(thumbMiddle), adjustingThumbIndex);
            }
        }

        private void setHorizontalBarPosition(int x) {
            Rectangle rect = thumbRects[adjustingThumbIndex];
            int thumbMiddle = 0;
            int halfThumbWidth = rect.width / 2;
            int thumbLeft = x - offset;
            int trackLeft = trackRect.x;
            int trackRight = trackRect.x + (trackRect.width - 1);

            thumbLeft = Math.max(thumbLeft, trackLeft - halfThumbWidth);
            thumbLeft = Math.min(thumbLeft, trackRight - halfThumbWidth);

            if (isValidPosition(thumbLeft, rect.y)) {
                setThumbLocationAt(thumbLeft, rect.y, adjustingThumbIndex);
                thumbMiddle = thumbLeft + halfThumbWidth;
                mSlider.setValueAt(ui.valueForXPosition(thumbMiddle), adjustingThumbIndex);
            }
        }

        public void mouseDragged(MouseEvent e) {
            if (!slider.isEnabled() || !isDragging || !slider.getValueIsAdjusting() || adjustingThumbRect == null) {
                return;
            }

            currentMouseX = e.getX();
            currentMouseY = e.getY();

            trackRect = getTrackRect();
            switch (slider.getOrientation()) {
                case JSlider.VERTICAL:
                    setVerticaleBarPosition(currentMouseY);

                    break;

                case JSlider.HORIZONTAL:

                    setHorizontalBarPosition(currentMouseX);
                    break;
            }
            slider.currentInputValueIndex = slider.getValueAt(adjustingThumbIndex);
            String currentValue = "";
            try {

                Object value = mSlider.getMinimumBarValue();

                if (value instanceof Date) {
                    DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
                    currentValue = dateFormatter.format(value);
                    mSlider.displayMinValue(dateFormatter.format(value));
                } else {

                    currentValue = mSlider.getMinimumBarValue().toString();
                    if (currentValue.length() > 15) {
                        currentValue = currentValue.substring(0, 14) + "...";
                    }

                    mSlider.displayMinValue(currentValue);

                }
            } catch (ArrayIndexOutOfBoundsException arrInd) {
                String value = Integer.toString(mSlider.getMinimum());
                if (value.length() > 15) {
                    value = value.substring(0, 14) + "...";
                }

                mSlider.displayMinValue(value);
            }
            try {
                Object value = mSlider.getMaximumBarValue();
                if (value instanceof Date) {
                    DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
                    mSlider.displayMaxValue(dateFormatter.format(value));
                } else {
                    String maxValue = mSlider.getMaximumBarValue().toString();
                    if (maxValue.length() > 15) {
                        maxValue = maxValue.substring(0, 14) + "...";
                    }
                    mSlider.displayMaxValue(maxValue);

                }
            } catch (ArrayIndexOutOfBoundsException arrInd) {
                String value = Integer.toString(mSlider.getMaximum());
                if (value.length() > 15) {
                    value = value.substring(0, 14) + "...";
                }
                mSlider.displayMaxValue(value);
            }

        }

        public void mouseMoved(MouseEvent e) {
            if (slider.getOrientation() == JSlider.VERTICAL) {
                slider.currentInputValueIndex = ui.valueForYPosition(e.getY());
            } else {
                slider.currentInputValueIndex = ui.valueForXPosition(e.getX());
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (!slider.isEnabled()) {
                return;
            }
            offset = 0;
            isDragging = false;
            mSlider.setValueIsAdjusting(false);
            slider.currentInputValueIndex = slider.getValueAt(adjustingThumbIndex);
            mSlider.repaint();
            Object min = null;
            Object max = null;
            try {
                min = mSlider.getMinimumBarValue();

            } catch (ArrayIndexOutOfBoundsException arrInd) {
                min = Integer.toString(mSlider.getMinimum());

            }
            try {
                max = mSlider.getMaximumBarValue();

            } catch (ArrayIndexOutOfBoundsException arrInd) {
                max = Integer.toString(mSlider.getMaximum());

            }
            mSlider.setFilterValues(min, max);

        }

        /**
         * Gives a value whether scroll is allowed or not in given direction
         * @param direction
         * @return
         */
        public boolean shouldScroll(int direction) {
            return false;
        }

        /**
         * @param x
         * @param y
         * @return
         */
        public boolean isValidPosition(int x, int y) {
            boolean isValidPosition = true;
            for (int i = 0; i < adjustingThumbIndex; i++) {
                if (thumbRects[i].x > x || thumbRects[i].y > y) {
                    isValidPosition = false;
                    break;
                }
            }

            for (int i = adjustingThumbIndex + 1; i < thumbNum; i++) {

                if (thumbRects[i].x < x || thumbRects[i].y < y) {
                    isValidPosition = false;
                    break;
                }
            }
            return isValidPosition;
        }
    }
}
