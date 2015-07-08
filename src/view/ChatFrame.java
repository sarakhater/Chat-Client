package view;

import common.Contact;
import common.Group;
import controller.Controller;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;
import xml.XMLWriter;

public class ChatFrame extends javax.swing.JFrame {

    // Constants
    private static final int MAX_FILE_SIZE = 10 * 1024 * 1024;

    private Controller controller;

    private boolean isGroup;

    private Contact contact;
    private Group group;

    private Vector<Message> messages = new Vector<Message>();

    private String fontName = Font.DIALOG;
    private int fontStyle = Font.PLAIN;
    private int fontSize = 12;
    private Color fontColor = Color.BLACK;

    private boolean shiftPressed;

    private String html = "<html>";

    // Constructors
    public ChatFrame(Controller controller, Contact contact) {
        setTitle(contact.getFullName());
        this.contact = contact;
        init(controller);
    }

    public ChatFrame(Controller controller, Group group) {
        this.group = group;
        isGroup = true;
        init(controller);

        String title = "";
        for (int i = 0; i < group.getContacts().size(); i++) {
            title += group.getContacts().elementAt(i).getUserName();
            if (i != group.getContacts().size() - 1) {
                title += ", ";
            }
        }
        setTitle(title);
    }

    // Init
    private void init(Controller controller) {
        this.controller = controller;

        initComponents();

        comboBox_fontName.setModel(new DefaultComboBoxModel(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));

        int[] styles = {Font.PLAIN, Font.ITALIC, Font.BOLD};
        DefaultComboBoxModel<Integer> fontStyles = new DefaultComboBoxModel<Integer>();
        for (int style : styles) {
            fontStyles.addElement(style);
        }
        comboBox_fontStyle.setModel(fontStyles);
        comboBox_fontStyle.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                if ((Integer) value == Font.PLAIN) {
                    value = "Plain";
                } else if ((Integer) value == Font.ITALIC) {
                    value = "Italic";
                } else if ((Integer) value == Font.BOLD) {
                    value = "Bold";
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        DefaultComboBoxModel<Integer> fontSizes = new DefaultComboBoxModel<Integer>();
        for (int i = 12; i <= 72; i += 2) {
            fontSizes.addElement(i);
        }
        comboBox_fontSize.setModel(fontSizes);

        if (isGroup) {
            try {
                label_otherImage.setIcon(new ImageIcon(ImageIO.read(new File("Images\\group.png")).getScaledInstance(75, 75, Image.SCALE_SMOOTH)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            Image image = contact.getPhoto();
            if (image == null) {
                try {
                    image = ImageIO.read(new File("images\\default.png"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            image = image.getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            label_otherImage.setIcon(new ImageIcon(image));
        }

        try {
            button_saveChat.setIcon(new ImageIcon(ImageIO.read(new File("Images\\save.png")).getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
            button_sendFile.setIcon(new ImageIcon(ImageIO.read(new File("Images\\attach.png")).getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
            button_sendMessage.setIcon(new ImageIcon(ImageIO.read(new File("Images\\send.png")).getScaledInstance(75, 75, Image.SCALE_SMOOTH)));

            Image image = controller.getContact().getPhoto();
            if (image == null) {
                try {
                    image = ImageIO.read(new File("images\\default.png"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            image = image.getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            
            label_selfImage.setIcon(new ImageIcon(image));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        button_saveChat.setToolTipText("Save Chat");
        button_sendFile.setToolTipText("Send File");
        button_sendMessage.setToolTipText("Send");

        button_saveChat.setMargin(new Insets(3, 3, 3, 3));
        button_sendFile.setMargin(new Insets(3, 3, 3, 3));

        textPane_messages.setContentType("text/html");
        textPane_messages.setEditable(false);

        button_fontColor.setBackground(Color.BLACK);
        button_fontColor.setForeground(Color.WHITE);
        button_sendMessage.setEnabled(false);

        scrollPane_write.setPreferredSize(new Dimension(775, 75));

        textArea_write.setWrapStyleWord(true);
        textArea_write.setLineWrap(true);

        textArea_write.addKeyListener(new KeyHandler());
        textArea_write.getDocument().addDocumentListener(new DocumentHandler());
        this.addWindowListener(new WindowHandler());

        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setSize(new Dimension(800, 500));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int) (screenSize.getWidth() - getWidth()) / 2, (int) (screenSize.getHeight() - getHeight()) / 2);

        textArea_write.requestFocus();
    }

    // Get Group
    public Group getGroup() {
        return group;
    }

    // Receive Message
    public void receiveMessage(String sender, String message, Font font, Color color) {
        messages.add(new Message(sender, message));

        String htmlFace = "font-family:" + font.getName();
        String htmlSize = "font-size:" + font.getSize();
        String htmlColor = "color:#" + Integer.toHexString(color.getRGB() & 0xFFFFFF);

        String fontTag = "<span style=\"" + htmlFace + "; " + htmlSize + "; " + htmlColor + "\">"
                + message.replace("<", "&lt;").replace(">", "&gt;") + "</span>";
        if (font.getStyle() == Font.ITALIC) {
            fontTag = "<i>" + fontTag + "</i>";
        } else if (font.getStyle() == Font.BOLD) {
            fontTag = "<b>" + fontTag + "</b>";
        }

//        String time = LocalTime.now().toString();
        fontTag = "<pre>" + sender + ": " + fontTag + "</pre>";

        html += fontTag;

        textPane_messages.setText(html);
    }

    // Send Message
    private void sendMessage() {
        String message = textArea_write.getText();
        textArea_write.setText("");
        Font font = new Font(fontName, fontStyle, fontSize);
        receiveMessage(controller.getContact().getUserName(), message, font, fontColor);
        if (isGroup) {
            controller.sendMessage(group, message, font, fontColor);
        } else {
            controller.sendMessage(contact.getUserName(), message, font, fontColor);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_send = new javax.swing.JPanel();
        button_sendMessage = new javax.swing.JButton();
        scrollPane_write = new javax.swing.JScrollPane();
        textArea_write = new javax.swing.JTextArea();
        panel_format = new javax.swing.JPanel();
        comboBox_fontName = new javax.swing.JComboBox();
        comboBox_fontStyle = new javax.swing.JComboBox();
        comboBox_fontSize = new javax.swing.JComboBox();
        button_fontColor = new javax.swing.JButton();
        panel_actions = new javax.swing.JPanel();
        button_saveChat = new javax.swing.JButton();
        button_sendFile = new javax.swing.JButton();
        scrollPane_messages = new javax.swing.JScrollPane();
        textPane_messages = new javax.swing.JTextPane();
        panel_images = new javax.swing.JPanel();
        label_selfImage = new javax.swing.JLabel();
        label_otherImage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panel_send.setBackground(new java.awt.Color(51, 153, 255));
        panel_send.setLayout(new java.awt.BorderLayout());

        button_sendMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_sendMessageActionPerformed(evt);
            }
        });
        panel_send.add(button_sendMessage, java.awt.BorderLayout.EAST);

        scrollPane_write.setBackground(new java.awt.Color(51, 153, 255));

        textArea_write.setColumns(20);
        textArea_write.setTabSize(0);
        scrollPane_write.setViewportView(textArea_write);

        panel_send.add(scrollPane_write, java.awt.BorderLayout.CENTER);

        panel_format.setBackground(new java.awt.Color(51, 153, 255));
        panel_format.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        comboBox_fontName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBox_fontName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBox_fontNameActionPerformed(evt);
            }
        });
        panel_format.add(comboBox_fontName);

        comboBox_fontStyle.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBox_fontStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBox_fontStyleActionPerformed(evt);
            }
        });
        panel_format.add(comboBox_fontStyle);

        comboBox_fontSize.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBox_fontSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBox_fontSizeActionPerformed(evt);
            }
        });
        panel_format.add(comboBox_fontSize);

        button_fontColor.setText("Color");
        button_fontColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_fontColorActionPerformed(evt);
            }
        });
        panel_format.add(button_fontColor);

        panel_send.add(panel_format, java.awt.BorderLayout.NORTH);

        getContentPane().add(panel_send, java.awt.BorderLayout.SOUTH);

        panel_actions.setBackground(new java.awt.Color(51, 153, 255));
        panel_actions.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        button_saveChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_saveChatActionPerformed(evt);
            }
        });
        panel_actions.add(button_saveChat);

        button_sendFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_sendFileActionPerformed(evt);
            }
        });
        panel_actions.add(button_sendFile);

        getContentPane().add(panel_actions, java.awt.BorderLayout.NORTH);

        scrollPane_messages.setBackground(new java.awt.Color(51, 153, 255));
        scrollPane_messages.setViewportView(textPane_messages);

        getContentPane().add(scrollPane_messages, java.awt.BorderLayout.CENTER);

        panel_images.setBackground(new java.awt.Color(51, 153, 255));
        panel_images.setLayout(new java.awt.BorderLayout());

        label_selfImage.setBackground(new java.awt.Color(51, 204, 255));
        label_selfImage.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panel_images.add(label_selfImage, java.awt.BorderLayout.SOUTH);

        label_otherImage.setBackground(new java.awt.Color(51, 153, 255));
        label_otherImage.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panel_images.add(label_otherImage, java.awt.BorderLayout.NORTH);

        getContentPane().add(panel_images, java.awt.BorderLayout.LINE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Save Chat
    private void button_saveChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_saveChatActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getPath();
            XMLWriter.writeXML(path, messages);
            messages.removeAllElements();
        }
    }//GEN-LAST:event_button_saveChatActionPerformed

    // Send File
    private void button_sendFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_sendFileActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file.length() > MAX_FILE_SIZE) {
                String message = "The maximum size of file is " + MAX_FILE_SIZE / 1024 / 1024 + " MB";
                JOptionPane.showMessageDialog(this, message);
                return;
            }
            try {
                byte[] fileData = Files.readAllBytes(Paths.get(file.getPath()));
                if (isGroup) {
                    for (Contact contact : group.getContacts()) {
                        controller.sendFile(contact.getUserName(), file, fileData);
                    }
                } else {
                    controller.sendFile(contact.getUserName(), file, fileData);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_button_sendFileActionPerformed

    // Send Message
    private void button_sendMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_sendMessageActionPerformed
        sendMessage();
    }//GEN-LAST:event_button_sendMessageActionPerformed

    // Font Name
    private void comboBox_fontNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBox_fontNameActionPerformed
        fontName = (String) comboBox_fontName.getSelectedItem();
        textArea_write.setFont(new Font(fontName, fontStyle, fontSize));
    }//GEN-LAST:event_comboBox_fontNameActionPerformed

    // Font Style
    private void comboBox_fontStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBox_fontStyleActionPerformed
        fontStyle = (Integer) comboBox_fontStyle.getSelectedItem();
        textArea_write.setFont(new Font(fontName, fontStyle, fontSize));
    }//GEN-LAST:event_comboBox_fontStyleActionPerformed

    // Font Size
    private void comboBox_fontSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBox_fontSizeActionPerformed
        fontSize = (Integer) comboBox_fontSize.getSelectedItem();
        textArea_write.setFont(new Font(fontName, fontStyle, fontSize));
    }//GEN-LAST:event_comboBox_fontSizeActionPerformed

    // Font Color
    private void button_fontColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_fontColorActionPerformed
        Color color = JColorChooser.showDialog(this, "Font Color", fontColor);
        if (color != null) {
            fontColor = color;
            textArea_write.setForeground(fontColor);
            button_fontColor.setBackground(fontColor);
            if (fontColor.getRed() + fontColor.getGreen() + fontColor.getBlue() > 128 * 3) {
                button_fontColor.setForeground(Color.BLACK);
            } else {
                button_fontColor.setForeground(Color.WHITE);
            }
        }
    }//GEN-LAST:event_button_fontColorActionPerformed

    // Key Handler
    private class KeyHandler extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent event) {
            if (event.getSource() == textArea_write) {
                if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (shiftPressed) {
                        textArea_write.append("\n");
                    } else if (!textArea_write.getText().trim().equals("")) {
                        sendMessage();
                        event.consume();
                    }
                } else if (event.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shiftPressed = true;
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.VK_SHIFT) {
                shiftPressed = false;
            }
        }

    }

    // Document Handler
    private class DocumentHandler implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent event) {
            if (event.getDocument() == textArea_write.getDocument()) {
                button_sendMessage.setEnabled(!textArea_write.getText().trim().equals(""));
            }
        }

        @Override
        public void removeUpdate(DocumentEvent event) {
            if (event.getDocument() == textArea_write.getDocument()) {
                button_sendMessage.setEnabled(!textArea_write.getText().trim().equals(""));
            }
        }

        @Override
        public void changedUpdate(DocumentEvent event) {
        }

    }

    // Window Handler
    private class WindowHandler extends WindowAdapter {
//        @Override
//        public void windowClosing(WindowEvent e) {
//            if (isGroup) {
//                controller.removeChatFrame(groupID);
//            } else {
//                controller.removeChatFrame(contact.getUserName());
//            }
//        }
    }

    // Message
    public class Message {

        private String sender;
        private String body;

        public Message(String sender, String body) {
            this.sender = sender;
            this.body = body;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_fontColor;
    private javax.swing.JButton button_saveChat;
    private javax.swing.JButton button_sendFile;
    private javax.swing.JButton button_sendMessage;
    private javax.swing.JComboBox comboBox_fontName;
    private javax.swing.JComboBox comboBox_fontSize;
    private javax.swing.JComboBox comboBox_fontStyle;
    private javax.swing.JLabel label_otherImage;
    private javax.swing.JLabel label_selfImage;
    private javax.swing.JPanel panel_actions;
    private javax.swing.JPanel panel_format;
    private javax.swing.JPanel panel_images;
    private javax.swing.JPanel panel_send;
    private javax.swing.JScrollPane scrollPane_messages;
    private javax.swing.JScrollPane scrollPane_write;
    private javax.swing.JTextArea textArea_write;
    private javax.swing.JTextPane textPane_messages;
    // End of variables declaration//GEN-END:variables
}
