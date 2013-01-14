/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls;

import javax.swing.DefaultCellEditor;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * The default editor for table and tree cells.
 * @author Chetan_BH
 *
 */
public class TextAreaEditor extends DefaultCellEditor {
    /**
     * Creates TextAreaEditor with default settings
     */
    public TextAreaEditor() {
        super(new JTextField());
        final JTextArea textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        editorComponent = scrollPane;

        delegate = new DefaultCellEditor.EditorDelegate() {
            /* (non-Javadoc)
             * @see javax.swing.DefaultCellEditor$EditorDelegate#setValue(java.lang.Object)
             */
            public void setValue(Object value) {
                textArea.setText((value != null) ? value.toString() : "");
            }

            /* (non-Javadoc)
             * @see javax.swing.DefaultCellEditor$EditorDelegate#getCellEditorValue()
             */
            public Object getCellEditorValue() {
                return textArea.getText();
            }
        };
    }
}
