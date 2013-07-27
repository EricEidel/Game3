package game_3_core;

import items.Item;
import items.LeatherPants;
import GUI.Map;


public class ItemFactory 
{
	static final int LEATHER_PANTS = 31;
	static public Item makeNewItem(int type, Position pos)
	{
		if (type == LEATHER_PANTS)
			return new LeatherPants(pos);
		
		return null;
	}
}
