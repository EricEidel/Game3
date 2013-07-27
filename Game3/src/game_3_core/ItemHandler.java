package game_3_core;

import items.Item;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import creatures.Creature;
import creatures.Player;

public class ItemHandler 
{
	private ArrayList<Item> items;
	private Player player;
	private static String seperator = ",";
	
	public ItemHandler(String file_name, Player player)
	{
		items = new ArrayList<Item>();
		this.player = player;
		load_from_txt(file_name);
		
	}
	
	private void load_from_txt(String file_name) 
	{
		try
		{
			FileReader fr = new FileReader(file_name);
			BufferedReader br = new BufferedReader(fr);
		
			String line = "";
			while (!line.contains("width"))
			{
				line = br.readLine();
			}
			int length = Integer.parseInt(line.split("=")[1]);
			
			while (!line.contains("height"))
			{
				line = br.readLine();
			}
			int height = Integer.parseInt(line.split("=")[1]);
			
			
			while (!line.contains("data="))
			{
				line = br.readLine();
			}
			line = br.readLine();
			while (!line.contains("data="))
			{
				line = br.readLine();
			}
			line = br.readLine();
			while (!line.contains("data="))
			{
				line = br.readLine();
			}// Get to the object section of the map file.
			
			line = br.readLine();
			int column = 0;
			Item temp_item;
			Position temp_pos;
			while (!line.equals(""))
			{
				String[] one_line = line.split(seperator );
				
				for (int i=0; i<one_line.length-1; i++)
				{
					temp_pos =  new Position(i, column);
					temp_item = (ItemFactory.makeNewItem(Integer.parseInt(one_line[i]), temp_pos));
					if (temp_item != null)
					{
						boolean check = player.getLand().tileAt(temp_pos).setItem(temp_item, this);
						if (check)
							items.add(temp_item);
						else
							System.out.println("Could not create item "+temp_item+" at position "+temp_pos);
					}
				}
				column++;
				line = br.readLine();
			}
			
			br.close();
			fr.close();
			
		}
		catch (Exception e)
		{
			System.out.println("Problem with initiating ItemHandler!");
		}
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
