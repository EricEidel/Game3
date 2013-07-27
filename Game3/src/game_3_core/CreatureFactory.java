package game_3_core;

import GUI.Map;
import creatures.Creature;
import creatures.Rat;
import creatures.Wolf;

public class CreatureFactory 
{
	static final int WOLF = 29;
	static final int RAT = 30;
	
	static public Creature makeNewCreature(int type, Position pos, Map land)
	{
		if (type == WOLF)
			return new Wolf(pos, land);
		if (type == RAT)
			return new Rat(pos, land);
		
		return null;
	}
}

