package com.eoneifour.wms.home.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.eoneifour.common.util.ImgUtil;


/**
 * HomePage 백그라운드 이미지 설정
 * @author JH
 */
public class HomePage extends JPanel{
	
	private JFrame mainFrame;
	private Image backgroundImage;
	
	public HomePage(MainFrame frame) {
		this.mainFrame = frame;
		
		ImgUtil imgUtil = new ImgUtil();
//		backgroundImage = imgUtil.getImage("images/homeImage1.png", 1280, 800);
		backgroundImage = imgUtil.getImage("images/homeImage2.png", 1280, 800);
		
		setLayout(null); // 필요 시 컴포넌트 위치 수동 조절
		setOpaque(false); // 배경 투명화
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if(backgroundImage != null) {
			// 패널 크기에 맞게 이미지 늘려 그리기
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		}
	}
}
