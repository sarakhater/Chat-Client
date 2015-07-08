package view;

import common.Contact;
import common.Group;
import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

public class MessengerFrame extends javax.swing.JFrame {

    private Controller controller;

    private Image image_group = null;
    private Image image_user = null;

    private String[][] status = {{"Available", "Away", "Busy", "Invisible"},
    {"images\\available.png", "images\\away.png", "images\\busy.png", "images\\offline.png"}};

    // Popup Menus
    private JPopupMenu popupMenu_contact = new JPopupMenu();

    // Popup Menu Items
    private JMenuItem menuItem_viewDetails = new JMenuItem("View Details");
    private JMenuItem menuItem_delete = new JMenuItem("Delete");
    private JMenuItem menuItem_block = new JMenuItem("Block");

    // Constructors
    public MessengerFrame(Controller controller) {
        initComponents();
        this.controller = controller;
        setTitle(controller.getContact().getFullName());
        this.getContentPane().setBackground(this.getBackground());

        try {
            list_friends.setCellRenderer(new ListRenderer());
            // Set contact status
            DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel(status[0]);

            comboBox_status.setModel(defaultComboBoxModel1);
            comboBox_status.setRenderer(new ComboBoxRenderer());

            image_group = ImageIO.read(new File("images\\group.png")).getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            image_user = ImageIO.read(new File("images\\add_user.png")).getScaledInstance(20, 20, Image.SCALE_SMOOTH);

            // jButton1.setIcon(new ImageIcon(image_home));
            button_addFriend.setMargin(new Insets(2, 2, 2, 2));
            button_addGroup.setMargin(new Insets(2, 2, 2, 2));
            button_addFriend.setIcon(new ImageIcon(image_user));
            button_addGroup.setIcon(new ImageIcon(image_group));
            button_addFriend.setToolTipText("Add Friend");
            button_addGroup.setToolTipText("Create Group");

            label_name.setText(controller.getContact().getFullName()); // user name label
            comboBox_status.setSelectedIndex(controller.getContact().getStatus().ordinal());

            if (controller.getContact().getPhoto() != null) {
                label_image.setIcon(new ImageIcon(controller.getContact().getPhoto().getScaledInstance(60, 60, Image.SCALE_SMOOTH))); //profile picture
            } else {
                label_image.setIcon(new ImageIcon(ImageIO.read(new File("images\\default.png")).getScaledInstance(60, 60, Image.SCALE_SMOOTH))); //profile picture
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        list_friends.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (SwingUtilities.isRightMouseButton(event)) {
                    list_friends.setSelectedIndex(list_friends.locationToIndex(event.getPoint()));
                    popupMenu_contact.show(list_friends, event.getX(), event.getY());
                } else if (event.getClickCount() == 2) {
                    controller.createChatFrame(((Contact) list_friends.getSelectedValue()).getUserName());
                }
            }
        });

        menuItem_viewDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // 3ra2y
                System.out.println(list_friends.getSelectedValue());
            }
        });

        menuItem_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // Shima2
                System.out.println(list_friends.getSelectedValue());
            }
        });

        menuItem_block.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // Sara
                System.out.println(list_friends.getSelectedValue());
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String title = "Exit Confirmation";
                String message = "Are you sure you want to exit ?";
                int response = JOptionPane.showConfirmDialog(MessengerFrame.this, message, title, JOptionPane.YES_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    controller.signout();
                    dispose();
                    new Controller();
                }
            }
        });

        popupMenu_contact.add(menuItem_viewDetails);
        popupMenu_contact.add(menuItem_delete);
        popupMenu_contact.add(menuItem_block);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int) (screenSize.getWidth() - getWidth()), 0);
    }

    // Update List
    public void updatelist(Vector<common.Contact> friendlist) {
        Collections.sort(friendlist);
        DefaultListModel<Contact> friendsModel = new DefaultListModel<>();
        for (Contact contact : friendlist) {
            friendsModel.addElement(contact);
        }
        list_friends.setModel(friendsModel);
    }

    // Update Request Panel
    public void updateRequestPanel(Vector<common.Contact> requests) {
        panel_requests.removeAll();
        for (Contact contact : requests) {
            panel_requests.add(new RequestPanel(contact.getFullName(), contact.getUserName(), controller));
        }
        panel_requests.validate();
        panel_requests.repaint();
    }

    // Set Advertisement
    public void receiveAdvertisement(int[] image, int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.setRGB(0, 0, width, height, image, 0, width);
        label_advertisements.setIcon(new ImageIcon(bufferedImage.getScaledInstance(275, 150, Image.SCALE_SMOOTH)));
    }

    // Combo Box Renderer
    private class ComboBoxRenderer implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            String path = null;
            for (int i = 0; i < status[0].length; i++) {
                if (status[0][i].equals(value)) {
                    path = status[1][i];
                    break;
                }
            }

            Image image = null;
            try {
                image = ImageIO.read(new File(path)).getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            JPanel statuspanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 1, 1));
            statuspanel.add(new JLabel(new ImageIcon(image)));
            statuspanel.add(new JLabel((String) value));

            if (isSelected) {
                statuspanel.setBackground(MessengerFrame.this.getBackground());
            }

            return statuspanel;
        }
    }

    // Friends List Renderer
    private class ListRenderer implements ListCellRenderer<Object> {

        @Override
        public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JPanel panel_data = new JPanel(new GridLayout(2, 1));
            JPanel panel_left = new JPanel(new FlowLayout());
            JPanel panel_cell = new JPanel();

            Image image_status = null;
            Image image_personal = null;

            try {
                image_status = ImageIO.read(new File("images\\" + ((Contact) value).getStatus().toString() + ".png")).getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            image_personal = ((Contact) value).getPhoto();
            if (image_personal == null) {
                try {
                    image_personal = ImageIO.read(new File("images\\default.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            } else {
                image_personal = image_personal.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            }

            JLabel label_name = new JLabel(((Contact) value).getFullName());
            JLabel label_status = new JLabel(((Contact) value).getStatus().toString());

            label_name.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
            label_status.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));

            panel_data.setAlignmentY(JPanel.BOTTOM_ALIGNMENT);
            panel_data.add(label_name);
            panel_data.add(label_status);

            panel_left.add(new JLabel(new ImageIcon(image_status)));
            panel_left.add(panel_data);

            panel_cell.setLayout(new BorderLayout());
            panel_cell.add(panel_left, BorderLayout.LINE_START);
            panel_cell.add(new JLabel(new ImageIcon(image_personal)), BorderLayout.LINE_END);
            panel_cell.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

            if (isSelected) {
                label_name.setOpaque(true);
                label_status.setOpaque(true);
                label_name.setBackground(MessengerFrame.this.getBackground());
                label_status.setBackground(MessengerFrame.this.getBackground());
                panel_left.setBackground(MessengerFrame.this.getBackground());
                panel_cell.setBackground(MessengerFrame.this.getBackground());
            }

            return panel_cell;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane_contacts = new javax.swing.JTabbedPane();
        scrollPane_friends = new javax.swing.JScrollPane();
        list_friends = new javax.swing.JList();
        scrollPane_requests = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        panel_requests = new javax.swing.JPanel();
        panel_header = new javax.swing.JPanel();
        label_image = new javax.swing.JLabel();
        panel_info = new javax.swing.JPanel();
        panel_name = new javax.swing.JPanel();
        label_name = new javax.swing.JLabel();
        panel_controls = new javax.swing.JPanel();
        comboBox_status = new javax.swing.JComboBox();
        button_addFriend = new javax.swing.JButton();
        button_addGroup = new javax.swing.JButton();
        panel_advertisements = new javax.swing.JPanel();
        label_advertisements = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        menu_file = new javax.swing.JMenu();
        menuItem_signOut = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBackground(new java.awt.Color(51, 153, 255));
        setPreferredSize(new java.awt.Dimension(320, 700));
        setResizable(false);

        list_friends.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        scrollPane_friends.setViewportView(list_friends);

        tabbedPane_contacts.addTab("Friends", scrollPane_friends);

        jPanel2.setLayout(new java.awt.BorderLayout());

        panel_requests.setLayout(new javax.swing.BoxLayout(panel_requests, javax.swing.BoxLayout.PAGE_AXIS));
        jPanel2.add(panel_requests, java.awt.BorderLayout.NORTH);

        scrollPane_requests.setViewportView(jPanel2);

        tabbedPane_contacts.addTab("Requests", scrollPane_requests);

        getContentPane().add(tabbedPane_contacts, java.awt.BorderLayout.CENTER);

        panel_header.setBackground(new java.awt.Color(51, 153, 255));
        panel_header.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel_header.setLayout(new java.awt.BorderLayout());

        label_image.setBackground(new java.awt.Color(51, 153, 255));
        label_image.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panel_header.add(label_image, java.awt.BorderLayout.WEST);

        panel_info.setBackground(new java.awt.Color(51, 153, 255));
        panel_info.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panel_info.setLayout(new javax.swing.BoxLayout(panel_info, javax.swing.BoxLayout.PAGE_AXIS));

        panel_name.setBackground(new java.awt.Color(51, 153, 255));
        panel_name.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        label_name.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        panel_name.add(label_name);

        panel_info.add(panel_name);

        panel_controls.setBackground(new java.awt.Color(51, 153, 255));
        panel_controls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        comboBox_status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBox_statusActionPerformed(evt);
            }
        });
        panel_controls.add(comboBox_status);

        button_addFriend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_addFriendActionPerformed(evt);
            }
        });
        panel_controls.add(button_addFriend);

        button_addGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_addGroupActionPerformed(evt);
            }
        });
        panel_controls.add(button_addGroup);

        panel_info.add(panel_controls);

        panel_header.add(panel_info, java.awt.BorderLayout.CENTER);

        getContentPane().add(panel_header, java.awt.BorderLayout.NORTH);

        panel_advertisements.setBackground(new java.awt.Color(51, 153, 255));

        label_advertisements.setBackground(new java.awt.Color(51, 102, 255));
        label_advertisements.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panel_advertisements.add(label_advertisements);

        getContentPane().add(panel_advertisements, java.awt.BorderLayout.SOUTH);

        menu_file.setText("File");

        menuItem_signOut.setText("Sign Out");
        menuItem_signOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_signOutActionPerformed(evt);
            }
        });
        menu_file.add(menuItem_signOut);

        menuBar.add(menu_file);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboBox_statusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBox_statusActionPerformed
        controller.setStatus(comboBox_status.getSelectedIndex());
    }//GEN-LAST:event_comboBox_statusActionPerformed

    private void button_addFriendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_addFriendActionPerformed
        controller.displayAddContact();
    }//GEN-LAST:event_button_addFriendActionPerformed

    private void button_addGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_addGroupActionPerformed
        GroupDialog groupDialog = new GroupDialog(this, true);
        Vector<Contact> contacts = new Vector<Contact>();
        for (Contact contact : controller.getContact().getFriends()) {
            if (contact.getStatus() != Contact.Status.OFFLINE) {
                contacts.add(contact);
            }
        }
        if (groupDialog.showDialog(contacts)) {
            Group group = new Group(0);
            group.setContacts(groupDialog.getContacts());
            group.setCreator(controller.getContact());
            controller.createChatFrame(group);
        }
    }//GEN-LAST:event_button_addGroupActionPerformed

    private void menuItem_signOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_signOutActionPerformed
        controller.signout();
        dispose();
        new Controller();
    }//GEN-LAST:event_menuItem_signOutActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_addFriend;
    private javax.swing.JButton button_addGroup;
    private javax.swing.JComboBox comboBox_status;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel label_advertisements;
    private javax.swing.JLabel label_image;
    private javax.swing.JLabel label_name;
    private javax.swing.JList list_friends;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem menuItem_signOut;
    private javax.swing.JMenu menu_file;
    private javax.swing.JPanel panel_advertisements;
    private javax.swing.JPanel panel_controls;
    private javax.swing.JPanel panel_header;
    private javax.swing.JPanel panel_info;
    private javax.swing.JPanel panel_name;
    private javax.swing.JPanel panel_requests;
    private javax.swing.JScrollPane scrollPane_friends;
    private javax.swing.JScrollPane scrollPane_requests;
    private javax.swing.JTabbedPane tabbedPane_contacts;
    // End of variables declaration//GEN-END:variables
}
