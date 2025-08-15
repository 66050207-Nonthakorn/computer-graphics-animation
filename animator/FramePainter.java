package animator;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class FramePainter extends JPanel {

    private int fps;
    private int width;
    private int height;
    private int currentFrame;
    private List<Frame> frames;

    public FramePainter(int fps, int width, int height) {
        this.fps = fps;
        this.width = width;
        this.height = height;
        this.frames = new ArrayList<>();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        drawFrame(g2d, currentFrame);
        g2d.dispose();
    }

    public void start() {
        Runnable task = () -> {
            long delay = 1000 / fps;

            while (currentFrame < frames.size()) {
                SwingUtilities.invokeLater(() -> repaint());

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                currentFrame++;
            }
        };

        new Thread(task).start();
    }

    public void addFrame(Frame frame) {
        frames.add(frame);
    }

    public void drawFrame(Graphics2D g, int index) {
        if (index < 0 || index >= frames.size())
            return;

        Painter painter = new Painter(g, width, height);
        Frame frame = frames.get(index);

        frame.paint(painter);
        painter.drawBuffer();
    }
}