package toDo.utilities;

import toDo.gui.main.MainGui;

public class Global {

    private static MainGui mainGui;

    public static void setMainGui(MainGui mainGui) {
        Global.mainGui = mainGui;
    }

    public static MainGui getMainGui() {
        return mainGui;
    }
}
