package items;

import game_3_core.Position;

public abstract class UniqueItem extends Item
{

	public UniqueItem(Position pos) 
	{
		super(pos);
		// TODO Auto-generated constructor stub
	}

	public abstract long getID();
	public abstract void setID(long iD);
	public abstract boolean equals(Container cont);
	
}
