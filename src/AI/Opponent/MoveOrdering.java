package AI.Opponent;

import Classes.Board;
import Classes.Move;
import Classes.Piece;

import java.util.List;

public class MoveOrdering {

    private MoveOrdering() {
        throw new IllegalStateException("Utility class");
    }

    public static List<Move> OrderMoves(List<Move> moves){
        moves.sort((move1, move2) -> Double.compare(EvaluateMove(move2), EvaluateMove(move1)));

        return moves;
    }

    private static int EvaluateMove(Move move){
        int score = 0;
        int movePieceType = Piece.PieceType(Board.GetSquare()[move.startSquare]);
        int targetPieceType = Piece.PieceType(Board.GetSquare()[move.targetSquare]);

        if (targetPieceType != 0) {
            score = 10 * Evaluate.GetPieceValue(targetPieceType) - Evaluate.GetPieceValue(movePieceType);
        }

        if (move.promoPiece != 0){
            score += Evaluate.GetPieceValue(move.promoPiece);
        }

        int whiteKingPos = 0;
        int blackKingPos = 0;

        for (int i = 0; i < 64; i++){
            if (Piece.PieceChecker(Board.GetSquare()[i], Piece.KING, Piece.BLACK)) blackKingPos = i;
            if (Piece.PieceChecker(Board.GetSquare()[i], Piece.KING, Piece.WHITE)) whiteKingPos = i;
        }

        Board.MakeMove(move);

        if (Board.colorToMove == Piece.WHITE && Board.IsSquareAttacked(blackKingPos, Piece.WHITE)) score += 500;
        if (Board.colorToMove == Piece.BLACK && Board.IsSquareAttacked(whiteKingPos, Piece.BLACK)) score += 500;

        Board.UnmakeMove();

        if (Board.IsSquareAttacked(move.targetSquare, Board.opponentColor))
            score -= Evaluate.GetPieceValue(movePieceType);

        return score;
    }

}
