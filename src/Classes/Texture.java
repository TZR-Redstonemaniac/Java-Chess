package Classes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Texture {

    private Texture() {
        throw new IllegalStateException("Utility class");
    }

    public static Image WHITE_PAWN = null; //NOSONAR
    public static Image WHITE_KNIGHT = null; //NOSONAR
    public static Image WHITE_BISHOP = null; //NOSONAR
    public static Image WHITE_ROOK = null; //NOSONAR
    public static Image WHITE_KING = null; //NOSONAR
    public static Image WHITE_QUEEN = null; //NOSONAR

    public static Image BLACK_PAWN = null; //NOSONAR
    public static Image BLACK_KNIGHT = null; //NOSONAR
    public static Image BLACK_BISHOP = null; //NOSONAR
    public static Image BLACK_ROOK = null; //NOSONAR
    public static Image BLACK_KING = null; //NOSONAR
    public static Image BLACK_QUEEN = null; //NOSONAR

    private static final String WHITE_FOLDER = "/home/khalid/IntellijProjects/Chess/src/Textures/White_Pieces/";
    private static final String BLACK_FOLDER = "/home/khalid/IntellijProjects/Chess/src/Textures/Black_Pieces/";
    private static final Logger LOGGER = Logger.getLogger(Texture.class.getName());

    public static void Init(){
        try {
            WHITE_PAWN = ImageIO.read(new File(WHITE_FOLDER, "White_Pawn.png"));
            WHITE_KNIGHT = ImageIO.read(new File(WHITE_FOLDER, "White_Knight.png"));
            WHITE_BISHOP = ImageIO.read(new File(WHITE_FOLDER, "White_Bishop.png"));
            WHITE_ROOK = ImageIO.read(new File(WHITE_FOLDER, "White_Rook.png"));
            WHITE_KING = ImageIO.read(new File(WHITE_FOLDER, "White_King.png"));
            WHITE_QUEEN = ImageIO.read(new File(WHITE_FOLDER, "White_Queen.png"));

            BLACK_PAWN = ImageIO.read(new File(BLACK_FOLDER, "Black_Pawn.png"));
            BLACK_KNIGHT = ImageIO.read(new File(BLACK_FOLDER, "Black_Knight.png"));
            BLACK_BISHOP = ImageIO.read(new File(BLACK_FOLDER, "Black_Bishop.png"));
            BLACK_ROOK = ImageIO.read(new File(BLACK_FOLDER, "Black_Rook.png"));
            BLACK_KING = ImageIO.read(new File(BLACK_FOLDER, "Black_King.png"));
            BLACK_QUEEN = ImageIO.read(new File(BLACK_FOLDER, "Black_Queen.png"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

}
