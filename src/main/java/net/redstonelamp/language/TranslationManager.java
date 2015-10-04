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

import net.redstonelamp.Server;
import net.redstonelamp.network.Protocol;
import net.redstonelamp.response.ChatResponse;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manager for translations.
 *
 * @author RedstoneLamp Team
 */
public class TranslationManager {
    private final Server server;
    private boolean forceTranslations;
    private Map<String, MessageTranslator> translators = new ConcurrentHashMap<>();

    /**
     * Creates a new TranslationManager belonging to a specified <code>server</code>
     * @param server The server this manager belongs to.
     * @param languageFiles An array with the language file resource paths.
     */
    public TranslationManager(Server server, String[] languageFiles) {
        this.server = server;
        //loadLanguageFiles();

        forceTranslations = server.getYamlConfig().getBoolean("language.force-server-translations");
    }

    public void registerTranslator(Class<? extends Protocol> protocol, MessageTranslator translator) {
        translators.put(protocol.getName(), translator);
    }

    /**
     * Translates a RedstoneLamp constant translation to a protocol specific one.
     * @param protocol The protocol this translation will be translated to.
     *                 If no translator for that protocol is found or the protocol has no
     *                 translation available, the default
     *                 server-side translator will be used.
     * @param translation The original RedstoneLamp constant translation.
     * @return A protocol specific translation or the server-side translation if
     *         force translations are enabled.
     */
    public ChatResponse.ChatTranslation translate(Protocol protocol, ChatResponse.ChatTranslation translation) {
        if(forceTranslations) {
            ChatResponse.ChatTranslation t = translators.get("server").translate(translation);
            if(t != null) return t;
            else return translation;
        }
        if(translators.containsKey(protocol.getClass().getName())) {
            MessageTranslator translator = translators.get(protocol.getClass().getName());
            ChatResponse.ChatTranslation t = translator.translate(translation);
            if(t != null) return t;
            else {
                t = translators.get("server").translate(translation);
                if(t != null) return t;
                else return translation;
            }
        } else {
            ChatResponse.ChatTranslation t = translators.get("server").translate(translation);
            if(t != null) return t;
            else return translation;
        }
    }
}
