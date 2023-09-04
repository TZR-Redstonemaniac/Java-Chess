package Classes;

import javax.swing.*;

public class Move {
    public final int StartSquare;
    public final int TargetSquare;

    public final int promoPiece;

    public Move(int startSquare, int targetSquare) {
        this.StartSquare = startSquare;
        this.TargetSquare = targetSquare;
        promoPiece = 0;
    }

    public Move(int startSquare, int targetSquare, int promoPiece) {
        this.StartSquare = startSquare;
        this.TargetSquare = targetSquare;
        this.promoPiece = promoPiece;
    }
}
