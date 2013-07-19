package tiles;

import game_3_core.Position;
import items.Item;

import java.util.ArrayList;

public class GreenTile extends Tile
{

	public GreenTile(Position pos)
	{
		super(pos);
		setType(Tile.GREEN);
		setCanPutItem(true);
		setMovable(true);
		setSee("You see grass.");
	}
}
