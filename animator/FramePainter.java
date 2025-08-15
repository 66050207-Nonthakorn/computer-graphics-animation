package animator;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class FramePainter extends JPanel {

    private int fps;
    private int currentFrame;
    private List<Frame> frames;
    private Painter painter;

    public FramePainter(int fps, int width, int height) {
        this.fps = fps;
        this.frames = new ArrayList<>();
        this.painter = new Painter(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        drawFrame(g2d, currentFrame);
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

        Frame frame = frames.get(index);
        
        this.painter.setGraphics2D(g);
        this.painter.reset();
        frame.paint(this.painter);
        this.painter.drawBuffer();
    }
}