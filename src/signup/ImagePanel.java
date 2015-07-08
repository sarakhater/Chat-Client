package signup;

import javax.swing.*;

import java.awt.*;
import java.io.File;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
class ImagePanel extends JPanel {

    private BufferedImage bufferedImage;
    private JLabel label_image = new JLabel();

    // Constructors
    public ImagePanel(JPanel parentPanel, int panelWidth, int panelHeight) {
        super(new BorderLayout());
        this.add(label_image);
        this.setSize(new Dimension(panelWidth, panelHeight));
        this.setPreferredSize(new Dimension(panelWidth, panelHeight));
        this.setBackground(parentPanel.getBackground());
    }

    // Open Image
    public void openImage(String path) {
        try {
            displayImage(ImageIO.read(new File(path)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Display Image
    public void displayImage(BufferedImage image) {
        if (image == null) {
            try {
                image = ImageIO.read(new File(System.getProperty("user.dir") + "\\Images\\Default.png"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        Dimension dimension = new Dimension(image.getWidth(), image.getHeight());
        scale(dimension);
        int width = (int) (dimension.getWidth());
        int height = (int) (dimension.getHeight());
        this.bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.bufferedImage.getGraphics().drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

        ImageIcon imageIcon = new ImageIcon(bufferedImage);
        this.label_image.setIcon(imageIcon);
        this.label_image.setHorizontalAlignment(JLabel.CENTER);
        this.validate();
        this.repaint();
    }

    // Scale the image to the panel size
    private void scale(Dimension dimension) {
        int width, height;
        if (dimension.getWidth() > dimension.getHeight()) {
            width = this.getWidth();
            height = (int) (dimension.getHeight() * width / dimension.getWidth());
        } else {
            height = this.getHeight();
            width = (int) (dimension.getWidth() * height / dimension.getHeight());
        }
        dimension.setSize(width, height);
    }

    // Get Image
    public BufferedImage getImage() {
        return this.bufferedImage;
    }

}
