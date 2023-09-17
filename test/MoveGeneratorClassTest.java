import Classes.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class MoveGeneratorClassTest {

    //region Piece Generation Tests
    @Test
    void GenerateSlidingMovesTest(){
        PrecomputedMoveData.Init();

        //Bishop Test
        MoveGenerator.ClearMoves();
        MoveGenerator.GenerateSlidingMoves(22, Piece.WHITE | Piece.BISHOP);

        Assertions.assertEquals(9, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(22, 4, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(22, 50, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(22, 36, MoveGenerator.GetMoves()));

        Board.SetSquare(29, Piece.WHITE | Piece.BISHOP);

        MoveGenerator.ClearMoves();

        Board.SetColour(Piece.WHITE);

        MoveGenerator.GenerateSlidingMoves(22, Piece.WHITE | Piece.BISHOP);

        Assertions.assertEquals(4, MoveGenerator.GetMoves().size());
        Assertions.assertFalse(TestIfMoveExists(22, 36, MoveGenerator.GetMoves()));

        Board.SetSquare(29, Piece.NONE);

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
    void GenerateKnightMovesTest(){
        PrecomputedMoveData.Init();

        MoveGenerator.ClearMoves();

        MoveGenerator.GenerateKnightMoves(33);

        Assertions.assertEquals(6, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(33, 48, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(33, 43, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(33, 18, MoveGenerator.GetMoves()));
    }

    @Test
    void GeneratePawnMovesTest(){
        PrecomputedMoveData.Init();
        Board.Clear();

        //Test normal moves
        //White
        MoveGenerator.ClearMoves();

        Board.SetColour(Piece.WHITE);

        MoveGenerator.GeneratePawnMoves(15);
        Assertions.assertEquals(2, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(15, 23, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(15, 31, MoveGenerator.GetMoves()));

        //Black
        MoveGenerator.ClearMoves();

        Board.SetColour(Piece.BLACK);

        MoveGenerator.GeneratePawnMoves(55);
        Assertions.assertEquals(2, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(55, 47, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(55, 39, MoveGenerator.GetMoves()));

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

        //Test Captures
        //White
        MoveGenerator.ClearMoves();

        Board.SetColour(Piece.WHITE);

        Board.SetSquare(36, Piece.PAWN | Piece.BLACK);

        MoveGenerator.GeneratePawnMoves(27);
        Assertions.assertEquals(2, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(27, 36, MoveGenerator.GetMoves()));

        Board.Clear();

        //Black
        MoveGenerator.ClearMoves();

        Board.SetColour(Piece.BLACK);

        Board.SetSquare(27, Piece.PAWN | Piece.WHITE);

        MoveGenerator.GeneratePawnMoves(36);
        Assertions.assertEquals(2, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(36, 27, MoveGenerator.GetMoves()));

        Board.Clear();
    }

    @Test
    void GenerateKingMovesTest(){
        PrecomputedMoveData.Init();

        //Top Right
        MoveGenerator.ClearMoves();

        MoveGenerator.GenerateKingMoves(63);

        Assertions.assertEquals(3, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(63, 62, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(63, 54, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(63, 55, MoveGenerator.GetMoves()));

        //Top Left
        MoveGenerator.ClearMoves();

        MoveGenerator.GenerateKingMoves(56);

        Assertions.assertEquals(3, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(56, 57, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(56, 48, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(56, 49, MoveGenerator.GetMoves()));

        //Bottom Left
        MoveGenerator.ClearMoves();

        MoveGenerator.GenerateKingMoves(0);

        Assertions.assertEquals(3, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(0, 8, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(0, 9, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(0, 1, MoveGenerator.GetMoves()));

        //Bottom Right
        MoveGenerator.ClearMoves();

        MoveGenerator.GenerateKingMoves(7);

        Assertions.assertEquals(3, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(7, 6, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(7, 14, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(7, 15, MoveGenerator.GetMoves()));
    }

    @Test
    void GenerateCastleMovesTest(){
        //Castling White
        Board.wQueensideCastle = true;
        Board.wKingsideCastle = true;

        MoveGenerator.ClearMoves();

        Board.SetColour(Piece.WHITE);

        Board.SetSquare(7, Piece.WHITE | Piece.ROOK);
        Board.SetSquare(0, Piece.WHITE | Piece.ROOK);
        Board.SetSquare(4, Piece.WHITE | Piece.KING);

        MoveGenerator.GenerateKingMoves(4);

        Assertions.assertEquals(7, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(4, 2, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(4, 3, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(4, 5, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(4, 6, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(4, 11, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(4, 12, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(4, 13, MoveGenerator.GetMoves()));

        Board.Clear();

        //Castling Black
        Board.bQueensideCastle = true;
        Board.bKingsideCastle = true;

        MoveGenerator.ClearMoves();

        Board.SetColour(Piece.BLACK);

        Board.SetSquare(63, Piece.BLACK | Piece.ROOK);
        Board.SetSquare(56, Piece.BLACK | Piece.ROOK);
        Board.SetSquare(60, Piece.BLACK | Piece.KING);

        MoveGenerator.GenerateKingMoves(60);

        Assertions.assertEquals(7, MoveGenerator.GetMoves().size());

        Assertions.assertTrue(TestIfMoveExists(60, 58, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(60, 59, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(60, 61, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(60, 62, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(60, 51, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(60, 52, MoveGenerator.GetMoves()));
        Assertions.assertTrue(TestIfMoveExists(60, 53, MoveGenerator.GetMoves()));

        Board.Clear();
    }
    //endregion

    //region Move generation tests
    @Test
    void MoveGenerationTest1(){
        PrecomputedMoveData.Init();

        MoveGenerator.ClearMoves();

        FenManager.Init();
        FenManager.LoadFromFen(FenManager.START_FEN);

        Assertions.assertEquals(20, MoveGenerator.MoveGenerationTest(1,1));
        Assertions.assertEquals(400, MoveGenerator.MoveGenerationTest(2,0));
        Assertions.assertEquals(8902, MoveGenerator.MoveGenerationTest(3,0));
        Assertions.assertEquals(197281, MoveGenerator.MoveGenerationTest(4,0));
        Assertions.assertEquals(4865609, MoveGenerator.MoveGenerationTest(5,0));
    }

    @Test
    void MoveGenerationTest2(){
        MoveGenerator.ClearMoves();
        Board.Clear();
        PrecomputedMoveData.Init();
        FenManager.Init();

        FenManager.LoadFromFen("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
        Assertions.assertEquals(48, MoveGenerator.MoveGenerationTest(1,0));

        FenManager.LoadFromFen("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
        Assertions.assertEquals(2039, MoveGenerator.MoveGenerationTest(2,0));

        FenManager.LoadFromFen("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
        Assertions.assertEquals(97862, MoveGenerator.MoveGenerationTest(3,0));
    }

    @Test
    void MoveGenerationTest3(){
        MoveGenerator.ClearMoves();
        Board.Clear();
        PrecomputedMoveData.Init();
        FenManager.Init();
        FenManager.LoadFromFen("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -");

        Assertions.assertEquals(14, MoveGenerator.MoveGenerationTest(1,0));
        Assertions.assertEquals(191, MoveGenerator.MoveGenerationTest(2,0));
        Assertions.assertEquals(2812, MoveGenerator.MoveGenerationTest(3,0));
        Assertions.assertEquals(43238, MoveGenerator.MoveGenerationTest(4,0));
        Assertions.assertEquals(674624, MoveGenerator.MoveGenerationTest(5,0));
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
