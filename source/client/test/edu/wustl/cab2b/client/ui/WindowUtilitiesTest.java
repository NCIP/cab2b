package edu.wustl.cab2b.client.ui;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.UIManager;

import junit.framework.TestCase;
import edu.wustl.cab2b.client.ui.WindowUtilities;

/**
 * @author chandrakant_talele
 */
public class WindowUtilitiesTest extends TestCase {
    public void testSetNativeLookAndFeel() {
        WindowUtilities.setNativeLookAndFeel();
        String name = UIManager.getSystemLookAndFeelClassName();
        assertEquals(name, UIManager.getLookAndFeel().getClass().getName());
    }

    public void testSetJavaLookAndFeel() {
        WindowUtilities.setJavaLookAndFeel();
        String name = UIManager.getCrossPlatformLookAndFeelClassName();
        assertEquals(name, UIManager.getLookAndFeel().getClass().getName());
    }

    public void testSetMotifLookAndFeel() {
        WindowUtilities.setMotifLookAndFeel();
        String name = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
        assertEquals(name, UIManager.getLookAndFeel().getClass().getName());
    }

    public void testOpenInJFrame() {
        Container content = new Container();
        JFrame frame = WindowUtilities.openInJFrame(content, 100, 100);
        assertEquals(Color.white, frame.getBackground());
        assertEquals(Color.white, content.getBackground());
    }

    public void testOpenInJFrameWithTitle() {
        String title = "title";
        Container content = new Container();
        int width = 100;
        int height = 100;
        JFrame frame = WindowUtilities.openInJFrame(content, width, height, title);
        assertEquals(Color.white, frame.getBackground());
        assertEquals(Color.white, content.getBackground());
        assertEquals(title, frame.getTitle());
    }
}
