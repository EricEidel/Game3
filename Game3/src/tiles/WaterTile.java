package tiles;

import game_3_core.ItemHandler;
import game_3_core.Position;
import items.Item;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import GUI.Map;

import creatures.Player;

import sui.Timer;

public class WaterTile extends Tile implements DestroyItemTile
{
	int deltaDestroy;
	Timer timer = new Timer(1800);
	
	Animation destroyWater;
	
	{
		try 
		{
			Image img1 = new Image("Pics/bulp_1.png");
			Image img2 = new Image("Pics/bulp_2.png");
			Image img3 = new Image("Pics/bulp_3.png");
			Image img4 = new Image("Pics/bulp_4.png");
			Image img5 = new Image("Pics/bulp_5.png");
			Image img6 = new Image("Pics/bulp_6.png");
			
			Image[] imgArr = {img1, img2, img3, img4, img5, img6};
			int[] durations = {200, 200, 200, 200, 200, 200};
			destroyWater = new Animation(imgArr, durations);
		} 
		catch (SlickException e) 
		{
			System.out.println("Can't load bulp animation - WaterTile");
			e.printStackTrace();
		}
	}
	
	public WaterTile(Position pos)
	{
		super(pos);
		setType(Tile.WATER);
		setCanPutItem(true);
		setMovable(false);
		setSee("You see water.");
		timer.setRepeats(false);
	}
	
	
	public void PlayDestroyAnim(Position pos, Player player) 
	{
		System.out.println(pos.toString()); 
		int x = getPos().getX() - player.getPos().getX();
		int y = getPos().getY() - player.getPos().getY();
		
		x= Map.X_OFFSET+(Player.getPlayerXCenter() + x)*Map.SIZE_OF_TILE;
		y= Map.Y_OFFSET+(Player.getPlayerYCenter() + y)*Map.SIZE_OF_TILE;
		destroyWater.draw(x,y);

		if (timer.getNow() >= 1180)
		{
			timer.restart();
			setPlayDestroyItem(false);
			destroyWater.stop();
		}
		System.out.println(timer.getNow());
	}
	
	@Override 
	public boolean setItem(Item item, ItemHandler ih) 
	{
		destroyWater.restart();
		setPlayDestroyItem(true);
		timer.restart();
		ih.remove_item(item);
		return true;
	}

	public void update(GameContainer c, int delta) 
	{
		timer.update(c, delta);	
	}
}
