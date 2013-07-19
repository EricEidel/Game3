package tiles;

import game_3_core.Position;

public class ShoreTile extends Tile
{
	public ShoreTile(Position pos)
	{
		super(pos);
		setType(Tile.SHORE);
		setCanPutItem(true);
		setMovable(false);
		setSee("You see some sand near the water.");
	}
}
