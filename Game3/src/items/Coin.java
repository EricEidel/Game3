package items;

import game_3_core.Position;

public abstract class Coin extends StackableItem
{

	public Coin(Position pos) 
	{
		super(pos);
		setType(Item.COIN);
	}
	
	public Coin(Coin target, Coin isMoved)
	{
		super(target.getPos());
	}
	
	// These items are in place to force a worth for each coin. Basic coins would be worth 1 each, but more progressed versions can be worth more.
	// Example: Plat coin is worth 100, so it is equivalent of 100 gold coins.
	public abstract int getWorth();
	public abstract void setWorth(int worth);
	
}
