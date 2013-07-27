package tiles;
import game_3_core.ItemHandler;
import game_3_core.Position;
import items.Item;
import items.StackableItem;

import java.util.ArrayList;

import creatures.Creature;


public class Tile
{
	static public final int GREEN = 1;
	static public final int WATER = 2;
	static public final int SHORE = 3; // 3 - 20
	static public final int DEAD_TREE = 21;
	static public final int TRUNK = 22;
	static public final int BIG_STONE = 23;
	static public final int TOP_GREEN_TREE = 24;
	static public final int BOT_GREEN_TREE = 28;

	
	private Creature c = null;
	private ArrayList<Item> items = null;
	
	private Position pos;
	private int type;
	private boolean movable;

	private String see;
	private boolean canPutItem;
	
	private boolean playDestroyItem;
	
	public Tile (Position pos)
	{
		items = new ArrayList<Item>();
		this.pos = pos;		
	}
	
	// static factory method
	static public Tile makeNewTile(int type, Position pos)
	{
		switch (type)
		{
			case GREEN: 
				return new GreenTile(pos);
				
			case SHORE: case 4:case 5:case 6:case 7:case 8:case 9:case 10:case 11:case 12:case 13:case 14:case 15:case 16:case 17:case 18:case 19:case 20:
				return new ShoreTile(pos);
				
			case DEAD_TREE:
				return new DeadTree(pos);
			
			case TRUNK:
				return new Trunk(pos);
				
			case BIG_STONE:
				return new BigStone(pos);
				
			case BOT_GREEN_TREE: case TOP_GREEN_TREE:
				return new GreenTree(pos);

				
			default:
				return new WaterTile(pos);
		}

	}
	
	public ArrayList<Item> getItems() {
		return items;
	}


	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}


	public int getType() 
	{
		return type;
	}
	
	public void setType(int type) 
	{
		this.type = type;
	}
	
	// GETTERS AND SETTERS
	public Creature getC() {
		return c;
	}

	public void setC(Creature c) {
		this.c = c;
	}
	
	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	public boolean isMovable() 
	{
		if (c != null)
		{
			return false;
		}
		if (getItem() != null && movable)
		{
			return getItem().isMovable();
		}
		return movable;
	}
	
	public void setMovable(boolean movable) {
		this.movable = movable;
	}
	
	public boolean equals(Tile t2)
	{
		if (t2.getPos().getX() != getPos().getX())
			return false;
		else if (t2.getPos().getY() != getPos().getY())
			return false;
		else
			return true;
	}

	public Item getItem() 
	{
		if (items.isEmpty())
			return null;
		
		return items.get(items.size()-1);
	}

	public boolean setItem(Item item, ItemHandler ih) 
	{
		if (canPutItem)
		{
			Item topItem = getItem();
			if ((item instanceof StackableItem) && topItem != null)
			{
				
				StackableItem si = ((StackableItem)item);
				// try to stack the two items - if they're the same type and there's not too many.

				boolean check = si.stack(topItem);
				if (check)
				{
					// add item with changed amount
					items.remove(items.size()-1);
					ih.remove_item(topItem);
					items.add(si);
					return true;
				}
				else
				{
					items.add(item);
					return true;
				}
			}
			else
			{
				items.add(item);
				return true;
			}
		}
		return false;
	}


	public boolean isCanPutItem() {
		return canPutItem;
	}

	public void setCanPutItem(boolean canPutItem) {
		this.canPutItem = canPutItem;
	}

	public String getSee() {
		return see;
	}


	public void setSee(String see) {
		this.see = see;
	}


	public void removeItem(Item held) 
	{
		items.remove(held);
	}

	public boolean isPlayDestroyItem() {
		return playDestroyItem;
	}

	public void setPlayDestroyItem(boolean playDestroyItem) {
		this.playDestroyItem = playDestroyItem;
	}
}

