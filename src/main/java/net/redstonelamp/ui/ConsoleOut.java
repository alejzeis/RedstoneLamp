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
package net.redstonelamp.ui;

/**
 * Represents an output to the console.
 *
 * @author RedstoneLamp Team
 */
public abstract class ConsoleOut{
    private final String name;

    /**
     * Create a new ConsoleOut with the specified <code>name</code>
     *
     * @param name The name of this ConsoleOut
     */
    public ConsoleOut(String name){
        this.name = name;
    }

    /**
     * Write a TRACE Level message to this <code>ConsoleOut</code>.
     *
     * @param msg The Message to be written.
     */
    public abstract void trace(String msg);

    /**
     * Write a DEBUG Level message to this <code>ConsoleOut</code>.
     *
     * @param msg The Message to be written.
     */
    public abstract void debug(String msg);

    /**
     * Write an INFO Level message to this <code>ConsoleOut</code>.
     *
     * @param msg The Message to be written.
     */
    public abstract void info(String msg);

    /**
     * Write a WARNING Level message to this <code>ConsoleOut</code>.
     *
     * @param msg The Message to be written.
     */
    public abstract void warning(String msg);

    /**
     * Write an ERROR Level message to this <code>ConsoleOut</code>.
     *
     * @param msg The Message to be written.
     */
    public abstract void error(String msg);

    /**
     * Write a FATAL Level message to this <code>ConsoleOut</code>.
     *
     * @param msg The Message to be written.
     */
    public abstract void fatal(String msg);

    /**
     * Get the name of the ConsoleOut
     *
     * @return The name of the ConsoleOut
     */
    public String getName(){
        return name;
    }
}
