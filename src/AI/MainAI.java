package AI;

import Classes.Board;
import Classes.Move;
import Classes.MoveGenerator;
import Core.Game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainAI {

    private MainAI() {
        throw new IllegalStateException("Utility class");
    }

    private static final Map<Integer, Move> movesEvals = new HashMap<>();

    public static int Search(int depth, int alpha, int beta){

        Game.searching = true;

        if (depth == 0) return Evaluate.EvaluatePosition();

        List<Move> moves = MoveOrdering.OrderMoves(MoveGenerator.GenerateLegalMoves());

        for (Move move : moves){
            Board.MakeMove(move);

            int eval = -Search(depth - 1, -beta, -alpha);

            Board.UnmakeMove();

            if (eval >= beta) {
                movesEvals.put(beta, move);
                return beta;
            }
            alpha = Math.max(alpha, eval);
            movesEvals.put(alpha, move);
        }

        return alpha;
    }

    public static Move GetBestMove() {
        int highestEval = -999;

        for (int moveEval : movesEvals.keySet()){
            highestEval = Math.max(highestEval, moveEval);
        }

        Move bestMove = movesEvals.get(highestEval);
        movesEvals.clear();

        return bestMove;
    }
}
