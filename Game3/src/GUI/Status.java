package GUI;

import game_3_core.SimpleGame;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import creatures.Creature;
import creatures.Player;


public class Status 
{
	Graphics g;
    Color red = new Color(255, 0, 0);
    Color orange = new Color(255, 171, 0);
    Color green = new Color(0,255,0);
    Color black = new Color(0,0,0);
    int x_tile_dist;
    int y_tile_dist;
    int len_name;
    
	Creature c;

	public Status(Creature c)
	{
		g = SimpleGame.getGC().getGraphics();
		this.c = c;
		
	    len_name = g.getFont().getWidth(c.getName());
	    
	    len_name = (Map.SIZE_OF_TILE - len_name)/2;
	}
	
	public void draw(int x_tile_dist, int y_tile_dist)
	{
		this.x_tile_dist = x_tile_dist;
		this.y_tile_dist = y_tile_dist;
		
	    float healthScale = (float)c.getHp() / (float)c.getMaxhp();
	    
	    if (healthScale > 0.9)
	        g.setColor(green);
	    else if (healthScale > 0.45)
	    	g.setColor(orange);
	    else	
	    	g.setColor(red);
	    
	    g.fillRect(Map.X_OFFSET+(x_tile_dist*Map.SIZE_OF_TILE) + 5, (y_tile_dist*Map.SIZE_OF_TILE)+Map.Y_OFFSET - 10, 40 * healthScale, 5);
	    
	    g.drawString(c.getName(), Map.X_OFFSET+(x_tile_dist*Map.SIZE_OF_TILE)+(len_name), (y_tile_dist*Map.SIZE_OF_TILE)+Map.Y_OFFSET - 30);
	    
	    g.setColor(black);
	}

	public void drawMove(int direction, int offset) 
	{
		this.x_tile_dist = Player.getPlayerXCenter();
		this.y_tile_dist = Player.getPlayerYCenter();
		
	    float healthScale = (float)c.getHp() / (float)c.getMaxhp();
	    
	    if (healthScale > 0.9)
	        g.setColor(green);
	    else if (healthScale > 0.45)
	    	g.setColor(orange);
	    else	
	    	g.setColor(red);
	    
	    int x_move=0;
	    int y_move=0;
	    
	    g.fillRect(x_move + Map.X_OFFSET+(x_tile_dist*Map.SIZE_OF_TILE) + 5, y_move+(y_tile_dist*Map.SIZE_OF_TILE)+Map.Y_OFFSET - 10, 40 * healthScale, 5);
	    
	    g.drawString(c.getName(), x_move+Map.X_OFFSET+(x_tile_dist*Map.SIZE_OF_TILE)+(len_name), y_move+(y_tile_dist*Map.SIZE_OF_TILE)+Map.Y_OFFSET - 30);
	    
	    g.setColor(black);
		
	}
	
	public void drawMoveMonster(int actual_x, int actual_y)
	{
	    float healthScale = (float)c.getHp() / (float)c.getMaxhp();
	    
	    if (healthScale > 0.9)
	        g.setColor(green);
	    else if (healthScale > 0.45)
	    	g.setColor(orange);
	    else	
	    	g.setColor(red);
	    
	    g.fillRect(actual_x + 5, actual_y - 10, 40 * healthScale, 5);
	    
	    g.drawString(c.getName(), actual_x+(len_name), actual_y - 30);
	    
	    g.setColor(black);
	}
}
