package graphUtil;

import game_3_core.Position;

import java.util.ArrayList;

public class TileNode 
{
	private Position pos;
	private ArrayList<TileNode> neighbours;


	public ArrayList<TileNode> getNeighbours() 
	{
		return neighbours;
	}

	public TileNode(Position pos)
	{
		neighbours = new ArrayList<TileNode>();
		this.pos = pos;
	}
		
	public boolean contains_neighbour(TileNode tilenode)
	{
		return neighbours.contains(tilenode);
	}
	
	public void remove_neighbour(TileNode t)
	{
		if (contains_neighbour(t))
		{
			neighbours.remove(t);
		}
	}
	
	public boolean equals(TileNode t)
	{
		return pos.equals(t.pos);
	}
	
	// GETTERS AND SETTERS
	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}
	

	public void setNeighbours(ArrayList<TileNode> neighbour) {
		this.neighbours = neighbour;
	}

	public void add_neighbour(TileNode rest) 
	{
		if (rest.getPos().near(getPos(),1))
			neighbours.add(rest);		
	}
}

