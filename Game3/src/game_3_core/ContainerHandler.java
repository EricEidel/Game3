package game_3_core;

import items.Container;
import items.Item;

import java.util.ArrayList;

import creatures.Player;

public class ContainerHandler 
{
	static final int LIMIT = 4; // How many containers can be open at once on screen
	private ArrayList<Container> conts;
	private Player player;
	
	public ContainerHandler(Player player) 
	{
		this.player = player;
		conts = new ArrayList<Container>();
	}
	
	public void add(Container temp)
	{
		conts.add(temp);	
	}
	
	public void update(Player player)
	{
		ArrayList<Container> removed_conts = new ArrayList<Container>();
		for (Container c: conts)
		{
			player.getInv().setAllPos(player.getPos());
			if (player.getInv().getContainer() != null)
				for (Item i: player.getInv().getContainer().getItems())
				{
					if (i.getType() == Item.CONTAINER)
						((Container)i).setPos(player.getPos());
					else
						i.setPos(player.getPos());
				}
			if (player.getInv().getMisc() != null)
				if (player.getInv().getMisc().getType() == Item.CONTAINER)
				{
					for (Item i: ((Container)player.getInv().getMisc()).getItems())
					{
						if (i.getType() == Item.CONTAINER)
							((Container)i).setPos(player.getPos());
						else
							i.setPos(player.getPos());
					}
				}
			if (!c.isOpen()||!isDistance(c))
			{
				c.close();
				removed_conts.add(c);
			}
		}
		
		for (Container c: removed_conts)
		{
			conts.remove(c);
		}
	}
	
	private boolean isDistance(Container c) 
	{
		if (c.getPos() == null)
			return true;
		else
			return c.getPos().near(player.getPos(), 1);
	}

	public void draw()
	{
		int count = 0;
		for (int i=0; (i<conts.size())&&(count<LIMIT); i++)
		{
			if (conts.get(i).isOpen())
			{
				conts.get(i).drawCont(i);
				count++;
			}
		}
	}

	public void remove(Container cont) 
	{
		conts.remove(conts.indexOf(cont));
	}

	public Item getItem(int cont_num, int item_slot) 
	{
		if (cont_num >= conts.size()||(cont_num==-1)||(item_slot==-1))
				return null;
		
		return conts.get(cont_num).get_item(item_slot);
		 
	}
	
	public void removeItem(int cont_num, int item_slot) 
	{
		//System.out.println(cont_num + " " + item_slot);
		conts.get(cont_num).remove_from_container(item_slot);
	}
	
	public Container getContainer(int i)
	{
		if (i>=conts.size())
			return null;
		
		return conts.get(i);
	}

}
