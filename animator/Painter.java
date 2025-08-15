package animator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

public class Painter {    
    private int width;
    private int height;
    private int lineThickness;
    private int currentLayer;

    private Graphics2D frameG;

    private BufferedImage[] buffer;
    private Graphics2D[] bufferG;
    
    public Painter(Graphics2D g, int width, int height) {
        this.frameG = g;
        this.width = width;
        this.height = height;
        
        this.buffer = new BufferedImage[20];
        this.bufferG = new Graphics2D[20];
        for (int i = 0; i < 20; i++) {
            this.buffer[i] = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
            this.bufferG[i] = this.buffer[i].createGraphics();
            this.bufferG[i].setColor(Color.BLACK);
        }

        this.lineThickness = 1;
        this.currentLayer = 0;
    }

    public void drawBuffer() {
        for (BufferedImage img : buffer) {
            frameG.drawImage(img, 0, 0, null);
        }
    }

    public void setLayer(int layer) {
        this.currentLayer = layer;
    }

    public void setOutlineColor(Color color) {
        this.bufferG[currentLayer].setColor(color);
    }

    public void setOutlineThickness(int thickness) {
        this.lineThickness = thickness;
    }
    
    public void plot(int x, int y, int size) {
        this.bufferG[currentLayer].fillRect(x, y, size, size);
    }

    public void plot(int x, int y) {
        plot(x, y, lineThickness);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        
        boolean isSwapped = false;
        if (dy > dx) {
            int tmp = dx;
            dx = dy;
            dy = tmp;

            isSwapped = true;
        }

        int d = 2 * dy - dx;
        int x = x1;
        int y = y1;
        
        for (int i = 1; i <= dx; i++) {
            plot(x, y);

            if (d >= 0) {
                if (isSwapped) {
                    x += sx;
                }
                else {
                    y += sy;
                }

                d -= 2 * dx;
            }

            if (isSwapped) {
                y += sy;
            }
            else {
                x += sx;
            }

            d += 2 * dy;
        }
    }

    public void drawCircle(int xc, int yc, int r) {
        int x = 0, y = r;
        int d = 1 - r;
        int dx = 2 * x;
        int dy = 2 * y;

        while (x <= y) {
            // Plot all 8 octants
            plot(xc + x, yc + y);
            plot(xc - x, yc + y);
            plot(xc + x, yc - y);
            plot(xc - x, yc - y);
            plot(xc + y, yc + x);
            plot(xc - y, yc + x);
            plot(xc + y, yc - x);
            plot(xc - y, yc - x);

            x++;
            dx = 2 * x;
            d = d + dx + 1;
            if (d >= 0) {
                y--;
                dy = 2 * y;
                d = d - dy;
            }
        }
    }

    public void drawEllipse(int xc, int yc, int rx, int ry) {

        int a2 = rx * rx;
        int b2 = ry * ry;
        int twoA2 = 2 * a2;
        int twoB2 = 2 * b2;

        // Region 1
        int x = 0, y = ry;
        int dx = 0;
        int dy = twoA2 * y;
        int d = (int)Math.round(b2 - (a2 * ry) + (0.25 * a2));

        while (dx <= dy) {
            // Plot all 4 quadrants
            plot(xc + x, yc + y);
            plot(xc - x, yc + y);
            plot(xc + x, yc - y);
            plot(xc - x, yc - y);

            x++;
            dx = dx + twoB2;
            d = d + dx + b2;

            if (d >= 0) {
                y--;
                dy = dy - twoA2;
                d = d - dy;
            }
        }

        // Region 2
        x = rx;
        y = 0;
        dx = twoB2 * x;
        dy = 0;
        d = (int)Math.round(a2 - (b2 * rx) + (0.25 * b2));

        while (dx >= dy) {
            // Plot all 4 quadrants
            plot(xc + x, yc + y);
            plot(xc - x, yc + y);
            plot(xc + x, yc - y);
            plot(xc - x, yc - y);

            y++;
            dy = dy + twoA2;
            d = d + dy + a2;

            if (d >= 0) {
                x--;
                dx = dx - twoB2;
                d = d - dx;
            }
        }
    }
    
    public void drawCurve(
        int x1, int y1,
        int x2, int y2,
        int x3, int y3,
        int x4, int y4
    ) {
        for (float t = 0.0f; t <= 1.0f; t += (1 / 1000.0f)) {
            int x = (int)(
                Math.pow(1 - t, 3)           * x1 +
                3 * t * Math.pow(1 - t, 2)   * x2 +
                3 * Math.pow(t, 2) * (1 - t) * x3 +
                Math.pow(t, 3)               * x4
            );

            int y = (int)(
                Math.pow(1 - t, 3)           * y1 +
                3 * t * Math.pow(1 - t, 2)   * y2 +
                3 * Math.pow(t, 2) * (1 - t) * y3 +
                Math.pow(t, 3)               * y4
            );

            plot(x, y);
        }
    }

    public void drawPolygon(int... p) {
        for (int i = 0; i < p.length - 3; i += 2) {
            drawLine(p[i], p[i+1], p[i+2], p[i+3]);
        }
    }

    public void fillColor(int x, int y, Color color) {
        Queue<Integer[]> queue = new LinkedList<>();
        queue.add(new Integer[]{x, y});

        int replacementColor = color.getRGB();
        int targetColor = this.buffer[currentLayer].getRGB(x, y);

        if (replacementColor == targetColor) {
            return;
        }

        this.buffer[currentLayer].setRGB(x, y, replacementColor);

        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};

        while (!queue.isEmpty()) {
            Integer[] front = queue.poll();
            int cx = front[0];
            int cy = front[1];

            for (int i = 0; i < 4; i++) {
                int nx = cx + dx[i], ny = cy + dy[i];

                if (nx < 0 || nx >= width || ny < 0 || ny >= height)
                    continue;

                if (this.buffer[currentLayer].getRGB(nx, ny) != targetColor)
                    continue;

                this.buffer[currentLayer].setRGB(nx, ny, replacementColor);
                queue.add(new Integer[]{nx, ny});
            }
        }
    }

}
