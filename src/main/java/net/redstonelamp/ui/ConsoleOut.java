/**
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
package net.redstonelamp.ui;

/**
 * Represents an output to the console.
 *
 * @author RedstoneLamp Team
 */
public interface ConsoleOut {
    /**
     * Write a TRACE Level message to this <code>ConsoleOut</code>.
     * @param msg The Message to be written.
     */
    void trace(String msg);
    /**
     * Write a DEBUG Level message to this <code>ConsoleOut</code>.
     * @param msg The Message to be written.
     */
    void debug(String msg);
    /**
     * Write an INFO Level message to this <code>ConsoleOut</code>.
     * @param msg The Message to be written.
     */
    void info(String msg);
    /**
     * Write a WARNING Level message to this <code>ConsoleOut</code>.
     * @param msg The Message to be written.
     */
    void warning(String msg);
    /**
     * Write an ERROR Level message to this <code>ConsoleOut</code>.
     * @param msg The Message to be written.
     */
    void error(String msg);
    /**
     * Write a FATAL Level message to this <code>ConsoleOut</code>.
     * @param msg The Message to be written.
     */
    void fatal(String msg);
}
