package view;

import java.awt.*;

/**
 * Created by Laptop-6 on 2017/11/15.
 */
public class ComponentConfig {
    public static int INSECT = 5; // 组件之间的间距
    public static int MAIN_HEIGHT;
    public static int MAIN_WIDTH;
    public static int LEFT_PANEL_WIDTH;
    public static int CENTER_PANEL_WIDTH;
    public static int RIGHT_PANEL_WIDTH;
    public static Font COMMON_FONT;
    public static Font ADDR_FONT;
    public static int CELL_WIDTH = 35;
    public static int CELL_HEIGHT = 35;

    static {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        initCommonFont();

        MAIN_HEIGHT = (int) screenSize.getHeight();
        MAIN_HEIGHT = MAIN_HEIGHT * 90 / 100;
        MAIN_WIDTH = (int) screenSize.getWidth();
        LEFT_PANEL_WIDTH = MAIN_WIDTH * 3 / 10;
        CENTER_PANEL_WIDTH = MAIN_WIDTH / 2;
        RIGHT_PANEL_WIDTH = MAIN_WIDTH / 10;

    }

    public static void initCommonFont() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int) screenSize.getWidth();
        int h = (int) screenSize.getHeight();
        int fontSize = 16;
        if(w == 1600 && h == 900){
            fontSize = 16;
        }
        if(w == 1440 && h == 900){
            fontSize = 15;
        }
        if(w == 1366 && h == 768){
            fontSize = 14;
        }
        if(w == 1360 && h == 768){
            fontSize = 13;
        }
        if(w == 1280 && h == 800){
            fontSize = 13;
        }
        if(w == 1280 && h == 768){
            fontSize = 12;
        }
        if(w == 1280 && h == 720){
            fontSize = 12;
        }
        if(w == 1024 && h == 768){
            fontSize = 12;
        }
        if(w == 800 && h == 600){
            fontSize = 10;
        }
        COMMON_FONT = new Font("sansserif", 0, fontSize);
        ADDR_FONT = new Font("sansserif", 0, 9);
    }
}
