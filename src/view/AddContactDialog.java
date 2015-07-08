package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import common.Contact;
import controller.Controller;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

public class AddContactDialog extends JDialog implements ActionListener {

    // Constants
    private static final int USER_NAME_MAX_LENGTH = 25;
    
    private static final Font FONT = new Font(Font.DIALOG, Font.BOLD, 15);

    private static final Color BACKGROUND = new Color(51, 153, 255);
    private static final Color ERRORS_COLOR = Color.RED;

    // Panels
    private JPanel panel_userName = new JPanel();
    private JPanel panel_buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));

    // Labels
    private JLabel label_userName = new JLabel("Enter the user name of the contact you want to add:");
    private JLabel label_errors = new JLabel();

    // Text Fields
    private JTextField textField_userName = new JTextField(5);

    // Buttons
    private JButton button_ok = new JButton("OK");
    private JButton button_cancel = new JButton("Cancel");

    private Controller controller;

    // Constructors
    public AddContactDialog(Controller controller) {
        this.controller = controller;
        setTitle("Add Contact");
        setModal(true);
        initComponents();
    }

    // Initialize Components
    private void initComponents() {
        // User Name Panels
        label_errors.setOpaque(true);
        label_errors.setBackground(BACKGROUND);
        label_errors.setForeground(ERRORS_COLOR);

        label_userName.setFont(FONT);
        textField_userName.setFont(FONT);
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(label_userName);
        panel.add(textField_userName);
        panel.add(label_errors);
        panel_userName.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel_userName.setBackground(BACKGROUND);
        panel_userName.add(panel);

        textField_userName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent event) {
                if (textField_userName.getText().length() >= USER_NAME_MAX_LENGTH) {
                    event.consume();
                }
            }
        });

        // Buttons Panel
        button_ok.addActionListener(this);
        button_cancel.addActionListener(this);
        panel_buttons.setBackground(BACKGROUND);
        panel_buttons.add(button_ok);
        panel_buttons.add(button_cancel);

        this.setBackground(BACKGROUND);
        this.setLayout(new BorderLayout());
        this.add(panel_userName);
        this.add(panel_buttons, BorderLayout.PAGE_END);

        this.setResizable(false);
        this.pack();
        this.setLocation((int) (this.getToolkit().getScreenSize().getWidth() / 2 - this.getWidth() / 2),
                (int) (this.getToolkit().getScreenSize().getHeight() / 2 - this.getHeight() / 2));
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == button_ok) {
            try {
                String userName = textField_userName.getText().toLowerCase();
                new Contact(userName);

                if (userName.equals(controller.getContact().getUserName())) {
                    label_errors.setText("You can't add your self as friend");
                    this.pack();
                } else {
                    controller.sendFriendRequest(userName);
                    this.dispose();
                }
            } catch (RuntimeException ex) {
                label_errors.setText(ex.getMessage());
                this.pack();
                return;
            }
        } else if (event.getSource() == button_cancel) {
            this.dispose();
        }
    }

}
