package Core;

import Classes.*;

import javax.swing.*;
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
    //endregion

    //region Constructors
    public GUI (){
        super("Chess");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(1000,1080);
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

        depthLabel.setBounds(850, 75, 100, 25);
        moveTestDepth.setBounds(850, 100, 100, 25);

        delayLabel.setBounds(850, 125, 100, 25);
        moveTestDelay.setBounds(850, 150, 100, 25);

        moveCount.setBounds(875, 175, 99999, 25);

        undo.setBounds(850, 300, 100, 25);
        undo.addActionListener(this::Undo);

        moveTest.setBounds(850, 200, 100, 25);
        moveTest.addActionListener(e -> TestMoves());

        setWhite.setBounds(850, 500, 100, 25);
        setWhite.addActionListener(this::SetWhiteToMove);

        setBlack.setBounds(850, 525, 100, 25);
        setBlack.addActionListener(this::SetBlackToMove);

        showAttackedSquares.setBounds(800, 600, 200, 25);
        showAttackedSquares.addActionListener(this::ShowAttackedSquares);

        hideAttackedSquares.setBounds(800, 630, 200, 25);
        hideAttackedSquares.addActionListener(this::HideAttackedSquares);

        fenString.setBounds(850, 700, 100, 25);

        setFen.setBounds(850, 725, 100, 25);
        setFen.addActionListener(this::SetFEN);

        printLegalMoves.setBounds(825, 800, 150, 25);
        printLegalMoves.addActionListener(this::PrintLegal);

        moveDisplay.setEditable(false);
        moveScroll.setBounds(25, 850, 500, 200);
        moveScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        //region Position Labels
        N8_POS.setBounds(810, 25, 50, 50);
        N7_POS.setBounds(810, 125, 50, 50);
        N6_POS.setBounds(810, 225, 50, 50);
        N5_POS.setBounds(810, 325, 50, 50);
        N4_POS.setBounds(810, 425, 50, 50);
        N3_POS.setBounds(810, 525, 50, 50);
        N2_POS.setBounds(810, 625, 50, 50);
        N1_POS.setBounds(810, 725, 50, 50);

        A_POS.setBounds(50, 800, 50, 50);
        B_POS.setBounds(150, 800, 50, 50);
        C_POS.setBounds(250, 800, 50, 50);
        D_POS.setBounds(350, 800, 50, 50);
        E_POS.setBounds(450, 800, 50, 50);
        F_POS.setBounds(550, 800, 50, 50);
        G_POS.setBounds(650, 800, 50, 50);
        H_POS.setBounds(750, 800, 50, 50);
        //endregion
    }

    private void AddComponents (){
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
        Board.colorToMove = Piece.WHITE;
        Board.opponentColor = Piece.BLACK;
    }

    private void SetBlackToMove (ActionEvent e){
        Board.colorToMove = Piece.BLACK;
        Board.opponentColor = Piece.WHITE;
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
    //endregion

}
