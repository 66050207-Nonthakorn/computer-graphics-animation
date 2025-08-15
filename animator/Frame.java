package animator;

public abstract class Frame {
    protected abstract void paint(Painter painter);

    public static Frame[] of(PaintFunction paintFunction, int duration) {
        Frame[] frames = new Frame[duration];
        for (int i = 0; i < duration; i++) {
            frames[i] = new Frame() {
                @Override
                protected void paint(Painter painter) {
                    paintFunction.paint(painter);
                }
            };
        }
        return frames;
    }
}