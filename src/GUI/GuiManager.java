package GUI;

public class GuiManager {

    //region Variables
    public enum GUI_State {
        MAIN_MENU,
        GAME,
        SETTINGS
    }

    private static GUI_State guiMenu = GUI_State.MAIN_MENU;
    //endregion

    public static void Init(){
        GUI.SetMenu(guiMenu);
    }

    public static void SetGuiMenu(GUI_State state){
        guiMenu = state;
    }

    public static GUI_State GetGuiMenu () {
        return guiMenu;
    }
}
