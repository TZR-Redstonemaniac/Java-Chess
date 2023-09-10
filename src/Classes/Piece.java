package Classes;

public class Piece {

    //ignore
    private Piece() {
        throw new IllegalStateException("Utility class");
    }
    //endignore

    public static final int NONE = 0;
    public static final int PAWN = 1;
    public static final int KNIGHT = 2;
    public static final int BISHOP = 3;
    public static final int ROOK = 4;
    public static final int KING = 5;
    public static final int QUEEN = 6;

    public static final int WHITE = 8;
    public static final int BLACK = 16;

    public static boolean IsColour (int piece, int colour) {
        return (piece & colour) != 0;
    }

    public static int PieceType (int piece) {
        return piece > 16 ? piece - 16 : piece - 8;
    }

    public static boolean PieceChecker(int pieceToCheck, int referencePiece){
        return PieceType(pieceToCheck) == referencePiece;
    }

    public static boolean PieceChecker(int pieceToCheck, int referencePiece, int color){
        return PieceType(pieceToCheck) == referencePiece && IsColour(pieceToCheck, color);
    }

    public static String PieceName(int piece){
        if (piece > 16) piece -= 16;
        else piece -= 8;

        switch (piece){
            case 1 -> {
                return "Pawn";
            }
            case 2 -> {
                return "Knight";
            }
            case 3 -> {
                return "Bishop";
            }
            case 4 -> {
                return "Rook";
            }
            case 5 -> {
                return "King";
            }
            case 6 -> {
                return "Queen";
            }
            default -> {
                return "None";
            }
        }
    }

    public static String PosFromIndex (int index){
        char file = (char) ('a' + (index % 8));
        char rank = (char) ('1' + (index / 8));
        return String.valueOf(file) + rank;
    }
}
