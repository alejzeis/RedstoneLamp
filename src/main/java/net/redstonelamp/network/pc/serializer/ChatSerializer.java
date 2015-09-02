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
package net.redstonelamp.network.pc.serializer;

import org.json.simple.JSONObject;

public class ChatSerializer{

    private JSONObject chat;

    @SuppressWarnings("unchecked")
    public ChatSerializer(){
        chat = new JSONObject();
        chat.put("text", "");
    }

    public static String toChat(String text){
        ChatSerializer chat = new ChatSerializer();
        chat.setText(text);
        return chat.toString();
    }

    @SuppressWarnings("unchecked")
    public void append(String text){
        String old = chat.get("text").toString();
        chat.remove("text");
        chat.put("text", old + text);
    }

    @SuppressWarnings("unchecked")
    public void setText(String text){
        chat.remove("text");
        chat.put("text", text);
    }

    @Override
    public String toString(){
        return chat.toJSONString();
    }

}
