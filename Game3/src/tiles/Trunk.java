package tiles;

import game_3_core.Position;

public class Trunk extends Tile 
{
	public Trunk(Position pos)
	{
		super(pos);
		setType(Tile.TRUNK);
		setCanPutItem(false);
		setMovable(false);
		setSee("You see a fallen tree.");
	}
}
