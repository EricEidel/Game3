package GUI;

import game_3_core.SimpleGame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import sui.Display;
import sui.ScrollPane;
import sui.TextArea;

public class MyChat 
{
	ScrollPane pane;
	TextArea area;
	
	public MyChat()
	{
		Display display = SimpleGame.getDisplay();
		area = new TextArea("", 64, 10);
		pane = new ScrollPane(area);
        pane.setLocation(10, 570);
        pane.setSize(area.getWidth(), area.getHeight());
        
        display.add(pane);
	}
	
	public void update()
	{
		String msg = "11111111HI!!! fsjdhfkjasd ajsdhfkjsahdfkjsahf aksjd hfakjsdhfkajsdhf ksajdfh aksjdfhaskdjfhasdkf 11111111HI!!! fsjdhfkjasd ajsdhfkjsahdfkjsahf aksjd hfakjsdhfkajsdhf ksajdfh aksjdfhaskdjfhasdkf 11111111HI!!! fsjdhfkjasd ajsdhfkjsahdfkjsahf aksjd hfakjsdhfkajsdhf ksajdfh This is the end";
		area.setText(area.getText()+"\n"+msg);
		/*tbb.newMessage("22222222222HI!!!", "OMG HAHAHA");
		tbb.newMessage("3333333333HI!!! MY NAME IS OMG HAHAHA", "OMG HAHAHA");
		tbb.newMessage("44444444444HI!!! MY NAME IS OMG HAHAHA", "OMG HAHAHA");
		tbb.newMessage("555555555HI!!! MY NAME IS OMG HAHAHA", "OMG HAHAHA");
		tbb.newMessage("6666666666HI!!! MY NAME IS OMG HAHAHA", "OMG HAHAHA");
		tbb.newMessage("7777777777777HI!!! MY NAME IS OMG HAHAHA", "OMG HAHAHA");*/
	}
}
