
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import animator.*;

class Main {
    public static void main(String[] args) {
        Window window = new Window(Setup.TITLE, Setup.WIDTH, Setup.HEIGHT);
        FramePainter framePainter = new FramePainter(Setup.FPS, Setup.WIDTH, Setup.HEIGHT);

        List<PaintFunction> paintFunctions = Setup.painters;
        List<Integer> durations = Setup.durations;
        int frameLength = durations.size();

        for (int i = 0; i < frameLength; i++) {
            for (Frame frame : Frame.of(paintFunctions.get(i), durations.get(i))) {
                framePainter.addFrame(frame);
            }
        }

        window.add(framePainter);
        window.setVisible(true);

        framePainter.start();
    }
}

class Setup {
    static final String TITLE = "WHAT IF I REBORNED";
    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int FPS = 24;

    static List<PaintFunction> painters = new ArrayList<>();
    static List<Integer> durations = new ArrayList<>();

    static Clip clip;
    static {
        File file = new File("sounds/paddle_sfx.wav");
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    static {
        // Scene 1
        {
            // Idle background
            painters.add(backgroundComputer());
            durations.add(1);

            // Pong game
            for (int i = 0; i < 3; i++) {
                for (int j = 140; j < 460; j += 10) {
                    painters.add(PaintFunction.combine(backgroundComputer(), runPongGame(j)));
                    durations.add(1);
                }
                for (int j = 460; j > 140; j -= 10) {
                    painters.add(PaintFunction.combine(backgroundComputer(), runPongGame(j)));
                    durations.add(1);
                }
            }
        }

        // Scene 2
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

        // Scene 3
        {

        }
    }

    static PaintFunction backgroundComputer() {
        return (p) -> {
            p.setLayer(0);
            p.setOutlineThickness(3);

            // Desk
            p.drawPolygon(
                    0, 500,
                    100, 400,
                    500, 400,
                    600, 500);
            p.fillColor(300, 550, new Color(185, 122, 86));

            // Keyboard
            p.drawPolygon(
                    150, 520,
                    170, 480,
                    430, 480,
                    450, 520,
                    150, 520);
            p.fillColor(300, 500, new Color(41, 41, 41));
            p.drawPolygon(150, 530,
                    150, 520,
                    450, 520,
                    450, 530,
                    150, 530);
            p.fillColor(300, 525, new Color(23, 23, 23));

            // Monitor
            p.drawPolygon(250, 460,
                    250, 455,
                    350, 455,
                    350, 460,
                    250, 460);
            p.fillColor(300, 458, new Color(46, 46, 46));
            p.drawPolygon(290, 455,
                    290, 420,
                    310, 420,
                    310, 455);
            p.fillColor(300, 440, new Color(46, 46, 46));
            p.drawPolygon(100, 420,
                    100, 200,
                    500, 200,
                    500, 420,
                    100, 420);
            p.fillColor(300, 300, new Color(46, 46, 46));
            p.fillColor(300, 410, new Color(46, 46, 46));
            p.drawPolygon(105, 400,
                    105, 205,
                    495, 205,
                    495, 400,
                    105, 400);
            p.fillColor(300, 300, new Color(0, 0, 0));

            p.setOutlineThickness(1);
            p.drawCircle(485, 410, 5);
            p.fillColor(485, 410, new Color(0, 162, 255));
            p.setOutlineThickness(3);

            // Line divider
            p.setOutlineColor(new Color(255, 255, 255));
            p.drawLine(300, 400, 300, 205);

            // Scoreboard
            p.drawLine(230, 250, 230, 220);
            p.drawLine(230, 220, 250, 220);
            p.drawLine(250, 220, 250, 250);
            p.drawLine(250, 250, 230, 250);

            p.drawLine(350, 220, 370, 220);
            p.drawLine(370, 220, 370, 250);
            p.drawLine(350, 235, 370, 235);
            p.drawLine(370, 250, 350, 250);
        };
    }

    static PaintFunction runPongGame(int ballPosition) {
        return (p) -> {
            p.setLayer(1);
            p.setOutlineThickness(5);
            p.setOutlineColor(new Color(255, 255, 255));

            // Ball
            p.drawPolygon(ballPosition, 310,
                    ballPosition, 305,
                    ballPosition + 5, 305,
                    ballPosition + 5, 310,
                    ballPosition, 310);
            p.fillColor(ballPosition, 308, new Color(255, 255, 255));

            // Left paddle (player 1)
            p.drawPolygon(135, 320,
                    135, 290,
                    140, 290,
                    140, 320,
                    135, 320);
            p.fillColor(138, 300, new Color(255, 255, 255));

            // Right paddle (player 2)
            p.drawPolygon(460, 320,
                    460, 290,
                    465, 290,
                    465, 320,
                    460, 320);
            p.fillColor(138, 300, new Color(255, 255, 255));

            if (ballPosition == 150 || ballPosition == 450) {
                clip.start();

                Runnable task = () -> {
                    try {
                        Thread.sleep(1000);
                        clip.setMicrosecondPosition(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                };

                new Thread(task).start();
            }
        };
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
                    600, 600);
            p.drawLine(100, 500, 100, -1);
            p.drawLine(500, 500, 500, -1);

            // Door
            p.drawPolygon(
                    350, 500,
                    350, 320,
                    240, 320,
                    240, 500);
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
            p.drawLine(0, j + 1, 600, j + 1);
            p.drawLine(0, 0, 0, j);
            p.drawLine(600, 0, 600, j);
            p.fillColor(1, 1, new Color(0, 0, 0));
        };
    }
}