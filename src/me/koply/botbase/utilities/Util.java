package me.koply.botbase.utilities;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * @author Koply
 * @since 03/06/2020
 */
public final class Util {
    private final static Random random = new Random();
    public static Color randomColor() {
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    public static EmbedBuilder basicEmbed(String desc) {
        return new EmbedBuilder().setColor(randomColor()).setDescription(desc);
    }

    public static String readAll(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null){
                sb.append(line);
            }
            line = sb.toString();
            br.close();
            return line;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void writeToFile(File file, String str) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(str);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}