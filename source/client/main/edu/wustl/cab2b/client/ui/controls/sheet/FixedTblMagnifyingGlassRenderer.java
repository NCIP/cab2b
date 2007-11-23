/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wustl.cab2b.client.ui.controls.sheet;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author jasbir_sachdeva
 */
class FixedTblMagnifyingGlassRenderer extends DefaultTableCellRenderer {

    JComponent mg = new MagnifyingButton();

    FixedTblMagnifyingGlassRenderer() {
//        mg.setSize(15, 15);
//        mg.setToolTipText("Click Me to View This Row in More Details");
//        add(mg);
    }

//    static Graphics safelyGetGraphics(Component c, Component root) {
//        System.out.println("FixedTblMagnifyingGlassRenderer: safelyGetGraphics()");
//        Graphics g = c.getGraphics();
//        Graphics2D g2 = (Graphics2D) g;
//        g2.scale(2, 2);
//        return g2;
//    }

    @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {

        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }

    
    
//    @Override
//        public void paint(Graphics g) {
//
//        Graphics2D g2 = (Graphics2D) g;
//        Rectangle r = g2.getClipBounds();
//        g2.scale(2, 2);
//        super.paint(g2);
//
//
//        if (false) {
//            System.out.println("**********************************");
//            Thread.currentThread().dumpStack();
//            System.out.println("=====================================");
//            System.exit(0);
//        }
//    }

//    @Override
//    public void paintComponent(Graphics g) {
//
//        Graphics2D g2 = (Graphics2D) g;
//        Rectangle r = g2.getClipBounds();
//        g2.scale(2, 2);
//        super.paintComponent(g2);
//    }
//    
//        @Override
//    protected Graphics getComponentGraphics(Graphics g) {
//        Graphics2D g2 = (Graphics2D)g;
////        Rectangle r = g2.getClipBounds();
////        g2.setClip( r.x, r.y, r.width/2, r.height/2);
//        g2.scale( 2, 2);
//        return g2;
//    }
    
}
