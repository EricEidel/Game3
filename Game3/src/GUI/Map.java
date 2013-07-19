package GUI;
import game_3_core.ItemHandler;
import game_3_core.Position;
import graphUtil.TileGraph;
import items.Item;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import tiles.DestroyItemTile;
import tiles.Tile;
import creatures.Creature;
import creatures.Player;

/*
 1 - Green, walkable
 2 - Water, not walkable
 default - Water, not walkable
 */

public class Map 
{
	static private Image land_pic;

	public static final int  DISPLAY_HEIGHT = 11; // How much to display on screen	>>>>> 	THIS IS y | i <<<<<<
	public static final int  DISPLAY_LENGTH = 15; // How much to display on screen	>>>>> 	THIS IS x | j <<<<<<
	
	static public final int Y_LIMIT = 7;	// This is how much padding has to happen on Y with water to avoid out of bounds
	static public final int X_LIMIT = 9;	// This is how much padding has to happen on X with water to avoid out of bounds
	
	static public int SIZE_OF_TILE = 50;
	
	static public final int Y_OFFSET = 10;
	static public final int X_OFFSET = 10;
	static public final int X_LEN_TOTAL = X_OFFSET + (DISPLAY_LENGTH * SIZE_OF_TILE);
	static public final int Y_LEN_TOTAL = Y_OFFSET + (DISPLAY_HEIGHT * SIZE_OF_TILE);
	
	static private final String seperator = ",";
	
	public int height = 25; // Actual length of the world  >>>>> 	THIS IS y | i <<<<<<
	public int length = 30;  // Actual height of the world	>>>>> 	THIS IS x | j <<<<<<
	
	private Tile[][] land;

	public Map(String name)
	{
		load_from_txt(name);
		try
		{
			land_pic =  new Image("Maps/map2.jpg");
		}
		catch (Exception e)
		{
			System.out.println("Could not load map2.jpg!");
		}
	}
	
	public Map(int height, int length)
	{
		this.height = height;
		this.length = length;
		this.land = create_land(height, length);
	}
	
	public Tile[][] create_land(int hieght, int length)
	{
		land = new Tile[hieght][length];
		
		// This makes ALL green
	   	 for (int i = 0; i < hieght ; i++)
			 for ( int j = 0; j < length ; j++)
				land[i][j] = Tile.makeNewTile(Tile.GREEN, new Position(j,i));		
			
	   	 // This does the first 7 columns
	   	 for (int i = 0; i < hieght; i++)	
	   		 for ( int j = 0; j < X_LIMIT ; j++)
	   			 land[i][j] = Tile.makeNewTile(Tile.WATER, new Position(j,i));
	   	 
	   	 // This does the last 7 columns
	   	 for (int i = 0; i < hieght; i++)	
	   		 for ( int j = length - X_LIMIT; j < length ; j++)
	   			 land[i][j] = Tile.makeNewTile(Tile.WATER, new Position(j,i));
	   	 
	   	 // This does the first 7 rows   	 
	   	 for (int i = 0; i < Y_LIMIT; i++)							
			 for ( int j = 0; j < length ; j++)
				land[i][j] = Tile.makeNewTile(Tile.WATER, new Position(j,i));

	   	 // This does the last 7 rows   	 
	   	 for (int i = hieght - Y_LIMIT; i < hieght; i++)							
			 for ( int j = 0; j < length ; j++)
				land[i][j] = Tile.makeNewTile(Tile.WATER, new Position(j,i));
	   	 
	   	 save_to_txt("test_new.txt");
	   	 return land;
	}
	
	public boolean save_to_txt(String file_name)
	{
		try
		{
			FileWriter fw = new FileWriter(file_name);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("H:"+height + seperator + "L:"+length);
			bw.newLine();
			for (int i=0; i<height; i++)
			{
				for (int j=0; j<length; j++)
				{
					bw.write(land[i][j].getType() + seperator);
				}
				bw.newLine();
			}
			bw.close();
			fw.close();
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Could not write to file!");
			e.printStackTrace();
			return false;
		}
	}
	
	public void load_from_txt(String file_name)
	{
		try
		{
			FileReader fr = new FileReader(file_name);
			BufferedReader br = new BufferedReader(fr);
		
			String line = "";
			while (!line.contains("width"))
			{
				line = br.readLine();
			}
			length = Integer.parseInt(line.split("=")[1]);
			
			while (!line.contains("height"))
			{
				line = br.readLine();
			}
			height = Integer.parseInt(line.split("=")[1]);
			
			
			while (!line.contains("data="))
			{
				line = br.readLine();
			}
			land = new Tile[height][length];
			
			line = br.readLine();
			int column = 0;
			while (!line.equals(""))
			{
				String[] one_line = line.split(seperator);
				
				for (int i=0; i<one_line.length-1; i++)
				{
					land[column][i] = Tile.makeNewTile(Integer.parseInt(one_line[i]), new Position(i, column));
				}
				column++;
				line = br.readLine();
			}
			
			br.close();
			fr.close();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void draw(Player player)
	{
		Position bounds = player.getPos(); // x = 5, y = 7;
		
		int x_start = (bounds.getX() - 7)*SIZE_OF_TILE;
		int y_start = (bounds.getY() - 5)*SIZE_OF_TILE;
		int x_end = (DISPLAY_LENGTH)*SIZE_OF_TILE;
		int y_end = (DISPLAY_HEIGHT)*SIZE_OF_TILE;
		
		Image on_screen = land_pic.getSubImage(x_start,y_start , x_end, y_end);
		on_screen.draw(10,10);
	}
	
	public void drawMove(Player player)
	{
		Position bounds = player.getPos(); // x = 5, y = 7;
		
		float part = (float)player.getInputDelta() / (float)player.getSpeed() ;
		int offset = (int) (part*Map.SIZE_OF_TILE);
		
		int x_start = (bounds.getX() - 7)*SIZE_OF_TILE;
		int y_start = (bounds.getY() - 5)*SIZE_OF_TILE;
		int x_end = (DISPLAY_LENGTH)*SIZE_OF_TILE;
		int y_end = (DISPLAY_HEIGHT)*SIZE_OF_TILE;

		switch (player.getMoving())
		{
			case 1:
				x_start = x_start-offset+SIZE_OF_TILE;
				y_start = y_start+offset-SIZE_OF_TILE;
				break;
			case 2:			
				//x_start = x_start;
				y_start = y_start+offset-SIZE_OF_TILE;
				break;
			case 3:	
				x_start = x_start+offset-SIZE_OF_TILE;
				y_start = y_start+offset-SIZE_OF_TILE;
				break;
			case 4:
				x_start = x_start-offset+SIZE_OF_TILE;
				//y_start = y_start;
				break;
			case 6:
				x_start = x_start+offset-SIZE_OF_TILE;
				//y_start = y_start;
				break;
			case 7:
				x_start = x_start-offset+SIZE_OF_TILE;
				y_start = y_start-offset+SIZE_OF_TILE;
				break;
			case 8:
				//x_start = x_start;
				y_start = y_start-offset+SIZE_OF_TILE;
				break;
			case 9:
				x_start = x_start+offset-SIZE_OF_TILE;
				y_start = y_start-offset+SIZE_OF_TILE;
				break;
		}
		
		Image on_screen = land_pic.getSubImage(x_start,y_start , x_end, y_end);
		on_screen.draw(10,10);
	}
	
	public boolean isMovable(int x, int y)
	{			
		return land[y][x].isMovable();
	}
	
	public Tile tileAt(Position pos)
	{						//5/7
		return land[pos.getY()][pos.getX()];
	}

	
	// This method returns a graph that represents the position in the world.
	// Tiles that can be moved to are connected and tiles that are not movable are not.
	public TileGraph getGraph(Position pos, Position target) 
	{
		return new TileGraph(pos, target, land);
	}

	public Creature find_player(Position pos) 
	{
		Position target_pos = getGraph(pos, pos).choosePlayer();
		return land[target_pos.getY()][target_pos.getX()].getC();
	}

	public Creature getCreature(Position target) 
	{
		return land[target.getY()][target.getX()].getC();
	}

	public void drawCoordinate(Player player, Graphics g) 
	{
		Position bounds = player.getPos(); // x = 5, y = 7;
		int y = 0;
		for (int i = bounds.getY()-5; i <= bounds.getY()+5; i++)  // y
		{
			int x = 0;
			for ( int j = bounds.getX()-7; j <= bounds.getX()+7; j++)	// x
			{
					g.drawString(land[i][j].getPos().toString() , X_OFFSET+(x*SIZE_OF_TILE), Y_OFFSET+(y*SIZE_OF_TILE));
				x++;
			}
			y++;
		}
		
	}

	public void kill(Creature c, ItemHandler ih) 
	{
		Position pos = c.getPos();
		land[pos.getY()][pos.getX()].setC(null);
		
		Item temp = c.getDeadItem();
		if (temp != null)
		{
			land[pos.getY()][pos.getX()].setItem(temp, ih);
			ih.add_item(temp);
		}
	}
	
	public void updateDestoryItem(GameContainer c, int delta)
	{
		for (Tile[] tRow: land)
			for  (Tile t: tRow)
			{
				if (t != null)
					if (t.isPlayDestroyItem())
					{
						DestroyItemTile tDestroy = (DestroyItemTile)t;
						tDestroy.update(c, delta);
					}
			}
	}
	
	public void drawDestroyItem()
	{
		for (Tile[] tRow: land)
			for  (Tile t: tRow)
			{
				if (t != null)
					if (t.isPlayDestroyItem())
					{
						DestroyItemTile tDestroy = (DestroyItemTile)t;
						tDestroy.PlayDestroyAnim(t.getPos());
					}
			}
	}
}
