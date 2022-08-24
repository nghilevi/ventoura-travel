package com.Mindelo.VentouraServer.Util;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

public class CaptchaUtil {
	// a singleton class
	private static ImageCaptchaService instance = new DefaultManageableImageCaptchaService();

	public static ImageCaptchaService getInstance() {
		return instance;
	}

	/*
	 * Function about Captcha Verification
	 */
	public static boolean validateCaptcha(String captchaId, String inputChars) {
		boolean bValidated = false;
		try {
			bValidated = CaptchaUtil.getInstance().validateResponseForID(
					captchaId, inputChars);
		} catch (CaptchaServiceException cse) {
		}
		return bValidated;
	}
}
