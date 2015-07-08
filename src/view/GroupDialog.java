package view;

import common.Contact;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.BevelBorder;

public class GroupDialog extends javax.swing.JDialog {
    
    // Constants
    private static final Color BACKGROUND = new Color(51, 153, 255);

    private boolean confirmed;

    // List Models
    private DefaultListModel<Contact> listModel_all = new DefaultListModel<Contact>();
    private DefaultListModel<Contact> listModel_added = new DefaultListModel<Contact>();

    // Constructors
    public GroupDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setTitle("Group Chat");
        initComponents();

        list_all.setCellRenderer(new ListRenderer());
        list_added.setCellRenderer(new ListRenderer());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int) (screenSize.getWidth() - getWidth()) / 2, (int) (screenSize.getHeight() - getHeight()) / 2);
    }

    // Show Dialog
    public boolean showDialog(Vector<Contact> all) {
        listModel_all.removeAllElements();
        listModel_added.removeAllElements();
        for (Contact contact : all) {
            listModel_all.addElement(contact);
        }
        list_all.setModel(listModel_all);
        list_added.setModel(listModel_added);
        button_ok.setEnabled(false);
        confirmed = false;
        this.setVisible(true);
        return confirmed;
    }

    // Get Contacts
    public Vector<Contact> getContacts() {
        Vector<Contact> contacts = new Vector<Contact>();
        for (int i = 0; i < listModel_added.size(); i++) {
            contacts.add(listModel_added.elementAt(i));
        }
        return contacts;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_buttons = new javax.swing.JPanel();
        button_ok = new javax.swing.JButton();
        button_cancel = new javax.swing.JButton();
        panel_actions = new javax.swing.JPanel();
        panel_move = new javax.swing.JPanel();
        button_addOne = new javax.swing.JButton();
        button_addAll = new javax.swing.JButton();
        button_removeOne = new javax.swing.JButton();
        button_removeAll = new javax.swing.JButton();
        panel_all = new javax.swing.JPanel();
        scrollPane_all = new javax.swing.JScrollPane();
        list_all = new javax.swing.JList();
        panel_added = new javax.swing.JPanel();
        scrollPane_added = new javax.swing.JScrollPane();
        list_added = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(500, 300));
        getContentPane().setLayout(new java.awt.BorderLayout(0, 10));

        panel_buttons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));

        button_ok.setText("OK");
        button_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_okActionPerformed(evt);
            }
        });
        panel_buttons.add(button_ok);

        button_cancel.setText("Cancel");
        button_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_cancelActionPerformed(evt);
            }
        });
        panel_buttons.add(button_cancel);

        getContentPane().add(panel_buttons, java.awt.BorderLayout.SOUTH);

        panel_actions.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 5));

        panel_move.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 0, 10, 0));
        panel_move.setLayout(new java.awt.GridLayout(4, 1, 0, 10));

        button_addOne.setText(">");
        button_addOne.setMargin(new java.awt.Insets(2, 10, 2, 10));
        button_addOne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_addOneActionPerformed(evt);
            }
        });
        panel_move.add(button_addOne);

        button_addAll.setText(">>");
        button_addAll.setMargin(new java.awt.Insets(2, 10, 2, 10));
        button_addAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_addAllActionPerformed(evt);
            }
        });
        panel_move.add(button_addAll);

        button_removeOne.setText("<");
        button_removeOne.setMargin(new java.awt.Insets(2, 10, 2, 10));
        button_removeOne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_removeOneActionPerformed(evt);
            }
        });
        panel_move.add(button_removeOne);

        button_removeAll.setText("<<");
        button_removeAll.setMargin(new java.awt.Insets(2, 10, 2, 10));
        button_removeAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_removeAllActionPerformed(evt);
            }
        });
        panel_move.add(button_removeAll);

        panel_actions.add(panel_move);

        getContentPane().add(panel_actions, java.awt.BorderLayout.CENTER);

        panel_all.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel_all.setLayout(new java.awt.BorderLayout());

        scrollPane_all.setPreferredSize(new java.awt.Dimension(200, 300));

        list_all.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        scrollPane_all.setViewportView(list_all);

        panel_all.add(scrollPane_all, java.awt.BorderLayout.CENTER);

        getContentPane().add(panel_all, java.awt.BorderLayout.WEST);

        panel_added.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel_added.setLayout(new java.awt.BorderLayout());

        scrollPane_added.setPreferredSize(new java.awt.Dimension(200, 300));

        list_added.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        scrollPane_added.setViewportView(list_added);

        panel_added.add(scrollPane_added, java.awt.BorderLayout.CENTER);

        getContentPane().add(panel_added, java.awt.BorderLayout.EAST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_addOneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_addOneActionPerformed
        List<Contact> items = list_all.getSelectedValuesList();
        for (Contact item : items) {
            if (item != null) {
                listModel_added.addElement(item);
                listModel_all.removeElement(item);
            }
        }
        button_ok.setEnabled(!listModel_added.isEmpty());
    }//GEN-LAST:event_button_addOneActionPerformed

    private void button_addAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_addAllActionPerformed
        while (listModel_all.getSize() > 0) {
            Contact contact = listModel_all.getElementAt(0);
            listModel_added.addElement(contact);
            listModel_all.removeElement(contact);
        }
        button_ok.setEnabled(!listModel_added.isEmpty());
    }//GEN-LAST:event_button_addAllActionPerformed

    private void button_removeOneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_removeOneActionPerformed
        List<Contact> items = list_added.getSelectedValuesList();
        for (Contact item : items) {
            if (item != null) {
                listModel_all.addElement(item);
                listModel_added.removeElement(item);
            }
        }
        button_ok.setEnabled(!listModel_added.isEmpty());
    }//GEN-LAST:event_button_removeOneActionPerformed

    private void button_removeAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_removeAllActionPerformed
        while (listModel_added.getSize() > 0) {
            Contact contact = listModel_added.getElementAt(0);
            listModel_all.addElement(contact);
            listModel_added.removeElement(contact);
        }
        button_ok.setEnabled(!listModel_added.isEmpty());
    }//GEN-LAST:event_button_removeAllActionPerformed

    private void button_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_okActionPerformed
        confirmed = true;
        this.setVisible(false);
    }//GEN-LAST:event_button_okActionPerformed

    private void button_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_cancelActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_button_cancelActionPerformed

    private class ListRenderer implements ListCellRenderer<Object> {

        @Override
        public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            JPanel panel_cell = new JPanel(new BorderLayout(5, 5));
            JLabel label_image = new JLabel();
            JLabel label_name = new JLabel(((Contact) value).getFullName());

            Image image = ((Contact) value).getPhoto();
            if (image == null) {
                try {
                    image = ImageIO.read(new File("images\\default.png"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            image = image.getScaledInstance(30, 30, Image.SCALE_SMOOTH);

            label_image.setIcon(new ImageIcon(image));

            panel_cell.add(label_image, BorderLayout.LINE_START);
            panel_cell.add(label_name);
            
            panel_cell.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

            if (isSelected) {
                label_image.setOpaque(true);
                label_name.setOpaque(true);
                label_image.setBackground(BACKGROUND);
                label_name.setBackground(BACKGROUND);
                panel_cell.setBackground(BACKGROUND);
            }

            return panel_cell;
        }

    }

    // Main
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GroupDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GroupDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GroupDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GroupDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                GroupDialog dialog = new GroupDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_addAll;
    private javax.swing.JButton button_addOne;
    private javax.swing.JButton button_cancel;
    private javax.swing.JButton button_ok;
    private javax.swing.JButton button_removeAll;
    private javax.swing.JButton button_removeOne;
    private javax.swing.JList list_added;
    private javax.swing.JList list_all;
    private javax.swing.JPanel panel_actions;
    private javax.swing.JPanel panel_added;
    private javax.swing.JPanel panel_all;
    private javax.swing.JPanel panel_buttons;
    private javax.swing.JPanel panel_move;
    private javax.swing.JScrollPane scrollPane_added;
    private javax.swing.JScrollPane scrollPane_all;
    // End of variables declaration//GEN-END:variables
}
