package AI;

import Classes.Board;
import Classes.Piece;

import static Classes.Piece.*;

public class Evaluate {

    private Evaluate() {
        throw new IllegalStateException("Utility class");
    }

    private static final int PAWN_VALUE = 100;
    private static final int KNIGHT_VALUE = 300;
    private static final int BISHOP_VALUE = 325;
    private static final int ROOK_VALUE = 500;
    private static final int QUEEN_VALUE = 900;
    private static final int KING_VALUE = 999;

    public static int EvaluatePosition(){
        int whiteEval = CountMaterial(Piece.WHITE);
        int blackEval = CountMaterial(Piece.BLACK);

        return whiteEval - blackEval;
    }

    public static int GetPieceValue(int piece){
        switch (piece){
            case PAWN -> {
                return PAWN_VALUE;
            }
            case BISHOP -> {
                return BISHOP_VALUE;
            }
            case KNIGHT -> {
                return KNIGHT_VALUE;
            }
            case ROOK -> {
                return ROOK_VALUE;
            }
            case QUEEN -> {
                return QUEEN_VALUE;
            }
            case KING -> {
                return KING_VALUE;
            }
            default -> {
                return 0;
            }
        }
    }

    private static int CountMaterial(int color){
        int material = 0;

        for (int i = 0; i < 64; i++){
            if (Piece.IsColour(Board.GetSquare()[i], color)){
                switch (Piece.PieceType(Board.GetSquare()[i])){ //NOSONAR
                    case PAWN -> material += PAWN_VALUE;
                    case BISHOP -> material += BISHOP_VALUE;
                    case KNIGHT -> material += KNIGHT_VALUE;
                    case ROOK -> material += ROOK_VALUE;
                    case QUEEN -> material += QUEEN_VALUE;
                    case KING -> material += KING_VALUE;
                }
            }
        }

        return material;
    }

}
