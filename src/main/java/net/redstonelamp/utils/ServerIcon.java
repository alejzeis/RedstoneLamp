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
package net.redstonelamp.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

public class ServerIcon {
	
	private final BufferedImage image;
	private final String base64;
	private byte[] raw;

	public ServerIcon(File file) throws IOException {
		this.image = ImageIO.read(file);
		if (image.getWidth() != 64 || image.getHeight() != 64)
			throw new IOException("The server icon dimensions can not be different from 64x64!");
		ByteArrayOutputStream imgOut = new ByteArrayOutputStream();
		ImageIO.write(image, "png", imgOut);
		this.raw = imgOut.toByteArray();
		this.base64 = new String(Base64.getEncoder().encode(this.raw));
	}

	/**
	 * Used to get the server icon as a BufferedImage
	 * 
	 * @return The server icon as a BufferedImage
	 * @author Trent Summerlin
	 */
	public BufferedImage getImage() {
		return this.image;
	}

	/**
	 * Used to get the server icon as a Base64 string
	 * 
	 * @return The server icon as a Base64 String
	 * @author Trent Summerlin
	 */
	public String toString() {
		return ("data:image/png;base64," + this.base64);
	}

	/**
	 * Used to get the server icon as a raw byte array
	 * 
	 * @return The server icon as a byte array
	 * @author Trent Summerlin
	 */
	public byte[] toByteArray() {
		return this.raw;
	}
	
}
