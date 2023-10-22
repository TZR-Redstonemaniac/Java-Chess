package GUI;

public class GUI_Manager {

    //region Variables
    public enum GUI_State {
        MAIN_MENU,
        GAME,
        SETTINGS
    }

    public static GUI_State guiMenu = GUI_State.MAIN_MENU;
    //endregion

    public static void Init(){
        GUI.SetMenu(guiMenu);
    }

}
