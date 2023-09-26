package Core;

import AI.Evaluate;
import Classes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JFrame {

    //region Variables
    public static final Draw DRAW_BOARD = new Draw();

    private static final JLabel depthLabel = new JLabel("Depth");
    private static final JTextField moveTestDepth = new JTextField();

    private static final JLabel delayLabel = new JLabel("Delay");
    private static final JTextField moveTestDelay = new JTextField();

    private static final JLabel moveCount = new JLabel("0");

    private static final JLabel evaluation = new JLabel("0");

    private static final JButton undo = new JButton("Undo");

    private static final JButton moveTest = new JButton("Test");
    private static final JButton printLegalMoves = new JButton("Print Legal");

    private static final JButton setWhite = new JButton("White");
    private static final JButton setBlack = new JButton("Black");

    private static final JButton showAttackedSquares = new JButton("Show attacked Squares");
    private static final JButton hideAttackedSquares = new JButton("Hide attacked Squares");

    private static final JTextField fenString = new JTextField();
    private static final JButton setFen = new JButton("Set FEN");

    private static final JTextArea moveDisplay = new JTextArea(16, 58);
    private static final JScrollPane moveScroll = new JScrollPane(moveDisplay);

    //region Position Labels
    private static final JLabel A_POS = new JLabel("A");
    private static final JLabel B_POS = new JLabel("B");
    private static final JLabel C_POS = new JLabel("C");
    private static final JLabel D_POS = new JLabel("D");
    private static final JLabel E_POS = new JLabel("E");
    private static final JLabel F_POS = new JLabel("F");
    private static final JLabel G_POS = new JLabel("G");
    private static final JLabel H_POS = new JLabel("H");

    private static final JLabel N1_POS = new JLabel("1");
    private static final JLabel N2_POS = new JLabel("2");
    private static final JLabel N3_POS = new JLabel("3");
    private static final JLabel N4_POS = new JLabel("4");
    private static final JLabel N5_POS = new JLabel("5");
    private static final JLabel N6_POS = new JLabel("6");
    private static final JLabel N7_POS = new JLabel("7");
    private static final JLabel N8_POS = new JLabel("8");
    //endregion

    private static ArrayList<Integer> attackedSquares = new ArrayList<>();

    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static int screenWidth = (int) Math.round(screenSize.getWidth());
    static int screenHeight = (int) Math.round(screenSize.getHeight());

    static boolean debug = false;
    //endregion

    //region Constructors
    public GUI (){
        super("Chess");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(Math.round(screenWidth / 1.7777777777f),screenHeight);
        setPreferredSize(getSize());
        setMinimumSize(getPreferredSize());

        ConfigureComponents();
        AddComponents();

        setVisible(true);
        setLocationRelativeTo(null);
        pack();
    }

    private void ConfigureComponents (){
        Texture.Init();
        FenManager.Init();
        FenManager.LoadFromFen(FenManager.START_FEN);
        Board.Init();

        depthLabel.setBounds(GetRelativeWidthPos(850), GetRelativeHeightPos(75),
                GetRelativeWidthPos(100), GetRelativeHeightPos(25));
        moveTestDepth.setBounds(GetRelativeWidthPos(850), GetRelativeHeightPos(100),
                GetRelativeWidthPos(100), GetRelativeHeightPos(25));

        delayLabel.setBounds(GetRelativeWidthPos(850), GetRelativeHeightPos(125),
                GetRelativeWidthPos(100), GetRelativeHeightPos(25));
        moveTestDelay.setBounds(GetRelativeWidthPos(850), GetRelativeHeightPos(150),
                GetRelativeWidthPos(100), GetRelativeHeightPos(25));

        moveCount.setBounds(GetRelativeWidthPos(875), GetRelativeHeightPos(175),
                99999, GetRelativeHeightPos(25));

        undo.setBounds(GetRelativeWidthPos(850), GetRelativeHeightPos(300),
                GetRelativeWidthPos(100), GetRelativeHeightPos(25));
        undo.addActionListener(this::Undo);

        moveTest.setBounds(GetRelativeWidthPos(850), GetRelativeHeightPos(200),
                GetRelativeWidthPos(100), GetRelativeHeightPos(25));
        moveTest.addActionListener(e -> TestMoves());

        setWhite.setBounds(GetRelativeWidthPos(850), GetRelativeHeightPos(500),
                GetRelativeWidthPos(100), GetRelativeHeightPos(25));
        setWhite.addActionListener(this::SetWhiteToMove);

        setBlack.setBounds(GetRelativeWidthPos(850), GetRelativeHeightPos(525),
                GetRelativeWidthPos(100), GetRelativeHeightPos(25));
        setBlack.addActionListener(this::SetBlackToMove);

        showAttackedSquares.setBounds(GetRelativeWidthPos(800), GetRelativeHeightPos(600),
                GetRelativeWidthPos(200), GetRelativeHeightPos(25));
        showAttackedSquares.addActionListener(this::ShowAttackedSquares);

        hideAttackedSquares.setBounds(GetRelativeWidthPos(800), GetRelativeHeightPos(630),
                GetRelativeWidthPos(200), GetRelativeHeightPos(25));
        hideAttackedSquares.addActionListener(this::HideAttackedSquares);

        fenString.setBounds(GetRelativeWidthPos(850), GetRelativeHeightPos(700),
                GetRelativeWidthPos(100), GetRelativeHeightPos(25));

        setFen.setBounds(GetRelativeWidthPos(850), GetRelativeHeightPos(725),
                GetRelativeWidthPos(100), GetRelativeHeightPos(25));
        setFen.addActionListener(this::SetFEN);

        printLegalMoves.setBounds(GetRelativeWidthPos(825), GetRelativeHeightPos(800),
                GetRelativeWidthPos(150), GetRelativeHeightPos(25));
        printLegalMoves.addActionListener(this::PrintLegal);

        moveDisplay.setEditable(false);
        moveScroll.setBounds(GetRelativeWidthPos(25), GetRelativeHeightPos(850),
                GetRelativeWidthPos(500), GetRelativeHeightPos(200));
        moveScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        //region Position Labels
        N8_POS.setBounds(GetRelativeWidthPos(810), 25,
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        N7_POS.setBounds(GetRelativeWidthPos(810), GetRelativeHeightPos(125),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        N6_POS.setBounds(GetRelativeWidthPos(810), GetRelativeHeightPos(225),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        N5_POS.setBounds(GetRelativeWidthPos(810), GetRelativeHeightPos(325),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        N4_POS.setBounds(GetRelativeWidthPos(810), GetRelativeHeightPos(425),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        N3_POS.setBounds(GetRelativeWidthPos(810), GetRelativeHeightPos(525),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        N2_POS.setBounds(GetRelativeWidthPos(810), GetRelativeHeightPos(625),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        N1_POS.setBounds(GetRelativeWidthPos(810), GetRelativeHeightPos(725),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));

        A_POS.setBounds(50, GetRelativeHeightPos(800),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        B_POS.setBounds(GetRelativeWidthPos(150), GetRelativeHeightPos(800),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        C_POS.setBounds(GetRelativeWidthPos(250), GetRelativeHeightPos(800),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        D_POS.setBounds(GetRelativeWidthPos(350), GetRelativeHeightPos(800),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        E_POS.setBounds(GetRelativeWidthPos(450), GetRelativeHeightPos(800),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        F_POS.setBounds(GetRelativeWidthPos(550), GetRelativeHeightPos(800),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        G_POS.setBounds(GetRelativeWidthPos(650), GetRelativeHeightPos(800),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        H_POS.setBounds(GetRelativeWidthPos(750), GetRelativeHeightPos(800),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        //endregion

        evaluation.setBounds(GetRelativeWidthPos(850), GetRelativeHeightPos(75),
                GetRelativeWidthPos(100), GetRelativeHeightPos(25));
    }

    private void AddComponents (){
        if (debug) {
            add(depthLabel);
            add(moveTestDepth);

            add(delayLabel);
            add(moveTestDelay);

            add(moveCount);

            add(undo);

            add(moveTest);

            add(setWhite);
            add(setBlack);

            add(showAttackedSquares);
            add(hideAttackedSquares);

            add(fenString);
            add(setFen);

            add(printLegalMoves);

            add(moveScroll);
        } else {
            add(evaluation);
        }
        //region Position Labels
        add(A_POS);
        add(B_POS);
        add(C_POS);
        add(D_POS);
        add(E_POS);
        add(F_POS);
        add(G_POS);
        add(H_POS);

        add(N1_POS);
        add(N2_POS);
        add(N3_POS);
        add(N4_POS);
        add(N5_POS);
        add(N6_POS);
        add(N7_POS);
        add(N8_POS);
        //endregion

        add(DRAW_BOARD);

        MouseListener mouse = new Mouse();
        addMouseListener(mouse);
    }
    //endregion

    //region Methods
    private int GetDepth(){
        return Integer.parseInt(moveTestDepth.getText());
    }

    private void TestMoves() {
        MoveGenerator.Reset();

        if (Float.parseFloat(moveTestDelay.getText()) > 0)
            new Thread(() -> moveCount.setText(String.valueOf(MoveGenerator.MoveGenerationTest(GetDepth(), Float.parseFloat(moveTestDelay.getText()))))).start();
        else moveCount.setText(String.valueOf(MoveGenerator.MoveGenerationTest(GetDepth(), 0)));

        moveDisplay.setText(MoveGenerator.divide.toString());
    }

    private void SetFEN(ActionEvent e){
        FenManager.LoadFromFen(fenString.getText());
    }

    private void SetWhiteToMove (ActionEvent e){
        Board.colorToMove = Piece.WHITE; //NOSONAR
        Board.opponentColor = Piece.BLACK; //NOSONAR
    }

    private void SetBlackToMove (ActionEvent e){
        Board.colorToMove = Piece.BLACK; //NOSONAR
        Board.opponentColor = Piece.WHITE; //NOSONAR
    }

    private void PrintLegal(ActionEvent e){
        List<Move> moves = MoveGenerator.GenerateLegalMoves();

        StringBuilder result = new StringBuilder();
        for (Move move : moves) {
            String moveString = Piece.PosFromIndex(move.startSquare) + " to " + Piece.PosFromIndex(move.targetSquare);
            if (move.promoPiece != Piece.NONE) {
                moveString += " promoting to " + Piece.PieceName(move.promoPiece);
            }
            result.append(moveString).append("\n");
        }

        moveDisplay.setText(result.toString());
    }

    private void ShowAttackedSquares (ActionEvent e){
        attackedSquares = new ArrayList<>(); //NOSONAR

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < 64; i++) {
            if (Board.IsSquareAttacked(i, Board.opponentColor)) {
                attackedSquares.add(i);

                result.append(Piece.PosFromIndex(i))
                        .append(" is attacked by ")
                        .append(Board.WhatIsSquareAttackedBy(i, Board.opponentColor))
                        .append(" at ")
                        .append(Piece.PosFromIndex(Board.FromWhereIsTheSquareAttacked(i, Board.opponentColor)))
                        .append("\n");
            }
        }

        moveDisplay.setText(result.toString());
    }

    private void HideAttackedSquares (ActionEvent e){
        attackedSquares = new ArrayList<>(); //NOSONAR
        moveDisplay.setText("");
    }

    private void Undo(ActionEvent e) { Board.UnmakeMove(); }

    public static List<Integer> GetAttackedSquares (){
        return attackedSquares;
    }

    public static void GetEvaluation(){
        evaluation.setText(String.valueOf(Evaluate.EvaluatePosition()));
    }
    //endregion

    //region Convenience
    private int GetRelativeWidthPos(float pos){
        return Math.round(screenWidth/(1920/pos));
    }

    private int GetRelativeHeightPos(float pos){
        return Math.round(screenHeight/(1080/pos));
    }
    //endregion

    //region Getters
    public static int getScreenHeight () {
        return screenHeight;
    }

    public static int getScreenWidth () {
        return screenWidth;
    }
    //endregion

}
