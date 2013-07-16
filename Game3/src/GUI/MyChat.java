package GUI;

import game_3_core.SimpleGame;

import java.util.ArrayList;
import java.util.Date;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import sui.Button;
import sui.Display;
import sui.Label;
import sui.TextArea;
import sui.TextField;
import sui.event.ActionEvent;
import sui.event.ActionListener;
import sui.event.MouseWheelEvent;
import sui.event.MouseWheelListener;
import creatures.Player;

public class MyChat 
{
	final int MAX_LINES = 9;
	
	private Button upButton;
	private Button downButton;
	private Button allTheWayButton;
	
	TextArea area;
	TextField box;
	public Player player;
	
	private int index;
	
	private int enterCD = 200;
	private int deltaEnter = 0;
	
	private ArrayList<String> all_text;
	private int mouse_wheel_moved;
	String time;
	
    Label see_label = new Label();
	
    private Display display;
	private Graphics g;
	
	private ChatLine[] chatLines;

	
	
    public MyChat(final Player player, Graphics g) throws SlickException
	{
    	this.g = g;
		all_text = new ArrayList<String>();
		this.player = player;
		player.setChat(this);
		display = SimpleGame.getDisplay();
			
		time = getTime();
	
		String welcome = " "+time + " Welcome to Game 3! I hope you'll enjoy it.\n";
		all_text.add(welcome);
		index++;
		area = new TextArea("",64, 10);
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
        box.setMaxChars(255);
        
        chatLines = new ChatLine[MAX_LINES];

        for (int i = 0; i<MAX_LINES; i++)
        {
        	chatLines[i] = new ChatLine(10, 575+i*18, box.getWidth(), display);
        }
        
        ActionListener textAction = new ActionListener() {
            public void actionPerformed(ActionEvent ev) 
            {
                String text = box.getText();
                if (text.length()==0)
                {
                	box.releaseFocus();
                	deltaEnter = 0;
                	return;
                }// adds the formated text to all_text after breaking it into chat lines. Returns the text broken with \n when it's too much to display;
                String fixed = add_text_and_format(fromat_text(text));                  
                show_on_screen(fixed);
                deltaSaid = show_said_message;
                deltaEnter = 0;
            }
            
            /*
            
            // adds the formated text to all_text after breaking it into chat lines. Returns the text broken with \n when it's too much to display.
			private String add_text_and_format(String format_text) 
			{
				int index_str = 0;
				String show_to_screen = "";
                String new_str = "";
                System.out.println(format_text.length());
                while (index_str < format_text.length())
                {
	                while (SimpleGame.getGC().getGraphics().getFont().getWidth(new_str) < area.getWidth()-140 && index_str < format_text.length())
	                {
	                	new_str += format_text.charAt(index_str);
	                	index_str ++;
	                }
	                all_text.add(new_str);
	                index++;
	                show_to_screen += new_str+"\n";
	                new_str="";
                }

                return show_to_screen;
			}
         };
            
             */
            
			// adds the formated text to all_text after breaking it into chat lines. Returns the text broken with \n when it's too much to display.
			private String add_text_and_format(String format_text) 
			{
				String[] all_strings = format_text.split(" ");
				for (String s: all_strings)
				{
					System.out.print(s);
				}
				System.out.println();
				all_strings = make_sure_no_long_strings(all_strings);

				int index_str = 0;
				String show_to_screen = "";
                String new_str = "";
                while (index_str < all_strings.length)
                {
                	while (SimpleGame.getGC().getGraphics().getFont().getWidth(new_str + " " + all_strings[index_str]) < area.getWidth()-140)
                	{
                		new_str += " " + all_strings[index_str];
	                	index_str ++;
	                	if (index_str >= all_strings.length)
	                		break;
                	}

	                all_text.add(new_str);
	                index++;
	                show_to_screen += new_str+"\n";
	                new_str="";
                }

                return show_to_screen;
			}

			// return a string array where none of the strings is longer then allowed.
			private String[] make_sure_no_long_strings(String[] all_strings) 
			{
				ArrayList<String> strings = new ArrayList<String>();
				
				for (String s: all_strings)
				{
					if (SimpleGame.getGC().getGraphics().getFont().getWidth(" " + s) < area.getWidth()-140)
						strings.add(s);
					else
					{
						int index_str = 0;
		                String new_str = "";
		                while (index_str < s.length())
		                {
			                while (SimpleGame.getGC().getGraphics().getFont().getWidth("  "+new_str) < area.getWidth()-140 && index_str < s.length())
			                {
			                	new_str += s.charAt(index_str);
			                	index_str ++;
			                }
			                
			                strings.add(new_str);
			                new_str="";
		                }
					}
				}
				
				String[] ret = new String[strings.size()];
				int i = 0;
				for (String s: strings)
				{
					ret[i] = s;
					i++;
				}
			
				
				return ret;
			}
         };
        
        box.addActionListener(textAction);
        
        upButton = new Button("^");
        upButton.setLocation(area.getWidth()+12, area.getAbsoluteY());
        
        ActionListener upButList = new ActionListener() {
            public void actionPerformed(ActionEvent ev) 
            {    
            	index--;
                index = Math.max(MAX_LINES, index); 
            }
         };
        
        upButton.addActionListener(upButList);
         
        downButton = new Button("V");
        downButton.setLocation(area.getWidth()+12, area.getAbsoluteY()+area.getHeight()-downButton.getHeight());
        
        ActionListener dwonButList = new ActionListener() {
            public void actionPerformed(ActionEvent ev) 
            {    
            	index++;
                index = Math.min(all_text.size(), index); 
            }
         };
        
        downButton.addActionListener(dwonButList);
    	
        allTheWayButton = new Button("|");
        allTheWayButton.setLocation(area.getWidth()+15, area.getAbsoluteY()+area.getHeight()+downButton.getHeight()+10-allTheWayButton.getHeight());
        
        ActionListener allWayButList = new ActionListener() {
            public void actionPerformed(ActionEvent ev) 
            {    
            	index = all_text.size(); 
            }
         };
        
         allTheWayButton.addActionListener(allWayButList);
        
    	display.add(see_label);
        display.add(area);
        display.add(box);
        display.add(upButton);
        display.add(downButton);
        display.add(allTheWayButton);
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
		populate_labels();

		
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
		
		// time for drawing the see message	
		deltaDrawSee -= delta;
		deltaSaid -=delta;
	}
	
	private void populate_labels() 
	{
		if (all_text.size()<MAX_LINES)
			for (int i = 0; i < all_text.size(); i++)
			{
				chatLines[i].setText(all_text.get(i));
			}
		else
		{
			int chat_line_index = 0;
			for (int i = index-MAX_LINES; i<index; i++)
			{
				chatLines[chat_line_index].setText(all_text.get(i));
				chat_line_index++;
			}
		}
	}

	private String msg;
	private int show_see_message = 3000;
	private int show_said_message = 3000;
	private int deltaSaid;
	private int deltaDrawSee =0 ;
	
	public void add_see_meesage(String msg)
	{
		this.msg = msg;
		deltaDrawSee = show_see_message;
	}

	public void drawSeeMessage() 
	{
		if (deltaDrawSee > 0)
		{
			if (msg!=null)
			{
				g.setColor(Color.green);
				int len_name = g.getFont().getWidth(msg);
			    len_name = (Map.SIZE_OF_TILE - len_name)/2;
			    g.drawString(msg, 350+len_name, 100);
			    g.setColor(Color.black);
			}
		}
	}
	
	private String said;
	public void show_on_screen(String msg)
	{
		this.said = msg;
		deltaSaid = show_said_message;
	}
	
	public void drawSaid() 
	{
		if (deltaSaid > 0)
		{
			if (said!=null)
			{
				g.setColor(Color.yellow);
			
				int len_name = Math.min(g.getFont().getWidth(said), 600);
			    len_name = (Map.SIZE_OF_TILE - len_name)/2;
			    
			    g.drawString(said, 350+len_name, 125);
			    g.setColor(Color.black);
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
		all_text.add(" "+time+ " " +text+"\n");
		index++;
	}
	
	private String fromat_text(String text) 
	{
		time = getTime();
		String name = player.getName();
		String message = time + " " +name + " said: " + text + "\n";
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
