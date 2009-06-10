package edu.wustl.cab2b.client.ui.controls.slider;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

/**
 * 
 * @author Hrishikesh Rajpathak
 * @author Deepak Shingan
 * @author Atul Jawale
 * 
 *
 */
public class AssistantUIManager {

    /**
     * Creates UI with Look and Feel defined in UIManager
     * @param c
     * @return Component
     */
    public static ComponentUI createUI(JComponent c) {
        String componentName = c.getClass().getName();

        int index = componentName.lastIndexOf('.') + 1;
        StringBuffer sb = new StringBuffer();
        sb.append(componentName.substring(0, index));

        String lookAndFeelName = UIManager.getLookAndFeel().getName();
        if (lookAndFeelName.startsWith("CDE/")) {
            lookAndFeelName = lookAndFeelName.substring(4, lookAndFeelName.length());
        }
        sb.append(lookAndFeelName);
        sb.append(componentName.substring(index));
        sb.append("UI");

        ComponentUI componentUI = getInstance(sb.toString());

        if (componentUI == null) {
            sb.setLength(0);
            sb.append(componentName.substring(0, index));
            sb.append("Basic");
            sb.append(componentName.substring(index));
            sb.append("UI");
            componentUI = getInstance(sb.toString());
        }

        return componentUI;
    }

    private static ComponentUI getInstance(String name) {
        try {
            return (ComponentUI) Class.forName(name).newInstance();
        } catch (ClassNotFoundException ex) {
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Sets the name of UI for the Component passed 
     * @param c
     */
    public static void setUIName(JComponent c) {
        String key = c.getUIClassID();
        String uiClassName = (String) UIManager.get(key);

        if (uiClassName == null) {
            String componentName = c.getClass().getName();
            int index = componentName.lastIndexOf('.') + 1;
            StringBuffer sb = new StringBuffer();
            sb.append(componentName.substring(0, index));
            String lookAndFeelName = UIManager.getLookAndFeel().getName();
            if (lookAndFeelName.startsWith("CDE/")) {
                lookAndFeelName = lookAndFeelName.substring(4, lookAndFeelName.length());
            }
            sb.append(lookAndFeelName);
            sb.append(key);
            UIManager.put(key, sb.toString());
        }
    }

    /**
     * 
     */
    public AssistantUIManager() {
    }

}