package graphUtil;

import game_3_core.Position;
import game_3_core.SimpleGame;

import java.util.ArrayList;
import java.util.Collections;

import tiles.Tile;

import creatures.Creature;
import creatures.Path;
import creatures.Player;

public class TileGraph 
{
	TileNode root;
	TileNode target;
	ArrayList<TileNode> nodes;
	ArrayList<Position> player_pos;
	Tile[][] land;
	
	// This method creates a graph out of the section of the screen where pos is the middle.
	public TileGraph(Position location, Position target_pos, Tile[][] land) 
	{
		//System.out.println(location);
		player_pos = new ArrayList<Position>();
		this.land = land;
		nodes = new ArrayList<TileNode>();
		
		int HEIGHT = 17;
		int LEN = 21;
		
		Tile[][] temp = new Tile[HEIGHT][LEN];
		
		int pos_x = location.getX();
		int pos_y = location.getY();
		
		
		int y= 0;
		// total of HEIGH
		int half_h = (HEIGHT-1)/2;
		int half_l = (LEN - 1)/2;
		for (int i = pos_y - half_h; i<=pos_y+half_h; i++)
		{
			int x= 0;
			// total of LEN
			for (int j = pos_x - half_l; j<=pos_x+half_l; j++)
			{
				temp[y][x] = land[i][j];
				if (land[i][j].getC() instanceof Player)
				{
					player_pos.add(land[i][j].getPos());
				}
					
				x++;
			}
			y++;
		}
		
		/*
		// prints the temp array, what we see on screen.
		for (int i = 0; i<HEIGHT; i++)
		{
			for (int j = 0; j<LEN; j++)
				System.out.print(temp[i][j].getPos() + "\t");
			System.out.println();
		}
		*/
		
		// create and add the root. It is not movable, since creature calling this s
		this.root = new TileNode(location);
		nodes.add(this.root);
		
		this.target = new TileNode(target_pos);
		nodes.add(this.target);
		
		TileNode tempNode;	
		for (int i = 0; i<HEIGHT; i++)
		{
			for (int j = 0; j<LEN; j++)
			{
				if (temp[i][j].isMovable())
				{
					tempNode = new TileNode(temp[i][j].getPos());
					if (!nodes.contains(tempNode))
						nodes.add(tempNode);
				}
			}
		}
		
		// iterate through the graph and for every node, add it's adjacent movable nodes as neighbours. The adjecancy is checked in add_neighbour
		for (TileNode check: nodes)
		{
			for (TileNode rest: nodes)
			{
				if (!check.getPos().equals(rest.getPos()))
					check.add_neighbour(rest);
			}
		}
		
		
		/*
		// Prints a list of all the nodes in the graph and their neighbours
		System.out.println("START:");
		System.out.println("ROOT: " + this.root.getPos().toString());
		for (TileNode n: nodes)
		{
			System.out.print(n.getPos()+ " Neighbours:");
			for (TileNode ne: n.getNeighbours())
			{
				System.out.print(ne.getPos() + " ");
			}
			System.out.println();
		}
		System.out.println("\nEND");
		*/
	}

	public Path findShortestPath() 
	{
		int WHITE = 1;
		int GREY = 2;
		int BLACK = 3;
		double inf = Double.MAX_VALUE;
		
		int[] color = new int[nodes.size()];
		double[] dist = new double[nodes.size()];
		TileNode[] pi = new TileNode[nodes.size()];
		
		for (int i=0; i<nodes.size()-1; i++)
		{
			color[i] = WHITE;
			dist[i] = inf;
			pi[i] = null;
		}
		
		TileMinHeap tmh = new TileMinHeap(nodes.size());
		
		// This is the index of the root - always 0.
		int s = 0;
		// init the alg, start with the root.
		color[s] = GREY;
		dist[s] = 0;
		tmh.insert(nodes.get(0),0);
		
		while (tmh.getSize()!=0)
		{
			TileNode u = tmh.deleteMin().getValue();
			int index_of_u = nodes.indexOf(u);
			double c = dist[index_of_u];
			for(TileNode v: u.getNeighbours())
			{
				int index_of_v = nodes.indexOf(v);
				
				if (color[index_of_v] == WHITE)
				{
					// this is where a wieght can be added - 1 for all for now
					if (Position.diagonal(u.getPos(),v.getPos()))
						dist[index_of_v] = c + 1.2;
					else
						dist[index_of_v] = c + 1;
					color[index_of_v] = GREY;
					pi[index_of_v] = u;
					tmh.insert(v, dist[index_of_v]);
				}
				else if (color[index_of_v] == GREY)
				{
					/*if (((c+1) == dist[index_of_v])&&(v.getPos().getX() == u.getPos().getX() || v.getPos().getY() == u.getPos().getY()))
					{
							//System.out.println("YES!" + v.getPos().toString() + " " + target.getPos().toString());
							//dist[index_of_v] = c + 1;
							pi[index_of_v] = u;
							//tmh.updatePriority(v, dist[index_of_v]);
					}else*/
					if ((c+1) < dist[index_of_v])
					{	
						if (Position.diagonal(u.getPos(),v.getPos()))
							dist[index_of_v] = c + 1.2;
						else
							dist[index_of_v] = c + 1;
						pi[index_of_v] = u;
						tmh.updatePriority(v, dist[index_of_v]);
					}
				}
				else
					color[index_of_v] = BLACK;
			}
		}
		
		/*
		// This prints out some debugging stuff
		System.out.println();
		System.out.println(target.getPos().toString());
		
		System.out.println();
		for (int i = 0; i<nodes.size()-1; i++)
		{
			System.out.print(nodes.get(i).getPos().toString() + "\t");
		}
		System.out.println();
		for (int i = 0; i<nodes.size()-1; i++)
		{
			System.out.print(dist[i] + "\t");
		}
		System.out.println();
		for (int i = 0; i<nodes.size()-1; i++)
		{
			if (pi[i]!=null)
				System.out.print(pi[i].getPos() + "\t");
			else
				System.out.print(null + "\t");
		}
		*/
		
		ArrayList<Position> path = new ArrayList<Position>();
		TileNode temp_path = pi[nodes.indexOf(target)];
		
		try
		{
			while (!temp_path.equals(root))
			{
				path.add(temp_path.getPos());
				temp_path = pi[nodes.indexOf(temp_path)];
				if (temp_path == null)
				{
					System.out.println("Target is not connected!!!");
					break;
				}
			}
			
			if (temp_path != null)
			{
				Collections.reverse(path);
				path.add(target.getPos());
				/*
				System.out.println("\n NEW PATH:");
				for (Position p: path)
				{
					System.out.println(p.toString());
				}
				*/
			}
			return new Path(path);
		}
		catch (NullPointerException e)
		{
			System.out.println("can't move there!");
			return null;
		}
	}

	public Position choosePlayer() 
	{
		Position target = choose_one(player_pos);
		
		return target;
	}
	
	public Position choose_one(ArrayList<Position> player_pos)
	{
		if (player_pos.size() == 0)
			return null;
		if (player_pos.size() == 1)
			return player_pos.get(0);
		else
			return player_pos.get(SimpleGame.rand.nextInt(player_pos.size()));
	}
}
