package tiles;
import game_3_core.Position;
import items.Item;

import java.util.ArrayList;

import creatures.Creature;


public class Tile
{
	static public final int GREEN = 1;
	static public final int WATER = 2;
	
	static public final int SHORE_LEFT = 3;
	static public final int SHORE_UP_LEFT = 4;
	static public final int SHORE_UP= 5;
	static public final int SHORE_UP_RIGHT = 6;
	static public final int SHORE_RIGHT= 7;
	static public final int SHORE_DOWN_RIGHT = 8;
	static public final int SHORE_DOWN = 9;
	static public final int SHORE_DOWN_LEFT = 10;
	
	static public final int SHORE_UP_LEFT_INV = 11;
	static public final int SHORE_UP_RIGHT_INV = 12;
	static public final int SHORE_DOWN_RIGHT_INV = 13;
	static public final int SHORE_DOWN_LEFT_INV = 14;

	
	
	private Creature c = null;
	private ArrayList<Item> items = null;
	
	private Position pos;
	private int type;
	private boolean movable;

	private String see;
	
	public Tile (int type, Position pos)
	{
		this.type = type;
		this.pos = pos;
		items = new ArrayList<Item>();
		switch (type)
		{
			case GREEN: 
				this.movable = true;
				setSee("You see grass.");
				break;
				
			default:
				this.movable = false;
				setSee("You see water.");
				break;
		}

		
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

	public Item getItem() {
		if (items.isEmpty())
			return null;
		
		return items.get(items.size()-1);
	}

	public void setItem(Item item) {
		items.add(item);
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
}

