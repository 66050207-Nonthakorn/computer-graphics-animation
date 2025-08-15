
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import animator.*;

class Main {
    public static void main(String[] args) {
        Window window = new Window(Setup.TITLE, Setup.WIDTH, Setup.HEIGHT);
        FramePainter framePainter = new FramePainter(Setup.FPS, Setup.WIDTH, Setup.HEIGHT);

        List<PaintFunction> paintFunctions = Setup.painters;
        List<Integer> durations = Setup.durations;
        int frameLength = durations.size();

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

class Setup {
    static final String TITLE  = "WHAT IF I REBORNED";
    static final int    WIDTH  = 600;
    static final int    HEIGHT = 600;
    static final int    FPS    = 24;

    static List<PaintFunction> painters = new ArrayList<>();
    static List<Integer> durations = new ArrayList<>();

    static {
        // Scene 1
        {
            // Idle background
            painters.add(background());
            durations.add(20);

            // Blinking
            for (int i = 0; i < 4; i++) {
                painters.add(PaintFunction.combine(background(), blink(i)));
                durations.add(1);
            }
            for (int i = 3; i >= 0; i--) {
                painters.add(PaintFunction.combine(background(), blink(i)));
                durations.add(1);
            }
        }

        // Scene 2
        {
            
        }
    }

    static PaintFunction background() {
        return (p) -> {
            p.setLayer(0);
            p.setOutlineThickness(3);

            // Wall
            p.drawPolygon(
                0, 600,
                100, 500,
                500, 500,
                600, 600
            );
            p.drawLine(100, 500, 100, -1);
            p.drawLine(500, 500, 500, -1);
            
            // Door
            p.drawPolygon(
                350, 500,
                350, 320,
                240, 320,
                240, 500
            );
            p.fillColor(339, 410, new Color(50, 50, 0));
            p.drawCircle(335, 410, 6);
            p.fillColor(339, 410, new Color(50, 50, 0));
        };
    }

    static PaintFunction blink(int position) {
        return (p) -> {
            p.setLayer(1);
            p.setOutlineThickness(1);
            p.setOutlineColor(new Color(0, 0, 0));
            
            int j = position * 200;
            p.drawLine(0, j+1, 600, j+1);
            p.drawLine(0, 0, 0, j);
            p.drawLine(600, 0, 600, j);
            p.fillColor(1, 1, new Color(0, 0, 0));
        };
    }
}