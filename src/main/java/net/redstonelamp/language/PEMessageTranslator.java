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
package net.redstonelamp.language;


import net.redstonelamp.response.ChatResponse;
import net.redstonelamp.utils.TextFormat;

/**
 * A Message translator for the MCPE protocol.
 *
 * @author RedstoneLamp Team
 */
public class PEMessageTranslator implements MessageTranslator {

    @Override
    public ChatResponse.ChatTranslation translate(ChatResponse.ChatTranslation translation) {
        ChatResponse.ChatTranslation cr = new ChatResponse.ChatTranslation(translation.message, translation.params);
        TextFormat color;
        if(cr.message.startsWith(String.valueOf(TextFormat.ESCAPE))) {
            char colorChar = cr.message.toCharArray()[1];
            color = TextFormat.getByChar(colorChar);
        } else {
            color = TextFormat.WHITE;
        }
        switch (TextFormat.stripColors(cr.message)) {
            case "redstonelamp.translation.player.joined":
                cr.message = color + "%multiplayer.player.joined";
                break;
            case "redstonelamp.translation.player.left":
                cr.message = color + "%multiplayer.player.left";
                break;
            case "redstonelamp.translation.command.usage":
                cr.message = color + "%commands.generic.usage";
                break;
            case "redstonelamp.translation.command.help.header":
                cr.message = color + "%commands.help.header";
                break;
            case "redstonelamp.translation.command.help.listEntry":
                cr.message = color + "/"+cr.params[0]+" - "+cr.params[1];
                break;
            default:
                return null; //Signal manager to use server-side translation.
        }
        return cr;
    }
}