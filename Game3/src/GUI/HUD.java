package GUI;

import game_3_core.SimpleGame;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import creatures.Player;

	
	
public class HUD 
{
	public static final int X_OFFSET = 2*Map.X_OFFSET + 15* Map.SIZE_OF_TILE+30;
	public static final int Y_OFFSET = Map.Y_OFFSET+5;

	
    static final Color RED = new Color(255,0,0);
    static final Color BLUE = new Color(0, 205, 255);
    static final Color BLACK = new Color(0,0,0);
	public static final int BARS_HEIGHT = 60;
	public static final int BODY_HEIGHT = 220;
	
	public static int HIEGHT = 250;
	
    private int hp;
    private int maxHp;
    private int mp;
    private int maxMp;
    
    private String HPString;
    private String MPString;
    
    private Graphics g = SimpleGame.getGC().getGraphics();
    private Player player;
    
    Image doll;
    
	public HUD(Player player)
	{
		this.player = player;
		hp = player.getHp();
		maxHp = player.getMaxhp();
		mp = player.getMp();
		maxMp = player.getMaxmp();
		
		HPString = Integer.toString(hp);
		MPString = Integer.toString(mp);
		
		try
		{
			doll = new Image("Pics/paper_doll/Doll.jpg");
		}
		catch (Exception e)
		{
			System.out.println("Could not load picture - doll!");
		}
	}
	
	public void update()
	{
		hp = player.getHp();
		maxHp = player.getMaxhp();
		mp = player.getMp();
		maxMp = player.getMaxmp();
		
		HPString = Integer.toString(hp);
		MPString = Integer.toString(mp);
		
	}

	public void draw()
	{
		int BAR_LEN = 160;
		int BAR_HEIGHT = 20;
		int out_line_size = 1;
		int inner_pad_x = 30;
		int inner_pad_y = 25;
		int text_y_offset = 2;
		
		float healthScale =  (float)hp / (float)maxHp;
		float manaScale = (float)mp / (float) maxMp;
		   
		g.setColor(BLACK);
		g.drawString("HP:", X_OFFSET, Y_OFFSET+text_y_offset);	 
		g.drawRect((X_OFFSET+inner_pad_x-out_line_size), (Y_OFFSET-out_line_size), (BAR_LEN)+out_line_size, BAR_HEIGHT+out_line_size);
		g.drawString(HPString, (BAR_LEN+X_OFFSET+inner_pad_x-out_line_size)+inner_pad_x, (Y_OFFSET-out_line_size+text_y_offset));
		
		g.setColor(RED);
		g.fillRect(X_OFFSET+inner_pad_x, Y_OFFSET, (BAR_LEN * healthScale), BAR_HEIGHT);    
		
		g.setColor(BLACK);
		g.drawString("MP:", X_OFFSET, Y_OFFSET+text_y_offset+inner_pad_y);	 
		g.drawRect((X_OFFSET+inner_pad_x-out_line_size), (Y_OFFSET-out_line_size)+inner_pad_y, (BAR_LEN)+out_line_size, BAR_HEIGHT+out_line_size);
		g.drawString(MPString, (BAR_LEN+X_OFFSET+inner_pad_x-out_line_size)+inner_pad_x, (Y_OFFSET-out_line_size+text_y_offset)+inner_pad_y);
		
		g.setColor(BLUE);
		g.fillRect(X_OFFSET+inner_pad_x, Y_OFFSET+inner_pad_y, (BAR_LEN * manaScale), BAR_HEIGHT);
		g.setColor(BLACK);
		
		draw_body();
	}
	
	public void draw_body()
	{
		doll.draw(X_OFFSET, Y_OFFSET + BARS_HEIGHT);
		player.getInv().draw();
	}
}
