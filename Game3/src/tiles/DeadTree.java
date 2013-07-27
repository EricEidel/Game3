package tiles;

import game_3_core.Position;

public class DeadTree extends Tile 
{
	public DeadTree(Position pos)
	{
		super(pos);
		setType(Tile.DEAD_TREE);
		setCanPutItem(false);
		setMovable(false);
		setSee("You see a dead tree.");
	}
}
