package redstonelamp.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

public class ServerIcon {

	private BufferedImage image;
	private String base64;
	private byte[] raw;

	public ServerIcon(File file) {
		try {
			ByteArrayOutputStream imgOut = new ByteArrayOutputStream();
			this.image = ImageIO.read(file);
			ImageIO.write(image, "png", imgOut);
			this.raw = imgOut.toByteArray();
			this.base64 = new String(Base64.getEncoder().encode(this.raw));

			if (image.getWidth() != 64 || image.getHeight() != 64)
				throw new IOException(
						"The server icon dimensions can not be different from 64x64!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BufferedImage getImage() {
		return this.image;
	}

	public String toString() {
		return ("data:image/png;base64," + this.base64);
	}

	public byte[] toByteArray() {
		return this.raw;
	}

}
