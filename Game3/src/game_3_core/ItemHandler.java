package game_3_core;

import items.Item;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import creatures.Creature;
import creatures.Player;

public class ItemHandler 
{
	private ArrayList<Item> items;
	private Player player;
	
	public ItemHandler(Player player)
	{
		items = new ArrayList<Item>();
		this.player = player;
		
	}
	
	public void draw(Graphics g)
	{	
		for (Item i: items)
		{
			if (player.getPos().isDrawableRange(i.getPos()))
			{
				if (player.getMoving() == 5 || (player.getPos().equals(player.getOldPos())))
				{
					int x = i.getPos().getX() - player.getPos().getX();
					int y = i.getPos().getY() - player.getPos().getY();
					i.draw(Player.getPlayerXCenter() + x, Player.getPlayerYCenter() + y, g);
				}
				else
				{
					int x = i.getPos().getX() - player.getPos().getX();
					int y = i.getPos().getY() - player.getPos().getY();
					i.drawMoving(player, x, y);
				}
			}
		}
	}

	public void add_item(Item item) 
	{
		items.add(item);	
	}

	public void remove_item(Item item) 
	{
		items.remove(item);
	}
	
}
