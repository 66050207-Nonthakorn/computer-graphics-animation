package animator;

@FunctionalInterface
public interface PaintFunction {
    void paint(Painter painter);
    
    public static PaintFunction EMPTY = (p) -> {};

    static PaintFunction combine(PaintFunction... functions) {
        return (painter) -> {
            for (PaintFunction func : functions) {
                func.paint(painter);
            }
        };
    }
}