/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls.slider;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalSliderUI;

/**
 * @author Hrishikesh Rajpathak
 * @author Deepak Shingan
 * @author Atul Jawale
 *
 */
public class MetalMThumbSliderUI extends MetalSliderUI implements MThumbSliderAdditional {

    /**
     * Panel containg extra thumb slider components
     */
    MThumbSliderAdditionalUI additonalUi;

    /**
     * Thumn track listener
     */
    MouseInputAdapter mThumbTrackListener;

    /**
     * Method for generating UI
     * @param c
     * @return ComponentUI
     */
    public static ComponentUI createUI(JComponent c) {
        return new MetalMThumbSliderUI((JSlider) c);
    }

    /**
     * Constructor
     */
    public MetalMThumbSliderUI() {
        //super(null);
    }

    /**
     * Constructor
     * @param b
     */
    public MetalMThumbSliderUI(JSlider b) {
        //super(null);
    }

    /**
     * Sets UI for given Component
     * @param c JComponent
     * @see javax.swing.plaf.metal.MetalSliderUI#installUI(javax.swing.JComponent)
     */
    public void installUI(JComponent c) {
        additonalUi = new MThumbSliderAdditionalUI(this);
        additonalUi.installUI(c);
        mThumbTrackListener = createMThumbTrackListener((JSlider) c);
        super.installUI(c);
    }

    /**
     * Removes UI for given Component
     * @param c JComponenet
     * @see javax.swing.plaf.basic.BasicSliderUI#uninstallUI(javax.swing.JComponent)
     */
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        additonalUi.uninstallUI(c);
        additonalUi = null;
        mThumbTrackListener = null;
    }

    /**
     * Sets thumb track listener for slider
     * @param slider
     * @return
     */
    protected MouseInputAdapter createMThumbTrackListener(JSlider slider) {
        return additonalUi.trackListener;
    }

    /** 
     * @param slider
     * @return null
     * @see javax.swing.plaf.basic.BasicSliderUI#createTrackListener(javax.swing.JSlider)
     */
    protected TrackListener createTrackListener(JSlider slider) {
        return null;
    }

    /** 
     * @param slider
     * @return
     * @see javax.swing.plaf.basic.BasicSliderUI#createChangeListener(javax.swing.JSlider) 
     */
    protected ChangeListener createChangeListener(JSlider slider) {
        return additonalUi.changeHandler;
    }

    /**
     * Installs Listener
     * @param slider
     * @see javax.swing.plaf.basic.BasicSliderUI#installListeners(javax.swing.JSlider)
     */
    protected void installListeners(JSlider slider) {
        slider.addMouseListener(mThumbTrackListener);
        slider.addMouseMotionListener(mThumbTrackListener);
        slider.addFocusListener(focusListener);
        slider.addComponentListener(componentListener);
        slider.addPropertyChangeListener(propertyChangeListener);
        slider.getModel().addChangeListener(changeListener);
    }

    /**
     * Removes Slider Listener
     * @param slider
     * @see javax.swing.plaf.basic.BasicSliderUI#uninstallListeners(javax.swing.JSlider)
     */
    protected void uninstallListeners(JSlider slider) {
        slider.removeMouseListener(mThumbTrackListener);
        slider.removeMouseMotionListener(mThumbTrackListener);
        slider.removeFocusListener(focusListener);
        slider.removeComponentListener(componentListener);
        slider.removePropertyChangeListener(propertyChangeListener);
        slider.getModel().removeChangeListener(changeListener);
    }

    /**
     * Calculates Goemetry 
     * @see javax.swing.plaf.basic.BasicSliderUI#calculateGeometry()
     */
    protected void calculateGeometry() {
        super.calculateGeometry();
        additonalUi.calculateThumbsSize();
        additonalUi.calculateThumbsLocation();
    }

    /**
     * Calculates Thumb Location
     * @see javax.swing.plaf.basic.BasicSliderUI#calculateThumbLocation()
     * 
     */
    protected void calculateThumbLocation() {
        //do nothing
    }

    Icon thumbRenderer;

    /**
     * Paints the given component with given graphics
     * @param g Graphics
     * @param c JComponenet
     * @see javax.swing.plaf.basic.BasicSliderUI#paint(java.awt.Graphics, javax.swing.JComponent)
     */
    public void paint(Graphics g, JComponent c) {
        Rectangle clip = g.getClipBounds();
        Rectangle[] thumbRects = additonalUi.getThumbRects();
        thumbRect = thumbRects[0];
        int thumbNum = additonalUi.getThumbNum();

        if (slider.getPaintTrack() && clip.intersects(trackRect)) {
            boolean filledSlider_tmp = filledSlider;
            filledSlider = false;
            paintTrack(g);
            filledSlider = filledSlider_tmp;

            if (filledSlider) {
                g.translate(trackRect.x, trackRect.y);

                Point t1 = new Point(0, 0);
                Point t2 = new Point(0, 0);
                Rectangle maxThumbRect = new Rectangle(thumbRect);
                thumbRect = maxThumbRect;

                if (slider.getOrientation() == JSlider.HORIZONTAL) {
                    t2.y = (trackRect.height - 1) - getThumbOverhang();
                    t1.y = t2.y - (getTrackWidth() - 1);
                    t2.x = trackRect.width - 1;
                    int maxPosition = xPositionForValue(slider.getMaximum());
                    thumbRect.x = maxPosition - (thumbRect.width / 2) - 2;
                    thumbRect.y = trackRect.y;
                } else {
                    t1.x = (trackRect.width - getThumbOverhang()) - getTrackWidth();
                    t2.x = (trackRect.width - getThumbOverhang()) - 1;
                    t2.y = trackRect.height - 1;
                    int maxPosition = yPositionForValue(slider.getMaximum());
                    thumbRect.x = trackRect.x;
                    thumbRect.y = maxPosition - (thumbRect.height / 2) - 2;
                }

                Color fillColor = ((MThumbSlider) slider).getTrackFillColor();
                if (fillColor == null) {
                    fillColor = MetalLookAndFeel.getControlShadow();
                }
                fillTrack(g, t1, t2, fillColor);

                for (int i = thumbNum - 1; 0 <= i; i--) {
                    thumbRect = thumbRects[i];
                    fillColor = ((MThumbSlider) slider).getFillColorAt(i);
                    if (fillColor == null) {
                        fillColor = MetalLookAndFeel.getControlShadow();
                    }
                    fillTrack(g, t1, t2, fillColor);
                }

                g.translate(-trackRect.x, -trackRect.y);
            }
        }
        if (slider.getPaintTicks() && clip.intersects(tickRect)) {
            paintTicks(g);
        }
        if (slider.getPaintLabels() && clip.intersects(labelRect)) {
            paintLabels(g);
        }

        for (int i = thumbNum - 1; 0 <= i; i--) {
            if (clip.intersects(thumbRects[i])) {
                thumbRect = thumbRects[i];
                thumbRenderer = ((MThumbSlider) slider).getThumbRendererAt(i);
                if (thumbRenderer == null) {
                    if (slider.getOrientation() == JSlider.HORIZONTAL) {
                        thumbRenderer = horizThumbIcon;
                    } else {
                        thumbRenderer = vertThumbIcon;
                    }
                }
                paintThumb(g);
            }
        }
    }

    /**
     * paints the Thumb
     * @param g Graphics
     * @see javax.swing.plaf.metal.MetalSliderUI#paintThumb(java.awt.Graphics)
     */
    public void paintThumb(Graphics g) {
        thumbRenderer.paintIcon(slider, g, thumbRect.x, thumbRect.y);
    }

    /**
     * Set graphical properties for track
     * @param g
     * @param t1
     * @param t2
     * @param fillColor
     */
    public void fillTrack(Graphics g, Point t1, Point t2, Color fillColor) {
        //                               t1-------------------
        //                               |                   |
        //                               --------------------t2    
        int middleOfThumb = 0;

        if (slider.getOrientation() == JSlider.HORIZONTAL) {
            middleOfThumb = thumbRect.x + (thumbRect.width / 2) - trackRect.x;
            if (slider.isEnabled()) {
                g.setColor(fillColor);
                g.fillRect(t1.x + 2, t1.y + 2, middleOfThumb - t1.x - 1, t2.y - t1.y - 3);
                g.setColor(fillColor.brighter());
                g.drawLine(t1.x + 1, t1.y + 1, middleOfThumb, t1.y + 1);
                g.drawLine(t1.x + 1, t1.y + 1, t1.x + 1, t2.y - 2);
            } else {
                g.setColor(fillColor);
                g.fillRect(t1.x, t1.y, middleOfThumb - t1.x + 2, t2.y - t1.y);
            }
        } else {
            middleOfThumb = thumbRect.y + (thumbRect.height / 2) - trackRect.y;
            if (slider.isEnabled()) {
                g.setColor(slider.getBackground());
                g.drawLine(t1.x + 1, middleOfThumb, t2.x - 2, middleOfThumb);
                g.drawLine(t1.x + 1, middleOfThumb, t1.x + 1, t2.y - 2);
                g.setColor(fillColor);
                g.fillRect(t1.x + 2, middleOfThumb + 1, t2.x - t1.x - 3, t2.y - 2 - middleOfThumb);
            } else {
                g.setColor(fillColor);
                g.fillRect(t1.x, middleOfThumb + 2, t2.x - 1 - t1.x, t2.y - t1.y);
            }
        }
    }

    /**
     * @param direction
     * @see javax.swing.plaf.basic.BasicSliderUI#scrollByBlock(int)
     */
    public void scrollByBlock(int direction) {
    }

    /**
     * @param direction
     * @see javax.swing.plaf.basic.BasicSliderUI#scrollByUnit(int)
     */
    public void scrollByUnit(int direction) {
    }

    /**
     * @return Rectangle
     * @see edu.wustl.cab2b.client.ui.controls.slider.MThumbSliderAdditional#getTrackRect()
     */
    public Rectangle getTrackRect() {
        return trackRect;
    }

    /**
     * @return Dimension
     * @see javax.swing.plaf.metal.MetalSliderUI#getThumbSize()
     */
    public Dimension getThumbSize() {
        return super.getThumbSize();
    }

    /**
     * Gets X Position
     * @param value
     * @return X position
     * @see javax.swing.plaf.basic.BasicSliderUI#xPositionForValue(int)
     */
    public int xPositionForValue(int value) {
        return super.xPositionForValue(value);
    }

    /**
     * Gets Y Position
     * @param value
     * @return Y position
     * @see javax.swing.plaf.basic.BasicSliderUI#xPositionForValue(int)
     */
    public int yPositionForValue(int value) {
        return super.yPositionForValue(value);
    }
}
