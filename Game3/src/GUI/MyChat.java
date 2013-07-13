package GUI;

import game_3_core.SimpleGame;

import java.util.ArrayList;
import java.util.Date;

import org.newdawn.slick.Color;
import org.newdawn.slick.Input;

import sui.Button;
import sui.Display;
import sui.Label;
import sui.Popup;
import sui.TextArea;
import sui.TextField;
import sui.event.ActionEvent;
import sui.event.ActionListener;
import sui.event.MouseWheelEvent;
import sui.event.MouseWheelListener;
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
	
	private int enterCD = 200;
	private int deltaEnter = 0;
	
	private ArrayList<String> all_text;
	private int mouse_wheel_moved;
	String time;
	
	Label label;
	
	public MyChat(final Player player)
	{
		all_text = new ArrayList<String>();
		this.player = player;
		player.setChat(this);
		Display display = SimpleGame.getDisplay();
			
		time = getTime();
	
		String welcome = time + " Welcome to Game 3! I hope you'll enjoy it.\n";
		all_text.add(welcome);
		index++;
		area = new TextArea(welcome, 64, 10);
		area.setEditable(false);
		area.setBackground(Color.lightGray);
        area.setLocation(10, 570);
        area.setForeground(Color.black);
        
        MouseWheelListener mouseWheel  = new MouseWheelListener() 
        {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) 
			{
				System.out.println(e.getAmountChanged());
				
			}
         };
        
        area.addMouseWheelListener(mouseWheel);
        
        box = new TextField();
        box.setLocation(10, 570+area.getHeight()+10);
        box.setSize(area.getWidth(), 20);
        
        
        ActionListener textAction = new ActionListener() {
            public void actionPerformed(ActionEvent ev) 
            {
                String text = box.getText();
                if (text.length()==0)
                {
                	box.releaseFocus();
                	deltaEnter = 0;
                	return;
                }
                all_text.add(fromat_text(text));    
                index++;
                deltaEnter = 0;
            }
         };
        
        box.addActionListener(textAction);
        
        upButton = new Button("^");
        upButton.setLocation(area.getWidth()+10, area.getAbsoluteY());
        
        ActionListener upButList = new ActionListener() {
            public void actionPerformed(ActionEvent ev) 
            {    
            	index--;
                index = Math.max(MAX_LINES, index); 
            }
         };
        
        upButton.addActionListener(upButList);
         
        downButton = new Button("V");
        downButton.setLocation(area.getWidth()+10, area.getAbsoluteY()+area.getHeight()-downButton.getHeight());
        
        ActionListener dwonButList = new ActionListener() {
            public void actionPerformed(ActionEvent ev) 
            {    
            	index++;
                index = Math.min(all_text.size(), index); 
            }
         };
        
        downButton.addActionListener(dwonButList);
         
        label = new Label("test");
        label.setLocation(1000, 600);
        label.setZIndex(Popup.POPUP_LAYER);
        label.setForeground(Color.white);
        label.pack();
        
        // TODO shift + click to see item
        // TODO Use coin to break , have a popup coming saying wtf, how much to break?
        // TODO Battle text and messages displayed mid-screen for words you say.
        // TODO Once that's done, look into creating obsticles.
        // TODO fix wolf moving when can't move - change it's moving to no_mov if at the same place.
        
        display.add(label);
        display.add(area);
        display.add(box);
        display.add(upButton);
        display.add(downButton);
	}
	
	public void update(int delta, int mouse_wheel_moved)
	{	
		this.setMouse_wheel_moved(mouse_wheel_moved);
		
		Input input = SimpleGame.getGC().getInput();
		
		// Enter to grab focus for chat box, enter again to leave it. 0.1 sec CD on it.
		deltaEnter += delta;
		if (input.isKeyDown(Input.KEY_ENTER) && deltaEnter > enterCD)
		{
			if (!box.hasFocus())
				box.grabFocus();
			else
				box.releaseFocus();
			
			deltaEnter = 0;
		}
	
		// if there are fewer then max_lines entries, write all of them.
		String str = "";
		area.setText(str);
		if (all_text.size()<MAX_LINES)
			for (int i = 0; i < all_text.size(); i++)
				str += (all_text.get(i));
		else
		{
			for (int i = index-MAX_LINES; i<index; i++)
				str += (all_text.get(i));
		}
		area.setText(str);

		
		// check if player movment should be locked or not.
		if (box.hasFocus())
		{
			player.setEnableKeyMovment(false);
		}
		else
			player.setEnableKeyMovment(true);
		
		// check if user is scrolling wheel over the chat and if so scroll along.
		if (mouseOnChat(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()) && mouse_wheel_moved!=0)
		{			
			int change = mouse_wheel_moved/60;
			int temp = index-change;;
			if (change > 0)
			{
				index = Math.max(MAX_LINES, temp); 
			}	
			else
			{
				index = Math.min(all_text.size(), temp); 
			}
			
		}
	}
	
	private boolean mouseOnChat(int x, int y) 
	{
		if (x>10 && x<10+area.getWidth() )
			if(y > area.getAbsoluteY() && (y < (area.getAbsoluteY() + area.getHeight())))
				return true;
		
		return false;
	}

	// A message generated by the game to display in the chat.
	public void game_message(String text)
	{
		time = getTime();
		all_text.add(time+ " " +text+"\n");
		index++;
	}
	
	private String fromat_text(String text) 
	{
		time = getTime();
		String name = player.getName();
		String message = time + " " +name + " said: " + text+"\n";
		box.setText("");
		box.grabFocus();
		return message;
	}

	private String getTime()
	{
		Date date = new Date();
		int hours = date.getHours();
		int minutes = date.getMinutes();
		int seconds = date.getSeconds();
		
		String str = "";
		if (hours<10)
			str = "0"+hours;
		else
			str = Integer.toString(hours);
		
		if (minutes <10)
			str += ":0" + minutes;
		else
			str += ":" + minutes;
		
		if (seconds < 10)
			str += ":0" + seconds;
		else
			str += ":" + seconds;
		
		return str;
	}

	public int getMouse_wheel_moved() {
		return mouse_wheel_moved;
	}

	public void setMouse_wheel_moved(int mouse_wheel_moved) {
		this.mouse_wheel_moved = mouse_wheel_moved;
	} 
}
