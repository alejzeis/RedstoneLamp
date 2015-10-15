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
import org.ini4j.Ini;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
    protected Ini.Section serverTranslations;

    /**
     * Creates a new TranslationManager belonging to a specified <code>server</code>
     * @param server The server this manager belongs to.
     */
    public TranslationManager(Server server) {
        this.server = server;
        translators.put("server", new ServerMessageTranslator(this));
        try {
            loadLanguageFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        forceTranslations = server.getYamlConfig().getBoolean("language.force-server-translations");
    }

    private void loadLanguageFile() throws IOException {
        Ini ini = new Ini();
        InputStream stream = ClassLoader.getSystemResourceAsStream("lang/" + convertLanguageCode(server.getYamlConfig().getString("language.server-language")) + ".lang");
        if(stream == null) {
            server.getLogger().error("Could not find language file: "+convertLanguageCode(server.getYamlConfig().getString("language.server-language")) + ".lang, using default: \"en-US\"");
            stream = ClassLoader.getSystemResourceAsStream("lang/en-US.lang");
        }
        ini.load(stream);
        serverTranslations = ini.get("Translations");
    }

    public String convertLanguageCode(String code) {
    	code = (code == null ? "eng" : code);
        switch (code) {
            case "eng":
                return "en-US";
            default:
                return code;
        }
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
            return translateServerSide(translation);
        }
        if(translators.containsKey(protocol.getClass().getName())) {
            MessageTranslator translator = translators.get(protocol.getClass().getName());
            ChatResponse.ChatTranslation t = translator.translate(translation);
            if(t != null) return t;
            else {
                return translateServerSide(translation);
            }
        } else {
           return translateServerSide(translation);
        }
    }

    /**
     * Translates a RedstoneLamp constant translation to a protocol specific one.
     * @param translation The original RedstoneLamp constant translation.
     * @return A server-side translation for the constant.
     */
    public ChatResponse.ChatTranslation translateServerSide(ChatResponse.ChatTranslation translation) {
        ChatResponse.ChatTranslation t = translators.get("server").translate(translation);
        if(t != null) return t;
        else return translation;
    }
}