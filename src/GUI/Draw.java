package GUI;

import Classes.*;
import Core.Game;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Draw extends JPanel {

    private final Logger logger = Logger.getLogger(Draw.class.getName()); //NOSONAR

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);

        Graphics2D g2d = (Graphics2D) graphics;

        //Board
        for (Map<String, Object> map : Board.GetSquares()){
            int x = 0;
            int y = 0;
            int w = 0;
            int h = 0;
            int r = 0;
            int g = 0;
            int b = 0;

            for (Map.Entry<String, Object> set : map.entrySet()){
                switch (set.getKey()) {
                    case "x" -> x = (int) set.getValue();
                    case "y" -> y = (int) set.getValue();
                    case "w" -> w = (int) set.getValue();
                    case "h" -> h = (int) set.getValue();
                    case "r" -> r = (int) set.getValue();
                    case "g" -> g = (int) set.getValue();
                    case "b" -> b = (int) set.getValue();
                    default -> {
                        if (!Objects.equals(set.getKey(), "mr") && !Objects.equals(set.getKey(), "mg") && !Objects.equals(set.getKey(), "mb"))
                            logger.log(Level.WARNING,"Unknown Key: {0}", set.getKey());
                    }
                }
            }

            Color color = new Color(r, g, b);

            g2d.setColor(color);
            g2d.fillRect(x, y, w, h);
        }

        //Valid moves
        if (Game.GetMoves() != null) for (Move move : Game.GetMoves()){
            int x = (int) Board.GetSquares()[move.targetSquare].get("x");
            int y = (int) Board.GetSquares()[move.targetSquare].get("y");

            int w = (int) Board.GetSquares()[move.targetSquare].get("w");
            int h = (int) Board.GetSquares()[move.targetSquare].get("h");

            int r = (int) Board.GetSquares()[move.targetSquare].get("mr");
            int g = (int) Board.GetSquares()[move.targetSquare].get("mg");
            int b = (int) Board.GetSquares()[move.targetSquare].get("mb");

            g2d.setColor(new Color(r, g, b));
            g2d.fillRect(x, y, w, h);
        }

        //Pieces
        int grabbedX = 0;
        int grabbedY = 0;
        int grabbedIndex = -1;

        for (int index = 0; index < 64; index++){
            int x = 0;
            int y = 0;

            if (Mouse.GetGrabbed() && index == Game.GetStartIndex()) {
                grabbedX = MouseInfo.getPointerInfo().getLocation().x - Game.ui.getLocationOnScreen().x - 50;
                grabbedY = MouseInfo.getPointerInfo().getLocation().y - Game.ui.getLocationOnScreen().y - 82;

                grabbedIndex = index;
            } else {
                x = (int) Board.GetSquares()[index].get("x");
                y = (int) Board.GetSquares()[index].get("y");
            }

            if (index != grabbedIndex) DrawPiece(g2d, index, x, y);
        }

        if (grabbedIndex >= 0) DrawPiece(g2d, grabbedIndex, grabbedX, grabbedY);
    }

    private void DrawPiece (Graphics2D g2d, int index, int x, int y) {
        if (Piece.IsColour(Board.GetSquare()[index], Piece.WHITE)) {
            switch (Piece.PieceType(Board.GetSquare()[index])) { //NOSONAR
                case Piece.PAWN -> g2d.drawImage(Texture.WHITE_PAWN, x, y, GetRelativeWidthPos(100),
                        GetRelativeHeightPos(100), null);
                case Piece.KNIGHT -> g2d.drawImage(Texture.WHITE_KNIGHT, x, y, GetRelativeWidthPos(100),
                        GetRelativeHeightPos(100), null);
                case Piece.BISHOP -> g2d.drawImage(Texture.WHITE_BISHOP, x, y, GetRelativeWidthPos(100),
                        GetRelativeHeightPos(100), null);
                case Piece.ROOK -> g2d.drawImage(Texture.WHITE_ROOK, x, y, GetRelativeWidthPos(100),
                        GetRelativeHeightPos(100), null);
                case Piece.KING -> g2d.drawImage(Texture.WHITE_KING, x, y, GetRelativeWidthPos(100),
                        GetRelativeHeightPos(100), null);
                case Piece.QUEEN -> g2d.drawImage(Texture.WHITE_QUEEN, x, y, GetRelativeWidthPos(100),
                        GetRelativeHeightPos(100), null);
                case Piece.KNOOK -> g2d.drawImage(Texture.WHITE_KNOOK, x, y, GetRelativeWidthPos(100),
                        GetRelativeHeightPos(100), null);
            }
        } else {
            switch (Piece.PieceType(Board.GetSquare()[index])) { //NOSONAR
                case Piece.PAWN -> g2d.drawImage(Texture.BLACK_PAWN, x, y, GetRelativeWidthPos(100),
                        GetRelativeHeightPos(100), null);
                case Piece.KNIGHT -> g2d.drawImage(Texture.BLACK_KNIGHT, x, y, GetRelativeWidthPos(100),
                        GetRelativeHeightPos(100), null);
                case Piece.BISHOP -> g2d.drawImage(Texture.BLACK_BISHOP, x, y, GetRelativeWidthPos(100),
                        GetRelativeHeightPos(100), null);
                case Piece.ROOK -> g2d.drawImage(Texture.BLACK_ROOK, x, y, GetRelativeWidthPos(100),
                        GetRelativeHeightPos(100), null);
                case Piece.KING -> g2d.drawImage(Texture.BLACK_KING, x, y, GetRelativeWidthPos(100),
                        GetRelativeHeightPos(100), null);
                case Piece.QUEEN -> g2d.drawImage(Texture.BLACK_QUEEN, x, y, GetRelativeWidthPos(100),
                        GetRelativeHeightPos(100), null);
                case Piece.KNOOK -> g2d.drawImage(Texture.BLACK_KNOOK, x, y, GetRelativeWidthPos(100),
                        GetRelativeHeightPos(100), null);
            }
        }
    }

    //region Convenience
    private static int GetRelativeWidthPos(float pos){
        return Math.round(GUI.GetScreenWidth()/(1920/pos));
    }

    private static int GetRelativeHeightPos(float pos){
        return Math.round(GUI.GetScreenHeight()/(1080/pos));
    }
    //endregion

}
