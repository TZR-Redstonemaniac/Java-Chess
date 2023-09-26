package Classes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MoveGenerator {

    //ignore
    private MoveGenerator() {
        throw new IllegalStateException("Utility class");
    }
    //endignore

    //region Variables
    private static ArrayList<Move> moves = new ArrayList<>();

    private static boolean firstIteration;
    private static int totalNodes = 0;

    public static final StringBuilder divide = new StringBuilder();

    private static final Logger LOGGER = Logger.getLogger(MoveGenerator.class.getName());
    //endregion

    private static ArrayList<Move> GenerateMoves(){
        moves = new ArrayList<>();

        for (int startSquare = 0; startSquare < 64; startSquare++){
            int piece = Board.GetSquare()[startSquare];

            if (piece == Piece.NONE) continue;

            if (Piece.IsColour(piece, Board.colorToMove)){
                switch (Piece.PieceType(piece)){
                    case Piece.BISHOP, Piece.ROOK, Piece.QUEEN -> GenerateSlidingMoves(startSquare, piece);
                    case Piece.KNIGHT -> GenerateKnightMoves(startSquare);
                    case Piece.PAWN -> GeneratePawnMoves(startSquare);
                    case Piece.KING -> GenerateKingMoves(startSquare);
                    default -> throw new IllegalStateException("Unexpected value: " + Piece.PieceType(piece));
                }
            }
        }

        return moves;
    }

    public static List<Move> GenerateLegalMoves(){
        ArrayList<Move> pseudoLegalMoves = GenerateMoves();
        ArrayList<Move> legalMoves = new ArrayList<>();

        int kingSquare = 0;

        for (Move moveToVerify : pseudoLegalMoves) {
            Board.MakeMove(moveToVerify);
            ArrayList<Move> opponentResponses = GenerateMoves();

            for (int startSquare = 0; startSquare < 64; startSquare++){
                int piece = Board.GetSquare()[startSquare];
                if (Piece.PieceChecker(piece, Piece.KING, Board.opponentColor)) kingSquare = startSquare;
            }

            boolean anyResponseMatches = false;
            for (Move response : opponentResponses) {
                if (response.targetSquare == kingSquare) {
                    anyResponseMatches = true;
                    break;
                }
            }

            if (!anyResponseMatches) legalMoves.add(moveToVerify);

            Board.UnmakeMove();
        }

        return legalMoves;
    }

    public static void GenerateSlidingMoves (int start, int piece){
        int startDirIndex = Piece.PieceType(piece) == Piece.BISHOP ? 4: 0;
        int endDirIndex = Piece.PieceType(piece) == Piece.ROOK ? 4: 8;

        for (int dirIndex = startDirIndex; dirIndex < endDirIndex; dirIndex++){
            for (int n = 0; n < PrecomputedMoveData.NumSquaresToEdge[start][dirIndex]; n++){
                int target = start + PrecomputedMoveData.DirectionOffsets[dirIndex] * (n + 1);
                int pieceOnTarget = Board.GetSquare()[target];

                if (Piece.IsColour(pieceOnTarget, Board.colorToMove)) break;

                moves.add(new Move(start, target));

                if (!Piece.IsColour(pieceOnTarget, Board.colorToMove) && pieceOnTarget != Piece.NONE) break;
            }
        }
    }

    public static void GenerateKnightMoves (int startSquare){ //NOSONAR
        if ((startSquare - 1) % 8 != 0 && startSquare % 8 != 0)
        {
            if (startSquare < 56)
            {
                var targetSquare = startSquare + 6;
                int pieceOnTarget = Board.GetSquare()[targetSquare];
                if (!Piece.IsColour(pieceOnTarget, Board.colorToMove)) moves.add(new Move(startSquare, targetSquare));
            }

            if (startSquare > 7)
            {
                var targetSquare = startSquare - 10;
                int pieceOnTarget = Board.GetSquare()[targetSquare];
                if (!Piece.IsColour(pieceOnTarget, Board.colorToMove)) moves.add(new Move(startSquare, targetSquare));
            }
        }

        if (startSquare < 48)
        {
            if (startSquare % 8 != 0)
            {
                var targetSquare = startSquare + 15;
                int pieceOnTarget = Board.GetSquare()[targetSquare];
                if (!Piece.IsColour(pieceOnTarget, Board.colorToMove)) moves.add(new Move(startSquare, targetSquare));
            }

            if ((startSquare - 7) % 8 != 0)
            {
                var targetSquare = startSquare + 17;
                int pieceOnTarget = Board.GetSquare()[targetSquare];
                if (!Piece.IsColour(pieceOnTarget, Board.colorToMove)) moves.add(new Move(startSquare, targetSquare));
            }
        }

        if ((startSquare - 6) % 8 != 0 && (startSquare - 7) % 8 != 0)
        {
            if (startSquare < 56)
            {
                var targetSquare = startSquare + 10;
                int pieceOnTarget = Board.GetSquare()[targetSquare];
                if (!Piece.IsColour(pieceOnTarget, Board.colorToMove)) moves.add(new Move(startSquare, targetSquare));
            }

            if (startSquare > 7)
            {
                var targetSquare = startSquare - 6;
                int pieceOnTarget = Board.GetSquare()[targetSquare];
                if (!Piece.IsColour(pieceOnTarget, Board.colorToMove)) moves.add(new Move(startSquare, targetSquare));
            }
        }

        if (startSquare > 15)
        {
            if (startSquare % 8 != 0)
            {
                var targetSquare = startSquare - 17;
                int pieceOnTarget = Board.GetSquare()[targetSquare];
                if (!Piece.IsColour(pieceOnTarget, Board.colorToMove)) moves.add(new Move(startSquare, targetSquare));
            }

            if ((startSquare - 7) % 8 != 0)
            {
                var targetSquare = startSquare - 15;
                int pieceOnTarget = Board.GetSquare()[targetSquare];
                if (!Piece.IsColour(pieceOnTarget, Board.colorToMove)) moves.add(new Move(startSquare, targetSquare));
            }
        }
    }

    public static void GeneratePawnMoves (int startSquare) {
        if (Board.colorToMove == Piece.WHITE){
            int target = startSquare + 8;
            int piece = Board.GetSquare()[target];

            if (piece == Piece.NONE) {
                if (target >= 56) GeneratePromotionMoves(startSquare, target, Piece.WHITE);
                else moves.add(new Move(startSquare, target));
                if (startSquare >= 8 && startSquare <= 15 && Board.GetSquare()[target + 8] == Piece.NONE) moves.add(new Move(startSquare, target + 8));
            }

            GeneratePawnCaptures(startSquare, target);
        }

        if (Board.colorToMove == Piece.BLACK){
            int target = startSquare - 8;
            int piece = Board.GetSquare()[target];

            if (piece == Piece.NONE) {
                if (target <= 7) GeneratePromotionMoves(startSquare, target, Piece.BLACK);
                else moves.add(new Move(startSquare, target));
                if (startSquare >= 48 && startSquare <= 55 && Board.GetSquare()[target - 8] == Piece.NONE) moves.add(new Move(startSquare, target - 8));
            }

            GeneratePawnCaptures(startSquare, target);
        }
    }

    public static void GenerateKingMoves (int startSquare) {
        for (int dir = 0; dir < 8; dir++){
            int target = startSquare + PrecomputedMoveData.DirectionOffsets[dir];
            if (target < 0 || target > 63) continue;

            if (((startSquare - 7) % 8 == 0 && (PrecomputedMoveData.DirectionOffsets[dir] == 9 || PrecomputedMoveData.DirectionOffsets[dir] == 1
                    || PrecomputedMoveData.DirectionOffsets[dir] == -7)) ||
             (startSquare % 8 == 0 && (PrecomputedMoveData.DirectionOffsets[dir] == -9 || PrecomputedMoveData.DirectionOffsets[dir] == -1
                    || PrecomputedMoveData.DirectionOffsets[dir] == 7)) ||
                (startSquare >= 56 && (PrecomputedMoveData.DirectionOffsets[dir] == 9 || PrecomputedMoveData.DirectionOffsets[dir] == 8
                    || PrecomputedMoveData.DirectionOffsets[dir] == 7)) ||
            (startSquare <= 7 && (PrecomputedMoveData.DirectionOffsets[dir] == -9 || PrecomputedMoveData.DirectionOffsets[dir] == -8
                    || PrecomputedMoveData.DirectionOffsets[dir] == -7))) continue;


            int piece = Board.GetSquare()[target];

            if (Piece.IsColour(piece, Board.colorToMove)) continue;

            moves.add(new Move(startSquare, target));
        }

        if (Piece.IsColour(Board.GetSquare()[startSquare], Piece.WHITE)
                && Board.wKingsideCastle
                && startSquare == 4
                && Board.GetSquare()[startSquare + 1] == Piece.NONE
                && Board.GetSquare()[startSquare + 2] == Piece.NONE
                && Piece.PieceChecker(Board.GetSquare()[startSquare + 3], Piece.ROOK, Piece.WHITE)
                && !Board.IsSquareAttacked(startSquare, Board.opponentColor)
                && !Board.IsSquareAttacked(startSquare + 1, Board.opponentColor)
                && !Board.IsSquareAttacked(startSquare + 2, Board.opponentColor))
            moves.add(new Move(startSquare, startSquare + 2));

        if (Piece.IsColour(Board.GetSquare()[startSquare], Piece.BLACK)
                && Board.bKingsideCastle
                && startSquare == 60
                && Board.GetSquare()[startSquare + 1] == Piece.NONE
                && Board.GetSquare()[startSquare + 2] == Piece.NONE
                && Piece.PieceChecker(Board.GetSquare()[startSquare + 3], Piece.ROOK, Piece.BLACK)
                && !Board.IsSquareAttacked(startSquare, Board.opponentColor)
                && !Board.IsSquareAttacked(startSquare + 1, Board.opponentColor)
                && !Board.IsSquareAttacked(startSquare + 2, Board.opponentColor))
            moves.add(new Move(startSquare, startSquare + 2));

        if (Piece.IsColour(Board.GetSquare()[startSquare], Piece.WHITE)
                && Board.wQueensideCastle
                && startSquare == 4
                && Board.GetSquare()[startSquare - 1] == Piece.NONE
                && Board.GetSquare()[startSquare - 2] == Piece.NONE
                && Board.GetSquare()[startSquare - 3] == Piece.NONE
                && Piece.PieceChecker(Board.GetSquare()[startSquare - 4], Piece.ROOK, Piece.WHITE)
                && !Board.IsSquareAttacked(startSquare, Board.opponentColor)
                && !Board.IsSquareAttacked(startSquare - 1, Board.opponentColor)
                && !Board.IsSquareAttacked(startSquare - 2, Board.opponentColor))
            moves.add(new Move(startSquare, startSquare - 2));

        if (Piece.IsColour(Board.GetSquare()[startSquare], Piece.BLACK)
                && Board.bQueensideCastle
                && startSquare == 60
                && !Board.IsSquareAttacked(startSquare, Board.opponentColor)
                && !Board.IsSquareAttacked(startSquare - 1, Board.opponentColor)
                && !Board.IsSquareAttacked(startSquare - 2, Board.opponentColor)
                && Board.GetSquare()[startSquare - 1] == Piece.NONE
                && Board.GetSquare()[startSquare - 2] == Piece.NONE
                && Board.GetSquare()[startSquare - 3] == Piece.NONE
                && Piece.PieceChecker(Board.GetSquare()[startSquare - 4], Piece.ROOK, Piece.BLACK))
            moves.add(new Move(startSquare, startSquare - 2));
    }

    public static void GeneratePawnCaptures (int startSquare, int target) {
        if (target + 1 < 64 && (target - 7) % 8 != 0 && Board.GetSquare()[target + 1] != Piece.NONE &&
                Piece.IsColour(Board.GetSquare()[target + 1], Board.opponentColor))
        {
            if (target + 1 >= 56 && (target - 7) % 8 != 0) GeneratePromotionMoves(startSquare, target + 1, Piece.WHITE);
            else if (target - 1 <= 7 && target % 8 != 0) GeneratePromotionMoves(startSquare, target - 1, Piece.BLACK);
            else moves.add(new Move(startSquare, target + 1));
        }
        if (target - 1 >= 0 && target % 8 != 0 && Board.GetSquare()[target - 1] != Piece.NONE &&
                Piece.IsColour(Board.GetSquare()[target - 1], Board.opponentColor))
        {
            if (target + 1 >= 56 && (target - 7) % 8 != 0) GeneratePromotionMoves(startSquare, target + 1, Piece.WHITE);
            else if (target - 1 <= 7 && target % 8 != 0) GeneratePromotionMoves(startSquare, target - 1, Piece.BLACK);
            else moves.add(new Move(startSquare, target - 1));
        }

        if (target + 1 < 64 && (target - 7) % 8 != 0  && Board.enPassantSquare[startSquare + 1]) moves.add(new Move(startSquare, target + 1));
        if (target - 1 >= 0 && target % 8 != 0  && Board.enPassantSquare[startSquare - 1]) moves.add(new Move(startSquare, target - 1));
    }

    public static void GeneratePromotionMoves(int startSquare, int target, int color){
        moves.add(new Move(startSquare, target, Piece.ROOK | color));
        moves.add(new Move(startSquare, target, Piece.KNIGHT | color));
        moves.add(new Move(startSquare, target, Piece.BISHOP | color));
        moves.add(new Move(startSquare, target, Piece.QUEEN | color));
    }

    public static int MoveGenerationTest(int depth, float delay) {
        if (depth == 0) return 1;

        boolean first = false;

        List<Move> moves = MoveGenerator.GenerateLegalMoves();

        Set<Move> set = new HashSet<>(moves);
        moves.clear();
        moves.addAll(set);

        int numPositions = 0;

        for (Move move : moves){
            if (!firstIteration) {
                firstIteration = true;
                first = true;
            }

            Board.MakeMove(move);

            if (delay != 0){
                try {
                    Thread.sleep((long) (delay * 1000));
                } catch (InterruptedException e) {
                    LOGGER.log(Level.WARNING, "Interrupted!", e);
                    Thread.currentThread().interrupt();
                }
            }

            numPositions += MoveGenerationTest(depth - 1, delay);
            Board.UnmakeMove();

            if (first){
                int nodes = (numPositions - totalNodes);
                totalNodes += nodes;
                divide.append(Piece.PosFromIndex(move.startSquare))
                        .append(Piece.PosFromIndex(move.targetSquare))
                        .append(": ")
                        .append(nodes)
                        .append("\n");
                firstIteration = false;
                first = false;
            }
        }

        return numPositions;
    }

    //ignore
    public static void Reset(){
        divide.delete(0, divide.length() - 1);
        totalNodes = 0;
    }
    //endignore

    public static List<Move> GetMoves(){
        return moves;
    }

    public static void ClearMoves(){
        moves = new ArrayList<>();
    }
}
