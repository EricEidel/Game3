package items;

import org.newdawn.slick.Image;

import game_3_core.Position;

public class LeatherPants extends Item
{

	public LeatherPants(Position pos) 
	{
		super(pos);
		setMovable(true);
		setSee("You see a pair of leather pants.");
		setName("Leather pants");
		setType(Item.PANTS);
		
		try
		{
			 setPic(new Image("Pics/pants.gif"));
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
