package Classes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Texture {

    public static Image WHITE_PAWN = null;
    public static Image WHITE_KNIGHT = null;
    public static Image WHITE_BISHOP = null;
    public static Image WHITE_ROOK = null;
    public static Image WHITE_KING = null;
    public static Image WHITE_QUEEN = null;

    public static Image BLACK_PAWN = null;
    public static Image BLACK_KNIGHT = null;
    public static Image BLACK_BISHOP = null;
    public static Image BLACK_ROOK = null;
    public static Image BLACK_KING = null;
    public static Image BLACK_QUEEN = null;

    public static void Init(){
        try {
            WHITE_PAWN = ImageIO.read(new File("/home/khalid/IntellijProjects/Chess/src/Textures/White_Pieces/White_Pawn.png"));
            WHITE_KNIGHT = ImageIO.read(new File("/home/khalid/IntellijProjects/Chess/src/Textures/White_Pieces/White_Knight.png"));
            WHITE_BISHOP = ImageIO.read(new File("/home/khalid/IntellijProjects/Chess/src/Textures/White_Pieces/White_Bishop.png"));
            WHITE_ROOK = ImageIO.read(new File("/home/khalid/IntellijProjects/Chess/src/Textures/White_Pieces/White_Rook.png"));
            WHITE_KING = ImageIO.read(new File("/home/khalid/IntellijProjects/Chess/src/Textures/White_Pieces/White_King.png"));
            WHITE_QUEEN = ImageIO.read(new File("/home/khalid/IntellijProjects/Chess/src/Textures/White_Pieces/White_Queen.png"));

            BLACK_PAWN = ImageIO.read(new File("/home/khalid/IntellijProjects/Chess/src/Textures/Black_Pieces/Black_Pawn.png"));
            BLACK_KNIGHT = ImageIO.read(new File("/home/khalid/IntellijProjects/Chess/src/Textures/Black_Pieces/Black_Knight.png"));
            BLACK_BISHOP = ImageIO.read(new File("/home/khalid/IntellijProjects/Chess/src/Textures/Black_Pieces/Black_Bishop.png"));
            BLACK_ROOK = ImageIO.read(new File("/home/khalid/IntellijProjects/Chess/src/Textures/Black_Pieces/Black_Rook.png"));
            BLACK_KING = ImageIO.read(new File("/home/khalid/IntellijProjects/Chess/src/Textures/Black_Pieces/Black_King.png"));
            BLACK_QUEEN = ImageIO.read(new File("/home/khalid/IntellijProjects/Chess/src/Textures/Black_Pieces/Black_Queen.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
