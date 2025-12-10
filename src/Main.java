import db.DbManager;
import ui.GameFrame;

import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DbManager dbManager = new DbManager();
            dbManager.init(); // creates tables if DB is available

            GameFrame frame = new GameFrame(dbManager);
            frame.setVisible(true);
        });
    }
}