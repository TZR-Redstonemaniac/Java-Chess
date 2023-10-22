package Classes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FenManager {

    //ignore
    private FenManager() {
        throw new IllegalStateException("Utility class");
    }
    //endignore

    public static final String START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    static Map<Character, Integer> pieceTypeFromSymbol = new HashMap<>();

    public static void Init(){
        pieceTypeFromSymbol.put('k', Piece.KING);
        pieceTypeFromSymbol.put('p', Piece.PAWN);
        pieceTypeFromSymbol.put('n', Piece.KNIGHT);
        pieceTypeFromSymbol.put('b', Piece.BISHOP);
        pieceTypeFromSymbol.put('r', Piece.ROOK);
        pieceTypeFromSymbol.put('q', Piece.QUEEN);
    }

    public static void LoadFromFen(String fen){
        for (int i = 0; i < 64; i++) Board.SetSquare(i, 0);

        String[] sections = fen.split (" ");

        int file = 0;
        int rank = 7;

        for (char symbol : sections[0].toCharArray()) {
            if (symbol == '/') {
                file = 0;
                rank--;
            } else {
                if (Character.isDigit(symbol)) {
                    file += Character.getNumericValue(symbol);
                } else {
                    int pieceColour = (Character.isUpperCase(symbol)) ? Piece.WHITE : Piece.BLACK;
                    int pieceType = pieceTypeFromSymbol.get(Character.toLowerCase(symbol));
                    Board.SetSquare(rank * 8 + file, pieceType | pieceColour);
                    file++;
                }
            }
        }

        Board.colorToMove = (Objects.equals(sections[1], "w")) ? Piece.WHITE : Piece.BLACK;
        Board.opponentColor = (Objects.equals(sections[1], "w")) ? Piece.BLACK : Piece.WHITE;

        String castlingRights = (sections.length > 2) ? sections[2] : "KQkq";
        Board.wKingsideCastle = castlingRights.contains("K");
        Board.wQueensideCastle = castlingRights.contains("Q");
        Board.bKingsideCastle = castlingRights.contains("k");
        Board.bQueensideCastle = castlingRights.contains("q");
    }
}
