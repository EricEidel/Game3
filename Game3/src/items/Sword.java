package items;

import game_3_core.Position;

import org.newdawn.slick.Image;

import creatures.Player;

public class Sword extends Item 
{
	public Sword(Position pos)
	{
		super(pos);
		setMovable(true);
		setSee("You see a sword.");
		setName("Sword");
		setType(Item.WEAPON);
		
		try
		{
			 setPic(new Image("Pics/sword.gif"));
		}
		catch (Exception e)
		{
			System.out.println("No image - sword");
		}
	}

	@Override
	public void use(Player player) 
	{
		// Can't use sword
		
	}
}
