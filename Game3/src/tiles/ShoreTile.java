package tiles;

import game_3_core.Position;

public class ShoreTile extends Tile
{
	public ShoreTile(Position pos)
	{
		super(pos);
		setType(Tile.WATER);
		setCanPutItem(true);
		setMovable(false);
		setSee("You see water.");
	}
}
