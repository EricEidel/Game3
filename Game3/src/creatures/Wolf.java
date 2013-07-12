package creatures;

import game_3_core.Position;
import game_3_core.SimpleGame;
import items.Dead_wolf;
import items.Item;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import GUI.Status;
import GUI.Map;
import abilities.Spell;


public class Wolf extends Creature 
{
	
	public Wolf(Position pos, Map world)
	{	
		super(pos, world);

		setName("Wolf");
		setStatus(new Status(this));
		setDead(false);
		
		setSpeed(800);
		setHp(10);
		
		try 
		{
			setPic(new Image ("Pics/Wolf.gif"));
		} 
		catch (SlickException e) 
		{
			System.out.println("Fail to create picture - wolf");
		}
		
		try
		{
			Image pic1 = new Image ("Pics/Wolf.gif");
			Image pic2 = new Image ("Pics/Wolf_mov_1.gif");
			Image pic3 = new Image ("Pics/Wolf_mov_2.gif");
			
			Image[] pics = {pic1, pic2, pic3};
			int[] durations = {100, 100, 100};
			
			setAnim(new Animation(pics, durations));
		}
		catch (Exception e)
		{
			System.out.println("Fail to create animation - wolf");
		}
		
	}

	@Override
	public int cast_spell(Spell spell)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public Item getDeadItem() 
	{	
		return (new Dead_wolf(getPos()));
	}

}
