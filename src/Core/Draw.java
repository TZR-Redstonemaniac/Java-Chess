package Core;

import Classes.*;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Draw extends JPanel {

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);

        Graphics2D g2d = (Graphics2D) graphics;

        //Board
        for (Map<String, Object> map : Board.getSquares()){
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
                }
            }

            Color color = new Color(r, g, b);

            g2d.setColor(color);
            g2d.fillRect(x, y, w, h);
        }

        //Attacked Squares
        for (int index : GUI.getAttackedSquares()){
            int x = (int) Board.getSquares()[index].get("x");
            int y = (int) Board.getSquares()[index].get("y");

            g2d.setColor(new Color(100, 100, 100));
            g2d.fillRect(x, y, 100, 100);
        }

        //Valid moves
        if (Game.GetMoves() != null) for (Move move : Game.GetMoves()){
            int x = (int) Board.getSquares()[move.TargetSquare].get("x");
            int y = (int) Board.getSquares()[move.TargetSquare].get("y");

            int r = (int) Board.getSquares()[move.TargetSquare].get("mr");
            int g = (int) Board.getSquares()[move.TargetSquare].get("mg");
            int b = (int) Board.getSquares()[move.TargetSquare].get("mb");

            g2d.setColor(new Color(r, g, b));
            g2d.fillRect(x, y, 100, 100);
        }

        //Pieces
        int grabbedX = 0;
        int grabbedY = 0;
        int grabbedIndex = -1;

        for (int index = 0; index < 64; index++){
            int x = 0;
            int y = 0;

            if (Mouse.grabbed && index == Game.getStartIndex()) {
                grabbedX = MouseInfo.getPointerInfo().getLocation().x - Game.ui.getLocationOnScreen().x - 50;
                grabbedY = MouseInfo.getPointerInfo().getLocation().y - Game.ui.getLocationOnScreen().y - 82;

                grabbedIndex = index;
            } else {
                x = (int) Board.getSquares()[index].get("x");
                y = (int) Board.getSquares()[index].get("y");
            }

            if (index != grabbedIndex) DrawPiece(g2d, index, x, y);
        }

        if (grabbedIndex >= 0) DrawPiece(g2d, grabbedIndex, grabbedX, grabbedY);
    }

    private void DrawPiece (Graphics2D g2d, int index, int x, int y) {
        if (Piece.IsColour(Board.Square[index], Piece.White)) {
            switch (Piece.PieceType(Board.Square[index])) {
                case Piece.Pawn -> g2d.drawImage(Texture.WHITE_PAWN, x, y, 100, 100, null);
                case Piece.Knight -> g2d.drawImage(Texture.WHITE_KNIGHT, x, y, 100, 100, null);
                case Piece.Bishop -> g2d.drawImage(Texture.WHITE_BISHOP, x, y, 100, 100, null);
                case Piece.Rook -> g2d.drawImage(Texture.WHITE_ROOK, x, y, 100, 100, null);
                case Piece.King -> g2d.drawImage(Texture.WHITE_KING, x, y, 100, 100, null);
                case Piece.Queen -> g2d.drawImage(Texture.WHITE_QUEEN, x, y, 100, 100, null);
            }
        } else {
            switch (Piece.PieceType(Board.Square[index])) {
                case Piece.Pawn -> g2d.drawImage(Texture.BLACK_PAWN, x, y, 100, 100, null);
                case Piece.Knight -> g2d.drawImage(Texture.BLACK_KNIGHT, x, y, 100, 100, null);
                case Piece.Bishop -> g2d.drawImage(Texture.BLACK_BISHOP, x, y, 100, 100, null);
                case Piece.Rook -> g2d.drawImage(Texture.BLACK_ROOK, x, y, 100, 100, null);
                case Piece.King -> g2d.drawImage(Texture.BLACK_KING, x, y, 100, 100, null);
                case Piece.Queen -> g2d.drawImage(Texture.BLACK_QUEEN, x, y, 100, 100, null);
            }
        }
    }

}
