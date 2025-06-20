package com.eoneifour.common.util;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Form 필드 생성 유틸
 */
public class FieldUtil {
	// 기본 텍스트 필드 라벨
    public static JPanel createField(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 36));
        field.setPreferredSize(new Dimension(300, 36));

        panel.add(label);
        panel.add(field);
        return panel;
    }

    // 기본 라벨 + 버튼 필드
    public static JPanel createFieldWithButton(String labelText, JTextField field, JButton button) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 36));
        field.setPreferredSize(new Dimension(190, 36));
        field.setMinimumSize(new Dimension(190, 36));
        field.setBorder(BorderFactory.createCompoundBorder(
    	    field.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)
    	));
        button.setPreferredSize(new Dimension(90, 36));
        button.setMinimumSize(new Dimension(90, 36));
        button.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        
        panel.add(label);
        panel.add(field);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(button);
        return panel;
    }

    // 기본 콤보박스 필드
    public static JPanel createComboField(String labelText, JComboBox<String> comboBox) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 36));
        comboBox.setPreferredSize(new Dimension(300, 36));

        panel.add(label);
        panel.add(comboBox);
        return panel;
    }
}
