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
package net.redstonelamp.response;

public class ChatResponse extends Response{
    public static final String DEFAULT_source = "";
    public static final ChatTranslation DEFAULT_translation = null;

    public String source = DEFAULT_source;
    public String message;
    public ChatTranslation translation = DEFAULT_translation;

    public ChatResponse(String message){
        this.message = message;
    }

    public static class ChatTranslation{
        public String message;
        public final String[] params;

        public ChatTranslation(String message, String[] params){
            this.message = message;
            this.params = params;
        }
        
        @Override
        public String toString() {
        	return String.format(message, (Object[]) params);
        }
    }
}
