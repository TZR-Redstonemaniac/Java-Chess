package Core;

import Classes.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game {

    //region Variables
    public static final GUI ui = new GUI();

    private static int startIndex = -1;
    private static int targetIndex = -1;
    private static int currentIndex;

    private static ArrayList<Move> moves = new ArrayList<>();
    //endregion

    //region Run Function and Game Loop
    public static void main(String[] args) { //NOSONAR
        PrecomputedMoveData.Init();


        //noinspection InfiniteLoopStatement
        while (true) MainGameLoop(); //NOSONAR
    }

    public static void MainGameLoop() {
        currentIndex = GetIndex();

        if (Mouse.pressed && !Mouse.grabbed && !(currentIndex < 0 || currentIndex > 63)) Grab();
        else if (!Mouse.pressed && Mouse.grabbed && !(currentIndex < 0 || currentIndex > 63)) Release();

        //Set colors
        Board.OpponentColor = Board.ColorToMove == Piece.White ? Piece.Black : Piece.White;

        ui.repaint();
    }
    //endregion

    //region Methods
    private static void Grab(){
        //Return if grabbing no piece
        if (Board.Square[currentIndex] == Piece.None) return;

        //Set the grabbed variable and startIndex
        Mouse.grabbed = true;
        startIndex = currentIndex;

        //Generate the moves and remove any non-valid moves for the piece
        moves = MoveGenerator.GenerateLegalMoves();
        moves.removeIf(move -> move.StartSquare != startIndex);
    }

    private static void Release(){
        //Set the grabbed variable and the targetIndex
        Mouse.grabbed = false;
        targetIndex = currentIndex;

        //If the piece is placed in its starting position, return and set moves to null
        if (targetIndex == startIndex) {
            moves = null;
            return;
        }

        //Check if the move was valid, and return and set moves to null if not
        Move move = null;

        boolean valid = false;
        for (Move moveToCheck : moves)
            if (moveToCheck.TargetSquare == targetIndex) {
                valid = true;
                move = moveToCheck;
                break;
            }
        if (!valid) {
            moves = null;
            return;
        }

        //Save the board position before making the move
        Board.prevSquares.add(Board.Square.clone());
        Board.prevEnPassantSquares.add(Board.EnPassantSquare.clone());

        int newColorToMove = Board.ColorToMove;
        Board.prevColorToMove.add(newColorToMove);

        boolean newWKingsideCastle = Board.WKingsideCastle;
        Board.prevWKCastling.add(newWKingsideCastle);

        boolean newBKingsideCastle = Board.BKingsideCastle;
        Board.prevBKCastling.add(newBKingsideCastle);

        boolean newWQueensideCastle = Board.WQueensideCastle;
        Board.prevWQCastling.add(newWQueensideCastle);

        boolean newBQueensideCastle = Board.BQueensideCastle;
        Board.prevBQCastling.add(newBQueensideCastle);

        //Set pieceToMove to the piece being moved
        int pieceToMove = Board.Square[startIndex];

        Castling(pieceToMove);
        EnPassant(pieceToMove);

        //Set the target square to the piece and remove it from the starting square
        Board.Square[targetIndex] = Board.Square[startIndex];
        Board.Square[startIndex] = 0;

        //Promotion
        if (move.promoPiece != Piece.None){
            Board.Square[targetIndex] = move.promoPiece;
        }

        //Swap the color to move
        Board.ColorToMove = Board.ColorToMove == Piece.White ? Piece.Black : Piece.White;
        Board.OpponentColor = Board.ColorToMove == Piece.White ? Piece.Black : Piece.White;

        //Castling checks
        if (Piece.PieceChecker(pieceToMove, Piece.King, Piece.White)) {
            Board.WKingsideCastle = false;
            Board.WQueensideCastle = false;
        }
        if (Piece.PieceChecker(pieceToMove, Piece.King, Piece.Black)) {
            Board.BKingsideCastle = false;
            Board.BQueensideCastle = false;
        }

        //Set moves to null
        moves = null;
    }
    //endregion

    //region Convenience
    private static int GetIndex(){
        int mouseX =  MouseInfo.getPointerInfo().getLocation().x - ui.getLocationOnScreen().x;
        int mouseY = MouseInfo.getPointerInfo().getLocation().y - ui.getLocationOnScreen().y - 32;

        if (mouseX > ui.getWidth() || mouseX < 0 || mouseY > ui.getHeight() || mouseY < 0) {
            mouseX = -1;
            mouseY = -1;
        }

        mouseX = ConvertX(mouseX);
        mouseY = ConvertY(mouseY);

        return mouseX + mouseY;
    }

    private static int ConvertX(int x){
        if (x >= 700) return 7;
        else if (x >= 600) return 6;
        else if (x >= 500) return 5;
        else if (x >= 400) return 4;
        else if (x >= 300) return 3;
        else if (x >= 200) return 2;
        else if (x >= 100) return 1;
        else if (x >= 0) return 0;

        return -1;
    }

    private static int ConvertY(int y){
        if (y >= 700) return 0;
        else if (y >= 600) return 8;
        else if (y >= 500) return 16;
        else if (y >= 400) return 24;
        else if (y >= 300) return 32;
        else if (y >= 200) return 40;
        else if (y >= 100) return 48;
        else if (y >= 0) return 56;

        return -1;
    }

    private static void EnPassant(int pieceToMove){
        //En-passant managing
        if (Piece.PieceChecker(pieceToMove, Piece.Pawn) &&
                (startIndex + 9 == targetIndex || startIndex + 7 == targetIndex) && Board.EnPassantSquare[targetIndex - 8]) Board.Square[targetIndex - 8] = Piece.None;
        if (Piece.PieceChecker(pieceToMove, Piece.Pawn) &&
                (startIndex - 9 == targetIndex || startIndex - 7 == targetIndex) && Board.EnPassantSquare[targetIndex + 8]) Board.Square[targetIndex + 8] = Piece.None;

        for (int i = 0; i < 64; i++) Board.EnPassantSquare[i] = false;

        if (Piece.PieceChecker(pieceToMove, Piece.Pawn) && (startIndex + 16 == targetIndex || startIndex - 16 == targetIndex)) Board.EnPassantSquare[targetIndex] = true;
    }

    private static void Castling(int pieceToMove){
        //Perform castling
        if (Piece.PieceChecker(pieceToMove, Piece.King)) {
            if (startIndex + 2 == targetIndex) {
                Board.Square[startIndex + 1] = Board.Square[targetIndex + 1];
                Board.Square[targetIndex + 1] = Piece.None;

                if (Board.ColorToMove == Piece.White) {
                    Board.WKingsideCastle = false;
                    Board.WQueensideCastle = false;
                }
                else {
                    Board.BKingsideCastle = false;
                    Board.BQueensideCastle = false;
                }
            } else if (startIndex - 2 == targetIndex) {
                Board.Square[startIndex - 1] = Board.Square[targetIndex - 2];
                Board.Square[targetIndex - 2] = Piece.None;

                if (Board.ColorToMove == Piece.White) {
                    Board.WKingsideCastle = false;
                    Board.WQueensideCastle = false;
                }
                else {
                    Board.BKingsideCastle = false;
                    Board.BQueensideCastle = false;
                }
            }
        }
    }
    //endregion

    //region Getters and Setters
    public static List<Move> GetMoves() {
        return moves;
    }

    public static int GetStartIndex() {
        return startIndex;
    }
    //endregion
}