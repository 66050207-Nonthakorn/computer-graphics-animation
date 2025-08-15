
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import animator.*;

class Main {
    static final String TITLE  = "WHAT IF I REBORNED";
    static final int    WIDTH  = 600;
    static final int    HEIGHT = 600;
    static final int    FPS    = 30;

    public static void main(String[] args) {
        Window window = new Window(TITLE, WIDTH, HEIGHT);
        FramePainter framePainter = new FramePainter(FPS, WIDTH, HEIGHT);

        int frameLength = AnimationFrames.painters.size();

        for (int i = 0; i < frameLength; i++) {
            Frame[] frames = Frame.of(
                AnimationFrames.painters.get(i),
                AnimationFrames.durations.get(i)
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
    public static List<PaintFunction> painters = new ArrayList<>();
    public static List<Integer> durations = new ArrayList<>();

    static {
        painters.add((p) -> {
            
        });
        durations.add(1);
    }
}