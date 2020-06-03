package me.koply.botbase.utilities;

import java.awt.*;
import java.util.Random;

/**
 * @author Koply
 * @since 03/06/2020
 */
public class Util {
    private final static Random random = new Random();
    public static Color randomColor() {
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }
}