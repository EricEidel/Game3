package tiles;

import org.newdawn.slick.GameContainer;

import game_3_core.Position;
 // this interface represents tiles that destroy an item moved to it.
public interface DestroyItemTile 
{
	public void PlayDestroyAnim(Position pos);

	public void update(GameContainer c, int delta);
}
