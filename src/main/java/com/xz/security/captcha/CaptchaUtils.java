package com.xz.security.captcha;

import nl.captcha.Captcha;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class CaptchaUtils {

	public static String encodeBase64(Captcha captcha) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(captcha.getImage(), "png", bos);
			return DatatypeConverter.printBase64Binary(bos.toByteArray());
		} catch (IOException e) {
			return "";
		}
	}

}
