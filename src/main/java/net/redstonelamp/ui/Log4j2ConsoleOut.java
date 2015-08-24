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

import org.apache.logging.log4j.*;
import org.apache.logging.log4j.Logger;

/**
 * An implementation of <code>ConsoleOut</code> that writes to a log4j2 logger.
 *
 * @author RedstoneLamp Team
 */
public class Log4j2ConsoleOut extends ConsoleOut{
    private final org.apache.logging.log4j.Logger logger;

    /**
     * Create a new <code>Log4j2ConsoleOut</code> with the specified log4j2 Logger.
     * @param logger The log4j2 logger.
     */
    public Log4j2ConsoleOut(Logger logger) {
        super(logger.getName());
        this.logger = logger;
    }

    /**
     * Create a new <code>Log4j2ConsoleOut</code> with the specified logger name. A new underlying log4j2 logger will be created.
     * @param name The log4j2 Logger name.
     */
    public Log4j2ConsoleOut(String name) {
        super(name);
        logger = LogManager.getLogger(name);
    }

    @Override
    public void trace(String msg) {
        logger.trace(msg);
    }

    @Override
    public void debug(String msg) {
        logger.debug(msg);
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public void warning(String msg) {
        logger.warn(msg);
    }

    @Override
    public void error(String msg) {
        logger.error(msg);
    }

    @Override
    public void fatal(String msg) {
        logger.fatal(msg);
    }
}
