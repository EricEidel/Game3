package creatures;

import game_3_core.Position;

import java.util.ArrayList;

import tiles.Tile;


public class Path 
{
	private ArrayList<Position> tiles;
	
	public Path(ArrayList<Position> tiles) 
	{
		this.tiles = tiles;
	}
	
	public Position getNext()
	{
		Position pos = tiles.get(0);
		tiles.remove(0);
		return pos;
	}
	
	public boolean hasNext()
	{
		return (tiles.size()>0);
	}

	public boolean hasOneNext() 
	{
		return (tiles.size()==1);
	}

	public Path removeLast() 
	{
		tiles.remove(tiles.size()-1);
		return this;
	}
}
