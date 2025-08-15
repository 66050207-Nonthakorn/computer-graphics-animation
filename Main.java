
import java.awt.Color;

import animator.*;

class Main {
    static final String TITLE  = "WHAT IF I REBORNED";
    static final int    WIDTH  = 600;
    static final int    HEIGHT = 600;
    static final int    FPS    = 10;

    public static void main(String[] args) {
        Window window = new Window(TITLE, WIDTH, HEIGHT);
        FramePainter framePainter = new FramePainter(FPS, WIDTH, HEIGHT);

        int frameLength = AnimationFrames.painters.length;

        for (int i = 0; i < frameLength; i++) {
            Frame[] frames = Frame.of(
                AnimationFrames.painters[i],
                AnimationFrames.durations[i]
            );
            for (Frame frame: frames) {
                framePainter.addFrame(frame);
            }
        }

        window.add(framePainter);
        window.setVisible(true);

        framePainter.start();
    }
}

class AnimationFrames {
    public static PaintFunction[] painters = {
        (p) -> {
            p.setOutlineColor(Color.BLACK);
    
        },
        (p) -> {
            p.setOutlineColor(Color.BLACK);
            p.drawCircle(100, 100, 30);
            p.fillColor(100, 100, Color.GREEN);
        },
    };

    public static int[] durations = {
        20,
        60
    };

    static {
        
    }
}