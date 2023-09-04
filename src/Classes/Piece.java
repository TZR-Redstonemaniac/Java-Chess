package Classes;

public class Piece {

    public static final int None = 0;
    public static final int Pawn = 1;
    public static final int Knight = 2;
    public static final int Bishop = 3;
    public static final int Rook = 4;
    public static final int King = 5;
    public static final int Queen = 6;

    public static final int White = 8;
    public static final int Black = 16;

    public static boolean IsColour (int piece, int colour) {
        return (piece & colour) != 0;
    }

    public static int PieceType (int piece) {
        return piece > 16 ? piece - 16 : piece - 8;
    }

    public static boolean IsRookOrQueen (int piece) {
        return PieceType(piece) == Rook || PieceType(piece) == Queen;
    }

    public static boolean IsBishopOrQueen (int piece) {
        return PieceType(piece) == Bishop || PieceType(piece) == Queen;
    }

    public static boolean IsSlidingPiece (int piece) {
        return PieceType(piece) == Rook || PieceType(piece) == Queen || PieceType(piece) == Bishop;
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
        }

        return "None";
    }

    public static String PosFromIndex (int index){
        char file = (char) ('a' + (index % 8));
        char rank = (char) ('1' + (index / 8));
        return String.valueOf(file) + rank;
    }
}
