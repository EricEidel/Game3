package game_3_core;

import creatures.Creature;
import sui.Label;

public class LabelTracker 
{
	Label label;
	int delta;
	Creature c;
	
	public LabelTracker(Creature c, Label label, int delta)
	{
		this.c = c;
		this.label = label;
		this.delta = delta;
	}
}
