package items;

import game_3_core.Position;

import org.newdawn.slick.Image;

public class Dead_wolf extends Container
{
	public Dead_wolf(Position pos)
	{
		super(pos, 8);
		setMovable(true);
		setMoveItem(true);
		setName("Dead_wolf");
		setSee("You see a dead Wolf. It might have loot in it!");
		initLoot();
		try
		{
			 setPic(new Image("Pics/Dead_wolf.gif"));
		}
		catch (Exception e)
		{
			System.out.println("No image - dead wolf");
		}
	}
	
	private void initLoot()
	{
		add_to_container(new Sword(null));
		add_to_container(new Sword(null));
		add_to_container(new Sword(null));
		add_to_container(new Sword(null));
		
	}

}
