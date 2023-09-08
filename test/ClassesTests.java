import Classes.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ClassesTests {

    //region Piece Class
    @Test
    public void IsColourTest(){
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
    public void PieceTypeTest(){
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
    public void PieceCheckerTest(){
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
    public void PieceNameTest(){
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
        Assertions.assertEquals("None", Piece.PieceName(65));
    }

    @Test
    public void PosFromIndexTest(){
        int index1 = 35;
        int index2 = 27;
        int index3 = 53;
        int index4 = 16;
        int index5 = 5;

        Assertions.assertEquals("d5", Piece.PosFromIndex(index1));
        Assertions.assertEquals("d4", Piece.PosFromIndex(index2));
        Assertions.assertEquals("f7", Piece.PosFromIndex(index3));
        Assertions.assertEquals("a3", Piece.PosFromIndex(index4));
        Assertions.assertEquals("f1", Piece.PosFromIndex(index5));
    }
    //endregion

    //region Move Generator Class
    @Test
    public void GenerateSlidingMovesTest(){
        PrecomputedMoveData.Init();

        //Bishop Test
        MoveGenerator.ClearMoves();
        MoveGenerator.GenerateSlidingMoves(22, Piece.WHITE | Piece.BISHOP);

        Assertions.assertEquals(9, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(22, 4, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(22, 50, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(22, 36, MoveGenerator.GetMoves()));

        //Rook Test
        MoveGenerator.ClearMoves();

        MoveGenerator.GenerateSlidingMoves(41, Piece.WHITE | Piece.ROOK);

        Assertions.assertEquals(14, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(41, 9, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(41, 47, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(41, 57, MoveGenerator.GetMoves()));

        //Queen Test
        MoveGenerator.ClearMoves();

        MoveGenerator.GenerateSlidingMoves(37, Piece.BLACK | Piece.QUEEN);

        Assertions.assertEquals(25, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(37, 10, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(37, 13, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(37, 23, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(37, 32, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(37, 55, MoveGenerator.GetMoves()));
    }

    @Test
    public void GenerateKnightMovesTest(){
        PrecomputedMoveData.Init();

        MoveGenerator.ClearMoves();

        MoveGenerator.GenerateKnightMoves(33);

        Assertions.assertEquals(6, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(33, 48, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(33, 43, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(33, 18, MoveGenerator.GetMoves()));
    }

    @Test
    public void GeneratePawnMovesTest(){
        PrecomputedMoveData.Init();

        //Test normal moves
        //White
        MoveGenerator.ClearMoves();

        Board.SetColour(Piece.WHITE);

        MoveGenerator.GeneratePawnMoves(20);
        Assertions.assertEquals(1, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(20, 28, MoveGenerator.GetMoves()));

        //Black
        MoveGenerator.ClearMoves();

        Board.SetColour(Piece.BLACK);

        MoveGenerator.GeneratePawnMoves(20);
        Assertions.assertEquals(1, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(20, 12, MoveGenerator.GetMoves()));

        //Test promotion moves
        //White
        MoveGenerator.ClearMoves();

        Board.SetColour(Piece.WHITE);

        MoveGenerator.GeneratePawnMoves(49);
        Assertions.assertEquals(4, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(49, 57, Piece.ROOK | Board.colorToMove, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(49, 57, Piece.BISHOP | Board.colorToMove, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(49, 57, Piece.KNIGHT | Board.colorToMove, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(49, 57, Piece.QUEEN | Board.colorToMove, MoveGenerator.GetMoves()));

        //Black
        MoveGenerator.ClearMoves();

        Board.SetColour(Piece.BLACK);

        MoveGenerator.GeneratePawnMoves(15);
        Assertions.assertEquals(4, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(15, 7, Piece.ROOK | Board.colorToMove, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(15, 7, Piece.BISHOP | Board.colorToMove, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(15, 7, Piece.KNIGHT | Board.colorToMove, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(15, 7, Piece.QUEEN | Board.colorToMove, MoveGenerator.GetMoves()));
    }

    @Test
    public void GenerateKingMovesTest(){
        PrecomputedMoveData.Init();

        MoveGenerator.ClearMoves();

        MoveGenerator.GenerateKingMoves(63);

        Assertions.assertEquals(3, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(63, 62, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(63, 54, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(63, 55, MoveGenerator.GetMoves()));
    }

    @Test
    public void MoveGenerationTestTest(){
        MoveGenerator.ClearMoves();

        PrecomputedMoveData.Init();
        FenManager.Init();
        FenManager.LoadFromFen(FenManager.START_FEN);
        Board.Init();

        Assertions.assertEquals(20, MoveGenerator.MoveGenerationTest(1,0));
        Assertions.assertEquals(400, MoveGenerator.MoveGenerationTest(2,0));
        Assertions.assertEquals(8902, MoveGenerator.MoveGenerationTest(3,0));
        Assertions.assertEquals(197281, MoveGenerator.MoveGenerationTest(4,0));
        Assertions.assertEquals(4865609, MoveGenerator.MoveGenerationTest(5,0));
    }
    //endregion

    //region Test Functions
    private boolean TestIfMoveExists(int start, int target, List<Move> moves){
        for (Move move : moves) {
            if (move.startSquare == start && move.targetSquare == target) {
                return true;
            }
        }

        return false;
    }

    private boolean TestIfMoveExists(int start, int target, int promoPiece, List<Move> moves){
        for (Move move : moves) {
            if (move.startSquare == start && move.targetSquare == target && move.promoPiece == promoPiece) {
                return true;
            }
        }

        return false;
    }
    //endregion

}
