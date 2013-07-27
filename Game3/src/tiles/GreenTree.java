package tiles;

import game_3_core.Position;

public class GreenTree extends Tile 
{
	public GreenTree(Position pos)
	{
		super(pos);
		setType(Tile.TOP_GREEN_TREE);
		setCanPutItem(false);
		setMovable(false);
		setSee("You see a big green tree.");
	}
}
