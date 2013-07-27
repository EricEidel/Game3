package tiles;

import game_3_core.Position;

public class BigStone extends Tile 
{
	public BigStone(Position pos)
	{
		super(pos);
		setType(Tile.BIG_STONE);
		setCanPutItem(false);
		setMovable(false);
		setSee("You see a big stone.");
	}
}
