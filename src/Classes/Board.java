package Classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("RedundantIfStatement")
public class Board {

    //region Constructor
    private Board() {
        throw new IllegalStateException("Utility class");
    }
    //endregion

    //region Variables
    private static final Map<String, Object>[] Squares = new HashMap[64];

    private static int[] square = new int[64];
    public static boolean[] enPassantSquare = new boolean[64]; //NOSONAR

    public static final List<int[]> prevSquares = new ArrayList<>();
    public static ArrayList<boolean[]> prevEnPassantSquares = new ArrayList<>(); //NOSONAR
    
    public static final List<Integer> prevColorToMove = new ArrayList<>();
    
    public static final List<Boolean> prevWKCastling = new ArrayList<>();
    public static final List<Boolean> prevWQCastling = new ArrayList<>();
    public static final List<Boolean> prevBKCastling = new ArrayList<>();
    public static final List<Boolean> prevBQCastling = new ArrayList<>();

    public static int colorToMove = Piece.WHITE; //NOSONAR
    public static int opponentColor = Piece.BLACK; //NOSONAR

    public static boolean wKingsideCastle = true; //NOSONAR
    public static boolean wQueensideCastle = true; //NOSONAR
    public static boolean bKingsideCastle = true; //NOSONAR
    public static boolean bQueensideCastle = true; //NOSONAR
    //endregion

    //region Methods
    public static Map<String, Object>[] GetSquares () {
        return Squares;
    }

    public static void MakeMove(Move move){
        //Save the board position before making the move
        prevSquares.add(square.clone());
        prevEnPassantSquares.add(enPassantSquare.clone());

        int newColorToMove = colorToMove;
        prevColorToMove.add(newColorToMove);

        boolean newWKingsideCastle = wKingsideCastle;
        prevWKCastling.add(newWKingsideCastle);

        boolean newBKingsideCastle = bKingsideCastle;
        prevBKCastling.add(newBKingsideCastle);

        boolean newWQueensideCastle = wQueensideCastle;
        prevWQCastling.add(newWQueensideCastle);

        boolean newBQueensideCastle = bQueensideCastle;
        prevBQCastling.add(newBQueensideCastle);

        //Set pieceToMove to the piece being moved
        int pieceToMove = square[move.startSquare];

        Castling(pieceToMove, move);

        EnPassant(pieceToMove, move);

        for (int i = 0; i < 64; i++) enPassantSquare[i] = false;

        if (Piece.PieceChecker(pieceToMove, Piece.PAWN) && (move.startSquare + 16 == move.targetSquare || move.startSquare - 16 == move.targetSquare))
            enPassantSquare[move.targetSquare] = true;

        //Set the target square to the piece and remove it from the starting square
        square[move.targetSquare] = square[move.startSquare];
        square[move.startSquare] = 0;

        //Promotion
        if (move.promoPiece != Piece.NONE){
            Board.square[move.targetSquare] = move.promoPiece;
        }

        //Swap the color to move
        colorToMove = colorToMove == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        opponentColor = colorToMove == Piece.WHITE ? Piece.BLACK : Piece.WHITE;

        //Castling checks
        if (Piece.PieceChecker(pieceToMove, Piece.KING, Piece.WHITE)) {
            wKingsideCastle = false;
            wQueensideCastle = false;
        }
        if (Piece.PieceChecker(pieceToMove, Piece.KING, Piece.BLACK)) {
            bKingsideCastle = false;
            bQueensideCastle = false;
        }
    }

    public static void UnmakeMove(){
        if (prevSquares.isEmpty()) return;

        square = prevSquares.get(prevSquares.size() - 1);
        prevSquares.remove(prevSquares.size() - 1);

        enPassantSquare = prevEnPassantSquares.get(prevEnPassantSquares.size() - 1);
        prevEnPassantSquares.remove(prevEnPassantSquares.size() - 1);

        colorToMove = prevColorToMove.get(prevColorToMove.size() - 1);
        prevColorToMove.remove(prevColorToMove.size() - 1);

        wKingsideCastle = prevWKCastling.get(prevWKCastling.size() - 1);
        bKingsideCastle = prevBKCastling.get(prevBKCastling.size() - 1);
        wQueensideCastle = prevWQCastling.get(prevWQCastling.size() - 1);
        bQueensideCastle = prevBQCastling.get(prevBQCastling.size() - 1);

        prevWKCastling.remove(prevWKCastling.size() - 1);
        prevBKCastling.remove(prevBKCastling.size() - 1);
        prevWQCastling.remove(prevWQCastling.size() - 1);
        prevBQCastling.remove(prevBQCastling.size() - 1);
    }
    //endregion

    //region Constructor
    public static void Init(){
        int index = 0;

        for (int column = 7; column >= 0; column--){
            for (int row = 0; row < 8; row++){
                Squares[index] = new HashMap<>();

                boolean isLight = (column + row) % 2 == 0;

                if (isLight) {
                    Squares[index].put("x", row * 100);
                    Squares[index].put("y", column * 100);
                    Squares[index].put("w", 100);
                    Squares[index].put("h", 100);
                    Squares[index].put("r", 232);
                    Squares[index].put("g", 209);
                    Squares[index].put("b", 184);
                    Squares[index].put("mr", 217);
                    Squares[index].put("mg", 66);
                    Squares[index].put("mb", 78);
                }
                else {
                    Squares[index].put("x", row * 100);
                    Squares[index].put("y", column * 100);
                    Squares[index].put("w", 100);
                    Squares[index].put("h", 100);
                    Squares[index].put("r", 121);
                    Squares[index].put("g", 83);
                    Squares[index].put("b", 73);
                    Squares[index].put("mr", 173);
                    Squares[index].put("mg", 43);
                    Squares[index].put("mb", 50);
                }

                index++;
            }
        }
    }

    public static boolean IsSquareAttacked (int startSquare, int attackingColor) {
        if (Board.square[startSquare] != Piece.NONE && Piece.IsColour(Board.square[startSquare], attackingColor)) return false;

        // Check if the square is attacked by a pawn
        if (AttackedByPawn(startSquare, attackingColor)) return true;

        // Check if the square is attacked by a knight
        if (AttackedByKnight(startSquare, attackingColor)) return true;

        // Check if the square is attacked by a bishop or rook or queen
        if (AttackedBySlidingPiece(startSquare, attackingColor)) return true;

        // Check if the square is attacked by a king
        if (AttackedByKing(startSquare, attackingColor)) return true; //NOSONAR

        // If none of the above conditions were met, the square is not attacked
        return false;
    }



    public static String WhatIsSquareAttackedBy (int startSquare, int attackingColor) {
        if (Board.square[startSquare] != Piece.NONE && Piece.IsColour(Board.square[startSquare], attackingColor)) return "None";

        // Check if the square is attacked by a pawn
        String pawn = PawnAttackerChecker(startSquare, attackingColor);
        if (pawn != null) return pawn;

        // Check if the square is attacked by a knight
        String knight = KnightAttackerChecker(startSquare, attackingColor);
        if (knight != null) return knight;

        // Check if the square is attacked by a bishop or queen
        String sliding = SlidingAttackerChecker(startSquare, attackingColor);
        if (sliding != null) return sliding;

        // Check if the square is attacked by a king
        String king = KingAttackerChecker(startSquare, attackingColor);
        if (king != null) return king;

        // If none of the above conditions were met, the square is not attacked
        return "None";
    }



    public static int FromWhereIsTheSquareAttacked (int startSquare, int attackingColor) {
        if (Board.square[startSquare] != Piece.NONE && Piece.IsColour(Board.square[startSquare], attackingColor)) return -1;

        // Check if the square is attacked by a pawn
        Integer target1 = PawnAttackLocation(startSquare, attackingColor);
        if (target1 != null) return target1;

        // Check if the square is attacked by a knight
        Integer target2 = KnightAttackLocation(startSquare, attackingColor);
        if (target2 != null) return target2;

        // Check if the square is attacked by a bishop or rook or queen
        Integer target3 = SlidingAttackLocation(startSquare, attackingColor);
        if (target3 != null) return target3;

        // Check if the square is attacked by a king
        Integer target4 = KingAttackLocation(startSquare, attackingColor);
        if (target4 != null) return target4;

        // If none of the above conditions were met, the square is not attacked
        return -1;
    }

    //endregion

    //region Convenience

    //region Special Moves
    private static void Castling(int pieceToMove, Move move){
        //Perform castling
        if (Piece.PieceChecker(pieceToMove, Piece.KING)) {
            if (move.startSquare + 2 == move.targetSquare) {
                square[move.startSquare + 1] = square[move.targetSquare + 1];
                square[move.targetSquare + 1] = Piece.NONE;

                if (colorToMove == Piece.WHITE) {
                    wKingsideCastle = false;
                    wQueensideCastle = false;
                }
                else {
                    bKingsideCastle = false;
                    bQueensideCastle = false;
                }
            } else if (move.startSquare - 2 == move.targetSquare) {
                square[move.startSquare - 1] = square[move.targetSquare - 2];
                square[move.targetSquare - 2] = Piece.NONE;

                if (colorToMove == Piece.WHITE) {
                    wKingsideCastle = false;
                    wQueensideCastle = false;
                }
                else {
                    bKingsideCastle = false;
                    bQueensideCastle = false;
                }
            }
        }
    }

    private static void EnPassant(int pieceToMove, Move move){
        //En-passant managing
        if (Piece.PieceChecker(pieceToMove, Piece.PAWN) &&
                (move.startSquare + 9 == move.targetSquare || move.startSquare + 7 == move.targetSquare) && enPassantSquare[move.targetSquare - 8])
            square[move.targetSquare - 8] = Piece.NONE;
        if (Piece.PieceChecker(pieceToMove, Piece.PAWN) &&
                (move.startSquare - 9 == move.targetSquare || move.startSquare - 7 == move.targetSquare) && enPassantSquare[move.targetSquare + 8])
            square[move.targetSquare + 8] = Piece.NONE;
    }
    //endregion

    //region Attacking Checks
    private static boolean AttackedByKing (int startSquare, int attackingColor) { //NOSONAR
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

            int piece = Board.square[target];

            if (piece == Piece.NONE || !Piece.IsColour(piece, attackingColor)) continue;
            if (Piece.PieceChecker(piece, Piece.KING, attackingColor)) return true;
        }
        return false;
    }

    private static boolean AttackedBySlidingPiece (int startSquare, int attackingColor) {
        for (int dirIndex = 4; dirIndex < 8; dirIndex++){
            for (int n = 0; n < PrecomputedMoveData.NumSquaresToEdge[startSquare][dirIndex]; n++){
                int target = startSquare + PrecomputedMoveData.DirectionOffsets[dirIndex] * (n + 1);
                int pieceOnTarget = Board.square[target];

                if (pieceOnTarget == Piece.NONE) continue;

                if (Piece.PieceChecker(pieceOnTarget, Piece.BISHOP, attackingColor) || Piece.PieceChecker(pieceOnTarget, Piece.QUEEN, attackingColor)) return true;

                break;
            }
        }

        for (int dirIndex = 0; dirIndex < 4; dirIndex++){
            for (int n = 0; n < PrecomputedMoveData.NumSquaresToEdge[startSquare][dirIndex]; n++){
                int target = startSquare + PrecomputedMoveData.DirectionOffsets[dirIndex] * (n + 1);
                int pieceOnTarget = Board.square[target];

                if (pieceOnTarget == Piece.NONE) continue;

                if (Piece.PieceChecker(pieceOnTarget, Piece.ROOK, attackingColor) || Piece.PieceChecker(pieceOnTarget, Piece.QUEEN, attackingColor)) return true;

                break;
            }
        }
        return false;
    }

    private static boolean AttackedByKnight (int startSquare, int attackingColor) { //NOSONAR
        if ((startSquare - 1) % 8 != 0 && startSquare % 8 != 0)
        {
            if (startSquare < 56)
            {
                var targetSquare = startSquare + 6;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return true;
            }

            if (startSquare > 7)
            {
                var targetSquare = startSquare - 10;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return true;
            }
        }

        if (startSquare < 48)
        {
            if (startSquare % 8 != 0)
            {
                var targetSquare = startSquare + 15;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return true;
            }

            if ((startSquare - 7) % 8 != 0)
            {
                var targetSquare = startSquare + 17;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return true;
            }
        }

        if ((startSquare - 6) % 8 != 0 && (startSquare - 7) % 8 != 0)
        {
            if (startSquare < 56)
            {
                var targetSquare = startSquare + 10;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return true;
            }

            if (startSquare > 7)
            {
                var targetSquare = startSquare - 6;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return true;
            }
        }

        if (startSquare > 15)
        {
            if (startSquare % 8 != 0)
            {
                var targetSquare = startSquare - 17;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return true;
            }

            if ((startSquare - 7) % 8 != 0)
            {
                var targetSquare = startSquare - 15;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return true;
            }
        }
        return false;
    }

    private static boolean AttackedByPawn (int startSquare, int attackingColor) {
        if (attackingColor == Piece.WHITE) {
            if (startSquare - 9 >= 0 && (startSquare % 8) != 0 && Board.square[startSquare - 9] == (attackingColor | Piece.PAWN)) {
                return true;
            }
            if (startSquare - 7 >= 0 && ((startSquare - 7) % 8) != 0 && Board.square[startSquare - 7] == (attackingColor | Piece.PAWN)) {
                return true;
            }
        } else {
            if (startSquare + 7 <= 63 && (startSquare % 8) != 0 && Board.square[startSquare + 7] == (attackingColor | Piece.PAWN)) {
                return true;
            }
            if (startSquare + 9 <= 63 && ((startSquare - 7) % 8) != 0 && Board.square[startSquare + 9] == (attackingColor | Piece.PAWN)) {
                return true;
            }
        }
        return false;
    }
    //endregion

    //region Attacker Check
    private static String KingAttackerChecker (int startSquare, int attackingColor) { //NOSONAR
        for (int dir = 0; dir < 8; dir++){
            int target = startSquare + PrecomputedMoveData.DirectionOffsets[dir];
            if (target < 0 || target > 63) continue;

            if ((startSquare - 7) % 8 == 0 && (PrecomputedMoveData.DirectionOffsets[dir] == 9 || PrecomputedMoveData.DirectionOffsets[dir] == 1
                    || PrecomputedMoveData.DirectionOffsets[dir] == -7) ||
            (startSquare % 8 == 0 && (PrecomputedMoveData.DirectionOffsets[dir] == -9 || PrecomputedMoveData.DirectionOffsets[dir] == -1
                    || PrecomputedMoveData.DirectionOffsets[dir] == 7)) ||
            (startSquare >= 56 && (PrecomputedMoveData.DirectionOffsets[dir] == 9 || PrecomputedMoveData.DirectionOffsets[dir] == 8
                    || PrecomputedMoveData.DirectionOffsets[dir] == 7)) ||
            (startSquare <= 7 && (PrecomputedMoveData.DirectionOffsets[dir] == -9 || PrecomputedMoveData.DirectionOffsets[dir] == -8
                    || PrecomputedMoveData.DirectionOffsets[dir] == -7))) continue;


            int piece = Board.square[target];

            if (piece == Piece.NONE || !Piece.IsColour(piece, attackingColor)) continue;
            if (Piece.PieceChecker(piece, Piece.KING, attackingColor)) return "King";
        }
        return null;
    }

    private static String SlidingAttackerChecker (int startSquare, int attackingColor) {
        for (int dirIndex = 4; dirIndex < 8; dirIndex++){
            for (int n = 0; n < PrecomputedMoveData.NumSquaresToEdge[startSquare][dirIndex]; n++){
                int target = startSquare + PrecomputedMoveData.DirectionOffsets[dirIndex] * (n + 1);
                int pieceOnTarget = Board.square[target];

                if (pieceOnTarget == Piece.NONE) continue;

                if (Piece.PieceChecker(pieceOnTarget, Piece.BISHOP, attackingColor) || Piece.PieceChecker(pieceOnTarget, Piece.QUEEN, attackingColor)) return "Bishop/Queen";

                break;
            }
        }

        for (int dirIndex = 0; dirIndex < 4; dirIndex++){
            for (int n = 0; n < PrecomputedMoveData.NumSquaresToEdge[startSquare][dirIndex]; n++){
                int target = startSquare + PrecomputedMoveData.DirectionOffsets[dirIndex] * (n + 1);
                int pieceOnTarget = Board.square[target];

                if (pieceOnTarget == Piece.NONE) continue;

                if (Piece.PieceChecker(pieceOnTarget, Piece.ROOK, attackingColor) || Piece.PieceChecker(pieceOnTarget, Piece.QUEEN, attackingColor)) return "Rook/Queen";

                break;
            }
        }
        return null;
    }

    private static String KnightAttackerChecker (int startSquare, int attackingColor) { //NOSONAR
        String knight = "Knight";

        if ((startSquare - 1) % 8 != 0 && startSquare % 8 != 0)
        {
            if (startSquare < 56)
            {
                var targetSquare = startSquare + 6;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return knight;
            }

            if (startSquare > 7)
            {
                var targetSquare = startSquare - 10;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return knight;
            }
        }

        if (startSquare < 48)
        {
            if (startSquare % 8 != 0)
            {
                var targetSquare = startSquare + 15;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return knight;
            }

            if ((startSquare - 7) % 8 != 0)
            {
                var targetSquare = startSquare + 17;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return knight;
            }
        }

        if ((startSquare - 6) % 8 != 0 && (startSquare - 7) % 8 != 0)
        {
            if (startSquare < 56)
            {
                var targetSquare = startSquare + 10;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return knight;
            }

            if (startSquare > 7)
            {
                var targetSquare = startSquare - 6;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return knight;
            }
        }

        if (startSquare > 15)
        {
            if (startSquare % 8 != 0)
            {
                var targetSquare = startSquare - 17;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return knight;
            }

            if ((startSquare - 7) % 8 != 0)
            {
                var targetSquare = startSquare - 15;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return knight;
            }
        }
        return null;
    }

    private static String PawnAttackerChecker (int startSquare, int attackingColor) {
        if (attackingColor == Piece.WHITE) {
            if (startSquare - 9 >= 0 && (startSquare % 8) != 0 && Board.square[startSquare - 9] == (attackingColor | Piece.PAWN)) {
                return "Pawn";
            }
            if (startSquare - 7 >= 0 && ((startSquare + 7) % 8) != 0 && Board.square[startSquare - 7] == (attackingColor | Piece.PAWN)) {
                return "Pawn";
            }
        } else {
            if (startSquare + 7 <= 63 && (startSquare % 8) != 0 && Board.square[startSquare + 7] == (attackingColor | Piece.PAWN)) {
                return "Pawn";
            }
            if (startSquare + 9 <= 63 && ((startSquare + 7) % 8) != 0 && Board.square[startSquare + 9] == (attackingColor | Piece.PAWN)) {
                return "Pawn";
            }
        }
        return null;
    }
    //endregion

    //region Checker Location
    private static Integer KingAttackLocation (int startSquare, int attackingColor) { //NOSONAR
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

            int piece = Board.square[target];

            if (piece == Piece.NONE || !Piece.IsColour(piece, attackingColor)) continue;
            if (Piece.PieceChecker(piece, Piece.KING, attackingColor)) return target;
        }
        return null;
    }

    private static Integer SlidingAttackLocation (int startSquare, int attackingColor) {
        for (int dirIndex = 4; dirIndex < 8; dirIndex++){
            for (int n = 0; n < PrecomputedMoveData.NumSquaresToEdge[startSquare][dirIndex]; n++){
                int target = startSquare + PrecomputedMoveData.DirectionOffsets[dirIndex] * (n + 1);
                int pieceOnTarget = Board.square[target];

                if (pieceOnTarget == Piece.NONE) continue;

                if (Piece.PieceChecker(pieceOnTarget, Piece.BISHOP, attackingColor) || Piece.PieceChecker(pieceOnTarget, Piece.QUEEN, attackingColor)) return target;

                break;
            }
        }

        for (int dirIndex = 0; dirIndex < 4; dirIndex++){
            for (int n = 0; n < PrecomputedMoveData.NumSquaresToEdge[startSquare][dirIndex]; n++){
                int target = startSquare + PrecomputedMoveData.DirectionOffsets[dirIndex] * (n + 1);
                int pieceOnTarget = Board.square[target];

                if (pieceOnTarget == Piece.NONE) continue;

                if (Piece.PieceChecker(pieceOnTarget, Piece.ROOK, attackingColor) || Piece.PieceChecker(pieceOnTarget, Piece.QUEEN, attackingColor)) return target;

                break;
            }
        }
        return null;
    }

    private static Integer KnightAttackLocation (int startSquare, int attackingColor) { //NOSONAR
        if ((startSquare - 1) % 8 != 0 && startSquare % 8 != 0)
        {
            if (startSquare < 56)
            {
                var targetSquare = startSquare + 6;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return startSquare + 6;
            }

            if (startSquare > 7)
            {
                var targetSquare = startSquare - 10;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return startSquare - 10;
            }
        }

        if (startSquare < 48)
        {
            if (startSquare % 8 != 0)
            {
                var targetSquare = startSquare + 15;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return startSquare + 15;
            }

            if ((startSquare - 7) % 8 != 0)
            {
                var targetSquare = startSquare + 17;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return startSquare + 17;
            }
        }

        if ((startSquare - 6) % 8 != 0 && (startSquare - 7) % 8 != 0)
        {
            if (startSquare < 56)
            {
                var targetSquare = startSquare + 10;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return startSquare + 10;
            }

            if (startSquare > 7)
            {
                var targetSquare = startSquare - 6;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return startSquare - 6;
            }
        }

        if (startSquare > 15)
        {
            if (startSquare % 8 != 0)
            {
                var targetSquare = startSquare - 17;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return startSquare - 17;
            }

            if ((startSquare - 7) % 8 != 0)
            {
                var targetSquare = startSquare - 15;
                int pieceOnTarget = Board.square[targetSquare];
                if (pieceOnTarget == (Piece.KNIGHT | attackingColor)) return startSquare - 15;
            }
        }
        return null;
    }

    private static Integer PawnAttackLocation (int startSquare, int attackingColor) {
        if (attackingColor == Piece.WHITE) {
            if (startSquare - 9 >= 0 && (startSquare % 8) != 0 && Board.square[startSquare - 9] == (attackingColor | Piece.PAWN)) {
                return startSquare - 9;
            }
            if (startSquare - 7 >= 0 && ((startSquare + 7) % 8) != 0 && Board.square[startSquare - 7] == (attackingColor | Piece.PAWN)) {
                return startSquare - 7;
            }
        } else {
            if (startSquare + 7 <= 63 && (startSquare % 8) != 0 && Board.square[startSquare + 7] == (attackingColor | Piece.PAWN)) {
                return startSquare + 7;
            }
            if (startSquare + 9 <= 63 && ((startSquare + 7) % 8) != 0 && Board.square[startSquare + 9] == (attackingColor | Piece.PAWN)) {
                return startSquare + 9;
            }
        }
        return null;
    }
    //endregion

    //endregion

    //region Getters and setters

    public static int[] GetSquare() {
        return square;
    }

    public static void SetSquare (int index, int value) {
        square[index] = value;
    }

    //endregion
}
