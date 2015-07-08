package signup;

import javax.imageio.ImageIO;
import javax.swing.*;

import common.Contact;
import common.Contact.Gender;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class ContactDialog extends JDialog implements ActionListener {

    // Constants
    private static final int FIELDS_COLUMNS = 23;

    private static final Font FONT = new Font(Font.DIALOG, Font.BOLD, 15);
    private static final Color COLOR = Color.BLACK;
    private static final Color ERRORS_COLOR = Color.RED;

    private static final String TITLE = "Create Account";

    // Fields
    private Contact contact;
    private boolean confirmed;

    private BufferedImage photo;

    // Panels
    private DatePanel panel_date = new DatePanel("Birthdate", FONT, COLOR);
    private ImagePanel panel_image;

    // Text Fields
    private HintTextField textField_name = new HintTextField("User Name", FIELDS_COLUMNS, 25, FONT, COLOR);
    private HintPasswordField textField_password = new HintPasswordField("Password", FIELDS_COLUMNS, 25, FONT, COLOR);
    private HintPasswordField textField_repeatPassword = new HintPasswordField("Repeat Password", FIELDS_COLUMNS, 25, FONT, COLOR);
    private HintTextField textField_fullName = new HintTextField("Full Name", FIELDS_COLUMNS, 25, FONT, COLOR);

    // Labels
    private JLabel label_name_errors = new JLabel();
    private JLabel label_password_errors = new JLabel();
    private JLabel label_repeatedPassword_errors = new JLabel();
    private JLabel label_fullName_errors = new JLabel();
    private JLabel label_birthDate_errors = new JLabel();
    private JLabel label_gender_errors = new JLabel();

    // Buttons
    private JButton button_uploadPhoto = new JButton("Upload Photo");
    private JButton button_ok = new JButton("OK");
    private JButton button_cancel = new JButton("Cancel");

    // Radio Buttons
    private JRadioButton radioButton_male = new JRadioButton("Male");
    private JRadioButton radioButton_female = new JRadioButton("Female");

    // File Chooser
    private JFileChooser fileChooser_photo = new JFileChooser();

    // Constructors
    public ContactDialog() {
        this(TITLE);
    }

    public ContactDialog(String title) {
        setTitle(title);
        setModal(true);
        initComponents();
    }

    // Initialize Components
    private void initComponents() {
        label_name_errors.setForeground(ERRORS_COLOR);
        label_password_errors.setForeground(ERRORS_COLOR);
        label_repeatedPassword_errors.setForeground(ERRORS_COLOR);
        label_fullName_errors.setForeground(ERRORS_COLOR);
        label_gender_errors.setForeground(ERRORS_COLOR);
        label_birthDate_errors.setForeground(ERRORS_COLOR);

        // Title Panel
        JPanel panel_title = new JPanel();
        JLabel label_title = new JLabel(TITLE);
        label_title.setFont(new Font(Font.DIALOG, Font.BOLD, 35));
        panel_title.add(label_title);

        // Name Panel
        JPanel panel_name = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JPanel panel_name_components = new JPanel(new BorderLayout());
        panel_name_components.add(textField_name);
        panel_name_components.add(label_name_errors, BorderLayout.PAGE_END);
        panel_name.add(panel_name_components);

        // Password Panel
        JPanel panel_password = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JPanel panel_password_components = new JPanel(new BorderLayout());
        panel_password_components.add(textField_password);
        panel_password_components.add(label_password_errors, BorderLayout.PAGE_END);
        panel_password.add(panel_password_components);

        // Repeated Password Panel
        JPanel panel_repeatedPassword = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JPanel panel_repeatedPasswordComponents = new JPanel(new BorderLayout());
        panel_repeatedPasswordComponents.add(textField_repeatPassword);
        panel_repeatedPasswordComponents.add(label_repeatedPassword_errors, BorderLayout.PAGE_END);
        panel_repeatedPassword.add(panel_repeatedPasswordComponents);

        // Full Name Panel
        JPanel panel_fullName = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JPanel panel_fullName_components = new JPanel(new BorderLayout());
        panel_fullName_components.add(textField_fullName);
        panel_fullName_components.add(label_fullName_errors, BorderLayout.PAGE_END);
        panel_fullName.add(panel_fullName_components);

        // Gender Panel
        JPanel panel_gender = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel_gender.add(radioButton_male);
        panel_gender.add(radioButton_female);
        panel_gender.add(label_gender_errors);
        radioButton_male.addActionListener(this);
        radioButton_female.addActionListener(this);
        radioButton_male.setFont(FONT);
        radioButton_female.setFont(FONT);

        // BirthDate Panel
        JPanel panel_birthDate = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel panel_birthDate_components = new JPanel(new BorderLayout());
        panel_birthDate_components.add(panel_date);
        panel_birthDate_components.add(label_birthDate_errors, BorderLayout.PAGE_END);
        panel_birthDate.add(panel_birthDate_components);

        // Photo Panel
        JPanel panel_photo = new JPanel();
        panel_photo.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        JPanel panel_photo_components = new JPanel(new BorderLayout());
        panel_image = new ImagePanel(panel_photo, 130, 130);
        try {
            panel_image.displayImage(ImageIO.read(new File("images\\default.png")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        button_uploadPhoto.setFont(FONT);
        button_uploadPhoto.addActionListener(this);
        panel_photo_components.add(button_uploadPhoto, BorderLayout.PAGE_START);
        panel_photo_components.add(panel_image);
        panel_photo.add(panel_photo_components);

        // Buttons Panel
        JPanel panel_buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        button_ok.setPreferredSize(new Dimension(100, 25));
        button_cancel.setPreferredSize(new Dimension(100, 25));
        button_ok.addActionListener(this);
        button_cancel.addActionListener(this);
        panel_buttons.add(button_ok);
        panel_buttons.add(button_cancel);

        // Big Panel
        JPanel panel_big = new JPanel();
        panel_big.setLayout(new BoxLayout(panel_big, BoxLayout.PAGE_AXIS));
        panel_big.add(panel_name);
        panel_big.add(panel_password);
        panel_big.add(panel_repeatedPassword);
        panel_big.add(panel_fullName);
        panel_big.add(panel_birthDate);
        panel_big.add(panel_gender);

        JPanel panel_center = new JPanel();
        panel_center.setLayout(new BoxLayout(panel_center, BoxLayout.LINE_AXIS));
        panel_center.add(panel_big);
        panel_center.add(panel_photo);

        // This Dialog
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent event) {
                ContactDialog.this.pack();
            }
        });

        this.setLayout(new BorderLayout());
        this.setResizable(false);

        this.add(panel_title, BorderLayout.PAGE_START);
        this.add(panel_center);
        this.add(panel_buttons, BorderLayout.PAGE_END);

        this.pack();
        this.setLocation((int) (this.getToolkit().getScreenSize().getWidth() / 2 - this.getWidth() / 2),
                (int) (this.getToolkit().getScreenSize().getHeight() / 2 - this.getHeight() / 2));
    }

    // Show Dialog
    public boolean showDialog(boolean error) {
        if (error) {
            label_name_errors.setText("User name already exists");
        } else {
            contact = new Contact();
//            reset();
        }
        confirmed = false;
        button_cancel.requestFocus();
        this.setVisible(true);
        return confirmed;
    }

    // Get Contact
    public Contact getContact() {
        return contact;
    }

    // Reset
    private void reset() {
        textField_name.clear();
        textField_password.clear();
        textField_repeatPassword.clear();
        textField_fullName.clear();

        label_name_errors.setText("");
        label_password_errors.setText("");
        label_repeatedPassword_errors.setText("");
        label_fullName_errors.setText("");
        label_birthDate_errors.setText("");
        label_gender_errors.setText("");

        panel_date.setDate(null);
        radioButton_male.setSelected(false);
        radioButton_female.setSelected(false);
        panel_image.displayImage(null);
    }

    // Update
    private boolean update() {
        boolean valid = true;
        try {
            contact.setUserName(textField_name.getWrittenText());
            label_name_errors.setText("");
        } catch (RuntimeException ex) {
            label_name_errors.setText(ex.getMessage());
            valid = false;
        }
        try {
            contact.setPassword(textField_password.getWrittenText());
            label_password_errors.setText("");
            if (!textField_password.getWrittenText().equals(textField_repeatPassword.getWrittenText())) {
                label_repeatedPassword_errors.setText("Passwords don't match");
                valid = false;
            } else {
                label_repeatedPassword_errors.setText("");
            }
        } catch (RuntimeException ex) {
            label_password_errors.setText(ex.getMessage());
            label_repeatedPassword_errors.setText("");
            valid = false;
        }
        try {
            contact.setFullName(textField_fullName.getWrittenText());
            label_fullName_errors.setText("");
        } catch (RuntimeException ex) {
            label_fullName_errors.setText(ex.getMessage());
            valid = false;
        }
        if (radioButton_male.isSelected()) {
            contact.setGender(Gender.MALE);
            label_gender_errors.setText("");
        } else if (radioButton_female.isSelected()) {
            contact.setGender(Gender.FEMALE);
            label_gender_errors.setText("");
        } else {
            label_gender_errors.setText("Gender is required");
            valid = false;
        }
        try {
            contact.setBirthDate(panel_date.getDate());
            label_birthDate_errors.setText("");
        } catch (RuntimeException ex) {
            label_birthDate_errors.setText("Birthdate is required");
            valid = false;
        }

        try {
            contact.setPhoto(photo);
        } catch (RuntimeException ex) {
            valid = false;
        }

        this.pack();
        return valid;
    }

    // Action Performed
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == radioButton_male) {
            radioButton_male.setSelected(true);
            radioButton_female.setSelected(false);
        } else if (event.getSource() == radioButton_female) {
            radioButton_female.setSelected(true);
            radioButton_male.setSelected(false);
        }
        if (event.getSource() == button_uploadPhoto) {
            if (fileChooser_photo.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file_photo = fileChooser_photo.getSelectedFile();
                try {
                    photo = ImageIO.read(file_photo);
                    if (photo == null) {
                        JOptionPane.showMessageDialog(this, "Invalid Format");
                    }
                    panel_image.displayImage(photo);
                    this.pack();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else if (event.getSource() == button_ok) {
            if (update()) {
                confirmed = true;
                this.setVisible(false);
            }
        } else if (event.getSource() == button_cancel) {
            this.setVisible(false);
        }
    }

}
