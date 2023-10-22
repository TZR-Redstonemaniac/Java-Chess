package GUI;

import Classes.*;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static GUI.GuiManager.GUI_State.*;

public class GUI extends JFrame {

    //region Variables
    private static final CardLayout cardLayout = new CardLayout();
    private static final JPanel CARD_PANEL = new JPanel(cardLayout);

    public static final JPanel MAIN_PANEL = new JPanel(new MigLayout(new LC().fillX().fillY()));
    public static final JPanel SETTINGS_PANEL = new JPanel(new MigLayout(new LC().fillX().fillY()));

    public static final Draw PLAY_PANEL = new Draw();

    //region Main Menu
    public static final JLabel MAIN_TITLE = new JLabel("Khalid's Chess");

    private static final JButton PLAY = new JButton("Play");
    private static final JButton SETTINGS_BUTTON = new JButton("Settings");
    //endregion

    //region Main Menu
    public static final JLabel SETTINGS_TITLE = new JLabel("Settings");
    //endregion

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

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    private static final int SCREEN_WIDTH = (int) Math.round(SCREEN_SIZE.getWidth());
    private static final int SCREEN_HEIGHT = (int) Math.round(SCREEN_SIZE.getHeight());

    private static final String CENTER = "center";
    private static final String TOP = "top";

    private static final CC ALIGN_TOP_CENTER = new CC().alignX(CENTER).alignY(TOP);
    private static final CC ALIGN_CENTER = new CC().alignX(CENTER).alignY(CENTER);

    private static final String ARIAL = "Arial";
    //endregion

    //region Constructors
    public GUI (){
        super("Chess");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setPreferredSize(getSize());
        setMinimumSize(getPreferredSize());
        setContentPane(CARD_PANEL);

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

        MAIN_PANEL.setBackground(Color.DARK_GRAY);

        PLAY_PANEL.setBackground(Color.DARK_GRAY);
        PLAY_PANEL.setLayout(null);

        SETTINGS_PANEL.setBackground(Color.DARK_GRAY);

        //region Main Menu
        MAIN_TITLE.setFont(new Font(ARIAL, Font.BOLD, 50));
        MAIN_TITLE.setHorizontalAlignment(SwingConstants.CENTER);
        MAIN_TITLE.setForeground(Color.WHITE);

        PLAY.setFont(new Font(ARIAL, Font.PLAIN, 25));
        PLAY.setForeground(Color.BLACK);
        PLAY.setBackground(Color.GRAY);
        PLAY.setBorder(null);
        PLAY.setFocusPainted(false);
        PLAY.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PLAY.setBackground(Color.LIGHT_GRAY);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PLAY.setBackground(Color.GRAY);
            }

            @Override
            public void mouseClicked (MouseEvent e) {
                SetMenu(GAME);
            }
        });
        //endregion

        //region Game Menu
        //region Position Labels
        N8_POS.setBounds(GetRelativeWidthPos(1360) + GetRelativeWidthPos(25),
                GetRelativeHeightPos(115) + GetRelativeHeightPos(25),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        N7_POS.setBounds(GetRelativeWidthPos(1360) + GetRelativeWidthPos(25),
                GetRelativeHeightPos(115) + GetRelativeHeightPos(125),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        N6_POS.setBounds(GetRelativeWidthPos(1360) + GetRelativeWidthPos(25),
                GetRelativeHeightPos(115) + GetRelativeHeightPos(225),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        N5_POS.setBounds(GetRelativeWidthPos(1360) + GetRelativeWidthPos(25),
                GetRelativeHeightPos(115) + GetRelativeHeightPos(325),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        N4_POS.setBounds(GetRelativeWidthPos(1360) + GetRelativeWidthPos(25),
                GetRelativeHeightPos(115) + GetRelativeHeightPos(425),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        N3_POS.setBounds(GetRelativeWidthPos(1360) + GetRelativeWidthPos(25),
                GetRelativeHeightPos(115) + GetRelativeHeightPos(525),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        N2_POS.setBounds(GetRelativeWidthPos(1360) + GetRelativeWidthPos(25),
                GetRelativeHeightPos(115) + GetRelativeHeightPos(625),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        N1_POS.setBounds(GetRelativeWidthPos(1360) + GetRelativeWidthPos(25),
                GetRelativeHeightPos(115) + GetRelativeHeightPos(725),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));

        N8_POS.setForeground(Color.WHITE);
        N7_POS.setForeground(Color.WHITE);
        N6_POS.setForeground(Color.WHITE);
        N5_POS.setForeground(Color.WHITE);
        N4_POS.setForeground(Color.WHITE);
        N3_POS.setForeground(Color.WHITE);
        N2_POS.setForeground(Color.WHITE);
        N1_POS.setForeground(Color.WHITE);

        N8_POS.setFont(new Font(ARIAL, Font.BOLD, 20));
        N7_POS.setFont(new Font(ARIAL, Font.BOLD, 20));
        N6_POS.setFont(new Font(ARIAL, Font.BOLD, 20));
        N5_POS.setFont(new Font(ARIAL, Font.BOLD, 20));
        N4_POS.setFont(new Font(ARIAL, Font.BOLD, 20));
        N3_POS.setFont(new Font(ARIAL, Font.BOLD, 20));
        N2_POS.setFont(new Font(ARIAL, Font.BOLD, 20));
        N1_POS.setFont(new Font(ARIAL, Font.BOLD, 20));

        A_POS.setBounds(GetRelativeWidthPos(560) + GetRelativeWidthPos(50), GetRelativeHeightPos(925),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        B_POS.setBounds(GetRelativeWidthPos(560) + GetRelativeWidthPos(150), GetRelativeHeightPos(925),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        C_POS.setBounds(GetRelativeWidthPos(560) + GetRelativeWidthPos(250), GetRelativeHeightPos(925),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        D_POS.setBounds(GetRelativeWidthPos(560) + GetRelativeWidthPos(350), GetRelativeHeightPos(925),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        E_POS.setBounds(GetRelativeWidthPos(560) + GetRelativeWidthPos(450), GetRelativeHeightPos(925),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        F_POS.setBounds(GetRelativeWidthPos(560) + GetRelativeWidthPos(550), GetRelativeHeightPos(925),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        G_POS.setBounds(GetRelativeWidthPos(560) + GetRelativeWidthPos(650), GetRelativeHeightPos(925),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));
        H_POS.setBounds(GetRelativeWidthPos(560) + GetRelativeWidthPos(750), GetRelativeHeightPos(925),
                GetRelativeWidthPos(50), GetRelativeHeightPos(50));

        A_POS.setForeground(Color.WHITE);
        B_POS.setForeground(Color.WHITE);
        C_POS.setForeground(Color.WHITE);
        D_POS.setForeground(Color.WHITE);
        E_POS.setForeground(Color.WHITE);
        F_POS.setForeground(Color.WHITE);
        G_POS.setForeground(Color.WHITE);
        H_POS.setForeground(Color.WHITE);

        A_POS.setFont(new Font(ARIAL, Font.BOLD, 20));
        B_POS.setFont(new Font(ARIAL, Font.BOLD, 20));
        C_POS.setFont(new Font(ARIAL, Font.BOLD, 20));
        D_POS.setFont(new Font(ARIAL, Font.BOLD, 20));
        E_POS.setFont(new Font(ARIAL, Font.BOLD, 20));
        F_POS.setFont(new Font(ARIAL, Font.BOLD, 20));
        G_POS.setFont(new Font(ARIAL, Font.BOLD, 20));
        H_POS.setFont(new Font(ARIAL, Font.BOLD, 20));
        //endregion
        //endregion

        //region Settings Menu
        SETTINGS_TITLE.setFont(new Font(ARIAL, Font.BOLD, 50));
        SETTINGS_TITLE.setHorizontalAlignment(SwingConstants.CENTER);
        SETTINGS_TITLE.setForeground(Color.WHITE);

        SETTINGS_BUTTON.setFont(new Font(ARIAL, Font.PLAIN, 25));
        SETTINGS_BUTTON.setForeground(Color.BLACK);
        SETTINGS_BUTTON.setBackground(Color.GRAY);
        SETTINGS_BUTTON.setBorder(null);
        SETTINGS_BUTTON.setFocusPainted(false);
        SETTINGS_BUTTON.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SETTINGS_BUTTON.setBackground(Color.LIGHT_GRAY);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SETTINGS_BUTTON.setBackground(Color.GRAY);
            }

            @Override
            public void mouseClicked (MouseEvent e) {
                SetMenu(SETTINGS);
            }
        });
        //endregion
    }

    private void AddComponents (){
        CARD_PANEL.add(MAIN_PANEL, "MAIN");
        CARD_PANEL.add(PLAY_PANEL, "PLAY");
        CARD_PANEL.add(SETTINGS_PANEL, "SETTINGS");

        //region Main Menu
        MAIN_PANEL.add(MAIN_TITLE, ALIGN_TOP_CENTER.gapY("75", "0").wrap());

        MAIN_PANEL.add(PLAY, ALIGN_CENTER.gapY("0", "400")
                .minWidth(String.valueOf(GetRelativeWidthPos(500)))
                .minHeight(String.valueOf(GetRelativeHeightPos(100)))
                .wrap());

        MAIN_PANEL.add(SETTINGS_BUTTON, ALIGN_CENTER.gapY("0", "0")
                .minWidth(String.valueOf(GetRelativeWidthPos(500)))
                .minHeight(String.valueOf(GetRelativeHeightPos(100))));
        //endregion

        //region Game Menu
        //region Position Labels
        PLAY_PANEL.add(A_POS);
        PLAY_PANEL.add(B_POS);
        PLAY_PANEL.add(C_POS);
        PLAY_PANEL.add(D_POS);
        PLAY_PANEL.add(E_POS);
        PLAY_PANEL.add(F_POS);
        PLAY_PANEL.add(G_POS);
        PLAY_PANEL.add(H_POS);

        PLAY_PANEL.add(N1_POS);
        PLAY_PANEL.add(N2_POS);
        PLAY_PANEL.add(N3_POS);
        PLAY_PANEL.add(N4_POS);
        PLAY_PANEL.add(N5_POS);
        PLAY_PANEL.add(N6_POS);
        PLAY_PANEL.add(N7_POS);
        PLAY_PANEL.add(N8_POS);
        //endregion
        //endregion

        //region Settings Menu
        SETTINGS_PANEL.add(SETTINGS_TITLE, ALIGN_TOP_CENTER.gapY("75", "0").wrap());
        //endregion

        MouseListener mouse = new Mouse();
        addMouseListener(mouse);
    }
    //endregion

    //region Change Menu Functions
    public static void SetMenu (GuiManager.GUI_State state) {
        switch (state) {
            case MAIN_MENU -> {
                cardLayout.show(CARD_PANEL, "MAIN");
                GuiManager.SetGuiMenu(MAIN_MENU);
            }
            case GAME -> {
                cardLayout.show(CARD_PANEL, "PLAY");
                GuiManager.SetGuiMenu(GAME);
            }
            case SETTINGS -> {
                cardLayout.show(CARD_PANEL, "SETTINGS");
                GuiManager.SetGuiMenu(SETTINGS);
            }
        }
    }
    //endregion

    //region Convenience
    private static int GetRelativeWidthPos (float pos){
        return Math.round(SCREEN_WIDTH/(1920/pos));
    }

    private static int GetRelativeHeightPos (float pos){
        return Math.round(SCREEN_HEIGHT /(1080/pos));
    }
    //endregion

    //region Getters
    public static int GetScreenHeight () {
        return SCREEN_HEIGHT;
    }

    public static int GetScreenWidth () {
        return SCREEN_WIDTH;
    }
    //endregion

}
