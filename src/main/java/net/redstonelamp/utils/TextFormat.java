/*
 * This file is part of RedstoneLamp.
 *
 * RedstoneLamp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.redstonelamp.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for coloring/formatting text or chat.
 *
 * @author RedstoneLamp Team
 */
public enum TextFormat {
    BLACK('0', false),
    DARK_BLUE('1', false),
    DARK_GREEN('2', false),
    DARK_AQUA('3', false),
    DARK_RED('4', false),
    DARK_PURPLE('5', false),
    GOLD('6', false),
    GRAY('7', false),
    DARK_GREY('8', false),
    BLUE('9', false),
    GREEN('a', false),
    AQUA('b', false),
    RED('c', false),
    LIGHT_PURPLE('d', false),
    YELLOW('e', false),
    WHITE('f', false),

    OBFUSCATED('k', true),
    BOLD('l', true),
    STRIKETHROUGH('m', true),
    UNDERLINE('n', true),
    ITALIC('o', true),
    RESET('p', false);

    public final static char ESCAPE = '\u00A7';
    public final static Map<Character, TextFormat> mapByChar = new HashMap<>();

    private final char colorCode;
    private final boolean format;
    private final String asString;

    private TextFormat(char colorCode, boolean format){
        this.colorCode = colorCode;
        this.format = format;
        asString = new String(new char[] {ESCAPE, colorCode});
    }

    @Override
    public String toString() {
        return asString;
    }

    public static TextFormat getByChar(char c){
        return mapByChar.get(c);
    }

    static {
        for(TextFormat format : values()){
            mapByChar.put(format.colorCode, format);
        }
    }

    public boolean isFormatChar() {
        return format;
    }

    public static String stripColors(String input) {
        char[] ri = input.toCharArray();
        StringBuilder stripped = new StringBuilder();
        for(int i = 0; i < ri.length; i++) {
            if(ri[i] == ESCAPE)
                i+=2;
            stripped.append(ri[i]);
        }
        return stripped.toString();
    }
}
