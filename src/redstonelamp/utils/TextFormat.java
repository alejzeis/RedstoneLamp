package redstonelamp.utils;


import java.util.HashMap;
import java.util.Map;

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
/*
public class TextFormat {
	public static char ESCAPE = '\u00A7';
	
	public static String BLACK = ESCAPE+"0";
	public static String DARK_BLUE = ESCAPE+"1";
	public static String DARK_GREEN = ESCAPE+"2";
	public static String DARK_AQUA = ESCAPE+"3";
	public static String DARK_RED = ESCAPE+"4";
	public static String DARK_PURPLE = ESCAPE+"5";
	public static String GOLD = ESCAPE+"6";
	public static String GRAY = ESCAPE+"7";
	public static String DARK_GREY = ESCAPE+"8";
	public static String BLUE = ESCAPE+"9";
	public static String GREEN = ESCAPE+"a";
	public static String AQUA = ESCAPE+"b";
	public static String RED = ESCAPE+"c";
	public static String LIGHT_PURPLE = ESCAPE+"d";
	public static String YELLOW = ESCAPE+"e";
	public static String WHITE = ESCAPE+"f";
	
	public static String OBFUSCATED = ESCAPE+"k";
	public static String BOLD = ESCAPE+"l";
	public static String STRIKETHROUGH = ESCAPE+"m";
	public static String UNDERLINE = ESCAPE+"n";
	public static String ITALIC = ESCAPE+"o";
	public static String RESET = ESCAPE+"p";

	/**
	 * Get the name of a color by its TextFormat representation.
	 * @param s The TextFormat representation (ESCAPE+char)
	 * @return The name of the color as a string.
	 *
	public static String getName(String s){
		if(s.charAt(0) != ESCAPE){
			throw new IllegalArgumentException("Color must start with ESCAPE character!");
		}
		switch (s.charAt(1)){
			case '0':
				return "black";
			case '1':
				return "darkblue";

		}
	}
}

*/