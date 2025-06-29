package com.eoneifour.shop.common.util;

import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class ImgUtil {
    // 원본 이미지 캐시
    private static final Map<String, ImageIcon> originalCache = new HashMap<>();
    // 스케일링된 이미지 캐시
    private static final Map<String, ImageIcon> scaledCache = new HashMap<>();

    // 원본 이미지 로드
    public static ImageIcon getImageIcon(String filename) {
        if (filename == null || filename.isEmpty()) return null;

        if (originalCache.containsKey(filename)) {
            return originalCache.get(filename);
        }

        URL imgUrl = ImgUtil.class.getClassLoader().getResource(filename);
        if (imgUrl != null) {
            ImageIcon icon = new ImageIcon(imgUrl);
            originalCache.put(filename, icon);
            return icon;
        }

        return null;
    }

    // 스케일된 이미지 로드 (없으면 만들어서 캐시)
    public static ImageIcon getScaledImageIcon(String filename, int width, int height) {
        if (filename == null || filename.isEmpty()) return null;

        String key = filename + "_" + width + "x" + height;
        if (scaledCache.containsKey(key)) {
            return scaledCache.get(key);
        }

        ImageIcon originalIcon = getImageIcon(filename);
        if (originalIcon != null) {
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            scaledCache.put(key, scaledIcon);
            return scaledIcon;
        }

        return null;
    }
}