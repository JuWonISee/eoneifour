package com.eoneifour.common.util;

import javax.swing.ImageIcon;

/**
 * 이미지 관련 클래스
 * @author JH재환
 * @since 2025. 6. 15.
 */
public class ImgUtil {
	public ImgUtil() {}

	public static void getIcon(String path) {
		ImageIcon icon = new ImageIcon(path);
	}
	
}
