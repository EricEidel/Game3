package tiles;

import org.newdawn.slick.GameContainer;

import creatures.Player;

import game_3_core.Position;
 // this interface represents tiles that destroy an item moved to it.
public interface DestroyItemTile 
{
	public void PlayDestroyAnim(Position pos, Player player);

	public void update(GameContainer c, int delta);
}
