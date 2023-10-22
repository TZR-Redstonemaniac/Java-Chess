package Classes;

public class PgnManager {

    //region Variables
    private static int moveIndex = 1;

    private static final StringBuilder PgnString = new StringBuilder();
    //endregion

    //region Constructor
    private PgnManager() {
        throw new IllegalStateException("Utility class");
    }

    public static void Init(){
        PgnString.append(moveIndex).append(". ");
        moveIndex++;
    }
    //endregion

    //region Methods
    @SuppressWarnings("unused")
    public static void AddMoveToPgn(Move move, boolean captured){
        if (move.targetSquare == move.startSquare + 2 && Piece.PieceChecker(Board.GetSquare()[move.targetSquare],
                Piece.KING)) PgnString.append("O-O").append(" ");
        else if (move.targetSquare == move.startSquare - 2 && Piece.PieceChecker(Board.GetSquare()[move.targetSquare],
                Piece.KING)) PgnString.append("O-O-O").append(" ");
        else if (captured) {
            switch (Piece.PieceType(Board.GetSquare()[move.targetSquare])) {
                case Piece.BISHOP -> PgnString.append("B").append(GetColumnIfMultipleOptions(move)).append("x")
                        .append(Piece.PosFromIndex(move.targetSquare)).append(" ");
                case Piece.KNIGHT -> PgnString.append("N").append(GetColumnIfMultipleOptions(move)).append("x")
                        .append(Piece.PosFromIndex(move.targetSquare)).append(" ");
                case Piece.KING -> PgnString.append("K").append(GetColumnIfMultipleOptions(move)).append("x")
                        .append(Piece.PosFromIndex(move.targetSquare)).append(" ");
                case Piece.QUEEN -> PgnString.append("Q").append(GetColumnIfMultipleOptions(move)).append("x")
                        .append(Piece.PosFromIndex(move.targetSquare)).append(" ");
                case Piece.ROOK -> PgnString.append("R").append(GetColumnIfMultipleOptions(move)).append("x")
                        .append(Piece.PosFromIndex(move.targetSquare)).append(" ");
                case Piece.PAWN -> PgnString.append(GetColumnIfMultipleOptions(move)).append("x")
                        .append(Piece.PosFromIndex(move.targetSquare)).append(" ");
                default ->
                        throw new IllegalStateException("Unexpected value: " +
                                Piece.PieceType(Board.GetSquare()[move.targetSquare]) + " when the turn is for " +
                                Board.colorToMove + "\n The move is " + move.startSquare + " to " + move.targetSquare);
            }
        }
        else {
            switch (Piece.PieceType(Board.GetSquare()[move.targetSquare])) {
                case Piece.BISHOP -> PgnString.append("B").append(GetColumnIfMultipleOptions(move))
                        .append(Piece.PosFromIndex(move.targetSquare)).append(" ");
                case Piece.KNIGHT -> PgnString.append("N").append(GetColumnIfMultipleOptions(move))
                        .append(Piece.PosFromIndex(move.targetSquare)).append(" ");
                case Piece.KING -> PgnString.append("K").append(GetColumnIfMultipleOptions(move))
                        .append(Piece.PosFromIndex(move.targetSquare)).append(" ");
                case Piece.QUEEN -> PgnString.append("Q").append(GetColumnIfMultipleOptions(move))
                        .append(Piece.PosFromIndex(move.targetSquare)).append(" ");
                case Piece.ROOK -> PgnString.append("R").append(GetColumnIfMultipleOptions(move))
                        .append(Piece.PosFromIndex(move.targetSquare)).append(" ");
                case Piece.PAWN -> PgnString
                        .append(Piece.PosFromIndex(move.targetSquare)).append(" ");
                default ->
                        throw new IllegalStateException("Unexpected value: " +
                                Piece.PieceType(Board.GetSquare()[move.targetSquare]) + " at " + move.targetSquare);
            }
        }

        if (Piece.IsColour(Board.GetSquare()[move.targetSquare], Piece.BLACK)) {
            PgnString.append(moveIndex).append(". ");
            moveIndex++;
        }
    }

    private static String GetColumnIfMultipleOptions(Move move){
        if (Board.IsSquareAttacked(move.targetSquare, Board.colorToMove)){
            if (Board.WhatIsSquareAttackedBy(move.targetSquare, Board.colorToMove).equals("Knight") &&
                    Piece.PieceChecker(Board.GetSquare()[move.targetSquare], Piece.KNIGHT, Board.opponentColor)){
                return Piece.PosFromIndex(move.startSquare).substring(0, 1).toLowerCase();
            }

            if (Board.WhatIsSquareAttackedBy(move.targetSquare, Board.colorToMove).equals("Rook") &&
                    Piece.PieceChecker(Board.GetSquare()[move.targetSquare], Piece.ROOK, Board.opponentColor)){
                return Piece.PosFromIndex(move.startSquare).substring(0, 1).toLowerCase();
            }

            if (Board.WhatIsSquareAttackedBy(move.targetSquare, Board.colorToMove).equals("Bishop") &&
                    Piece.PieceChecker(Board.GetSquare()[move.targetSquare], Piece.BISHOP, Board.opponentColor)){
                return Piece.PosFromIndex(move.startSquare).substring(0, 1).toLowerCase();
            }

            if (Board.WhatIsSquareAttackedBy(move.targetSquare, Board.colorToMove).equals("Queen") &&
                    Piece.PieceChecker(Board.GetSquare()[move.targetSquare], Piece.QUEEN, Board.opponentColor)){
                return Piece.PosFromIndex(move.startSquare).substring(0, 1).toLowerCase();
            }

            if (Board.WhatIsSquareAttackedBy(move.targetSquare, Board.colorToMove).equals("Pawn") &&
                    Piece.PieceChecker(Board.GetSquare()[move.targetSquare], Piece.PAWN, Board.opponentColor)){
                return Piece.PosFromIndex(move.startSquare).substring(0, 1).toLowerCase();
            }
        }

        return "";
    }
    //endregion

    //region Getter
    public static StringBuilder GetPgnString () {
        return PgnString;
    }
    //endregion

}
