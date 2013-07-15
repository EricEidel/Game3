package GUI;

import org.newdawn.slick.Color;

import sui.Display;
import sui.Label;

public class ChatLine 
{
	private Label label;
	private Color defualt = Color.black;
	
	public ChatLine(float x, float f, float g, Display d)
	{
		label = new Label();
		label.setLocation(x, f);
		label.setVisible(true);
		label.pack();
		label.setWidth(g);
		d.add(label);
	}
	
	public void setText(String s)
	{
		label.setText(s);
		label.setForeground(defualt);
		label.pack();
	}
	
	public void setText(String s, Color c)
	{
		label.setForeground(c);
		label.setText(s);
		label.setForeground(defualt);
		label.pack();
	}
	

}
