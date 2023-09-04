package Classes;

import java.util.ArrayList;

public class MoveGenerator {

    //region Variables
    private static ArrayList<Move> moves;

    private static boolean firstIteration;
    private static int totalNodes = 0;

    public static StringBuilder divide = new StringBuilder();
    //endregion

    private static ArrayList<Move> GenerateMoves(){
        moves = new ArrayList<>();

        for (int startSquare = 0; startSquare < 64; startSquare++){
            int piece = Board.Square[startSquare];

            if (piece == Piece.None) continue;

            if (Piece.IsColour(piece, Board.ColorToMove)){
                switch (Piece.PieceType(piece)){
                    case Piece.Bishop, Piece.Rook, Piece.Queen -> GenerateSlidingMoves(startSquare, piece);
                    case Piece.Knight -> GenerateKnightMoves(startSquare);
                    case Piece.Pawn -> GeneratePawnMoves(startSquare);
                    case Piece.King -> GenerateKingMoves(startSquare);
                }
            }
        }

        return moves;
    }

    public static ArrayList<Move> GenerateLegalMoves(){
        ArrayList<Move> pseudoLegalMoves = GenerateMoves();
        ArrayList<Move> legalMoves = new ArrayList<>();

        int kingSquare = 0;

        for (Move moveToVerify : pseudoLegalMoves) {
            Board.MakeMove(moveToVerify);
            ArrayList<Move> opponentResponses = GenerateMoves();

            for (int startSquare = 0; startSquare < 64; startSquare++){
                int piece = Board.Square[startSquare];
                if (Piece.PieceChecker(piece, Piece.King, Board.OpponentColor)) kingSquare = startSquare;
            }

            boolean anyResponseMatches = false;
            for (Move response : opponentResponses) {
                if (response.TargetSquare == kingSquare) {
                    anyResponseMatches = true;
                    break;
                }
            }

            if (!anyResponseMatches) legalMoves.add(moveToVerify);

            Board.UnmakeMove();
        }

        return legalMoves;
    }

    private static void GenerateSlidingMoves (int start, int piece){
        int startDirIndex = Piece.PieceType(piece) == Piece.Bishop ? 4: 0;
        int endDirIndex = Piece.PieceType(piece) == Piece.Rook ? 4: 8;

        for (int dirIndex = startDirIndex; dirIndex < endDirIndex; dirIndex++){
            for (int n = 0; n < PrecomputedMoveData.NumSquaresToEdge[start][dirIndex]; n++){
                int target = start + PrecomputedMoveData.DirectionOffsets[dirIndex] * (n + 1);
                int pieceOnTarget = Board.Square[target];

                if (Piece.IsColour(pieceOnTarget, Board.ColorToMove)) break;

                moves.add(new Move(start, target));

                if (!Piece.IsColour(pieceOnTarget, Board.ColorToMove) && pieceOnTarget != Piece.None) break;
            }
        }
    }

    private static void GenerateKnightMoves (int startSquare){
        if ((startSquare - 1) % 8 != 0 && startSquare % 8 != 0)
        {
            if (startSquare < 56)
            {
                var targetSquare = startSquare + 6;
                int pieceOnTarget = Board.Square[targetSquare];
                if (!Piece.IsColour(pieceOnTarget, Board.ColorToMove)) moves.add(new Move(startSquare, targetSquare));
            }

            if (startSquare > 7)
            {
                var targetSquare = startSquare - 10;
                int pieceOnTarget = Board.Square[targetSquare];
                if (!Piece.IsColour(pieceOnTarget, Board.ColorToMove)) moves.add(new Move(startSquare, targetSquare));
            }
        }

        if (startSquare < 48)
        {
            if (startSquare % 8 != 0)
            {
                var targetSquare = startSquare + 15;
                int pieceOnTarget = Board.Square[targetSquare];
                if (!Piece.IsColour(pieceOnTarget, Board.ColorToMove)) moves.add(new Move(startSquare, targetSquare));
            }

            if ((startSquare - 7) % 8 != 0)
            {
                var targetSquare = startSquare + 17;
                int pieceOnTarget = Board.Square[targetSquare];
                if (!Piece.IsColour(pieceOnTarget, Board.ColorToMove)) moves.add(new Move(startSquare, targetSquare));
            }
        }

        if ((startSquare - 6) % 8 != 0 && (startSquare - 7) % 8 != 0)
        {
            if (startSquare < 56)
            {
                var targetSquare = startSquare + 10;
                int pieceOnTarget = Board.Square[targetSquare];
                if (!Piece.IsColour(pieceOnTarget, Board.ColorToMove)) moves.add(new Move(startSquare, targetSquare));
            }

            if (startSquare > 7)
            {
                var targetSquare = startSquare - 6;
                int pieceOnTarget = Board.Square[targetSquare];
                if (!Piece.IsColour(pieceOnTarget, Board.ColorToMove)) moves.add(new Move(startSquare, targetSquare));
            }
        }

        if (startSquare > 15)
        {
            if (startSquare % 8 != 0)
            {
                var targetSquare = startSquare - 17;
                int pieceOnTarget = Board.Square[targetSquare];
                if (!Piece.IsColour(pieceOnTarget, Board.ColorToMove)) moves.add(new Move(startSquare, targetSquare));
            }

            if ((startSquare - 7) % 8 != 0)
            {
                var targetSquare = startSquare - 15;
                int pieceOnTarget = Board.Square[targetSquare];
                if (!Piece.IsColour(pieceOnTarget, Board.ColorToMove)) moves.add(new Move(startSquare, targetSquare));
            }
        }
    }

    private static void GeneratePawnMoves (int startSquare) {
        if (Board.ColorToMove == Piece.White){
            int target = startSquare + 8;
            int piece = Board.Square[target];

            if (piece == Piece.None) {
                if (target >= 56) GeneratePromotionMoves(startSquare, target, Piece.White);
                else moves.add(new Move(startSquare, target));
                if (startSquare >= 8 && startSquare <= 15 && Board.Square[target + 8] == Piece.None) moves.add(new Move(startSquare, target + 8));
            }

            GeneratePawnCaptures(startSquare, target);
        }

        if (Board.ColorToMove == Piece.Black){
            int target = startSquare - 8;
            int piece = Board.Square[target];

            if (piece == Piece.None) {
                if (target <= 7) GeneratePromotionMoves(startSquare, target, Piece.Black);
                else moves.add(new Move(startSquare, target));
                if (startSquare >= 48 && startSquare <= 55 && Board.Square[target - 8] == Piece.None) moves.add(new Move(startSquare, target - 8));
            }

            GeneratePawnCaptures(startSquare, target);
        }
    }

    private static void GeneratePromotionMoves(int startSquare, int target, int color){
        moves.add(new Move(startSquare, target, Piece.Rook | color));
        moves.add(new Move(startSquare, target, Piece.Knight | color));
        moves.add(new Move(startSquare, target, Piece.Bishop | color));
        moves.add(new Move(startSquare, target, Piece.Queen | color));
    }

    private static void GenerateKingMoves (int startSquare) {
        for (int dir = 0; dir < 8; dir++){
            int target = startSquare + PrecomputedMoveData.DirectionOffsets[dir];
            if (target < 0 || target > 63) continue;

            if ((startSquare - 7) % 8 == 0) {
                if (PrecomputedMoveData.DirectionOffsets[dir] == 9 || PrecomputedMoveData.DirectionOffsets[dir] == 1 || PrecomputedMoveData.DirectionOffsets[dir] == -7)
                    continue;
            }

            if (startSquare % 8 == 0) {
                if (PrecomputedMoveData.DirectionOffsets[dir] == -9 || PrecomputedMoveData.DirectionOffsets[dir] == -1 || PrecomputedMoveData.DirectionOffsets[dir] == 7)
                    continue;
            }

            if (startSquare >= 56) {
                if (PrecomputedMoveData.DirectionOffsets[dir] == 9 || PrecomputedMoveData.DirectionOffsets[dir] == 8 || PrecomputedMoveData.DirectionOffsets[dir] == 7)
                    continue;
            }

            if (startSquare <= 7) {
                if (PrecomputedMoveData.DirectionOffsets[dir] == -9 || PrecomputedMoveData.DirectionOffsets[dir] == -8 || PrecomputedMoveData.DirectionOffsets[dir] == -7)
                    continue;
            }

            int piece = Board.Square[target];

            if (Piece.IsColour(piece, Board.ColorToMove)) continue;

            moves.add(new Move(startSquare, target));
        }

        if (Piece.IsColour(Board.Square[startSquare], Piece.White)
                && Board.WKingsideCastle
                && Board.Square[startSquare + 1] == Piece.None
                && Board.Square[startSquare + 2] == Piece.None
                && Piece.PieceChecker(Board.Square[startSquare + 3], Piece.Rook, Piece.White)
                && !Board.isSquareAttacked(startSquare + 1, Board.OpponentColor)
                && !Board.isSquareAttacked(startSquare + 2, Board.OpponentColor))
            moves.add(new Move(startSquare, startSquare + 2));

        if (Piece.IsColour(Board.Square[startSquare], Piece.Black)
                && Board.BKingsideCastle
                && Board.Square[startSquare + 1] == Piece.None
                && Board.Square[startSquare + 2] == Piece.None
                && Piece.PieceChecker(Board.Square[startSquare + 3], Piece.Rook, Piece.Black)
                && !Board.isSquareAttacked(startSquare + 1, Board.OpponentColor)
                && !Board.isSquareAttacked(startSquare + 2, Board.OpponentColor))
            moves.add(new Move(startSquare, startSquare + 2));

        if (Piece.IsColour(Board.Square[startSquare], Piece.White)
                && Board.WQueensideCastle
                && Board.Square[startSquare - 1] == Piece.None
                && Board.Square[startSquare - 2] == Piece.None
                && Board.Square[startSquare - 3] == Piece.None
                && Piece.PieceChecker(Board.Square[startSquare - 4], Piece.Rook, Piece.White)
                && !Board.isSquareAttacked(startSquare - 1, Board.OpponentColor)
                && !Board.isSquareAttacked(startSquare - 2, Board.OpponentColor))
            moves.add(new Move(startSquare, startSquare - 2));

        if (Piece.IsColour(Board.Square[startSquare], Piece.Black)
                && Board.BQueensideCastle
                && !Board.isSquareAttacked(startSquare - 1, Board.OpponentColor)
                && !Board.isSquareAttacked(startSquare - 2, Board.OpponentColor)
                && Board.Square[startSquare - 1] == Piece.None
                && Board.Square[startSquare - 2] == Piece.None
                && Board.Square[startSquare - 3] == Piece.None
                && Piece.PieceChecker(Board.Square[startSquare - 4], Piece.Rook, Piece.Black))
            moves.add(new Move(startSquare, startSquare - 2));
    }

    private static void GeneratePawnCaptures (int startSquare, int target) {
        if (target + 1 < 64 && (target - 7) % 8 != 0 && Board.Square[target + 1] != Piece.None && Piece.IsColour(Board.Square[target + 1], Board.OpponentColor))
        {
            if (target + 1 >= 56) GeneratePromotionMoves(startSquare, target + 1, Piece.White);
            else if (target - 1 <= 7) GeneratePromotionMoves(startSquare, target + 1, Piece.Black);
            else moves.add(new Move(startSquare, target + 1));
        }
        if (target - 1 >= 0 && target % 8 != 0 && Board.Square[target - 1] != Piece.None && Piece.IsColour(Board.Square[target - 1], Board.OpponentColor))
        {
            if (target + 1 >= 56) GeneratePromotionMoves(startSquare, target - 1, Piece.White);
            else if (target - 1 <= 7) GeneratePromotionMoves(startSquare, target - 1, Piece.Black);
            else moves.add(new Move(startSquare, target - 1));
        }

        if (target + 1 < 64 && (target - 7) % 8 != 0  && Board.EnPassantSquare[startSquare + 1]) moves.add(new Move(startSquare, target + 1));
        if (target - 1 >= 0 && target % 8 != 0  && Board.EnPassantSquare[startSquare - 1]) moves.add(new Move(startSquare, target - 1));
    }

    public static int MoveGenerationTest(int depth, float delay) {
        if (depth == 0) return 1;

        boolean first = false;

        ArrayList<Move> moves = MoveGenerator.GenerateLegalMoves();
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
                    e.printStackTrace();
                }
            }

            numPositions += MoveGenerationTest(depth - 1, delay);
            Board.UnmakeMove();

            if (first){
                int nodes = (numPositions - totalNodes);
                totalNodes += nodes;
                divide.append(Piece.PosFromIndex(move.StartSquare))
                        .append(" to ")
                        .append(Piece.PosFromIndex(move.TargetSquare))
                        .append(": ")
                        .append(nodes)
                        .append("\n");
                firstIteration = false;
                first = false;
            }
        }

        return numPositions;
    }

    public static void Reset(){
        divide = new StringBuilder();
        totalNodes = 0;
    }
}
