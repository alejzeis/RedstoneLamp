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

import java.util.regex.Pattern;

/**
 * The server-side message translator that translates messages
 * directly.
 *
 * @author RedstoneLamp Team
 */
public class ServerMessageTranslator implements MessageTranslator{
    private final TranslationManager mgr;

    public ServerMessageTranslator(TranslationManager mgr) {
        this.mgr = mgr;
    }

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
        String message = TextFormat.stripColors(cr.message);
        String trans = mgr.serverTranslations.get(message);
        if(trans == null) trans = message;
        for(int i = 0; i < cr.params.length; i++) {
            trans = trans.replaceAll(Pattern.quote("%param-"+i), cr.params[i]);
        }
        cr.message = color + trans;
        return cr;
    }
}