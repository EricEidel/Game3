package creatures;

import game_3_core.Position;
import game_3_core.SimpleGame;
import items.Dead_Rat;
import items.Dead_wolf;
import items.Item;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import GUI.Status;
import GUI.Map;
import abilities.Spell;

public class Rat extends Creature
{
	public Rat(Position pos, Map world)
	{	
		super(pos, world);

		setName("Rat");
		setStatus(new Status(this));
		setDead(false);
		setSee("You see a Rat.");
		setSpeed(800);
		setMaxhp(25);
		setHp(25);
		
		try 
		{
			setPic(new Image ("Pics/Rat.png"));
		} 
		catch (SlickException e) 
		{
			System.out.println("Fail to create picture - rat");
		}
		
		try
		{
			Image pic1 = new Image ("Pics/Rat.png");
			
			Image[] pics = {pic1};
			int[] durations = {10000};
			
			setAnim(new Animation(pics, durations));
		}
		catch (Exception e)
		{
			System.out.println("Fail to create animation - rat");
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
		return (new Dead_Rat(getPos()));
	}

}
