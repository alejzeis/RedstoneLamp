package redstonelamp.network.pc;

import org.json.simple.JSONObject;

import redstonelamp.utils.TextFormat;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * A class to simplify sending Chat messages to the PC Client. (The system uses JSON)
 *
 * @author jython234
 */
public class Chat {
    private String rawMessage;

    public Chat(String message){
        rawMessage = message;
    }

    /**
     * Builds the rawMessage into a JSON String.
     * <br>
     * Based on code from the Glowstone project. The original code can be found here:
     * https://github.com/GlowstoneMC/Glowstone/blob/master/src/main/java/net/glowstone/util/TextMessage.java
     * @return The Chat message, as a JSON string.
     */
    @SuppressWarnings("unchecked")
	public String buildJSON() {
        List<JSONObject> objects = new ArrayList<>();
        StringBuilder sb = new StringBuilder(); //The builder for the message
        TextFormat color = null;
        Set<TextFormat> formatting = EnumSet.noneOf(TextFormat.class);
        for(int i = 0; i < rawMessage.length(); i++){
            char c = rawMessage.charAt(i);
            if(c != TextFormat.ESCAPE){
                sb.append(c);
                continue;
            }

            if(i == rawMessage.length() - 1){
                continue;
            }

            append(objects, sb, color, formatting);
            TextFormat code = TextFormat.getByChar(rawMessage.charAt(i++));
            if(code == null){
                continue;
            }
            if(code == TextFormat.RESET){
                code = null;
                formatting.clear();
            } else if (code.isFormatChar()){
                formatting.add(code);
            } else {
                formatting.clear();
            }
        }
        append(objects, sb, color, formatting);

        if(objects.isEmpty()){
            JSONObject obj = new JSONObject();
            obj.put("text", "");
            return obj.toJSONString();
        } else if (objects.size() == 1){
            return objects.get(0).toJSONString();
        } else {
            JSONObject obj = objects.get(0);
            if(obj.size() == 1){
                obj.put("extra", objects.subList(1, objects.size()));
            } else {
                obj = new JSONObject();
                obj.put("text", "");
                obj.put("extra", objects);
            }
            return obj.toJSONString();
        }
    }

    /**
     * Based on code from the Glowstone project. The original code can be found here:
     * https://github.com/GlowstoneMC/Glowstone/blob/master/src/main/java/net/glowstone/util/TextMessage.java
     * @param objects
     * @param sb
     * @param color
     * @param formatting
     */
    @SuppressWarnings("unchecked")
	private void append(List<JSONObject> objects, StringBuilder sb, TextFormat color, Set<TextFormat> formatting) {
        if(sb.length() == 0){
            return;
        }

        JSONObject obj = new JSONObject();
        obj.put("text", sb.toString());
        if(color != null){
            obj.put("color", color.name().toLowerCase());
        }
        for(TextFormat format : formatting){
            obj.put(format.name().toLowerCase(), true);
        }
        sb.setLength(0);
        objects.add(obj);
    }

    @Override
    public String toString() {
        return buildJSON();
    }
}
