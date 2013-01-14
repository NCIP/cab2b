/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.client.ui.searchDataWizard.dag;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.netbeans.graph.api.control.editor.IGraphEditor;
import org.netbeans.graph.api.control.editor.TextFieldEditor;
import org.netbeans.graph.api.model.ability.INameEditable;
import org.openide.util.NbBundle;

/**
 * @author deepak_shingan
 *
 */
public class DagTextFieldEditor extends TextFieldEditor {
    /**
     * Rectangle
     */
    Rectangle rectangle;

    /**
     * INameEditable interface
     */
    INameEditable nameEditable;

    /**
     * Constructor
     * @param rect
     * @param nameEditable
     */
    DagTextFieldEditor(Rectangle rect, INameEditable nameEditable) {
        rectangle = rect;
        this.nameEditable = nameEditable;
    }

    /* (non-Javadoc)
     * @see org.netbeans.graph.api.control.editor.TextFieldEditor#getValue()
     */
    public String getValue() {
        final String name = nameEditable.getName();
        return name != null ? name : NbBundle.getMessage(IconNodeRenderer.class, "TXT_IconNodeDriver_EnterName"); // NOI18N
    }

    /* (non-Javadoc)
     * @see org.netbeans.graph.api.control.editor.TextFieldEditor#setValue(java.lang.String)
     */
    public void setValue(String value) {
        nameEditable.setName(value);
    }

    /* (non-Javadoc)
     * @see org.netbeans.graph.api.control.editor.IGraphEditor#getActiveArea()
     */
    public Rectangle getActiveArea() {
        return rectangle;
    }

    /* (non-Javadoc)
     * @see org.netbeans.graph.api.control.editor.TextFieldEditor#notifyAttached(org.netbeans.graph.api.control.editor.IGraphEditor.EditorPresenter)
     */
    public void notifyAttached(IGraphEditor.EditorPresenter presenter) {
        super.notifyAttached(presenter);
        final int height = getComponent().getPreferredSize().height;
        getComponent().setMinimumSize(new Dimension(64, height));
        getComponent().setMaximumSize(new Dimension(256, height));
        getComponent().setPreferredSize(new Dimension(128, height));
    }
}
