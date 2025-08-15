package animator;


import javax.swing.JFrame;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Window extends JFrame {
    public Window(String title, int width, int height) {
        this.setSize(width, height);
        this.setTitle(title);
        this.setLayout(new CardLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(
            (int)screenSize.getWidth()  / 2 - width  / 2,
            (int)screenSize.getHeight() / 2 - height / 2
        );

        this.setVisible(true);
    }
}