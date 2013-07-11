package items;

import game_3_core.Position;

import org.newdawn.slick.Image;

public class ShortSword extends Item 
{
	public ShortSword(Position pos)
	{
		super(pos);
		setMovable(true);
		setSee("You see a short sword.");
		setName("Short sword");
		setType(Item.WEAPON);
		
		try
		{
			 setPic(new Image("Pics/short_sword.gif"));
		}
		catch (Exception e)
		{
			System.out.println("No image - sword");
		}
	}

	@Override
	public void use() 
	{
		// TODO Auto-generated method stub
		
	}
}