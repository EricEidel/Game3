package creatures;

import game_3_core.Position;
import game_3_core.SimpleGame;
import items.Item;

import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import sui.Label;
import tiles.Tile;
import GUI.Map;
import GUI.Status;
import abilities.Spell;


// TODO when creature is on the same spot, need to cancel animation with setMov(no_mov);
public abstract class Creature 
{
	static Color RED = new Color(255, 0, 0);
    static Color BLACK = new Color(0,0,0);
   
    // MOVING CONSTANTS
    public static final int down_left = 1;
    public static final int down = 2;
    public static final int down_right = 3;
    public static final int left = 4;
    public static final int no_move = 5;
    public static final int right = 6;
    public static final int up_left = 7;
    public static final int up = 8;
    public static final int up_right = 9;
	
	static final int AGRESSIVE = 1;
	static final int COWARD = 2;
	static final int MELEE = 1;
	static final int RANGED = 2;
	
	static final int DEF_HP = 100;
	static final int DEF_MP = 100;
	static final int DEF_SPEED = 800;
	
	private Map world;
	private Tile tile;
	
	static final Random rand = new Random();
	
	private static final int VAR_DAMAGE = 5;
	private static final int MELEE_BASE_DAMAGE = 5;
	private static final int RANGED_BASE_DAMAGE = 7;
	
	private Status status;
	private int hp;
	private int maxhp;
	private int mp;
	private int maxmp;
	
	private Creature target;
	private Creature targeted;

	private int isMoving;
	
	private boolean dead;
	
	private Animation anim;
	
	private String name;
	private int speed;
	private int attackSpeed;
	
	private Position oldPos;
	private Position pos;
	private Image pic;
	
	private boolean active;	// If the creature is active it's AI starts and it gets drawn
	
	private boolean frindly; // if a creature is friendly, it will not attack before being attacked.
	private int behaviour; // agressive or coward. Cowards run away at 15% hp, agressives do 5% more damage when below 15%.
	private int type;	// melee or ranged. Melee run to engage, 
	
	private Path path;
	
	private int inputDelta = 0;

	private int attackDelta = 0;
	
	private boolean attacked;
	
    private Item deadItem;

	private String see;
	
	private Label damageTaken;
	private Label healTaken;
	
	private int deltaDamage;
	public static final int show_damage_time = 1200;

	public Creature(Position pos, Map world)
	{

		setMoving(5);
		attacked = false;
		setTarget(null);
		setTile(world.tileAt(pos)); 
		getTile().setC(this);
		this.setPos(pos);
		this.world = world;
		
		setHp(DEF_HP);
		setMp(DEF_MP);
		setMaxhp(DEF_HP);
		setMaxmp(DEF_MP);
		setSpeed(DEF_SPEED);
		
		setActive(false);
		setDead(false);
		
		damageTaken = new Label("");
		damageTaken.setForeground(Color.red);
		
		healTaken = new Label("");
		healTaken.setForeground(Color.green);
	}
	
	public void move(Map world) 
	{
		setOldPos(getPos());
		if (frindly)
		{
	    	int des = 1 + rand.nextInt(4);
		
			switch (des)
			{
				case 1:
					moveTo(new Position(getPos().getX()-1, getPos().getY()), world);
					break;
				case 2:
					moveTo(new Position(getPos().getX()+1, getPos().getY()), world);
					break;
				case 3:
					moveTo(new Position(getPos().getX(), getPos().getY()-1), world);
					break;
				case 4:
					moveTo(new Position(getPos().getX(), getPos().getY()+1), world);
					break;
				case 5:
					break;
				default:
			}
		}
		else
		{
			if (path != null)
			{
				if (path.hasNext())
					moveTo(path.getNext(), world);
			}
			if (target == null)
    		{
				target = world.find_player(getPos());
    		}
			else
			{
				path = world.getGraph(getPos(), target.getPos()).findShortestPath();
			}
    	}
		if (getPos()==getOldPos())
			setMoving(no_move);
	}
	
	public void melee_attack()
	{
		getTarget().takeDamage(get_damage(MELEE_BASE_DAMAGE));
	}
	
	public void ranged_attack()
	{
		getTarget().takeDamage(get_damage(RANGED_BASE_DAMAGE));
	}
	
	public int get_damage(int base)
	{
		return (base + SimpleGame.rand.nextInt(VAR_DAMAGE));
	}
	public abstract int cast_spell(Spell spell);
	
	// MOVEMENT
	

	public void moveTo(Position target, Map world)
	{			
		int diff = getPos().find_diff(target);
		setMoving(diff);
		if (world.isMovable(target.getX(), target.getY()))
		{
			setPos(target);
			tile.setC(null);
			tile = world.tileAt(target);
			tile.setC(this);
		}
		else
		{
			setMoving(Creature.no_move);
			setPath(null);
			setMoving(Creature.no_move);
		}
	}

	/// ==================== DRAW METHODS =============
		
	// This method draws when BOTH player and C are moving
	public void drawMoving(Player player, int x_tile_dist,  int y_tile_dist ,Graphics g)
	{
		float c_part = (float)inputDelta / (float)getSpeed() ;
		int c_offset = (int) (c_part*Map.SIZE_OF_TILE);
		
		float p_part = (float)player.getInputDelta() / (float)player.getSpeed() ;
		int p_offset = (int) (p_part*Map.SIZE_OF_TILE);
		
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
				actual_x = x_move-tile+p_offset;
				actual_y = y_move+tile-p_offset;
				break;
			case 2:
				actual_x = x_move;
				actual_y = y_move+tile-p_offset;
				break;
			case 3:	
				actual_x = x_move+tile-p_offset;
				actual_y = y_move+tile-p_offset;
				break;
			case 4:
				actual_x = x_move-tile+p_offset;
				actual_y = y_move;
				break;
			case 6:
				actual_x = x_move+tile-p_offset;
				actual_y = y_move;
				break;
			case 7:
				actual_x = x_move-tile+p_offset;
				actual_y = y_move+p_offset-tile;
				break;
			case 8:			
				actual_x = x_move;
				actual_y = y_move+p_offset-tile;
				break;
			case 9:
				actual_x = x_move+tile-p_offset;
				actual_y = y_move+p_offset-tile;
				break;
		}
		
		switch (getMoving())
		{
			case 1:
				//anim.draw(x_move+tile-offset, y_move+offset-tile);
				actual_x = actual_x-c_offset+tile;
				actual_y = actual_y+c_offset-tile;
				break;
			case 2:
				//anim.draw(x_move, y_move+offset-tile);				
				//actual_x = actual_x;
				actual_y = actual_y+c_offset-tile;
				break;
			case 3:	
				//anim.draw(x_move-tile+offset, y_move+offset-tile);
				actual_x = actual_x+c_offset-tile;
				actual_y = actual_y+c_offset-tile;
				break;
			case 4:
				//anim.draw(x_move+tile-offset, y_move);
				actual_x = actual_x-c_offset+tile;
				//actual_y = actual_y;
				break;
			case 6:
				//anim.draw(x_move-tile+offset, y_move);
				actual_x = actual_x+c_offset-tile;
				//actual_y = actual_y;
				break;
			case 7:
				//anim.draw(x_move+tile-offset, y_move+tile-offset);
				actual_x = actual_x-c_offset+tile;
				actual_y = actual_y-c_offset+tile;
				break;
			case 8:
				//anim.draw(x_move, y_move+tile-offset);
				//actual_x = actual_x;
				actual_y = actual_y-c_offset+tile;
				break;
			case 9:
				//anim.draw(x_move-tile+offset, y_move+tile-offset);
				actual_x = actual_x+c_offset-tile;
				actual_y = actual_y-c_offset+tile;
				break;
		}
		
		anim.draw(actual_x, actual_y);
		
		if (attacked)
		{
			g.setColor(RED);
			g.drawRect(actual_x, actual_y, Map.SIZE_OF_TILE, Map.SIZE_OF_TILE );
			g.setColor(BLACK);
		}
		
		getStatus().drawMoveMonster(actual_x, actual_y);
	}
	
	// This method draws when player is still
	public void drawMovingNoPlayerMov(int x_tile_dist, int y_tile_dist, Graphics g) 
	{
		//System.out.println(getMoving());
		//System.out.println(inputDelta);
		
		float part = (float)getInputDelta() / (float)getSpeed() ;
		int offset = (int) (part*Map.SIZE_OF_TILE);
		
		//System.out.println(part);
		//System.out.println(offset);

		int x_move =((Player.getPlayerXCenter()+x_tile_dist)*Map.SIZE_OF_TILE)+Map.X_OFFSET; 
		int y_move =((Player.getPlayerYCenter()+y_tile_dist)*Map.SIZE_OF_TILE)+Map.Y_OFFSET;
		
		int tile = Map.SIZE_OF_TILE;
		
		int actual_x = 0;
		int actual_y = 0;
		switch (getMoving())
		{
			case 1:
				//anim.draw(x_move+tile-offset, y_move+offset-tile);
				actual_x = x_move+tile-offset;
				actual_y = y_move+offset-tile;
				break;
			case 2:
				//anim.draw(x_move, y_move+offset-tile);				
				actual_x = x_move;
				actual_y = y_move+offset-tile;
				break;
			case 3:	
				//anim.draw(x_move-tile+offset, y_move+offset-tile);
				actual_x = x_move-tile+offset;
				actual_y = y_move+offset-tile;
				break;
			case 4:
				//anim.draw(x_move+tile-offset, y_move);
				actual_x = x_move+tile-offset;
				actual_y = y_move;
				break;
			case 6:
				//anim.draw(x_move-tile+offset, y_move);
				actual_x = x_move-tile+offset;
				actual_y = y_move;
				break;
			case 7:
				//anim.draw(x_move+tile-offset, y_move+tile-offset);
				actual_x = x_move+tile-offset;
				actual_y = y_move+tile-offset;
				break;
			case 8:
				//anim.draw(x_move, y_move+tile-offset);
				actual_x = x_move;
				actual_y = y_move+tile-offset;
				break;
			case 9:
				//anim.draw(x_move-tile+offset, y_move+tile-offset);
				actual_x = x_move-tile+offset;
				actual_y = y_move+tile-offset;
				break;
		}
		
		anim.draw(actual_x, actual_y);
		
		if (attacked)
		{
			g.setColor(RED);
			g.drawRect(actual_x, actual_y, Map.SIZE_OF_TILE, Map.SIZE_OF_TILE );
			g.setColor(BLACK);
		}
		
		getStatus().drawMoveMonster(actual_x, actual_y);
		
	}
	
	// This method draws when c is still (either player move or not, don't care)
	public void draw(int x_tile_dist, int y_tile_dist, Graphics g)
	{
		getPic().draw(Map.X_OFFSET+(x_tile_dist*Map.SIZE_OF_TILE), (y_tile_dist*Map.SIZE_OF_TILE)+Map.Y_OFFSET);
		
		if (attacked)
		{
			g.setColor(RED);
			g.drawRect(Map.X_OFFSET+(x_tile_dist*Map.SIZE_OF_TILE), (y_tile_dist*Map.SIZE_OF_TILE)+Map.Y_OFFSET, Map.SIZE_OF_TILE, Map.SIZE_OF_TILE );
			g.setColor(BLACK);
		}
		status.draw(x_tile_dist, y_tile_dist);
	}
	
	public void drawMovingNoCreatureMov(Player player, int x_tile_dist, int y_tile_dist, Graphics g) 
	{
		{
			float p_part = (float)player.getInputDelta() / (float)player.getSpeed() ;
			int p_offset = (int) (p_part*Map.SIZE_OF_TILE);
			
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
					actual_x = x_move-tile+p_offset;
					actual_y = y_move+tile-p_offset;
					break;
				case 2:
					actual_x = x_move;
					actual_y = y_move+tile-p_offset;
					break;
				case 3:	
					actual_x = x_move+tile-p_offset;
					actual_y = y_move+tile-p_offset;
					break;
				case 4:
					actual_x = x_move-tile+p_offset;
					actual_y = y_move;
					break;
				case 6:
					actual_x = x_move+tile-p_offset;
					actual_y = y_move;
					break;
				case 7:
					actual_x = x_move-tile+p_offset;
					actual_y = y_move+p_offset-tile;
					break;
				case 8:			
					actual_x = x_move;
					actual_y = y_move+p_offset-tile;
					break;
				case 9:
					actual_x = x_move+tile-p_offset;
					actual_y = y_move+p_offset-tile;
					break;
			}
			
			getPic().draw(actual_x, actual_y);
			
			if (attacked)
			{
				g.setColor(RED);
				g.drawRect(actual_x, actual_y, Map.SIZE_OF_TILE, Map.SIZE_OF_TILE );
				g.setColor(BLACK);
			}
			
			getStatus().drawMoveMonster(actual_x, actual_y);
		}
	}
	
	// GETTERS AND SETTERS.
	
	public int getHp() 
	{
		return hp;
	}

	public void takeDamage(int dmg)
	{
		// battle text points
		damageTaken.setText("-"+Integer.toString(dmg));
		damageTaken.pack();
		deltaDamage = show_damage_time;
		
		int x = getPos().getX() - targeted.getPos().getX();
		int y = getPos().getY() - targeted.getPos().getY();
		
		int center_x = 7*50 + 10;
		int center_y = 5*50 + 10;
		x = center_x + x*50+30;
		y = center_y + y*50+10;
		
		damageTaken.setLocation(x,y);
		
		
		this.hp-=dmg;
		if (this.hp<1)
		{
			this.hp = 0;
			setDead(true);
		}
	}
	
	public void animateBT()
	{
		float x = damageTaken.getAbsoluteX();
		float y = damageTaken.getAbsoluteY();
		damageTaken.setLocation(x,y-deltaDamage/500);
	}
	
	public Label getDamageTaken() {
		return damageTaken;
	}

	public void setDamageTaken(Label damageTaken) {
		this.damageTaken = damageTaken;
	}

	public void setHp(int hp) 
	{
		this.hp = hp;
		if (this.hp<1)
		{
			this.hp = 0;
			setDead(true);
		}
	}

	public int getMp() 
	{
		return mp;
	}

	public void setMp(int mp) 
	{
		this.mp = mp;
	}

	public boolean isFrindly() 
	{
		return frindly;
	}

	public void setFrindly(boolean frindly) 
	{
		this.frindly = frindly;
	}

	public int getBehaviour() 
	{
		return behaviour;
	}

	public void setBehaviour(int behaviour) 
	{
		this.behaviour = behaviour;
	}

	public int getType() 
	{
		return type;
	}

	public void setType(int type) 
	{
		this.type = type;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public Image getPic() {
		return pic;
	}

	public void setPic(Image pic) {
		this.pic = pic;
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getMaxhp() {
		return maxhp;
	}

	public void setMaxhp(int maxhp) {
		this.maxhp = maxhp;
	}

	public int getMaxmp() {
		return maxmp;
	}

	public void setMaxmp(int maxmp) {
		this.maxmp = maxmp;
	}
	
	public Status getStatus()
	{
		return status;
	}
	
	
	public void setStatus(Status status) 
	{
		this.status = status;
	}

	public Tile getTile() 
	{
		return tile;
	}

	public void setTile(Tile tile) 
	{
		this.tile = tile;
	}

	public Map getNewTM() 
	{
		return world;
	}

	public void setNewTM(Map world) 
	{
		this.world = world;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}
	
	public Map getWorld() {
		return world;
	}

	public void setWorld(Map world) {
		this.world = world;
	}
	
	public Creature getTarget() {
		return target;
	}

	public void setTarget(Creature target) {
		this.target = target;
	}

	public void toogleAttacked() 
	{
		this.attacked = !attacked;
	}

	public int getAttackSpeed() {
		return attackSpeed;
	}

	public void setAttackSpeed(int attackSpeed) {
		this.attackSpeed = attackSpeed;
	}

	public Creature getTargeted() {
		return targeted;
	}

	public void setTargeted(Creature targeted) {
		this.targeted = targeted;
	}

	public int getMoving() {
		return isMoving;
	}

	public void setMoving(int isMoving) {
		this.isMoving = isMoving;
	}
	
	public Position getOldPos() {
		return oldPos;
	}

	public void setOldPos(Position oldPos) {
		this.oldPos = oldPos;
	}
	
	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public Animation getAnim() {
		return anim;
	}

	public void setAnim(Animation anim) {
		this.anim = anim;
	}

	public int getAttackDelta() {
		return attackDelta;
	}

	public void setAttackDelta(int attackDelta) 
	{
		this.attackDelta = attackDelta;
	}
	
	public int getInputDelta() 
	{
		return inputDelta;
	}

	public void setInputDelta(int inputDelta) 
	{
		this.inputDelta = inputDelta;
	}

	public String getSee() {
		return see;
	}

	public void setSee(String see_string) {
		this.see = see_string;
	}

	public Item getDeadItem() 
	{
		return this.deadItem;
	}
	
	public void setDeadItem(Item deadItem) {
		this.deadItem = deadItem;
	}

	public int getDeltaDamage() 
	{
		return this.deltaDamage;
	}
	
	public void setDeltaDamage( int deltaDamage) 
	{
		this.deltaDamage = deltaDamage;
	}
}
