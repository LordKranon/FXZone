package fxzone.engine.utils;

import javafx.scene.paint.Color;

public class FxUtils
{
    public static String toRGBCode(Color color) {
        return String.format( "#%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 ) );
    }

    public static Color toColor(String colorRGBCode){
        Color color;
        try {
            color = Color.web(colorRGBCode);
        } catch (IllegalArgumentException e){
            color = Color.web("#000000");
        }
        return color;
    }

    public static java.awt.Color toAwtColor(javafx.scene.paint.Color color) {
        java.awt.Color awtColor = new java.awt.Color(
            (float) color.getRed(),
            (float) color.getGreen(),
            (float) color.getBlue(),
            (float) color.getOpacity()
        );
        return awtColor;
    }

    public static Color easeColor(Color color){
        Color easedColor = new Color(
            color.getRed()/2. + .5,
            color.getGreen()/2. + .5,
            color.getBlue()/2. + .5,
            color.getOpacity()
        );
        return easedColor;
    }
}
