package Core;

import AI.Opponent.MainAI;
import Classes.*;
import GUI.*;

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

import static AI.Opponent.Evaluate.CountMaterialWithoutKing;

public class Game {

    //region Variables
    public static final GUI ui = new GUI();

    private static int startIndex = -1;
    private static int targetIndex = -1;
    private static int currentIndex;

    private static List<Move> moves = new ArrayList<>();

    private static final Logger LOGGER = Logger.getLogger("Game");

    private static boolean checkmated = false;

    private static final boolean BLACK_AI = false;
    private static final boolean WHITE_AI = false;
    private static final boolean WHITE_RANDOM = true;

    private static final Random random = new Random();

    public enum GamePhase {
        OPENING,
        MIDGAME,
        ENDGAME
    }

    private static GamePhase gamePhase;
    //endregion

    //region Run Function and Game Loop
    public static void main(String[] args) throws SQLException { //NOSONAR
        //Initialize the piece move data and the PGN manager
        PrecomputedMoveData.Init();
        PgnManager.Init();
        GUI_Manager.Init();

        if (WHITE_AI) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.log(Level.WARNING, "Interrupted", e);
                Thread.currentThread().interrupt();
            }
        }

        //Run the game loop
        //noinspection InfiniteLoopStatement
        while (true) MainGameLoop(); //NOSONAR
    }

    public static void MainGameLoop() throws SQLException {
        int whiteEval = CountMaterialWithoutKing(Piece.WHITE) / 100;
        int blackEval = CountMaterialWithoutKing(Piece.BLACK) / 100;

        if (whiteEval > 30 || blackEval > 30) gamePhase = GamePhase.MIDGAME;
        else gamePhase = GamePhase.ENDGAME;

        //Get the board index of where the mouse is
        currentIndex = GetIndex();

        if (WHITE_AI) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOGGER.log(Level.WARNING, "Interrupted", e);
                Thread.currentThread().interrupt();
            }
        }

        //Check if a side is in checkmate
        CheckmateChecker();

        //Check if it is the players turn
        PlayerTurnManager();

        //Redraw the ui
        ui.repaint();
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
            LOGGER.log(Level.WARNING, "Could not connect to database", e);
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
            LOGGER.log(Level.WARNING, "Unable to connect", e);
        }
        return conn;
    }

    private static void InsertPgnToDatabase(String date, String result, String moves) throws SQLException {
        //SQL command
        String sql = "INSERT INTO ChessGames(date, result, moves, eco, ecoName) VALUES(?, ?, ?, ?, ?)";

        //Initialize variables for finding the matching Chess Encyclopedic Code
        String longestMatch = "";
        String longestMatchEco = "";
        String longestMatchEcoName = "";
        int longestMatchLength = 0;

        Connection conn;
        Statement stmt = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConnectEcoCodes();

            if (conn == null) {
                LOGGER.log(Level.WARNING, "Unable to connect to database");
                return;
            }

            stmt = conn.createStatement(); //NOSONAR
            pstmt = conn.prepareStatement("SELECT * FROM ChessEcoCodes WHERE opening = ?");

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
                pstmt.setString(1, longestMatch); // Set the search string as a parameter
                resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    longestMatchEco = resultSet.getString("code");
                    longestMatchEcoName = resultSet.getString("name");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error", e);
        } finally {
            assert pstmt != null;
            pstmt.close();
            stmt.close();
        }

        try {
            conn = ConnectGameHistory();

            if (conn == null) {
                LOGGER.log(Level.WARNING, "Unable to connect to database");
                return;
            }

            pstmt = conn.prepareStatement(sql);

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
            LOGGER.log(Level.WARNING, "Error", e);
        } finally {
            pstmt.close();
        }
    }
    //endregion

    //region Methods
    private static void Grab() throws SQLException {
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

        if (moves.isEmpty() &&
                ((Board.colorToMove == Piece.WHITE && !Board.IsSquareAttacked(whiteKingPos, Piece.BLACK)) ||
                    (Board.colorToMove == Piece.BLACK && !Board.IsSquareAttacked(blackKingPos, Piece.WHITE)))){
                LOGGER.log(Level.INFO, PgnManager.GetPgnString().append("1/2-1/2").toString()); //NOSONAR

                LocalDate date = LocalDate.now();
                int year = date.getYear();
                int month = date.getMonthValue();
                int day = date.getDayOfMonth();

                InsertPgnToDatabase(year + "." + month + "." + day, "1/2-1/2",
                        PgnManager.GetPgnString().toString());

                System.exit(0);

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

        //PgnManager.AddMoveToPgn(move, capturing);

        //Set moves to null
        moves = null;

    }

    private static void CheckmateChecker() throws SQLException {
        int whiteKingPos = 0;
        int blackKingPos = 0;

        for (int i = 0; i < 64; i++){
            if (Piece.PieceChecker(Board.GetSquare()[i], Piece.KING, Piece.BLACK)) blackKingPos = i;
            if (Piece.PieceChecker(Board.GetSquare()[i], Piece.KING, Piece.WHITE)) whiteKingPos = i;
        }

        if ((Board.IsSquareAttacked(whiteKingPos, Piece.BLACK) || Board.IsSquareAttacked(blackKingPos, Piece.WHITE)) &&
                !checkmated){

            List<Move> opponentMoves = MoveGenerator.GenerateLegalMoves();
            if (!opponentMoves.isEmpty())
                return;
            checkmated = true;

            String winner = Board.colorToMove == Piece.WHITE ? "White" : "Black";
            String result = Board.colorToMove == Piece.WHITE ? "1-0" : "0-1";

            LOGGER.log(Level.INFO, "Checkmate, " + winner + " wins\n" + PgnManager.GetPgnString().append(result)); //NOSONAR

            LocalDate date = LocalDate.now();
            int year = date.getYear();
            int month = date.getMonthValue();
            int day = date.getDayOfMonth();

            InsertPgnToDatabase(year + "." + month + "." + day, "0-1", PgnManager.GetPgnString().toString());

            System.exit(0);
        }

        if (MoveGenerator.GenerateLegalMoves().isEmpty()){
            LOGGER.log(Level.INFO, "Stalemate\n" + PgnManager.GetPgnString().append("1/2-1/2")); //NOSONAR

            LocalDate date = LocalDate.now();
            int year = date.getYear();
            int month = date.getMonthValue();
            int day = date.getDayOfMonth();

            InsertPgnToDatabase(year + "." + month + "." + day, "1/2-1/2", PgnManager.GetPgnString().toString());

            System.exit(0);
        }
    }

    private static void PlayerTurnManager() throws SQLException {
        if (GUI_Manager.guiMenu == GUI_Manager.GUI_State.GAME) {
            if (Board.colorToMove == Piece.WHITE) {
                if (!WHITE_AI) {
                    //Grab and release the pieces if the mouse is on the board
                    if (Mouse.GetPressed() && !Mouse.GetGrabbed() && !(currentIndex < 0 || currentIndex > 63)) Grab();
                    else if (!Mouse.GetPressed() && Mouse.GetGrabbed() && !(currentIndex < 0 || currentIndex > 63))
                        Release();
                } else {
                    if (WHITE_RANDOM) {
                        List<Move> movesList = MoveGenerator.GenerateLegalMoves();
                        int bound = movesList.size() - 1;
                        Move move;

                        if (bound < 1) move = movesList.get(0);
                        else move = movesList.get(random.nextInt(bound));
                        boolean capturing = Board.GetSquare()[move.targetSquare] != Piece.NONE;

                        Board.MakeMove(move);

                        //PgnManager.AddMoveToPgn(move, capturing);
                    } else {
                        Move move = MainAI.GetBestMove(Piece.WHITE);
                        boolean capturing = Board.GetSquare()[move.targetSquare] != Piece.NONE &&
                                Piece.IsColour(Board.GetSquare()[move.targetSquare], Piece.BLACK);

                        Board.MakeMove(move);

                        //PgnManager.AddMoveToPgn(move, capturing);
                    }
                }
            } else {
                if (BLACK_AI) {
                    Move move = MainAI.GetBestMove(Piece.BLACK);
                    boolean capturing = Board.GetSquare()[move.targetSquare] != Piece.NONE &&
                            Piece.IsColour(Board.GetSquare()[move.targetSquare], Piece.WHITE);

                    Board.MakeMove(move);

                    //PgnManager.AddMoveToPgn(move, capturing);
                } else {
                    //Grab and release the pieces if the mouse is on the board
                    if (Mouse.GetPressed() && !Mouse.GetGrabbed() && !(currentIndex < 0 || currentIndex > 63)) Grab();
                    else if (!Mouse.GetPressed() && Mouse.GetGrabbed() && !(currentIndex < 0 || currentIndex > 63))
                        Release();
                }
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

        if (mouseX < GetRelativeWidthPos(560) || mouseX > GetRelativeWidthPos(800) + GetRelativeWidthPos(560)) return -1;
        if (mouseY < GetRelativeHeightPos(115) || mouseY > GetRelativeHeightPos(800) + GetRelativeHeightPos(115)) return -1;

        mouseX = ConvertX(mouseX);
        mouseY = ConvertY(mouseY);

        return mouseX + mouseY;
    }

    //ignore
    private static int ConvertX(int x){
        if (x >= GetRelativeWidthPos(700) + GetRelativeWidthPos(560)) return 7;
        else if (x >= GetRelativeWidthPos(600) + GetRelativeWidthPos(560)) return 6;
        else if (x >= GetRelativeWidthPos(500) + GetRelativeWidthPos(560)) return 5;
        else if (x >= GetRelativeWidthPos(400) + GetRelativeWidthPos(560)) return 4;
        else if (x >= GetRelativeWidthPos(300) + GetRelativeWidthPos(560)) return 3;
        else if (x >= GetRelativeWidthPos(200) + GetRelativeWidthPos(560)) return 2;
        else if (x >= GetRelativeWidthPos(100) + GetRelativeWidthPos(560)) return 1;
        else if (x >= GetRelativeWidthPos(560)) return 0;

        return -1;
    }

    private static int ConvertY(int y){
        if (y >= GetRelativeHeightPos(700) + GetRelativeHeightPos(115)) return 0;
        else if (y >= GetRelativeHeightPos(600) + GetRelativeHeightPos(115)) return 8;
        else if (y >= GetRelativeHeightPos(500) + GetRelativeHeightPos(115)) return 16;
        else if (y >= GetRelativeHeightPos(400) + GetRelativeHeightPos(115)) return 24;
        else if (y >= GetRelativeHeightPos(300) + GetRelativeHeightPos(115)) return 32;
        else if (y >= GetRelativeHeightPos(200) + GetRelativeHeightPos(115)) return 40;
        else if (y >= GetRelativeHeightPos(100) + GetRelativeHeightPos(115)) return 48;
        else if (y >= GetRelativeHeightPos(115)) return 56;

        return -1;
    }
    //endignore

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
        return Math.round(GUI.GetScreenWidth()/(1920/pos));
    }

    private static int GetRelativeHeightPos(float pos){
        return Math.round(GUI.GetScreenHeight()/(1080/pos));
    }
    //endregion

    //region Getters and Setters
    public static List<Move> GetMoves() {
        return moves;
    }

    public static int GetStartIndex() {
        return startIndex;
    }

    public static GamePhase GetGamePhase () {
        return gamePhase;
    }
    //endregion
}