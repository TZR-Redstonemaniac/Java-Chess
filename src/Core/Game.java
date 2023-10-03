package Core;

import AI.MainAI;
import Classes.*;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game {

    //region Variables
    public static final GUI ui = new GUI();

    private static int startIndex = -1;
    private static int targetIndex = -1;
    private static int currentIndex;

    private static List<Move> moves = new ArrayList<>();

    private static final Logger LOGGER = Logger.getLogger("Game");

    private static boolean checkmated = false;

    public static boolean searching = false;

    private static final boolean whiteAI = false;
    //endregion

    //region Run Function and Game Loop
    public static void main(String[] args) { //NOSONAR
        //Initialize the piece move data and the PGN manager
        PrecomputedMoveData.Init();
        PgnManager.Init();

        //Run the game loop
        //noinspection InfiniteLoopStatement
        while (true) MainGameLoop(); //NOSONAR
    }

    public static void MainGameLoop() {
        //Get the board index of where the mouse is
        currentIndex = GetIndex();

        Random random = new Random();

        if (whiteAI) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //Check if a side is in checkmate
        CheckmateChecker();

        //Check if it is the players turn
        if (Board.colorToMove == Piece.WHITE){
            if (!whiteAI) {
                //Grab and release the pieces if the mouse is on the board
                if (Mouse.GetPressed() && !Mouse.GetGrabbed() && !(currentIndex < 0 || currentIndex > 63)) Grab();
                else if (!Mouse.GetPressed() && Mouse.GetGrabbed() && !(currentIndex < 0 || currentIndex > 63)) Release();
            } else {
                List<Move> movesList = MoveGenerator.GenerateLegalMoves();
                int bound = movesList.size() - 1;
                Move move;

                if (bound < 1) move = movesList.get(0);
                else move = movesList.get(random.nextInt(bound));
                boolean capturing = Board.GetSquare()[move.targetSquare] != Piece.NONE;

                Board.MakeMove(move);

                PgnManager.AddMoveToPgn(move, capturing);
            }
        } else {
            MainAI.Search(5, 0, 0);

            Move move = MainAI.GetBestMove();
            boolean capturing = Board.GetSquare()[move.targetSquare] != Piece.NONE;

            Board.MakeMove(move);

            PgnManager.AddMoveToPgn(move, capturing);
            searching = false;
        }

        //Redraw the ui
        GUI.GetEvaluation();
        if (!searching) ui.repaint();
    }

    private static Connection ConnectGameHistory (){
        // SQLite's connection string
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();

        String url = "jdbc:sqlite:" + s + "/GameHistory.db";

        //Attempt to make a connection with the database and return it
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private static Connection ConnectEcoCodes (){
        // SQLite's connection string
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();

        String url = "jdbc:sqlite:" + s + "/ChessEcoCodes.db";

        //Attempt to make a connection with the database and return it
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private static void InsertPgnToDatabase(String date, String result, String moves){
        //SQL command
        String sql = "INSERT INTO ChessGames(date, result, moves, eco, ecoName) VALUES(?, ?, ?, ?, ?)";

        //Initialize variables for finding the matching Chess Encyclopedic Code
        String longestMatch = "";
        String longestMatchEco = "";
        String longestMatchEcoName = "";
        int longestMatchLength = 0;

        try {
            Connection conn = ConnectEcoCodes();
            Statement stmt = conn.createStatement();

            // SQL query to retrieve all values from the specified column
            String query = "SELECT opening FROM ChessEcoCodes";
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                String dbValue = resultSet.getString("opening");

                // Check if the database value is a substring of the opening and longer than the current longest match
                if (moves.contains(dbValue) && dbValue.length() > longestMatchLength) {
                    longestMatch = dbValue;
                    longestMatchLength = dbValue.length();
                }
            }

            if (!longestMatch.isEmpty()) {
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ChessEcoCodes WHERE opening = ?");
                pstmt.setString(1, longestMatch); // Set the search string as a parameter
                resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    longestMatchEco = resultSet.getString("code");
                    longestMatchEcoName = resultSet.getString("name");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try{
            Connection conn = ConnectGameHistory();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            //Add to the database
            pstmt.setString(1, date);
            pstmt.setString(2, result);
            pstmt.setString(3, moves);
            if (!longestMatch.isEmpty()) {
                pstmt.setString(4, longestMatchEco);
                pstmt.setString(5, longestMatchEcoName);
            } else {
                pstmt.setString(4, "N/A");
                pstmt.setString(5, "N/A");
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    //endregion

    //region Methods
    private static void Grab(){
        //Return if grabbing no piece
        if (Board.GetSquare()[currentIndex] == Piece.NONE) return;

        //Set the grabbed variable and startIndex
        Mouse.SetGrabbed(true);
        startIndex = currentIndex;

        //Generate the moves and remove any non-valid moves for the piece
        moves = MoveGenerator.GenerateLegalMoves();

        int whiteKingPos = 0;
        int blackKingPos = 0;

        for (int i = 0; i < 64; i++){
            if (Piece.PieceChecker(Board.GetSquare()[i], Piece.KING, Piece.BLACK)) blackKingPos = i;
            if (Piece.PieceChecker(Board.GetSquare()[i], Piece.KING, Piece.WHITE)) whiteKingPos = i;
        }

        if (moves.isEmpty()) {
            if ((Board.colorToMove == Piece.WHITE && !Board.IsSquareAttacked(whiteKingPos, Piece.BLACK)) ||
                    (Board.colorToMove == Piece.BLACK && !Board.IsSquareAttacked(blackKingPos, Piece.WHITE))){
                LOGGER.log(Level.INFO, PgnManager.GetPgnString().append("1/2-1/2").toString()); //NOSONAR

                LocalDate date = LocalDate.now();
                int year = date.getYear();
                int month = date.getMonthValue();
                int day = date.getDayOfMonth();

                InsertPgnToDatabase(year + "." + month + "." + day, "1/2-1/2",
                        PgnManager.GetPgnString().toString());

                System.exit(0);
            }
        }

        moves.removeIf(move -> move.startSquare != startIndex);
    }

    private static void Release(){
        //Set the grabbed variable and the targetIndex
        Mouse.SetGrabbed(false);
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
            if (moveToCheck.targetSquare == targetIndex) {
                valid = true;
                move = moveToCheck;
                break;
            }
        if (!valid) {
            moves = null;
            return;
        }

        boolean capturing = Board.GetSquare()[targetIndex] != Piece.NONE;

        //Save the board position before making the move
        Board.prevSquares.add(Board.GetSquare().clone());
        Board.prevEnPassantSquares.add(Board.enPassantSquare.clone());

        int newColorToMove = Board.colorToMove;
        Board.prevColorToMove.add(newColorToMove);

        boolean newWKingsideCastle = Board.wKingsideCastle;
        Board.prevWKCastling.add(newWKingsideCastle);

        boolean newBKingsideCastle = Board.bKingsideCastle;
        Board.prevBKCastling.add(newBKingsideCastle);

        boolean newWQueensideCastle = Board.wQueensideCastle;
        Board.prevWQCastling.add(newWQueensideCastle);

        boolean newBQueensideCastle = Board.bQueensideCastle;
        Board.prevBQCastling.add(newBQueensideCastle);

        //Set pieceToMove to the piece being moved
        int pieceToMove = Board.GetSquare()[startIndex];

        Castling(pieceToMove);
        EnPassant(pieceToMove);

        //Set the target square to the piece and remove it from the starting square
        Board.SetSquare(targetIndex, Board.GetSquare()[startIndex]);
        Board.SetSquare(startIndex, 0);

        //Promotion
        if (move.promoPiece != Piece.NONE){
            Board.SetSquare(targetIndex, move.promoPiece);
        }

        //Swap the color to move
        Board.colorToMove = Board.colorToMove == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        Board.opponentColor = Board.colorToMove == Piece.WHITE ? Piece.BLACK : Piece.WHITE;

        //Castling checks
        if (Piece.PieceChecker(pieceToMove, Piece.KING, Piece.WHITE)) {
            Board.wKingsideCastle = false;
            Board.wQueensideCastle = false;
        }
        if (Piece.PieceChecker(pieceToMove, Piece.KING, Piece.BLACK)) {
            Board.bKingsideCastle = false;
            Board.bQueensideCastle = false;
        }

        PgnManager.AddMoveToPgn(move, capturing);

        //Set moves to null
        moves = null;

    }

    private static void CheckmateChecker(){
        int whiteKingPos = 0;
        int blackKingPos = 0;

        for (int i = 0; i < 64; i++){
            if (Piece.PieceChecker(Board.GetSquare()[i], Piece.KING, Piece.BLACK)) blackKingPos = i;
            if (Piece.PieceChecker(Board.GetSquare()[i], Piece.KING, Piece.WHITE)) whiteKingPos = i;
        }

        if ((Board.IsSquareAttacked(whiteKingPos, Piece.BLACK) || Board.IsSquareAttacked(blackKingPos, Piece.WHITE)) &&
                !checkmated){

            if (Board.colorToMove == Piece.WHITE){
                List<Move> opponentMoves = MoveGenerator.GenerateLegalMoves();
                if (!opponentMoves.isEmpty())
                    return;
                checkmated = true;
                LOGGER.log(Level.INFO, "Checkmate, Black wins\n" + PgnManager.GetPgnString().append("0-1")); //NOSONAR

                LocalDate date = LocalDate.now();
                int year = date.getYear();
                int month = date.getMonthValue();
                int day = date.getDayOfMonth();

                InsertPgnToDatabase(year + "." + month + "." + day, "0-1", PgnManager.GetPgnString().toString());

                System.exit(0);
            }

            if (Board.colorToMove == Piece.BLACK){
                List<Move> opponentMoves = MoveGenerator.GenerateLegalMoves();
                if (!opponentMoves.isEmpty())
                    return;
                checkmated = true;
                LOGGER.log(Level.INFO, "Checkmate, White wins\n" + PgnManager.GetPgnString().append("1-0")); //NOSONAR

                LocalDate date = LocalDate.now();
                int year = date.getYear();
                int month = date.getMonthValue();
                int day = date.getDayOfMonth();

                InsertPgnToDatabase(year + "." + month + "." + day, "1-0", PgnManager.GetPgnString().toString());

                System.exit(0);
            }
        }


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
        if (x >= GetRelativeWidthPos(700)) return 7;
        else if (x >= GetRelativeWidthPos(600)) return 6;
        else if (x >= GetRelativeWidthPos(500)) return 5;
        else if (x >= GetRelativeWidthPos(400)) return 4;
        else if (x >= GetRelativeWidthPos(300)) return 3;
        else if (x >= GetRelativeWidthPos(200)) return 2;
        else if (x >= GetRelativeWidthPos(100)) return 1;
        else if (x >= GetRelativeWidthPos(0)) return 0;

        return -1;
    }

    private static int ConvertY(int y){
        if (y >= GetRelativeHeightPos(700)) return 0;
        else if (y >= GetRelativeHeightPos(600)) return 8;
        else if (y >= GetRelativeHeightPos(500)) return 16;
        else if (y >= GetRelativeHeightPos(400)) return 24;
        else if (y >= GetRelativeHeightPos(300)) return 32;
        else if (y >= GetRelativeHeightPos(200)) return 40;
        else if (y >= GetRelativeHeightPos(100)) return 48;
        else if (y >= GetRelativeHeightPos(0)) return 56;

        return -1;
    }

    private static void EnPassant(int pieceToMove){
        //En-passant managing
        if (Piece.PieceChecker(pieceToMove, Piece.PAWN) &&
                (startIndex + 9 == targetIndex || startIndex + 7 == targetIndex) && Board.enPassantSquare[targetIndex - 8]) Board.SetSquare(targetIndex - 8, Piece.NONE);
        if (Piece.PieceChecker(pieceToMove, Piece.PAWN) &&
                (startIndex - 9 == targetIndex || startIndex - 7 == targetIndex) && Board.enPassantSquare[targetIndex + 8]) Board.SetSquare(targetIndex + 8, Piece.NONE);

        for (int i = 0; i < 64; i++) Board.enPassantSquare[i] = false;

        if (Piece.PieceChecker(pieceToMove, Piece.PAWN) && (startIndex + 16 == targetIndex || startIndex - 16 == targetIndex)) Board.enPassantSquare[targetIndex] = true;
    }

    private static void Castling(int pieceToMove){
        //Perform castling
        if (Piece.PieceChecker(pieceToMove, Piece.KING)) {
            if (startIndex + 2 == targetIndex) {
                Board.SetSquare(startIndex + 1, Board.GetSquare()[targetIndex + 1]);
                Board.SetSquare(targetIndex + 1, Piece.NONE);

                if (Board.colorToMove == Piece.WHITE) {
                    Board.wKingsideCastle = false;
                    Board.wQueensideCastle = false;
                }
                else {
                    Board.bKingsideCastle = false;
                    Board.bQueensideCastle = false;
                }
            } else if (startIndex - 2 == targetIndex) {
                Board.SetSquare(startIndex - 1, Board.GetSquare()[targetIndex - 2]);
                Board.SetSquare(targetIndex - 2, Piece.NONE);

                if (Board.colorToMove == Piece.WHITE) {
                    Board.wKingsideCastle = false;
                    Board.wQueensideCastle = false;
                }
                else {
                    Board.bKingsideCastle = false;
                    Board.bQueensideCastle = false;
                }
            }
        }
    }

    private static int GetRelativeWidthPos(float pos){
        return Math.round(GUI.getScreenWidth()/(1920/pos));
    }

    private static int GetRelativeHeightPos(float pos){
        return Math.round(GUI.getScreenHeight()/(1080/pos));
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