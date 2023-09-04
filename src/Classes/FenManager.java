package Classes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FenManager {

    public final static String startFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    static Map<Character, Integer> pieceTypeFromSymbol = new HashMap<>();

    public static void Init(){
        pieceTypeFromSymbol.put('k', Piece.King);
        pieceTypeFromSymbol.put('p', Piece.Pawn);
        pieceTypeFromSymbol.put('n', Piece.Knight);
        pieceTypeFromSymbol.put('b', Piece.Bishop);
        pieceTypeFromSymbol.put('r', Piece.Rook);
        pieceTypeFromSymbol.put('q', Piece.Queen);
    }

    public static void LoadFromFen(String fen){
        for (int i = 0; i < 64; i++) Board.Square[i] = 0;

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
                    int pieceColour = (Character.isUpperCase(symbol)) ? Piece.White : Piece.Black;
                    int pieceType = pieceTypeFromSymbol.get(Character.toLowerCase(symbol));
                    Board.Square[rank * 8 + file] = pieceType | pieceColour;
                    file++;
                }
            }
        }

        Board.ColorToMove = (Objects.equals(sections[1], "w")) ? Piece.White : Piece.Black;
        Board.OpponentColor = (Objects.equals(sections[1], "w")) ? Piece.Black : Piece.White;

        String castlingRights = (sections.length > 2) ? sections[2] : "KQkq";
        Board.WKingsideCastle = castlingRights.contains("K");
        Board.WQueensideCastle = castlingRights.contains("Q");
        Board.BKingsideCastle = castlingRights.contains("k");
        Board.BQueensideCastle = castlingRights.contains("q");
    }
}
