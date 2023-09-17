import Classes.PrecomputedMoveData;
import org.junit.jupiter.api.Test;

import static Classes.Board.*;
import static Classes.Piece.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardClassTest {

    @Test
    void IsSquareAttackedByPawnTest(){
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
}
