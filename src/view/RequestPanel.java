package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import controller.Controller;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;

public class RequestPanel extends JPanel {

    public RequestPanel(String fullname, String username, Controller controller) {
        this.username = username;
        this.controller = controller;
        JLabel label_name = new JLabel(fullname);
        accept_btn = new JButton("Accept");
        decline_btn = new JButton("Decline");
        accept_btn.setMargin(new Insets(2, 2, 2, 2));
        decline_btn.setMargin(new Insets(2, 2, 2, 2));
        JPanel panel_button = new JPanel(new FlowLayout());
        panel_button.add(accept_btn);
        panel_button.add(decline_btn);
        this.setLayout(new BorderLayout());
        this.add(label_name, BorderLayout.LINE_START);
        this.add(panel_button, BorderLayout.LINE_END);
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        accept_btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                accept_btnActionperformed();
            }
        });
        decline_btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                decline_btnActionperformed();
            }
        });

    }

    private void accept_btnActionperformed() {
        controller.acceptFriendRequest(username);
    }
    private void decline_btnActionperformed()
            {
        controller.declineFriendRequest(username);
    }

    String username;
    Controller controller;
    JPanel panel_button;
    JLabel label_name;
    JButton accept_btn;
    JButton decline_btn;
}
