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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;

/**
 * Used by the server at startup to allow the informal use of
 * System.out.printXXX(); with the output as if it was the logger printing it
 * 
 * @author RedstoneLamp Team
 */
public class SystemConsoleOut extends PrintStream {

	private final Logger logger;

	public SystemConsoleOut(Logger logger) {
		super(new ByteArrayOutputStream());
		this.logger = logger;
	}

	@Override
	public void print(boolean b) {
		logger.info(Boolean.toString(b));
	}

	@Override
	public void print(char c) {
		logger.info(Character.toString(c));
	}

	@Override
	public void print(char[] c) {
		logger.info(new String(c));
	}

	@Override
	public void print(double d) {
		logger.info(Double.toString(d));
	}

	@Override
	public void print(float f) {
		logger.info(Float.toString(f));
	}

	@Override
	public void print(int i) {
		logger.info(Integer.toString(i));
	}

	@Override
	public void print(Object o) {
		logger.info(o.toString());
	}

	@Override
	public void print(String s) {
		logger.info(s);
	}

	@Override
	public PrintStream printf(String s, Object... args) {
		logger.info(s, args);
		return null;
	}

	@Override
	public PrintStream printf(Locale l, String s, Object... args) {
		logger.info(s, args);
		return null;
	}

	@Override
	public void println() {
		logger.info("");
	}

	@Override
	public void println(boolean b) {
		logger.info(Boolean.toString(b));
	}

	@Override
	public void println(char c) {
		logger.info(Character.toString(c));
	}

	@Override
	public void println(char[] c) {
		logger.info(new String(c));
	}

	@Override
	public void println(double d) {
		logger.info(Double.toString(d));
	}

	@Override
	public void println(float f) {
		logger.info(Float.toString(f));
	}

	@Override
	public void println(int i) {
		logger.info(Integer.toString(i));
	}

	@Override
	public void println(Object o) {
		logger.info(o.toString());
	}

	@Override
	public void println(String s) {
		logger.info(s);
	}

}
