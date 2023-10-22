package AI.Opponent;

import Classes.Board;
import Classes.Move;
import Classes.MoveGenerator;
import Core.Game;

import java.util.ArrayList;
import java.util.List;

public class MainAI {

    private MainAI() {
        throw new IllegalStateException("Utility class");
    }

    private static Move bestMove;
    private static int bestEval = -99999999;

    private static boolean firstIteration = false;

    public static int Search(int depth, int alpha, int beta, int ogColor){
        boolean first = false;
        if (!firstIteration) {
            firstIteration = true;
            first = true;
        }

        if (depth == 0) return SearchAllCaptures(alpha, beta, ogColor);

        List<Move> moves = MoveOrdering.OrderMoves(MoveGenerator.GenerateLegalMoves());

        for (Move move : moves){
            Board.MakeMove(move);

            int eval = -Search(depth - 1, -beta, -alpha, ogColor);

            Board.UnmakeMove();

            if (eval >= beta) {
                if ((bestEval < beta) && first) {
                    bestEval = beta;
                    bestMove = move;
                }
                return beta;
            }
            alpha = Math.max(alpha, eval);
            if ((bestEval < alpha) && first) {
                bestEval = alpha;
                bestMove = move;
            }
        }

        return alpha;
    }

    private static int SearchAllCaptures(int alpha, int beta, int ogColor){
        int evaluation = Evaluate.EvaluatePosition(ogColor);
        if (evaluation >= beta) return beta;
        alpha = Math.max(alpha, evaluation);

        List<Move> moves = MoveGenerator.GenerateLegalMoves();
        List<Move> captureMoves = new ArrayList<>();

        for (Move move : moves){
            if (Board.GetSquare()[move.targetSquare] != 0) captureMoves.add(move);
        }

        for (Move move : captureMoves){
            Board.MakeMove(move);
            evaluation = -SearchAllCaptures(-beta, -alpha, ogColor);
            Board.UnmakeMove();

            if (evaluation >= beta) return beta;
            alpha = Math.max(alpha, evaluation);
        }

        return alpha;
    }

    public static Move GetBestMove(int colorToMove) {
        bestMove = null;
        bestEval = -99999999;
        firstIteration = false;

        int depth;
        if (Game.gamePhase == Game.GamePhase.ENDGAME) depth = 10;
        else depth = 5;

        Search(depth, 0, 0, colorToMove);

        return bestMove;
    }
}
