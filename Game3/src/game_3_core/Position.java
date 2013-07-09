package game_3_core;

import java.util.ArrayList;

import creatures.Player;

public class Position 
{
	private int x;
	private int y;
	
	public Position ()
	{
		this.x = 0;
		this.y = 0;
	}
	
	public Position (int x, int y)
	{
		this.x = x;
		this.y = y;
	}	
	
	public Position (Position pos)
	{
		this.x = pos.getX();
		this.y = pos.getY();
	}	
	
	public String toString()
	{
		return x+"/"+y;
	}
	public int getX() 
	{
		return x;
	}
	
	public void setX(int x) 
	{
		this.x = x;
	}
	
	public int getY() 
	{
		return y;
	}
	
	public void setY(int y) 
	{
		this.y = y;
	}
	
	public boolean isActiveRange(Position pos2)
	{
		if (Math.abs(getX()-pos2.getX()) > 9)
			return false;
		else if (Math.abs(getY()-pos2.getY()) > 7)
			return false;
		else
			return true;
	}

	public boolean isDrawableRange(Position pos2) 
	{
		if (Math.abs(getX()-pos2.getX()) > 7)
			return false;
		else if (Math.abs(getY()-pos2.getY()) > 5)
			return false;
		else
			return true;
	}

	public boolean near(Position pos, int distance) 
	{
		int distance_x = getX() - pos.getX();
		int distance_y = getY() - pos.getY();
		
		if (distance_x > distance || distance_x < -distance)
			return false;
		
		if (distance_y > distance || distance_y < -distance)
			return false;
		
		return true;
	}

	public static boolean diagonal(Position pos, Position pos2) 
	{
		if (pos.getX() == pos2.getX())
			return false;
		if (pos.getY() == pos2.getY())
			return false;
		return true;
	}
	
	public int find_diff(Position pos)
	{
		int x_diff = this.x-pos.getX();
		int y_diff = this.y-pos.getY();
		
		//1
		if (x_diff == 1 && y_diff == -1)
		{
			return Player.down_left;
		} //2
		else if (x_diff == 0 && y_diff == -1)
		{
			return Player.down;
		} //3
		else if (x_diff == -1 && y_diff == -1)
		{
			return Player.down_right;
		} //4
		else if (x_diff == 1 && y_diff == 0)
		{
			return Player.left;
		} //6
		else if (x_diff == -1 && y_diff == 0)
		{
			return Player.right;
		} //7
		else if (x_diff == 1 && y_diff == 1)
		{
			return Player.up_left;
		} //8
		else if (x_diff == 0 && y_diff == 1)
		{
			return Player.up;
		} //9
		else if (x_diff == -1 && y_diff == 1)
		{
			return Player.up_right;
		} 
		
		return Player.no_move;
	}
	
}
