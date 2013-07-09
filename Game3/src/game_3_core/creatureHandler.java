package game_3_core;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.tiled.Tile;

import GUI.Map;
import creatures.Creature;
import creatures.Player;

public class creatureHandler 
{
	private ArrayList<Creature> creatures;
	private Player player;
	
	public creatureHandler(Player player)
	{
		creatures = new ArrayList<Creature>();
		this.player = player;
		
	}
	
	public void draw(Graphics g)
	{	
		for (Creature c: creatures)
		{
			if (player.getPos().isDrawableRange(c.getPos()))
			{				
				if (player.getMoving() != Creature.no_move && c.getMoving()!=Creature.no_move)
				{
					int x = c.getPos().getX() - player.getPos().getX();
					int y = c.getPos().getY() - player.getPos().getY();
					
					c.drawMoving(player, x, y, g);
				}
				else if (player.getMoving() != Creature.no_move)
				{
					int x = c.getPos().getX() - player.getPos().getX();
					int y = c.getPos().getY() - player.getPos().getY();
					
					c.drawMovingNoCreatureMov(player, x, y, g);
				}
				else if (player.getMoving() == Creature.no_move && c.getMoving()!= Creature.no_move)
				{
					int x = c.getPos().getX() - player.getPos().getX();
					int y = c.getPos().getY() - player.getPos().getY();
					
					c.drawMovingNoPlayerMov(x, y, g);
				}
				else
				{
					int x = c.getPos().getX() - player.getPos().getX();
					int y = c.getPos().getY() - player.getPos().getY();
					
					c.draw(Player.getPlayerXCenter() + x, Player.getPlayerYCenter() + y, g);
				}
			}
		}
	}

	public void checkDead(Map land, ItemHandler ih)
	{
		ArrayList<Creature> dead_c = new ArrayList<Creature>();
		for (Creature c: creatures)
		{
			//System.out.println(c.getPos() + " " + c.isDead());
			if (c.isDead())
			{
				player.setTarget(null);
				land.kill(c, ih);
				dead_c.add(c);
			}
		}
		
		for (Creature c: dead_c)
			creatures.remove(c);
	}

	public void checkActive() 
	{
		for (Creature c: creatures)
		{		
			if (player.getPos().isActiveRange(c.getPos()))
			{
				c.setActive(true);
			}
			else
			{
				c.setActive(false);
			}
		}
	}
	
	public void add_creature(Creature creature)
	{
		creatures.add(creature);
	}
	
	public void move(Map land, int delta)
	{
		for (Creature c: creatures)
		{
			int move_time = c.getSpeed();
			c.setInputDelta(c.getInputDelta()+delta);

			if (c.isActive() && (c.getInputDelta() > move_time))
			{
			        c.move(land);
		    		c.setInputDelta(0);
			}
		}
	}
}
