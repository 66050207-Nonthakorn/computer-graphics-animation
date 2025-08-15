
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import animator.*;

class Main {
    static final String TITLE  = "WHAT IF I REBORNED";
    static final int    WIDTH  = 600;
    static final int    HEIGHT = 600;
    static final int    FPS    = 24;

    public static void main(String[] args) {
        Window window = new Window(TITLE, WIDTH, HEIGHT);
        FramePainter framePainter = new FramePainter(FPS, WIDTH, HEIGHT);

        int frameLength = AnimationFrames.painters.size();
        List<PaintFunction> paintFunctions = AnimationFrames.painters;
        List<Integer> durations = AnimationFrames.durations;

        for (int i = 0; i < frameLength; i++) {
            for (Frame frame: Frame.of(paintFunctions.get(i), durations.get(i))) {
                framePainter.addFrame(frame);
            }
        }

        window.add(framePainter);
        window.setVisible(true);

        framePainter.start();
    }
}

class AnimationFrames {
    public static List<PaintFunction> painters = new ArrayList<>();
    public static List<Integer> durations = new ArrayList<>();

    static void toilet(Painter p) {
        p.setOutlineThickness(3);
        p.drawPolygon(new int[] {
            0, 600,
            100, 500,
            500, 500,
            600, 600
        });

        p.drawLine(100, 500, 100, -1);
        p.drawLine(500, 500, 500, -1);
    }

    static void eye(Painter p, final int j) {
        p.setOutlineThickness(1);
        p.setLayer(1);
        p.setOutlineColor(new Color(0, 0, 0));
        
        p.drawLine(
            0, j+1,
            600, j+1
        );
        p.drawLine(0, 0, 0, j);
        p.drawLine(600, 0, 600, j);
        p.fillColor(1, 1, new Color(0, 0, 0));
    }

    static {
        painters.add((p) -> {
            toilet(p);
        });
        durations.add(20);

        for (int i = 0; i < 4; i++) {
            final int j = i * 200;
            painters.add((p) -> {
                toilet(p);
                eye(p, j);
            });
            durations.add(1);
        }

        for (int i = 4; i >= 0; i--) {
            final int j = i * 200;
            painters.add((p) -> {
                toilet(p);
                eye(p, j);
            });

            durations.add(1);
        }

        for (int i = 0; i < 4; i++) {
            final int j = i * 200;
            painters.add((p) -> {
                toilet(p);
                eye(p, j);
            });
            durations.add(1);
        }

        for (int i = 4; i >= 0; i--) {
            final int j = i * 200;
            painters.add((p) -> {
                toilet(p);
                eye(p, j);
            });

            durations.add(1);
        }
    }
}