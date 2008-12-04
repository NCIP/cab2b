package edu.wustl.cab2b.client.ui.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXStatusBar;

import edu.wustl.cab2b.client.ui.controls.IDialogInterface;

/**
 * It is a utility class which provide methods to customize and show different window components.
 * @author Chandrakant Talele
 */
public class WindowUtilities {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(WindowUtilities.class);
    /** Tell system to use native look and feel.
     * Metal (Java) LAF is the default otherwise.
     */
    public static void setNativeLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.error("Error setting native LAF: " + e);
        }
    }

    public static void setJavaLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            logger.error("Error setting Java LAF: " + e);
        }
    }

    public static void setMotifLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        } catch (Exception e) {
            logger.error("Error setting Motif LAF: " + e);
        }
    }

    /** A simplified way to see a JPanel or other Container.
     *  Pops up a JFrame with specified Container as the content pane.
     */

    public static JFrame openInJFrame(Container content, int width, int height, String title, Color bgColor) {
        JFrame frame = new JFrame(title);
        frame.setBackground(bgColor);
        content.setBackground(bgColor);
        frame.setSize(width, height);
        frame.setContentPane(content);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        return (frame);
    }

    /** Uses Color.white as the background color. */
    public static JFrame openInJFrame(Container content, int width, int height, String title) {
        return (openInJFrame(content, width, height, title, Color.white));
    }

    /** Uses Color.white as the background color, and the
     *  name of the Container's class as the JFrame title.
     */

    public static JFrame openInJFrame(Container content, int width, int height) {
        return (openInJFrame(content, width, height, content.getClass().getName(), Color.white));
    }

    /**
     * Creates and returns a JXFrame with the specified title, containing
     * the component wrapped into a JScrollPane.
     * 
     * @param component the JComponent to wrap
     * @param title the title to show in the frame
     * @return a configured, packed and located JXFrame.
     */
    public static JXFrame wrapWithScrollingInFrame(JComponent component, String title) {
        JScrollPane scroller = new JScrollPane(component);
        return wrapInFrame(scroller, title);
    }

    /**
     * Creates and returns a JXFrame with the specified title, containing
     * two components individually wrapped into a JScrollPane.
     * 
     * @param leftComp the left JComponent to wrap
     * @param rightComp the right JComponent to wrap
     * @param title the title to show in the frame
     * @return a configured, packed and located JXFrame
     */
    public static JXFrame wrapWithScrollingInFrame(JComponent leftComp, JComponent rightComp, String title) {
        JComponent comp = Box.createHorizontalBox();
        comp.add(new JScrollPane(leftComp));
        comp.add(new JScrollPane(rightComp));
        JXFrame frame = wrapInFrame(comp, title);
        return frame;
    }

    /**
     * Creates and returns a JXFrame with the specified title, containing
     * the component.
     * 
     * @param component the JComponent to wrap
     * @param title the title to show in the frame
     * @return a configured, packed and located JXFrame.
     */
    public static JXFrame wrapInFrame(JComponent component, String title) {
        JXFrame frame = new JXFrame(title, false);
        JToolBar toolbar = new JToolBar();
        frame.getRootPaneExt().setToolBar(toolbar);
        frame.getContentPane().add(BorderLayout.CENTER, component);

        Point frameLocation = new Point(0, 0);

        frame.pack();
        frame.setLocation(frameLocation);
        if (frameLocation.x == 0) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle(title);
        }
        frameLocation.x += 30;
        frameLocation.y += 30;
        return frame;
    }

    /**
     * Creates, shows and returns a JXFrame with the specified title, containing
     * the component wrapped into a JScrollPane.
     * 
     * @param component the JComponent to wrap
     * @param title the title to show in the frame
     * @return a configured, packed and located JXFrame.
     * @see #wrapWithScrollingInFrame(JComponent, String)
     */
    public static JXFrame showWithScrollingInFrame(JComponent component, String title) {
        JXFrame frame = wrapWithScrollingInFrame(component, title);
        frame.setVisible(true);
        return frame;
    }

    /**
     * Creates and returns a JXFrame with the specified title, containing
     * two components individually wrapped into a JScrollPane.
     * 
     * @param leftComp the left JComponent to wrap
     * @param rightComp the right JComponent to wrap
     * @param title the title to show in the frame
     * @return a configured, packed and located JXFrame
     */
    public static JXFrame showWithScrollingInFrame(JComponent leftComp, JComponent rightComp, String title) {
        JXFrame frame = wrapWithScrollingInFrame(leftComp, rightComp, title);
        frame.setVisible(true);
        return frame;
    }

    /**
     * Creates, shows and returns a JXFrame with the specified title, containing
     * the component.
     * 
     * @param component the JComponent to wrap
     * @param title the title to show in the frame
     * @return a configured, packed and located JXFrame.
     */
    public static JXFrame showInFrame(JComponent component, String title) {
        JXFrame frame = wrapInFrame(component, title);
        frame.setVisible(true);
        return frame;
    }

    /**
     * Adds action for frame
     * @param frame
     * @param action
     */
    public static void addAction(JXFrame frame, Action action) {
        JToolBar toolbar = frame.getRootPaneExt().getToolBar();
        if (toolbar != null) {
            AbstractButton button = toolbar.add(action);
            button.setFocusable(false);
        }
    }

    /**
     * Adds message in Frame status bar
     * @param frame
     * @param message
     */
    public static void addMessage(JXFrame frame, String message) {
        JXStatusBar statusBar = getStatusBar(frame);
        statusBar.add(new JLabel(message), JXStatusBar.Constraint.ResizeBehavior.FILL);
    }

    /**
     * Returns the <code>JXFrame</code>'s status bar. Lazily creates and 
     * sets an instance if necessary.
     * @param frame the target frame
     * @return the frame's statusbar
     */
    public static JXStatusBar getStatusBar(JXFrame frame) {
        JXStatusBar statusBar = frame.getRootPaneExt().getStatusBar();
        if (statusBar == null) {
            statusBar = new JXStatusBar();
            frame.getRootPaneExt().setStatusBar(statusBar);
        }
        return statusBar;
    }

    /** Center a window on the screen.
     * @param w The window to center.
     */
    public static final void centerWindow(Window w) {
        Dimension screenSize = w.getToolkit().getScreenSize();
        Dimension windowSize = w.getSize();

        int x = (screenSize.width - windowSize.width) / 2;
        int y = (screenSize.height - windowSize.height) / 2;

        w.setLocation(x, y);
    }

    /** Center a window within the bounds of another window.
     *
     * @param w The window to center.
     * @param parent The window to center within.
     */

    public static final void centerWindow(Window parent, Window w) {
        Dimension windowSize = w.getSize(); 
        Dimension parentSize = parent.getSize();
        Point parentLocation = parent.getLocationOnScreen();

        int x = ((parentSize.width - windowSize.width) / 2) + parentLocation.x;
        int y = ((parentSize.height - windowSize.height) / 2) + parentLocation.y;

        // If placing the window at (x,y) would make part if it off-screen,
        // then center it in the middle of the screen instead.
        Dimension screenSize = w.getToolkit().getScreenSize();
        if (((x + windowSize.width) > screenSize.width) || (x < 0) || ((y + windowSize.height) > screenSize.height) || (y < 0))
            centerWindow(w);
        else
            w.setLocation(x, y);
    }

    /** Center a window within the bounds of a component's parent window.
     *
     * @param w The window to center.
     * @param c The component within whose parent window this window should be
     * centered. If a window cannot be found in the component hierarchy above
     * <code>c</code>, the window is centered on the screen.
     */

    public static final void centerWindow(Component c, Window w) {
        Window pw = SwingUtilities.windowForComponent(c);

        if (pw == null)
            centerWindow(w);
        else
            centerWindow(pw, w);
    }

    /** Get the Frame parent of a component. This method searches upward in the
     * component hierarchy, searching for an ancestor that is a Frame.
     */

    public static final Frame getFrameForComponent(Component c) {
        while ((c = c.getParent()) != null)
            if (c instanceof Frame)
                return ((Frame) c);

        return (null);
    }

    /** Print a hardcopy of the contents of a window.
     *
     * @param window The window to print.
     * @param title A title for the print job.
     *
     * @return <b>true</b> if the print job was started, or <b>false</b> if
     * the user cancelled the print dialog.
     */

    public static boolean printWindow(Window window, String title) {
        final Frame phantomFrame = new Frame();
        PrintJob pj = Toolkit.getDefaultToolkit().getPrintJob(phantomFrame, title, null);

        if (pj == null)
            return (false);

        int res = Toolkit.getDefaultToolkit().getScreenResolution();
        // Dimension d = pj.getPageDimension(); // buggy in JDK 1.1.x
        Dimension d = new Dimension((int) (2 * res * 8.5), (int) (res * 11 * 2));

        d.width -= (int) (res * .75 /*in*/);
        d.height -= (int) (res * .75 /*in*/);

        Graphics gc = pj.getGraphics();
        window.paint(gc);

        gc.dispose();
        pj.end();
        return (true);
    }

    /**
     * Shows the component in the Dialog with specified settings.
     * @param componentToShow
     * @param title
     * @param size
     * @param isReSizable
     * @return
     */
    public static JDialog showInDialog(JComponent componentToShow, String title, Dimension size,
                                       boolean isReSizable) {
        return showInDialog(null, componentToShow, title, size, false, isReSizable);
    }

    /**
     * Shows the component in the Dialog with specified settings.
     * @param parentFrame
     * @param componentToShow
     * @param title
     * @param size
     * @param isModal
     * @param isReSizable
     * @return
     */
    public static JDialog showInDialog(Frame parentFrame, JComponent componentToShow, String title,
                                       Dimension size, boolean isModal, boolean isReSizable) {
        JDialog dialog = setInDialog(parentFrame, componentToShow, title, size, isModal, isReSizable);
        dialog.setVisible(true);
        return dialog;
    }

    /**
     * Sets the component in the Dialog with specified settings.
     * but won't make the dialog visible.
     * @param parentFrame
     * @param componentToShow
     * @param title
     * @param size
     * @param isModal
     * @param isReSizable
     * @return
     */
    public static JDialog setInDialog(Frame parentFrame, JComponent componentToShow, String title, Dimension size,
                                      boolean isModal, boolean isReSizable) {
        JDialog dialog = new JDialog(parentFrame, title, isModal);
        dialog.getContentPane().add(componentToShow);
        if (componentToShow instanceof IDialogInterface) {
            ((IDialogInterface) componentToShow).setParentWindow(dialog);
        }
        if (size == null)
            dialog.setSize(new Dimension(300, 150));
        else
            dialog.setSize(size);
        if (parentFrame != null && componentToShow != null)
            centerWindow(parentFrame, dialog);
        dialog.setResizable(isReSizable);
        return dialog;
    }
}