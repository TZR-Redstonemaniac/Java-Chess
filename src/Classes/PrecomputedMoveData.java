package Classes;

public class PrecomputedMoveData {

    private PrecomputedMoveData() {
        throw new IllegalStateException("Utility class");
    }

    public static final int[] DirectionOffsets = {8, -8, -1, 1, 7, -7, 9, -9};
    public static final int[][] NumSquaresToEdge = new int[64][8];

    public static void Init () {
        for (int file = 0; file < 8; file++){
            for (int rank = 0; rank < 8; rank++){
                int numNorth = 7 - rank;
                int numEast = 7 - file;

                int squareIndex = rank * 8 + file;

                NumSquaresToEdge[squareIndex] = new int[]{
                    numNorth,
                    rank,
                    file,
                    numEast,
                    Math.min(numNorth, file),
                    Math.min(rank, numEast),
                    Math.min(numNorth, numEast),
                    Math.min(rank, file)
                };
            }
        }
    }

}
