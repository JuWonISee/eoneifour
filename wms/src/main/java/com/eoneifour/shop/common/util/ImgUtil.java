package com.eoneifour.shop.common.util;

import javax.swing.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImgUtil {
    private static final Map<String, ImageIcon> cache = new HashMap<>();

    public static ImageIcon getImageIcon(String filename) {
        if (filename == null || filename.isEmpty()) return null;

        if (cache.containsKey(filename)) {
            return cache.get(filename);
        }
        URL imgUrl = ImgUtil.class.getClassLoader().getResource(filename);
        if (imgUrl != null) {
            ImageIcon icon = new ImageIcon(imgUrl);
            cache.put(filename, icon);
            return icon;
        }
        return null;
    }
}
