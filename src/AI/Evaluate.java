package AI;

import Classes.Board;
import Classes.Piece;

public class Evaluate {

    private Evaluate() {
        throw new IllegalStateException("Utility class");
    }

    private static final int PAWN_VALUE = 1;
    private static final int BISHOP_VALUE = 3;
    private static final int KNIGHT_VALUE = 3;
    private static final int ROOK_VALUE = 5;
    private static final int QUEEN_VALUE = 9;


    public static int EvaluatePosition(){
        int whiteEval = CountMaterial(Piece.WHITE);
        int blackEval = CountMaterial(Piece.BLACK);

        return whiteEval - blackEval;
    }

    private static int CountMaterial(int color){
        int material = 0;

        for (int i = 0; i < 64; i++){
            if (Piece.IsColour(Board.GetSquare()[i], color)){
                switch (Piece.PieceType(Board.GetSquare()[i])){ //NOSONAR
                    case Piece.PAWN -> material += PAWN_VALUE;
                    case Piece.BISHOP -> material += BISHOP_VALUE;
                    case Piece.KNIGHT -> material += KNIGHT_VALUE;
                    case Piece.ROOK -> material += ROOK_VALUE;
                    case Piece.QUEEN -> material += QUEEN_VALUE;
                }
            }
        }

        return material;
    }

}
