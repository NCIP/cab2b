import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 */

/**
 * @author deepak_shingan
 *
 */
class TestClass extends JFrame {
    TestClass() {
        JPanel panel = new JPanel();
        String html = "<html><body><font color=#0000ff> Deepak </font></body></html>";
        JLabel label = new JLabel(html);
        panel.add(label);
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        new TestClass();
    }
}
