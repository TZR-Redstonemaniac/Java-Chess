import Classes.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PieceClassTest {

    //region Piece Class
    @Test
    void IsColourTest(){
        int piece1 = Piece.WHITE | Piece.ROOK;
        int piece2 = Piece.WHITE | Piece.PAWN;
        int piece3 = Piece.BLACK | Piece.ROOK;
        int piece4 = Piece.BLACK | Piece.PAWN;

        Assertions.assertTrue(Piece.IsColour(piece1, Piece.WHITE));
        Assertions.assertTrue(Piece.IsColour(piece2, Piece.WHITE));
        Assertions.assertTrue(Piece.IsColour(piece3, Piece.BLACK));
        Assertions.assertTrue(Piece.IsColour(piece4, Piece.BLACK));
    }

    @Test
    void PieceTypeTest(){
        int piece1 = Piece.WHITE | Piece.ROOK;
        int piece2 = Piece.WHITE | Piece.PAWN;
        int piece3 = Piece.BLACK | Piece.BISHOP;
        int piece4 = Piece.BLACK | Piece.KNIGHT;

        Assertions.assertEquals(Piece.ROOK, Piece.PieceType(piece1));
        Assertions.assertEquals(Piece.PAWN, Piece.PieceType(piece2));
        Assertions.assertEquals(Piece.BISHOP, Piece.PieceType(piece3));
        Assertions.assertEquals(Piece.KNIGHT, Piece.PieceType(piece4));
    }

    @Test
    void PieceCheckerTest(){
        int piece1 = Piece.WHITE | Piece.ROOK;
        int piece2 = Piece.WHITE | Piece.PAWN;
        int piece3 = Piece.BLACK | Piece.BISHOP;
        int piece4 = Piece.BLACK | Piece.KNIGHT;

        Assertions.assertTrue(Piece.PieceChecker(piece1, Piece.ROOK));
        Assertions.assertTrue(Piece.PieceChecker(piece1, Piece.ROOK, Piece.WHITE));

        Assertions.assertTrue(Piece.PieceChecker(piece2, Piece.PAWN));
        Assertions.assertTrue(Piece.PieceChecker(piece2, Piece.PAWN, Piece.WHITE));

        Assertions.assertTrue(Piece.PieceChecker(piece3, Piece.BISHOP));
        Assertions.assertTrue(Piece.PieceChecker(piece3, Piece.BISHOP, Piece.BLACK));

        Assertions.assertTrue(Piece.PieceChecker(piece4, Piece.KNIGHT));
        Assertions.assertTrue(Piece.PieceChecker(piece4, Piece.KNIGHT, Piece.BLACK));
    }

    @Test
    void PieceNameTest(){
        int piece1 = Piece.WHITE | Piece.ROOK;
        int piece2 = Piece.WHITE | Piece.PAWN;
        int piece3 = Piece.BLACK | Piece.BISHOP;
        int piece4 = Piece.BLACK | Piece.KNIGHT;
        int piece5 = Piece.WHITE | Piece.KING;
        int piece6 = Piece.BLACK | Piece.QUEEN;

        Assertions.assertEquals("Rook", Piece.PieceName(piece1));
        Assertions.assertEquals("Pawn", Piece.PieceName(piece2));
        Assertions.assertEquals("Bishop", Piece.PieceName(piece3));
        Assertions.assertEquals("Knight", Piece.PieceName(piece4));
        Assertions.assertEquals("King", Piece.PieceName(piece5));
        Assertions.assertEquals("Queen", Piece.PieceName(piece6));
        Assertions.assertEquals("None", Piece.PieceName(6584));
    }

    @Test
    void PosFromIndexTest(){
        int index1 = 35;
        int index2 = 27;
        int index3 = 53;
        int index4 = 16;
        int index5 = 9;

        Assertions.assertEquals("d5", Piece.PosFromIndex(index1));
        Assertions.assertEquals("d4", Piece.PosFromIndex(index2));
        Assertions.assertEquals("f7", Piece.PosFromIndex(index3));
        Assertions.assertEquals("a3", Piece.PosFromIndex(index4));
        Assertions.assertEquals("b2", Piece.PosFromIndex(index5));
    }
    //endregion

}
