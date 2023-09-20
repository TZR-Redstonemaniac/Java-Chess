package Classes;

import java.time.LocalDate;

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
        LocalDate date = LocalDate.now();
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        PgnString.append(String.format("[Date \"%d.%d.%d\"]", year, month, day)).append("\n");

        PgnString.append(moveIndex).append(". ");
        moveIndex++;
    }
    //endregion

    //region Methods
    public static void AddMoveToPgn(Move move){
        if (move.targetSquare == move.startSquare + 2 && Piece.PieceChecker(Board.GetSquare()[move.targetSquare],
                Piece.KING)) PgnString.append("O-O").append(" ");
        else if (move.targetSquare == move.startSquare - 2 && Piece.PieceChecker(Board.GetSquare()[move.targetSquare],
                Piece.KING)) PgnString.append("O-O-O").append(" ");
        else PgnString.append(Piece.PosFromIndex(move.targetSquare)).append(" ");

        if (Piece.IsColour(Board.GetSquare()[move.targetSquare], Piece.BLACK)) {
            PgnString.append(moveIndex).append(". ");
            moveIndex++;
        }
    }
    //endregion

    //region Getter
    public static StringBuilder GetPgnString () {
        return PgnString;
    }
    //endregion

}
