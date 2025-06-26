package com.eoneifour.wms.monitoring.view;

import com.eoneifour.wms.home.view.MainFrame;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MonitoringPage extends JPanel {
    private final Map<String, Rectangle> rectangleMap = new HashMap<>();
    private final Map<String, Color> colorMap = new HashMap<>();

    public MonitoringPage(MainFrame mainFrame) {
        initRectangles();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (String key : rectangleMap.keySet()) {
            Rectangle rect = rectangleMap.get(key);
            g2.setColor(colorMap.getOrDefault(key, Color.LIGHT_GRAY));
            g2.fill(rect);
            g2.setColor(Color.BLACK);
            g2.draw(rect);

            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(key);
            int textHeight = fm.getAscent();
            int centerX = rect.x + (rect.width - textWidth) / 2;
            int centerY = rect.y + (rect.height + textHeight) / 2 - 4;

            g2.drawString(key, centerX, centerY);
        }
    }
    
    
    private void initRectangles() {
        int x = 100, y = 20;
        for (int i = 401; i <= 412; i++) {
            String key = String.valueOf(i);
            
            if (i == 401 || i == 403 || i == 411) {
                rectangleMap.put(key, new Rectangle(x, y, 150, 50));
                x += 150;
            } else {
                rectangleMap.put(key, new Rectangle(x, y, 50, 50));
                
                if(i == 402 || i == 404 || i == 410 || i == 412) {
            		int rackNum = 0;
            		
            		switch (i) {
						case 402 : rackNum = 226; break;
						case 404 : rackNum = 216; break;
						case 410 : rackNum = 126; break;
						case 412 : rackNum = 116; break;
					}
            		
            		int y2 = y + 50;
            		for(int j = rackNum; j <= (rackNum+1); j++) {
            			String key2 = String.valueOf(j);
            			rectangleMap.put(key2, new Rectangle(x, y2, 50, 50));
            			y2 += 50;
            		}
            	}
                
                x += 50;
            }
        }
        
//        colorMap.put("401", Color.GREEN);
        
        createRack(250, 200, 47);
        createRack(450, 200, 37);        
        createRack(750, 200, 27);        
        createRack(950, 200, 17);        
        
        x = 250; y = 730;
        
        for (int i = 317; i >= 303; i--) {
            String key = String.valueOf(i);
            
            if (i == 316 || i == 308) {
                rectangleMap.put(key, new Rectangle(x, y, 150, 50));
                x += 150;
            } else {
                rectangleMap.put(key, new Rectangle(x, y, 50, 50));
                
                if(i == 307 || i == 309 || i == 315 || i == 317) {
            		int rackNum = 0;
            		
            		switch (i) {
						case 307 : rackNum = 229; break;
						case 309 : rackNum = 219; break;
						case 315 : rackNum = 129; break;
						case 317 : rackNum = 119; break;
					}
            		
            		int y2 = y - 50;
            		for(int j = rackNum; j <= (rackNum+1); j++) {
            			String key2 = String.valueOf(j);
            			rectangleMap.put(key2, new Rectangle(x, y2, 50, 50));
            			y2 -= 50;
            		}
            	}
                
                x += 50;
            }
        }
        
        int inX = rectangleMap.get("303").x;
        int inY = rectangleMap.get("303").y;
        rectangleMap.put("302", new Rectangle(inX, (inY - 50), 50, 50));
        rectangleMap.put("301", new Rectangle(inX, (inY - 200), 50, 150));

    }

	private void createRack(int x, int y, int rackNum) {
    	for(int i = rackNum; i >= (rackNum -7); i--) {
    		String key = String.valueOf(i);
    		
			rectangleMap.put(key, new Rectangle(x, y, 50, 50));
			y += 50;
		}
	}
}
