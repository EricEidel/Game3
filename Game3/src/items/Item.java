package items;

import game_3_core.Position;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import GUI.Map;
import creatures.Player;

public abstract class Item 
{
	public static final int CONTAINER = 0;
	

	public static final int WEAPON = 1;
	public static final int HELM = 4;
	public static final int OFF_HAND= 5;
	public static final int CHEST = 6;
	public static final int PANTS = 7;
	public static final int BOOTS = 8;
	public static final int RING = 9;
	public static final int NECK = 10;
	
	public static final int FOOD = 11;
	public static final int POTION= 12;
	public static final int COIN = 13;
	
	public static final int OTHER = -1;
	
	public static final int SWORD = 1;
	public static final int AXE = 2;
	public static final int CLUB = 3;	
	
	private boolean moveItem;
	private boolean moveOnTop;
	private Animation anim;
	private Image pic;
	
	private int type;
	private Position pos;

	private String name;
	private String see;

	private boolean usable;
	
	public Item(Position pos)
	{
		this.pos = pos;
		this.usable = false;
		this.moveItem = true;
	}
	
	public void draw(int x_tile_dist, int y_tile_dist, Graphics g)
	{
		getPic().draw(Map.X_OFFSET+(x_tile_dist*Map.SIZE_OF_TILE), (y_tile_dist*Map.SIZE_OF_TILE)+Map.Y_OFFSET);
	}
	
	public void drawMoving(Player player, int x_tile_dist, int y_tile_dist)
	{
		float part = (float)player.getInputDelta() / (float)player.getSpeed() ;
		int offset = (int) (part*Map.SIZE_OF_TILE);
		
		//System.out.println(part);
		//System.out.println(offset);

		int x_move =((Player.getPlayerXCenter()+x_tile_dist)*Map.SIZE_OF_TILE)+Map.X_OFFSET; 
		int y_move =((Player.getPlayerYCenter()+y_tile_dist)*Map.SIZE_OF_TILE)+Map.Y_OFFSET;
		
		int tile = Map.SIZE_OF_TILE;
		
		int actual_x = 0;
		int actual_y = 0;
		switch (player.getMoving())
		{
			case 1:
				actual_x = x_move-tile+offset;
				actual_y = y_move+tile-offset;
				break;
			case 2:
				actual_x = x_move;
				actual_y = y_move+tile-offset;
				break;
			case 3:	
				actual_x = x_move+tile-offset;
				actual_y = y_move+tile-offset;
				break;
			case 4:
				actual_x = x_move-tile+offset;
				actual_y = y_move;
				break;
			case 6:
				actual_x = x_move+tile-offset;
				actual_y = y_move;
				break;
			case 7:
				actual_x = x_move-tile+offset;
				actual_y = y_move+offset-tile;
				break;
			case 8:			
				actual_x = x_move;
				actual_y = y_move+offset-tile;
				break;
			case 9:
				actual_x = x_move+tile-offset;
				actual_y = y_move+offset-tile;
				break;
		}
		
		getPic().draw(actual_x, actual_y);
	}
	
	// GETTERS AND SETTERS
	public Image getPic() {
		return pic;
	}
	public void setPic(Image pic) {
		this.pic = pic;
	}
	public boolean isMovable() {
		return moveOnTop;
	}
	public void setMovable(boolean moveOnTop) {
		this.moveOnTop = moveOnTop;
	}
	public Position getPos() {
		return pos;
	}
	public void setPos(Position pos) {
		this.pos = pos;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public boolean isWeapon()
	{
		if (getType()==CLUB||getType()==AXE||getType()==SWORD)
			return true;
					
		return false;
	}

	public String getSee() {
		return see;
	}

	public void setSee(String see) {
		this.see = see;
	}

	public Animation getAnim() {
		return anim;
	}

	public void setAnim(Animation anim) {
		this.anim = anim;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public abstract void use(Player player);

	public boolean isUsable() 
	{
		return usable;
	}
	
	public void setUsable(boolean usable) 
	{
		this.usable = usable;
	}
	
	public boolean isMoveItem() {
		return moveItem;
	}

	public void setMoveItem(boolean moveItem) {
		this.moveItem = moveItem;
	}

}
