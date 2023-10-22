package AI.Opponent;

import Classes.Board;
import Classes.MoveGenerator;
import Classes.Piece;
import Core.Game;

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

    public static int EvaluatePosition(int ogColor){
        int whiteEval = CountMaterial(Piece.WHITE);
        int blackEval = CountMaterial(Piece.BLACK);

        int whiteKingPos = 0;
        int blackKingPos = 0;

        for (int i = 0; i < 64; i++){
            if (Piece.PieceChecker(Board.GetSquare()[i], Piece.KING, Piece.BLACK)) blackKingPos = i;
            if (Piece.PieceChecker(Board.GetSquare()[i], Piece.KING, Piece.WHITE)) whiteKingPos = i;
        }

        if (ogColor != 0){
            MoveGenerator.ClearMoves();
            MoveGenerator.GenerateKingMoves(blackKingPos);
            if (!Board.IsSquareAttacked(blackKingPos, ogColor) && MoveGenerator.GetMoves().isEmpty()){
                whiteEval -= 999;
            }

            MoveGenerator.ClearMoves();
            MoveGenerator.GenerateKingMoves(whiteKingPos);
            if (!Board.IsSquareAttacked(blackKingPos, ogColor) && MoveGenerator.GetMoves().isEmpty()){
                blackEval -= 999;
            }
        }

        int eval = Math.abs(whiteEval - blackEval);

        if (ogColor == WHITE){
            eval += ForceKingIntoCornerEval(blackKingPos);
            eval += ReduceKingDistanceEval(whiteKingPos, blackKingPos);

            if (Game.gamePhase == Game.GamePhase.ENDGAME) eval += IncentiviseAttackingKing(blackKingPos, WHITE);
        } else {
            eval += ForceKingIntoCornerEval(whiteKingPos);
            eval += ReduceKingDistanceEval(blackKingPos, whiteKingPos);

            if (Game.gamePhase == Game.GamePhase.ENDGAME) eval += IncentiviseAttackingKing(whiteKingPos, BLACK);
        }

        return ogColor == WHITE ? Math.abs(eval) : -Math.abs(eval);
    }

    private static int ForceKingIntoCornerEval(int opponentKingPos){
        int eval = 0;

        int opponentKingRank = Board.Rank(opponentKingPos);
        int opponentKingFile = Board.File(opponentKingPos);

        int oppDistanceToCenterFile = Math.max(3 - opponentKingFile, opponentKingFile - 4);
        int oppDistanceToCenterRank = Math.max(3 - opponentKingRank, opponentKingRank - 4);
        int oppDistanceFromCenter = oppDistanceToCenterFile + oppDistanceToCenterRank;
        eval += oppDistanceFromCenter;

        return eval * 100;
    }

    private static int ReduceKingDistanceEval(int friendlyKingPos, int opponentKingPos){
        int eval = 0;

        int opponentKingRank = Board.Rank(opponentKingPos);
        int opponentKingFile = Board.File(opponentKingPos);

        int friendlyKingRank = Board.Rank(friendlyKingPos);
        int friendlyKingFile = Board.File(friendlyKingPos);

        int dstBetweenKingsFile = Math.abs(friendlyKingFile - opponentKingFile);
        int dstBetweenKingsRank = Math.abs(friendlyKingRank - opponentKingRank);
        int dstBetweenKings = dstBetweenKingsFile + dstBetweenKingsRank;
        eval -= dstBetweenKings;

        return eval * 10;
    }

    private static int IncentiviseAttackingKing(int opponentKingPos, int attackingColor){
        int eval = 0;

        if (Board.IsSquareAttacked(opponentKingPos, attackingColor)) eval += 1;
        else eval -= 1;

        return eval * 100;
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

    public static int CountMaterial (int color){
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

    public static int CountMaterialWithoutKing (int color){
        int material = 0;

        for (int i = 0; i < 64; i++){
            if (Board.GetSquare()[i] != NONE && Piece.IsColour(Board.GetSquare()[i], color)){
                switch (Piece.PieceType(Board.GetSquare()[i])){ //NOSONAR
                    case PAWN -> material += PAWN_VALUE;
                    case BISHOP -> material += BISHOP_VALUE;
                    case KNIGHT -> material += KNIGHT_VALUE;
                    case ROOK -> material += ROOK_VALUE;
                    case QUEEN -> material += QUEEN_VALUE;
                }
            }
        }

        return material;
    }

}
