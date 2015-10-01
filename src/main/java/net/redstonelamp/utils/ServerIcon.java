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
