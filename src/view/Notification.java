package view;

import common.Contact;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 *
 * @author Al-Jazayeerly
 */
public class Notification extends JWindow {

    public Notification(Frame owner, Contact contact, String msg) {

        super(owner);
        JPanel display_panel = new JPanel(new FlowLayout());
        JLabel label_name = new JLabel(contact.getFullName());
        JLabel text = new JLabel(msg);
        JPanel data_panel = new JPanel(new GridLayout(2, 1));
        data_panel.add(text);
        data_panel.add(label_name);
        JLabel img = null;
        if (contact.getPhoto() != null) {
            img = new JLabel(new ImageIcon((contact.getPhoto().getScaledInstance(50, 50, Image.SCALE_SMOOTH))));
        } else {
            try {
                img = new JLabel(new ImageIcon(ImageIO.read(new File("images\\default.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            } catch (IOException ex) {
                Logger.getLogger(Notification.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        label_name.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        text.setFont(new Font(Font.DIALOG, Font.PLAIN, 15));
        display_panel.add(img);
        display_panel.add(data_panel);
        //display_panel.add(label_name, BorderLayout.LINE_END);
        this.add(display_panel);
        this.setSize(300, 100);
        Dimension screen_dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screen_dimension.width - 300, screen_dimension.height - 100);

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                setVisible(false);
            }
        }.start();
    }

}
