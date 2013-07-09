package abilities;

import game_3_core.Position;

import org.newdawn.slick.Animation;

public class Spell 
{
	private static final int DEF_DAMAGE = 100;
	private static final int DEF_AREA = 3;
	
	Animation effect;
	Position pos;
	int area;
	int damage;
	
	public Spell(Position pos)
	{
		this.damage = DEF_DAMAGE;
		this.area = DEF_AREA;
		this.pos = pos;
		
	}
}
