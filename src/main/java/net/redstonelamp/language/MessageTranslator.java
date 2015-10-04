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

/**
 * Represents a translator for a specific protocol.
 *
 * @author RedstoneLamp Team
 */
public interface MessageTranslator {
    /**
     * Translates a RedstoneLamp constant to a message for a specific protocol.
     * @param translation The original RedstoneLamp constant translation.
     * @return The protocol-specific translation.
     */
    ChatResponse.ChatTranslation translate(ChatResponse.ChatTranslation translation);
}
