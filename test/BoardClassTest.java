import Classes.Board;
import Classes.PrecomputedMoveData;
import org.junit.jupiter.api.Test;

import static Classes.Board.*;
import static Classes.Piece.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardClassTest {

    @Test
    void IsSquareAttackedByPawnTest(){
        Board.Init();
        PrecomputedMoveData.Init();

        Clear();
        SetSquare(27, PAWN | WHITE);

        assertEquals("Pawn", WhatIsSquareAttackedBy(34, WHITE));
        assertEquals("Pawn", WhatIsSquareAttackedBy(36, WHITE));
    }

    @Test
    void IsSquareAttackedByKnight(){
        PrecomputedMoveData.Init();

        Clear();
        SetSquare(37, KNIGHT | WHITE);

        assertEquals("Knight", WhatIsSquareAttackedBy(43, WHITE));
        assertEquals("Knight", WhatIsSquareAttackedBy(27, WHITE));

        assertEquals("Knight", WhatIsSquareAttackedBy(52, WHITE));
        assertEquals("Knight", WhatIsSquareAttackedBy(54, WHITE));

        assertEquals("Knight", WhatIsSquareAttackedBy(47, WHITE));
        assertEquals("Knight", WhatIsSquareAttackedBy(31, WHITE));

        assertEquals("Knight", WhatIsSquareAttackedBy(22, WHITE));
        assertEquals("Knight", WhatIsSquareAttackedBy(20, WHITE));
    }

    @Test
    void IsSquareAttackedByKing(){
        PrecomputedMoveData.Init();

        Clear();
        SetSquare(27, KING | WHITE);

        assertEquals("King", WhatIsSquareAttackedBy(18, WHITE));
        assertEquals("King", WhatIsSquareAttackedBy(19, WHITE));
        assertEquals("King", WhatIsSquareAttackedBy(20, WHITE));
        assertEquals("King", WhatIsSquareAttackedBy(26, WHITE));
        assertEquals("King", WhatIsSquareAttackedBy(28, WHITE));
        assertEquals("King", WhatIsSquareAttackedBy(34, WHITE));
        assertEquals("King", WhatIsSquareAttackedBy(35, WHITE));
        assertEquals("King", WhatIsSquareAttackedBy(36, WHITE));
    }

    @Test
    void IsSquareAttackedByBishop(){
        PrecomputedMoveData.Init();

        Clear();
        SetSquare(27, BISHOP | WHITE);

        assertTrue(WhatIsSquareAttackedBy(20, WHITE).contains("Bishop"));
        assertTrue(WhatIsSquareAttackedBy(13, WHITE).contains("Bishop"));
        assertTrue(WhatIsSquareAttackedBy(6, WHITE).contains("Bishop"));

        assertTrue(WhatIsSquareAttackedBy(18, WHITE).contains("Bishop"));
        assertTrue(WhatIsSquareAttackedBy(9, WHITE).contains("Bishop"));
        assertTrue(WhatIsSquareAttackedBy(0, WHITE).contains("Bishop"));

        assertTrue(WhatIsSquareAttackedBy(34, WHITE).contains("Bishop"));
        assertTrue(WhatIsSquareAttackedBy(41, WHITE).contains("Bishop"));
        assertTrue(WhatIsSquareAttackedBy(48, WHITE).contains("Bishop"));

        assertTrue(WhatIsSquareAttackedBy(36, WHITE).contains("Bishop"));
        assertTrue(WhatIsSquareAttackedBy(45, WHITE).contains("Bishop"));
        assertTrue(WhatIsSquareAttackedBy(54, WHITE).contains("Bishop"));
        assertTrue(WhatIsSquareAttackedBy(63, WHITE).contains("Bishop"));
    }

    @Test
    void IsSquareAttackedByRook(){
        PrecomputedMoveData.Init();

        Clear();
        SetSquare(27, ROOK | WHITE);

        assertTrue(WhatIsSquareAttackedBy(19, WHITE).contains("Rook"));
        assertTrue(WhatIsSquareAttackedBy(11, WHITE).contains("Rook"));
        assertTrue(WhatIsSquareAttackedBy(3, WHITE).contains("Rook"));

        assertTrue(WhatIsSquareAttackedBy(24, WHITE).contains("Rook"));
        assertTrue(WhatIsSquareAttackedBy(25, WHITE).contains("Rook"));
        assertTrue(WhatIsSquareAttackedBy(26, WHITE).contains("Rook"));

        assertTrue(WhatIsSquareAttackedBy(28, WHITE).contains("Rook"));
        assertTrue(WhatIsSquareAttackedBy(29, WHITE).contains("Rook"));
        assertTrue(WhatIsSquareAttackedBy(30, WHITE).contains("Rook"));
        assertTrue(WhatIsSquareAttackedBy(31, WHITE).contains("Rook"));

        assertTrue(WhatIsSquareAttackedBy(35, WHITE).contains("Rook"));
        assertTrue(WhatIsSquareAttackedBy(43, WHITE).contains("Rook"));
        assertTrue(WhatIsSquareAttackedBy(51, WHITE).contains("Rook"));
        assertTrue(WhatIsSquareAttackedBy(59, WHITE).contains("Rook"));
    }

    @Test
    void IsSquareAttackedByQueen(){
        PrecomputedMoveData.Init();

        Clear();
        SetSquare(27, QUEEN | WHITE);

        assertTrue(WhatIsSquareAttackedBy(20, WHITE).contains("Queen"));
        assertTrue(WhatIsSquareAttackedBy(13, WHITE).contains("Queen"));
        assertTrue(WhatIsSquareAttackedBy(6, WHITE).contains("Queen"));

        assertTrue(WhatIsSquareAttackedBy(18, WHITE).contains("Queen"));
        assertTrue(WhatIsSquareAttackedBy(9, WHITE).contains("Queen"));
        assertTrue(WhatIsSquareAttackedBy(0, WHITE).contains("Queen"));

        assertTrue(WhatIsSquareAttackedBy(34, WHITE).contains("Queen"));
        assertTrue(WhatIsSquareAttackedBy(41, WHITE).contains("Queen"));
        assertTrue(WhatIsSquareAttackedBy(48, WHITE).contains("Queen"));

        assertTrue(WhatIsSquareAttackedBy(36, WHITE).contains("Queen"));
        assertTrue(WhatIsSquareAttackedBy(45, WHITE).contains("Queen"));
        assertTrue(WhatIsSquareAttackedBy(54, WHITE).contains("Queen"));
        assertTrue(WhatIsSquareAttackedBy(63, WHITE).contains("Queen"));

        assertTrue(WhatIsSquareAttackedBy(19, WHITE).contains("Queen"));
        assertTrue(WhatIsSquareAttackedBy(11, WHITE).contains("Queen"));
        assertTrue(WhatIsSquareAttackedBy(3, WHITE).contains("Queen"));

        assertTrue(WhatIsSquareAttackedBy(24, WHITE).contains("Queen"));
        assertTrue(WhatIsSquareAttackedBy(25, WHITE).contains("Queen"));
        assertTrue(WhatIsSquareAttackedBy(26, WHITE).contains("Queen"));

        assertTrue(WhatIsSquareAttackedBy(28, WHITE).contains("Queen"));
        assertTrue(WhatIsSquareAttackedBy(29, WHITE).contains("Queen"));
        assertTrue(WhatIsSquareAttackedBy(30, WHITE).contains("Queen"));
        assertTrue(WhatIsSquareAttackedBy(31, WHITE).contains("Queen"));

        assertTrue(WhatIsSquareAttackedBy(35, WHITE).contains("Queen"));
        assertTrue(WhatIsSquareAttackedBy(43, WHITE).contains("Queen"));
        assertTrue(WhatIsSquareAttackedBy(51, WHITE).contains("Queen"));
        assertTrue(WhatIsSquareAttackedBy(59, WHITE).contains("Queen"));
    }


    @Test
    void WhereIsSquareAttackedByPawnTest(){
        PrecomputedMoveData.Init();

        Clear();
        SetSquare(27, PAWN | WHITE);

        assertEquals(27, FromWhereIsTheSquareAttacked(34, WHITE) );
        assertEquals(27, FromWhereIsTheSquareAttacked(36, WHITE));
    }

    @Test
    void WhereIsSquareAttackedByKnight(){
        PrecomputedMoveData.Init();

        Clear();
        SetSquare(37, KNIGHT | WHITE);

        assertEquals(37, FromWhereIsTheSquareAttacked(43, WHITE));
        assertEquals(37, FromWhereIsTheSquareAttacked(27, WHITE));

        assertEquals(37, FromWhereIsTheSquareAttacked(52, WHITE));
        assertEquals(37, FromWhereIsTheSquareAttacked(54, WHITE));

        assertEquals(37, FromWhereIsTheSquareAttacked(47, WHITE));
        assertEquals(37, FromWhereIsTheSquareAttacked(31, WHITE));

        assertEquals(37, FromWhereIsTheSquareAttacked(22, WHITE));
        assertEquals(37, FromWhereIsTheSquareAttacked(20, WHITE));
    }

    @Test
    void WhereIsSquareAttackedByKing(){
        PrecomputedMoveData.Init();

        Clear();
        SetSquare(27, KING | WHITE);

        assertEquals(27, FromWhereIsTheSquareAttacked(18, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(19, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(20, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(26, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(28, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(34, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(35, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(36, WHITE));
    }

    @Test
    void WhereIsSquareAttackedByBishop(){
        PrecomputedMoveData.Init();

        Clear();
        SetSquare(27, BISHOP | WHITE);

        assertEquals(27, FromWhereIsTheSquareAttacked(20, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(13, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(6, WHITE));

        assertEquals(27, FromWhereIsTheSquareAttacked(18, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(9, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(0, WHITE));

        assertEquals(27, FromWhereIsTheSquareAttacked(34, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(41, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(48, WHITE));

        assertEquals(27, FromWhereIsTheSquareAttacked(36, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(45, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(54, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(63, WHITE));
    }

    @Test
    void WhereIsSquareAttackedByRook(){
        PrecomputedMoveData.Init();

        Clear();
        SetSquare(27, ROOK | WHITE);

        assertEquals(27, FromWhereIsTheSquareAttacked(19, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(11, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(3, WHITE));

        assertEquals(27, FromWhereIsTheSquareAttacked(24, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(25, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(26, WHITE));

        assertEquals(27, FromWhereIsTheSquareAttacked(28, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(29, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(30, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(31, WHITE));

        assertEquals(27, FromWhereIsTheSquareAttacked(35, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(43, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(51, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(59, WHITE));
    }

    @Test
    void WhereIsSquareAttackedByQueen(){
        PrecomputedMoveData.Init();

        Clear();
        SetSquare(27, QUEEN | WHITE);

        assertEquals(27, FromWhereIsTheSquareAttacked(20, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(13, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(6, WHITE));

        assertEquals(27, FromWhereIsTheSquareAttacked(18, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(9, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(0, WHITE));

        assertEquals(27, FromWhereIsTheSquareAttacked(34, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(41, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(48, WHITE));

        assertEquals(27, FromWhereIsTheSquareAttacked(36, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(45, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(54, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(63, WHITE));

        assertEquals(27, FromWhereIsTheSquareAttacked(19, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(11, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(3, WHITE));

        assertEquals(27, FromWhereIsTheSquareAttacked(24, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(25, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(26, WHITE));

        assertEquals(27, FromWhereIsTheSquareAttacked(28, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(29, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(30, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(31, WHITE));

        assertEquals(27, FromWhereIsTheSquareAttacked(35, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(43, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(51, WHITE));
        assertEquals(27, FromWhereIsTheSquareAttacked(59, WHITE));
    }
}
