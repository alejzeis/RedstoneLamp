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
 * Console out implementation that does nothing.
 *
 * @author RedstoneLamp Team
 */
public class SilentConsoleOut extends ConsoleOut {

    /**
     * Create a new ConsoleOut with the specified <code>name</code>
     *
     * @param name The name of this ConsoleOut
     */
    public SilentConsoleOut(String name) {
        super(name);
    }

    @Override
    public void trace(String msg) {

    }

    @Override
    public void debug(String msg) {

    }

    @Override
    public void info(String msg) {

    }

    @Override
    public void warning(String msg) {

    }

    @Override
    public void error(String msg) {

    }

    @Override
    public void fatal(String msg) {

    }
}
