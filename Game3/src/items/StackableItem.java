package items;

import game_3_core.Position;

public abstract class StackableItem extends Item
{

	public StackableItem(Position pos) 
	{
		super(pos);
	}
	
	// this creates the new stackable item when one is moved on the other.
	// Creates a new stackableItem at the position of the target, that has the amount of target.amount + isMoved.amount
	public StackableItem(StackableItem target, StackableItem isMoved)
	{
		super(target.getPos());
	}

	public abstract void setAmount(int amount);
	public abstract int getAmount();

	public abstract boolean stack(Item item);
}
