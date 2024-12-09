package me.dumplingdash.crackBusters.Utility;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.ArrayList;

public class ColorUtils {
    /**
     * Applies Color to string
     * @param text use #n# to choose nth color in colors, \# to add a # to string
     * @param colors
     * @return
     */
    public static String colorizeText(String text, ArrayList<Color> colors) {
        StringBuilder string = new StringBuilder();
        String previous = "";
        Color color = new Color(255,255,255);
        for(int i = 0; i < text.length(); ++i) {
            String current = String.valueOf(text.charAt(i));

            // Check if color change
            if(current.equalsIgnoreCase("#") && !previous.equalsIgnoreCase("\\")) {
                if (i + 1 < text.length()) { // Check if index i + 1 is not out of bounds
                    int endColorNumber = text.indexOf("#", i + 1);
                    if (endColorNumber != -1) { // Check if another # was found
                        // get index of color
                        int newColorIndex = -1;
                        try {
                            newColorIndex = Integer.parseInt(text.substring(i + 1, endColorNumber));
                        } catch (NumberFormatException e) {
                            System.out.println("NumberFormatException : Failed to colorize string " + text);
                        }
                        // check index is valid
                        if (newColorIndex != -1 && newColorIndex < colors.size()) {
                            color = colors.get(newColorIndex);
                            i = endColorNumber;
                            previous = current;
                            continue;
                        }
                    }
                }
            }

            string.append(ChatColor.of(color)).append(current);
            previous = current;
        }
        return string.toString() + ChatColor.of(new Color(255, 255, 255));
    }

    /**
     * Darkens the given color by specified amount, black(0) - same(1)
     * @param color
     * @param amount
     * @return darkened color
     */
    public static Color darkenColor(Color color, float amount) {
        int red = (int) (color.getRed() * amount);
        int green = (int) (color.getGreen() * amount);
        int blue = (int) (color.getBlue() * amount);

        return new Color(red, green, blue);
    }
}
