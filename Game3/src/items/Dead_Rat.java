package items;

import game_3_core.Position;
import game_3_core.SimpleGame;

import org.newdawn.slick.Image;


public class Dead_Rat extends Container 
{

	public Dead_Rat(Position pos)
	{
		super(pos, 8);
		setMovable(true);
		setMoveItem(true);
		setName("Dead_rat");
		setSee("You see a dead Rat. It might have loot in it!");
		initLoot();
		try
		{
			 setPic(new Image("Pics/Dead_rat.png"));
		}
		catch (Exception e)
		{
			System.out.println("No image - dead rat");
		}
	}
	
	private void initLoot()
	{
		add_to_container(new GoldCoin(SimpleGame.start_player_pos, 3));	
	}
}
