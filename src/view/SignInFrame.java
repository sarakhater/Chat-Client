package view;

import common.Contact;
import controller.Controller;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class SignInFrame extends javax.swing.JFrame {

    // Constants
    private static final int USER_NAME_MAX_LENGTH = 25;
    private static final int PASSWORD_MAX_LENGTH = 50;

    private Controller controller;

    private Image image_logo = null;

    // Constructors
    public SignInFrame(Controller controller) {
        super("Chat App");
        this.controller = controller;
        initComponents();

        try {
            image_logo = ImageIO.read(new File("images\\logo.png")).getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            label_image.setIcon(new ImageIcon(image_logo));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        textField_userName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent event) {
                if (textField_userName.getText().length() >= USER_NAME_MAX_LENGTH) {
                    event.consume();
                }
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent event) {
                if (passwordField.getText().length() >= PASSWORD_MAX_LENGTH) {
                    event.consume();
                }
            }
        });

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int) (screenSize.getWidth() - getWidth()), 0);
    }

    // Stop Service
    public void stopService() {
        JOptionPane stop_service_msg = new JOptionPane();
        setVisible(true);
        stop_service_msg.showMessageDialog(this, "Connection not avilable");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        label_title = new javax.swing.JLabel();
        label_image = new javax.swing.JLabel();
        label_userName = new javax.swing.JLabel();
        textField_userName = new javax.swing.JTextField();
        label_password = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        button_signIn = new javax.swing.JButton();
        button_createAccount = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(51, 153, 255));
        setPreferredSize(new java.awt.Dimension(320, 700));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(51, 153, 255));

        label_title.setFont(new java.awt.Font("Cooper Black", 1, 48)); // NOI18N
        label_title.setText("Chat App");

        label_userName.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        label_userName.setText("User Name:");

        textField_userName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        textField_userName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textField_userNameActionPerformed(evt);
            }
        });

        label_password.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        label_password.setText("Password:");

        passwordField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        passwordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordFieldActionPerformed(evt);
            }
        });

        button_signIn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        button_signIn.setText("Sign In");
        button_signIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_signInActionPerformed(evt);
            }
        });

        button_createAccount.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        button_createAccount.setText("Create Account");
        button_createAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_createAccountActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_userName)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(passwordField, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                        .addComponent(textField_userName))
                    .addComponent(label_password)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(label_title)))
                .addContainerGap(28, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(label_image, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(71, 71, 71))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(button_createAccount)
                        .addGap(64, 64, 64))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(button_signIn)
                .addGap(105, 105, 105))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(label_title)
                .addGap(31, 31, 31)
                .addComponent(label_image, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(label_userName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textField_userName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(label_password)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                .addComponent(button_signIn)
                .addGap(18, 18, 18)
                .addComponent(button_createAccount)
                .addGap(81, 81, 81))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_signInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_signInActionPerformed
        String username = textField_userName.getText();
        String password = passwordField.getText();
        try {
            Contact dumy = new Contact(username, password);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            return;
        }
        if (!controller.login(username, password)) {
            JOptionPane.showMessageDialog(this, "User name or password is incorrect");
        }

    }//GEN-LAST:event_button_signInActionPerformed

    private void button_createAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_createAccountActionPerformed
        controller.signup();
    }//GEN-LAST:event_button_createAccountActionPerformed

    private void passwordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordFieldActionPerformed

    private void textField_userNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textField_userNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textField_userNameActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_createAccount;
    private javax.swing.JButton button_signIn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel label_image;
    private javax.swing.JLabel label_password;
    private javax.swing.JLabel label_title;
    private javax.swing.JLabel label_userName;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JTextField textField_userName;
    // End of variables declaration//GEN-END:variables
}
