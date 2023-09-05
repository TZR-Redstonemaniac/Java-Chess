package Classes;


public class Move {
    public final int startSquare;
    public final int targetSquare;

    public final int promoPiece;

    public Move(int startSquare, int targetSquare) {
        this.startSquare = startSquare;
        this.targetSquare = targetSquare;
        promoPiece = 0;
    }

    public Move(int startSquare, int targetSquare, int promoPiece) {
        this.startSquare = startSquare;
        this.targetSquare = targetSquare;
        this.promoPiece = promoPiece;
    }
}
