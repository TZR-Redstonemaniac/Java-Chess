package Classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {

    //region Variables
    private static final Map<String, Object>[] Squares = new HashMap[64];

    public static int[] Square = new int[64];
    public static boolean[] EnPassantSquare = new boolean[64];

    public static ArrayList<int[]> prevSquares = new ArrayList<>();
    public static ArrayList<boolean[]> prevEnPassantSquares = new ArrayList<>();
    
    public static ArrayList<Integer> prevColorToMove = new ArrayList<>();
    
    public static ArrayList<Boolean> prevWKCastling = new ArrayList<>();
    public static ArrayList<Boolean> prevWQCastling = new ArrayList<>();
    public static ArrayList<Boolean> prevBKCastling = new ArrayList<>();
    public static ArrayList<Boolean> prevBQCastling = new ArrayList<>();

    public static int ColorToMove = Piece.White;
    public static int OpponentColor = Piece.Black;

    public static boolean WKingsideCastle = true;
    public static boolean WQueensideCastle = true;
    public static boolean BKingsideCastle = true;
    public static boolean BQueensideCastle = true;
    //endregion

    //region Methods
    public static Map<String, Object>[] getSquares () {
        return Squares;
    }

    public static void MakeMove(Move move){
        //Save the board position before making the move
        prevSquares.add(Square.clone());
        prevEnPassantSquares.add(EnPassantSquare.clone());

        int newColorToMove = ColorToMove;
        prevColorToMove.add(newColorToMove);

        boolean newWKingsideCastle = WKingsideCastle;
        prevWKCastling.add(newWKingsideCastle);

        boolean newBKingsideCastle = BKingsideCastle;
        prevBKCastling.add(newBKingsideCastle);

        boolean newWQueensideCastle = WQueensideCastle;
        prevWQCastling.add(newWQueensideCastle);

        boolean newBQueensideCastle = BQueensideCastle;
        prevBQCastling.add(newBQueensideCastle);

        //Set pieceToMove to the piece being moved
        int pieceToMove = Square[move.StartSquare];

        //Perform castling
        if (Piece.PieceChecker(pieceToMove, Piece.King)) {
            if (move.StartSquare + 2 == move.TargetSquare) {
                Square[move.StartSquare + 1] = Square[move.TargetSquare + 1];
                Square[move.TargetSquare + 1] = Piece.None;

                if (ColorToMove == Piece.White) {
                    WKingsideCastle = false;
                    WQueensideCastle = false;
                }
                else {
                    BKingsideCastle = false;
                    BQueensideCastle = false;
                }
            } else if (move.StartSquare - 2 == move.TargetSquare) {
                Square[move.StartSquare - 1] = Square[move.TargetSquare - 2];
                Square[move.TargetSquare - 2] = Piece.None;

                if (ColorToMove == Piece.White) {
                    WKingsideCastle = false;
                    WQueensideCastle = false;
                }
                else {
                    BKingsideCastle = false;
                    BQueensideCastle = false;
                }
            }
        }

        //En-passant managing
        if (Piece.PieceChecker(pieceToMove, Piece.Pawn) &&
                (move.StartSquare + 9 == move.TargetSquare || move.StartSquare + 7 == move.TargetSquare) && EnPassantSquare[move.TargetSquare - 8])
            Square[move.TargetSquare - 8] = Piece.None;
        if (Piece.PieceChecker(pieceToMove, Piece.Pawn) &&
                (move.StartSquare - 9 == move.TargetSquare || move.StartSquare - 7 == move.TargetSquare) && EnPassantSquare[move.TargetSquare + 8])
            Square[move.TargetSquare + 8] = Piece.None;

        for (int i = 0; i < 64; i++) EnPassantSquare[i] = false;

        if (Piece.PieceChecker(pieceToMove, Piece.Pawn) && (move.StartSquare + 16 == move.TargetSquare || move.StartSquare - 16 == move.TargetSquare))
            EnPassantSquare[move.TargetSquare] = true;

        //Set the target square to the piece and remove it from the starting square
        Square[move.TargetSquare] = Square[move.StartSquare];
        Square[move.StartSquare] = 0;

        //Promotion
        if (move.promoPiece != Piece.None){
            Board.Square[move.TargetSquare] = move.promoPiece;
        }

        //Swap the color to move
        ColorToMove = ColorToMove == Piece.White ? Piece.Black : Piece.White;
        OpponentColor = ColorToMove == Piece.White ? Piece.Black : Piece.White;

        //Castling checks
        if (Piece.PieceChecker(pieceToMove, Piece.King, Piece.White)) {
            WKingsideCastle = false;
            WQueensideCastle = false;
        }
        if (Piece.PieceChecker(pieceToMove, Piece.King, Piece.Black)) {
            BKingsideCastle = false;
            BQueensideCastle = false;
        }
    }

    public static void UnmakeMove(){
        if (prevSquares.size() == 0) return;

        Square = prevSquares.get(prevSquares.size() - 1);
        prevSquares.remove(prevSquares.size() - 1);

        EnPassantSquare = prevEnPassantSquares.get(prevEnPassantSquares.size() - 1);
        prevEnPassantSquares.remove(prevEnPassantSquares.size() - 1);

        ColorToMove = prevColorToMove.get(prevColorToMove.size() - 1);
        prevColorToMove.remove(prevColorToMove.size() - 1);

        WKingsideCastle = prevWKCastling.get(prevWKCastling.size() - 1);
        BKingsideCastle = prevBKCastling.get(prevBKCastling.size() - 1);
        WQueensideCastle = prevWQCastling.get(prevWQCastling.size() - 1);
        BQueensideCastle = prevBQCastling.get(prevBQCastling.size() - 1);

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

    public static boolean isSquareAttacked(int startSquare, int attackingColor) {
        if (Board.Square[startSquare] != Piece.None && Piece.IsColour(Board.Square[startSquare], attackingColor)) return false;

        // Check if the square is attacked by a pawn
        if (attackingColor == Piece.White) {
            if (startSquare - 9 >= 0 && (startSquare % 8) != 0 && Board.Square[startSquare - 9] == (attackingColor | Piece.Pawn)) {
                return true;
            }
            if (startSquare - 7 >= 0 && ((startSquare - 7) % 8) != 0 && Board.Square[startSquare - 7] == (attackingColor | Piece.Pawn)) {
                return true;
            }
        } else {
            if (startSquare + 7 <= 63 && (startSquare % 8) != 0 && Board.Square[startSquare + 7] == (attackingColor | Piece.Pawn)) {
                return true;
            }
            if (startSquare + 9 <= 63 && ((startSquare - 7) % 8) != 0 && Board.Square[startSquare + 9] == (attackingColor | Piece.Pawn)) {
                return true;
            }
        }

        // Check if the square is attacked by a knight
        if ((startSquare - 1) % 8 != 0 && startSquare % 8 != 0)
        {
            if (startSquare < 56)
            {
                var targetSquare = startSquare + 6;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return true;
            }

            if (startSquare > 7)
            {
                var targetSquare = startSquare - 10;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return true;
            }
        }

        if (startSquare < 48)
        {
            if (startSquare % 8 != 0)
            {
                var targetSquare = startSquare + 15;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return true;
            }

            if ((startSquare - 7) % 8 != 0)
            {
                var targetSquare = startSquare + 17;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return true;
            }
        }

        if ((startSquare - 6) % 8 != 0 && (startSquare - 7) % 8 != 0)
        {
            if (startSquare < 56)
            {
                var targetSquare = startSquare + 10;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return true;
            }

            if (startSquare > 7)
            {
                var targetSquare = startSquare - 6;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return true;
            }
        }

        if (startSquare > 15)
        {
            if (startSquare % 8 != 0)
            {
                var targetSquare = startSquare - 17;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return true;
            }

            if ((startSquare - 7) % 8 != 0)
            {
                var targetSquare = startSquare - 15;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return true;
            }
        }

        // Check if the square is attacked by a bishop or queen
        for (int dirIndex = 4; dirIndex < 8; dirIndex++){
            for (int n = 0; n < PrecomputedMoveData.NumSquaresToEdge[startSquare][dirIndex]; n++){
                int target = startSquare + PrecomputedMoveData.DirectionOffsets[dirIndex] * (n + 1);
                int pieceOnTarget = Board.Square[target];

                if (pieceOnTarget == Piece.None) continue;

                if (Piece.PieceChecker(pieceOnTarget, Piece.Bishop, attackingColor) || Piece.PieceChecker(pieceOnTarget, Piece.Queen, attackingColor)) return true;

                break;
            }
        }

        // Check if the square is attacked by a rook or queen
        for (int dirIndex = 0; dirIndex < 4; dirIndex++){
            for (int n = 0; n < PrecomputedMoveData.NumSquaresToEdge[startSquare][dirIndex]; n++){
                int target = startSquare + PrecomputedMoveData.DirectionOffsets[dirIndex] * (n + 1);
                int pieceOnTarget = Board.Square[target];

                if (pieceOnTarget == Piece.None) continue;

                if (Piece.PieceChecker(pieceOnTarget, Piece.Rook, attackingColor) || Piece.PieceChecker(pieceOnTarget, Piece.Queen, attackingColor)) return true;

                break;
            }
        }

        // Check if the square is attacked by a king
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

            if (piece == Piece.None || !Piece.IsColour(piece, attackingColor)) continue;
            if (Piece.PieceChecker(piece, Piece.King, attackingColor)) return true;
        }

        // If none of the above conditions were met, the square is not attacked
        return false;
    }

    public static String whatIsSquareAttackedBy (int startSquare, int attackingColor) {
        if (Board.Square[startSquare] != Piece.None && Piece.IsColour(Board.Square[startSquare], attackingColor)) return "None";

        // Check if the square is attacked by a pawn
        if (attackingColor == Piece.White) {
            if (startSquare - 9 >= 0 && (startSquare % 8) != 0 && Board.Square[startSquare - 9] == (attackingColor | Piece.Pawn)) {
                return "Pawn";
            }
            if (startSquare - 7 >= 0 && ((startSquare + 7) % 8) != 0 && Board.Square[startSquare - 7] == (attackingColor | Piece.Pawn)) {
                return "Pawn";
            }
        } else {
            if (startSquare + 7 <= 63 && (startSquare % 8) != 0 && Board.Square[startSquare + 7] == (attackingColor | Piece.Pawn)) {
                return "Pawn";
            }
            if (startSquare + 9 <= 63 && ((startSquare + 7) % 8) != 0 && Board.Square[startSquare + 9] == (attackingColor | Piece.Pawn)) {
                return "Pawn";
            }
        }

        // Check if the square is attacked by a knight
        if ((startSquare - 1) % 8 != 0 && startSquare % 8 != 0)
        {
            if (startSquare < 56)
            {
                var targetSquare = startSquare + 6;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return "Knight";
            }

            if (startSquare > 7)
            {
                var targetSquare = startSquare - 10;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return "Knight";
            }
        }

        if (startSquare < 48)
        {
            if (startSquare % 8 != 0)
            {
                var targetSquare = startSquare + 15;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return "Knight";
            }

            if ((startSquare - 7) % 8 != 0)
            {
                var targetSquare = startSquare + 17;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return "Knight";
            }
        }

        if ((startSquare - 6) % 8 != 0 && (startSquare - 7) % 8 != 0)
        {
            if (startSquare < 56)
            {
                var targetSquare = startSquare + 10;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return "Knight";
            }

            if (startSquare > 7)
            {
                var targetSquare = startSquare - 6;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return "Knight";
            }
        }

        if (startSquare > 15)
        {
            if (startSquare % 8 != 0)
            {
                var targetSquare = startSquare - 17;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return "Knight";
            }

            if ((startSquare - 7) % 8 != 0)
            {
                var targetSquare = startSquare - 15;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return "Knight";
            }
        }

        // Check if the square is attacked by a bishop or queen
        for (int dirIndex = 4; dirIndex < 8; dirIndex++){
            for (int n = 0; n < PrecomputedMoveData.NumSquaresToEdge[startSquare][dirIndex]; n++){
                int target = startSquare + PrecomputedMoveData.DirectionOffsets[dirIndex] * (n + 1);
                int pieceOnTarget = Board.Square[target];

                if (pieceOnTarget == Piece.None) continue;

                if (Piece.PieceChecker(pieceOnTarget, Piece.Bishop, attackingColor) || Piece.PieceChecker(pieceOnTarget, Piece.Queen, attackingColor)) return "Bishop/Queen";

                break;
            }
        }

        // Check if the square is attacked by a rook or queen
        for (int dirIndex = 0; dirIndex < 4; dirIndex++){
            for (int n = 0; n < PrecomputedMoveData.NumSquaresToEdge[startSquare][dirIndex]; n++){
                int target = startSquare + PrecomputedMoveData.DirectionOffsets[dirIndex] * (n + 1);
                int pieceOnTarget = Board.Square[target];

                if (pieceOnTarget == Piece.None) continue;

                if (Piece.PieceChecker(pieceOnTarget, Piece.Rook, attackingColor) || Piece.PieceChecker(pieceOnTarget, Piece.Queen, attackingColor)) return "Rook/Queen";

                break;
            }
        }

        // Check if the square is attacked by a king
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

            if (piece == Piece.None || !Piece.IsColour(piece, attackingColor)) continue;
            if (Piece.PieceChecker(piece, Piece.King, attackingColor)) return "King";
        }

        // If none of the above conditions were met, the square is not attacked
        return "None";
    }

    public static int fromWhereIsTheSquareAttacked (int startSquare, int attackingColor) {
        if (Board.Square[startSquare] != Piece.None && Piece.IsColour(Board.Square[startSquare], attackingColor)) return -1;

        // Check if the square is attacked by a pawn
        if (attackingColor == Piece.White) {
            if (startSquare - 9 >= 0 && (startSquare % 8) != 0 && Board.Square[startSquare - 9] == (attackingColor | Piece.Pawn)) {
                return startSquare - 9;
            }
            if (startSquare - 7 >= 0 && ((startSquare + 7) % 8) != 0 && Board.Square[startSquare - 7] == (attackingColor | Piece.Pawn)) {
                return startSquare - 7;
            }
        } else {
            if (startSquare + 7 <= 63 && (startSquare % 8) != 0 && Board.Square[startSquare + 7] == (attackingColor | Piece.Pawn)) {
                return startSquare + 7;
            }
            if (startSquare + 9 <= 63 && ((startSquare + 7) % 8) != 0 && Board.Square[startSquare + 9] == (attackingColor | Piece.Pawn)) {
                return startSquare + 9;
            }
        }

        // Check if the square is attacked by a knight
        if ((startSquare - 1) % 8 != 0 && startSquare % 8 != 0)
        {
            if (startSquare < 56)
            {
                var targetSquare = startSquare + 6;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return startSquare + 6;
            }

            if (startSquare > 7)
            {
                var targetSquare = startSquare - 10;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return startSquare - 10;
            }
        }

        if (startSquare < 48)
        {
            if (startSquare % 8 != 0)
            {
                var targetSquare = startSquare + 15;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return startSquare + 15;
            }

            if ((startSquare - 7) % 8 != 0)
            {
                var targetSquare = startSquare + 17;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return startSquare + 17;
            }
        }

        if ((startSquare - 6) % 8 != 0 && (startSquare - 7) % 8 != 0)
        {
            if (startSquare < 56)
            {
                var targetSquare = startSquare + 10;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return startSquare + 10;
            }

            if (startSquare > 7)
            {
                var targetSquare = startSquare - 6;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return startSquare - 6;
            }
        }

        if (startSquare > 15)
        {
            if (startSquare % 8 != 0)
            {
                var targetSquare = startSquare - 17;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return startSquare - 17;
            }

            if ((startSquare - 7) % 8 != 0)
            {
                var targetSquare = startSquare - 15;
                int pieceOnTarget = Board.Square[targetSquare];
                if (pieceOnTarget == (Piece.Knight | attackingColor)) return startSquare - 15;
            }
        }

        // Check if the square is attacked by a bishop or queen
        for (int dirIndex = 4; dirIndex < 8; dirIndex++){
            for (int n = 0; n < PrecomputedMoveData.NumSquaresToEdge[startSquare][dirIndex]; n++){
                int target = startSquare + PrecomputedMoveData.DirectionOffsets[dirIndex] * (n + 1);
                int pieceOnTarget = Board.Square[target];

                if (pieceOnTarget == Piece.None) continue;

                if (Piece.PieceChecker(pieceOnTarget, Piece.Bishop, attackingColor) || Piece.PieceChecker(pieceOnTarget, Piece.Queen, attackingColor)) return target;

                break;
            }
        }

        // Check if the square is attacked by a rook or queen
        for (int dirIndex = 0; dirIndex < 4; dirIndex++){
            for (int n = 0; n < PrecomputedMoveData.NumSquaresToEdge[startSquare][dirIndex]; n++){
                int target = startSquare + PrecomputedMoveData.DirectionOffsets[dirIndex] * (n + 1);
                int pieceOnTarget = Board.Square[target];

                if (pieceOnTarget == Piece.None) continue;

                if (Piece.PieceChecker(pieceOnTarget, Piece.Rook, attackingColor) || Piece.PieceChecker(pieceOnTarget, Piece.Queen, attackingColor)) return target;

                break;
            }
        }

        // Check if the square is attacked by a king
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

            if (piece == Piece.None || !Piece.IsColour(piece, attackingColor)) continue;
            if (Piece.PieceChecker(piece, Piece.King, attackingColor)) return target;
        }

        // If none of the above conditions were met, the square is not attacked
        return -1;
    }

    //endregion

}
