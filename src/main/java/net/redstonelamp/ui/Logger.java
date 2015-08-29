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

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Represents a Logger for RedstoneLamp. You can create loggers that log to different <code>ConsoleOut</code>'s
 *
 * @author RedstoneLamp Team
 */
public class Logger{
    private final ConsoleOut out;

    /**
     * Create a new <code>Logger</code> that writes to the specified <code>ConsoleOut</code>
     *
     * @param out The <code>ConsoleOut</code> that this logger will write to.
     */
    public Logger(ConsoleOut out){
        this.out = out;
    }

    /**
     * Log a TRACE level message to the underlying <code>ConsoleOut</code>
     *
     * @param msg  The Message to be logged.
     * @param args Any arguments used for formatting (If your <code>msg</code> contains formatting characters)
     */
    public void trace(String msg, Object... args){
        synchronized(out){
            out.trace(String.format(msg, args));
        }
    }

    /**
     * Log a DEBUG level message to the underlying <code>ConsoleOut</code>
     *
     * @param msg  The Message to be logged.
     * @param args Any arguments used for formatting (If your <code>msg</code> contains formatting characters)
     */
    public void debug(String msg, Object... args){
        synchronized(out){
            out.debug(String.format(msg, args));
        }
    }

    /**
     * Log an INFO level message to the underlying <code>ConsoleOut</code>
     *
     * @param msg  The Message to be logged.
     * @param args Any arguments used for formatting (If your <code>msg</code> contains formatting characters)
     */
    public void info(String msg, Object... args){
        synchronized(out){
            out.info(String.format(msg, args));
        }
    }

    /**
     * Log a WARNING level message to the underlying <code>ConsoleOut</code>
     *
     * @param msg  The Message to be logged.
     * @param args Any arguments used for formatting (If your <code>msg</code> contains formatting characters)
     */
    public void warning(String msg, Object... args){
        synchronized(out){
            out.warning(String.format(msg, args));
        }
    }

    /**
     * Log an ERROR level message to the underlying <code>ConsoleOut</code>
     *
     * @param msg  The Message to be logged.
     * @param args Any arguments used for formatting (If your <code>msg</code> contains formatting characters)
     */
    public void error(String msg, Object... args){
        synchronized(out){
            out.error(String.format(msg, args));
        }
    }

    /**
     * Log a FATAL level message to the underlying <code>ConsoleOut</code>
     *
     * @param msg  The Message to be logged.
     * @param args Any arguments used for formatting (If your <code>msg</code> contains formatting characters)
     */
    public void fatal(String msg, Object... args){
        synchronized(out){
            out.fatal(String.format(msg, args));
        }
    }

    /**
     * Log a <code>byte[]</code> buffer (in hex) as a DEBUG level message to the underlying <code>ConsoleOut</code>
     *
     * @param prefix The Prefix string before the Buffer.
     * @param buffer The <code>byte[]</code> to be logged (when logged, the buffer will be printed in hex)
     * @param suffix The Suffix string after the buffer.
     */
    public void buffer(String prefix, byte[] buffer, String suffix){
        StringBuilder out = new StringBuilder(buffer.length * 3);
        for(byte bite : buffer){
            String string = Integer.toHexString(bite & 0xFF);
            while(string.length() < 2){
                string = "0" + string;
            }
            out.append(string);
            out.append(',');
        }
        debug(prefix + "0x" + out.toString() + suffix);
    }

    /**
     * Log a Throwable as a TRACE level message to the underlying <code>ConsoleOut</code>
     *
     * @param t The Throwable to be logged.
     */
    public void trace(Throwable t){
        StringWriter writer = new StringWriter();
        t.printStackTrace(new PrintWriter(writer));
        for(String str : writer.toString().split("[\r\n]+")){
            trace(str);
        }
    }

    public Class<? extends ConsoleOut> getConsoleOutClass(){
        return out.getClass();
    }
}
