package com.eoneifour.wms.inboundrate.model;

import java.awt.Color;

public class Bar {
	private String label;
    private int value;
    private Color color;

    public Bar(String label, int value, Color color)
    {
        this.label = label;
        this.value = value;
        this.color = color;
    }

    public String getLabel()
    {
        return label;
    }

    public int getValue()
    {
        return value;
    }

    public Color getColor()
    {
        return color;
    }
}
