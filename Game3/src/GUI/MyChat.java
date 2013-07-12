package GUI;

import game_3_core.SimpleGame;

import java.util.ArrayList;

import org.newdawn.slick.Color;

import sui.Button;
import sui.Display;
import sui.TextArea;
import sui.TextField;
import sui.event.ActionEvent;
import sui.event.ActionListener;
import creatures.Player;

public class MyChat 
{
	final int MAX_LINES = 10;
	
	Button upButton;
	Button downButton;
	
	TextArea area;
	TextField box;
	public Player player;
	
	private int index;
	
	private ArrayList<String> all_text;
	public MyChat(final Player player)
	{
		all_text = new ArrayList<String>();
		this.player = player;
		player.setChat(this);
		Display display = SimpleGame.getDisplay();
		
		String welcome = "Welcome to Game 3! I hope you'll enjoy it.\n";
		all_text.add(welcome);
		index++;
		area = new TextArea(welcome, 64, 10);
		area.setEditable(false);
		area.setBackground(Color.lightGray);
        area.setLocation(10, 570);
        
        box = new TextField();
        box.setLocation(10, 570+area.getHeight()+10);
        box.setSize(area.getWidth(), 20);
        
        
        ActionListener textAction = new ActionListener() {
            public void actionPerformed(ActionEvent ev) 
            {
                String text = box.getText();
                if (text.length()==0)
                    return;
                all_text.add(fromat_text(text));    
                index++;
            }
         };
        
        box.addActionListener(textAction);
        
        upButton = new Button("^");
        upButton.setLocation(area.getWidth()+10, area.getAbsoluteY());
        
        ActionListener upButList = new ActionListener() {
            public void actionPerformed(ActionEvent ev) 
            {    
                index = Math.max(MAX_LINES, index--); // TODO
            }
         };
        
        upButton.addActionListener(upButList);
         
        downButton = new Button("V");
        downButton.setLocation(area.getWidth()+10, area.getAbsoluteY()+area.getHeight()-downButton.getHeight());
        
        display.add(area);
        display.add(box);
        display.add(upButton);
        display.add(downButton);
	}
	
	public void update()
	{
		//System.out.println(index);
		// if there are fewers then max_lines entries, write all of them.
		String str = "";
		area.setText(str);
		if (all_text.size()<=MAX_LINES)
			for (int i = 0; i < all_text.size(); i++)
				str += (all_text.get(i));
		else
		{
			for (int i = index-MAX_LINES; i<index; i++)
				str += (all_text.get(i));
		}
		//System.out.println("LIST:"+ all_text.toString());
		area.setText(str);

	}
	
	public void game_message(String text)
	{
		all_text.add(text);
	}
	
	private String fromat_text(String text) 
	{
		String name = player.getName();
		String message = name + " said: " + text+"\n";
		box.setText("");
		box.grabFocus();
		return message;
	} 
}
