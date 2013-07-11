package creatures;
import game_3_core.ContainerHandler;
import game_3_core.ItemHandler;
import game_3_core.Position;
import game_3_core.SimpleGame;
import graphUtil.TileGraph;
import items.Container;
import items.Inv;
import items.Item;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import GUI.HUD;
import GUI.Status;
import GUI.Map;
import abilities.Spell;



public class Player extends Creature
{
	// Player constants
	private static final int PLAYER_Y_CENTER = 5;
	private static final int PLAYER_X_CENTER = 7;
	
	public static final int Y_PLAYER_START = 10;	// array index at which player starts for y	
	public static final int X_PLAYER_START = 11;	// array index at which player starts for x
	
	private GameContainer gc;
	private HUD hud;
	
	private Animation anim;

	private Inv inv;
	
	Item targetItem;
	Item tryUse;
	
	private Item held;

	public ContainerHandler contH;
	
	public Player(Position pos, Map world)
	{
		super(pos, world);
		gc = SimpleGame.getGC();
   	 	
		setSpeed(600); // How often a player moves a tile - miliseconds
		setAttackSpeed(1000);
		
		setInputDelta(0); 
		setAttackDelta(0);
		
		setName("Player 1");
		setStatus(new Status(this));
		setActive(true);
		
		try 
		{
			setPic(new Image ("Pics/Player.gif"));
		} 
		catch (SlickException e) 
		{
			e.printStackTrace();
		}
		
		hud = new HUD(this);
		setPath(null);
		
		inv= new Inv();
		
		targetItem = null;
		contH = new ContainerHandler(this);
		
		try
		{
			Image pic1 = new Image ("Pics/Player.gif");
			Image pic2 = new Image ("Pics/Player_mov.gif");
			
			Image[] pics = {pic1, pic2};
			int[] durations = {100, 100};
			
			anim = new Animation(pics, durations);
		}
		catch (Exception e)
		{
			System.out.println("No such picture");
		}
		
		setSee("You see yourself. You are as handsome as always.");
		setTryUse(null);
	}

	static public int getPlayerXCenter()
	{
		return PLAYER_X_CENTER;
	}
	
	static public int getPlayerYCenter()
	{
		return PLAYER_Y_CENTER;
	}
	
	public void draw()
	{
		getPic().draw(Map.X_OFFSET+(PLAYER_X_CENTER*Map.SIZE_OF_TILE), (PLAYER_Y_CENTER*Map.SIZE_OF_TILE)+Map.Y_OFFSET);
		getStatus().draw(PLAYER_X_CENTER,PLAYER_Y_CENTER);
		hud.draw();
	}


	public void attack()
	{
		if (inv.getWeapon_type() == Inv.MELEE)
			melee_attack();
		else
			ranged_attack();
	}
	
	@Override
	public void melee_attack() 
	{
		if (getTarget().getPos().near(getPos(), 1))
		{	
			getTarget().takeDamage(5);
			System.out.println("You delt 5 damage to "+getTarget().getName() + " and it still has " + getTarget().getHp() + "left!");
		}
	}

	public void ranged_attack()
	{
		getTarget().takeDamage(5);
	}
	
	@Override
	public int cast_spell(Spell spell) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public void drawMoving()
	{
		float part = (float)getInputDelta() / (float)getSpeed() ;
		int offset = (int) (part*Map.SIZE_OF_TILE);
		

		int x_move =((PLAYER_X_CENTER)*Map.SIZE_OF_TILE)+Map.X_OFFSET; 
		int y_move =((PLAYER_Y_CENTER)*Map.SIZE_OF_TILE)+Map.Y_OFFSET;
		
		anim.draw(x_move, y_move);
		getStatus().drawMove(getMoving(), offset);
		hud.draw();
	
	}

	// This handles the player movment
	public void move(Map land) 
	{
		Input input = gc.getInput();
		boolean manual_move = false;
		
		if(input.isKeyPressed(Input.KEY_A)||input.isKeyDown(Input.KEY_A))
		{
			setOldPos(getPos());
			goLeft(land);
			manual_move = true;
			setPath(null);
		}

		else if(input.isKeyPressed(Input.KEY_D)||input.isKeyDown(Input.KEY_D))
		{
			setOldPos(getPos());
			goRight(land);
			manual_move = true;
			setPath(null);
		}

		else if(input.isKeyPressed(Input.KEY_W)||input.isKeyDown(Input.KEY_W))
		{
			setOldPos(getPos());
			goUp(land);
			manual_move = true;
			setPath(null);
		}
	
		else if(input.isKeyPressed(Input.KEY_S)||input.isKeyDown(Input.KEY_S))
	    {
	    	setOldPos(getPos());
	    	goDown(land);
	    	manual_move = true;
			setPath(null);
	    }

		else if(!manual_move)
	    {
	    	if (getPath() != null)
	    	{
	    		setOldPos(getPos());
	    		if (getPath().hasNext())
	    			moveTo(getPath().getNext(), getWorld());
	    		else
	    			setPath(null);
	    	}
	    	else
	    	{
	    		setMoving(Creature.no_move);
	    	}
	    }	    
	}
	
	// LEFT MOUSE CLICK CHECK FOR PLAYER
	public void check_mouse_click(Map land)
	{
		Input input = gc.getInput();
		
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON) && !(input.isKeyDown(Input.KEY_RCONTROL)) && !((input.isKeyDown(Input.KEY_LCONTROL))))
    	{
    		//System.out.println("click!");
    		Position target = proccess_mouse(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
    		if (target!=null)
    		{
    			TileGraph graph = land.getGraph(getPos(), target);
    			setPath(graph.findShortestPath());
    		}
    	}
	}
	
	// RIGHT MOUSE CLICK CHECK FOR PLAYER - ATTACK OR USE ITEM
	public void check_attack_command(Map land)
	{ 
		Input input = gc.getInput();
		
	    if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON))
	    {
	    	Position target = proccess_mouse(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
	    	// clicked on a tile on the map
	    	if (target != null)
	    	{
		    	Creature attacked = land.getCreature(target);
		    	// There is a creature on that tile
		    	if (attacked!=null && !(attacked instanceof Player))
		    	{
		    		// had a target before
		    		if (getTarget()!=null)
		    		{
		    			// had a different target before
		    			if (!getTarget().getPos().equals(attacked.getPos()))
				    	{
		    				// cancel attack on old target
		    				getTarget().toogleAttacked();
		    				getTarget().setTargeted(null);
		    				
		    				// set attack on new target
				    		attacked.toogleAttacked();
				    		setTarget(attacked);
				    		getTarget().setTargeted(this);
				    	}
		    			// had the same target as before
		    			else
		    			{
		    				attacked.toogleAttacked();
		    				getTarget().setTargeted(null);
		    				setTarget(null);
		    			}
		    		}
		    		// didn't have a target before
		    		else
		    		{
		    			attacked.toogleAttacked();
		    			setTarget(attacked);
		    		}
			    			
	    		}
		    	else
		    	{
		    		Item temp = land.tileAt(target).getItem();
		    		if (temp!=null)
		    		{
		    			if(temp.isUsable())
		    			{
			    			if (target.near(getPos(),1))
			    			{
				    			temp.use();
				    			if (temp.getType() == Item.CONTAINER)
				    			{
				    				Container cont = (Container) temp;
				    				contH.add(cont);
				    			}
			    			}
				    			else
				    			{
				        			TileGraph graph = land.getGraph(getPos(), target);
				        			try
				        			{
				        				setPath(graph.findShortestPath().removeLast());
				        			}
				        			catch (NullPointerException e)
				        			{
				        				setPath(null);
				        			}
				        			tryUse = temp;
				    			}
			    		}
		    		}
		    	}
	    	}
	    }
	}
	
	
	// return Position of tile on screen or null if off screen
	private Position proccess_mouse(int absoluteMouseX, int absoluteMouseY) 
	{
		// This gets the relative location of Y/X for the click.
		int reletive_x = (absoluteMouseX - Map.X_OFFSET) / Map.SIZE_OF_TILE;
		int reletive_y = (absoluteMouseY - Map.Y_OFFSET) / Map.SIZE_OF_TILE;
		
		//System.out.println(reletive_x + " " + reletive_y);
		
		if ( Map.X_OFFSET<=absoluteMouseX && reletive_x<=14 && Map.Y_OFFSET<=absoluteMouseY && reletive_y<=10 )
		{
			
			// This gets the actual position of the click
			int actual_x = getPos().getX() - PLAYER_X_CENTER + reletive_x;
			int actual_y = getPos().getY() - PLAYER_Y_CENTER + reletive_y;
			
			Position click = new Position(actual_x, actual_y);
			return click;
		}
		
		return null;
		
	}

	public HUD getHud() 
	{
		return this.hud;
	}

	public void update(Map land, int delta) 
	{	    
		setInputDelta(getInputDelta()+delta);
		setAttackDelta(getAttackDelta() + delta);
		
		if ((getInputDelta() > getSpeed()))
		{
			move(land);
			setInputDelta(0);
		}
    	
    	if ((getTarget()!=null) && getAttackDelta() > getAttackSpeed())
    	{
	    		attack();
	    		setAttackDelta(0);
    	}
    	
    	getHud().update();
		
    	if (getPos().equals(getOldPos()))
    	{
    		setMoving(no_move);
    	}
    	

		if (tryUse!=null && tryUse.getPos().near(getPos(), 1))
		{
	
			tryUse.use();
			if (tryUse instanceof Container)
				contH.add((Container)tryUse);
			tryUse = null;
		}
  
    	
   	 	contH.update(this);
	}
	
	// Thie mehod gets the item that the player holds the mouse over
    public Item getItemFromWorld(int mouse_x, int mouse_y, Map land)
    {
    	Item item = null;

    	// if mouse on world, get item from world;
    	if (isOnWorld(mouse_x, mouse_y))
    	{
    		Position pos = getPos(mouse_x, mouse_y);
    		//System.out.println(pos.toString());
    		item = land.tileAt(pos).getItem();
    		//System.out.println(item);
    	}
    	return item;
    }
    

    
	private boolean isOnWorld(int mouse_x, int mouse_y) 
	{
		if ((mouse_x >= Map.X_OFFSET) && (mouse_x <= Map.X_LEN_TOTAL))
			if ((mouse_y >= Map.Y_OFFSET) && (mouse_y <= Map.Y_LEN_TOTAL))
				return true;
		
		return false;
	}
	
    private Position getPos(int mouse_x, int mouse_y) 
    {
    	// formula for absolute world position from relative map click:
    	// absolute click x = (player x) - (map.center x) + (relative click x) 
    	int x = getPos().getX() - 7 + mouse_x/Map.SIZE_OF_TILE;
    	int y = getPos().getY() - 5 + mouse_y/Map.SIZE_OF_TILE;
		return new Position(x,y);
	}



	// This method puts the item where the player released the item
    public void putItemToWorld(int mouse_x, int mouse_y, Map land)
    {
    	// if mouse on world, put item to that tile world;
    	if (isOnWorld(mouse_x, mouse_y))
    	{
    		Position pos = getPos(mouse_x, mouse_y);
    		//System.out.println(pos.toString());
    		land.tileAt(pos).setItem(held);
    		if (held.getPos() != null)
    			land.tileAt(held.getPos()).removeItem(held);
    		held.setPos(pos);
    		held = null;
    	}
    }

    //private boolean moveFromInv = false;
    boolean w2w_invalid_choice = false;
	public void check_item_worldToWorld(Map land) 
	{
    	Input input = gc.getInput();
		
    	// if player held an item from the world but moved away from the item, forget about that item being held
    	if (held!=null && held.getPos() != null)
    	{
    		if (!(getPos().near(held.getPos(), 1)))
    			held = null;
    	}
    	
    	if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
		{
    		w2w_invalid_choice = false;
		}
    	
    	// identify mouse press + ctrl and get the item held
    	if (((input.isKeyDown(Input.KEY_LCONTROL)||input.isKeyDown(Input.KEY_RCONTROL))&&input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && held==null)&&!w2w_invalid_choice)
    	{
    		held = getItemFromWorld(input.getAbsoluteMouseX(), input.getAbsoluteMouseY(), land);
    		if (held!=null)
    		{
	    		if (!held.isMoveItem())
	    		{
	    			held = null;
	    		}
    		}
    		else
    		{
    			w2w_invalid_choice = true;
    		}
    	}
    
    	// identify mouse release and put the item held to the right place
    	else if (held!=null && !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
    	{
    		putItemToWorld(input.getAbsoluteMouseX(), input.getAbsoluteMouseY(), land);
    		held = null;
    	}
		
	}
	
	private Item held_from_inv = null;
	private boolean inv_invalid_choice = false;
	private int fromInvWhere = -1;  // 1 - neck, 2 - helm, 3 - bp, 4 - weapon, 5 - chest, 6 - offhand , 7 - ring, 8 - pants , 9 - misc, 10 - boots 
	public void check_item_InvToWorld(Map land, ItemHandler ih)
	{
		Input input = gc.getInput();
		
		if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !isOnWorld(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))
		{
			held_from_inv = null;
		}
			
		if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
		{
			inv_invalid_choice = false;
		}
		
		if ((input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && held_from_inv==null && isOnInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))&&!inv_invalid_choice)
    	{
    		//System.out.println("ON INV");
    		held_from_inv = getItemFromInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
    		if (held_from_inv!=null)
    			System.out.println(held_from_inv.getName());
    		else
    			inv_invalid_choice = true;
    	}

		if (held_from_inv!=null && !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && isOnWorld(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))
		{
			Position pos = getPos(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
    		//System.out.println(pos.toString());
    		land.tileAt(pos).setItem(held_from_inv);
    		ih.add_item(held_from_inv);
    		held_from_inv.setPos(pos);
    		switch (fromInvWhere)
    		{
    		case 1:
    			getInv().setNeck(null);
    			break;
    		case 2:
    			getInv().setHelmet(null);
    			break;
    		case 3:
    			getInv().setContainer(null);
    			break;
    		case 4:
    			getInv().setWeapon(null);
    			break;
    		case 5:
    			getInv().setChest(null);
    			break;
    		case 6:
    			getInv().setOff_hand(null);
    			break;
    		case 7:
    			getInv().setRing(null);
    			break;
    		case 8:
    			getInv().setPants(null);
    			break;
    		case 9:
    			getInv().setMisc(null);
    			break;
    		case 10:
    			getInv().setBoots(null);
    			break;
    		}
    		
    		held_from_inv = null;
		}
	}
	
	private boolean isOnInv(int mouse_x, int mouse_y) 
	{
		//System.out.println("LIMIT: " + (HUD.BODY_HEIGHT + HUD.BARS_HEIGHT + 100) );
		if ((mouse_x >= HUD.X_OFFSET))
			if ((mouse_y >= HUD.BARS_HEIGHT) && (mouse_y <= 300))
				return true;
		
		return false;
	}
	
	private Item getItemFromInv(int mouse_x, int mouse_y)
	{
		int firstRow_x = 20+HUD.X_OFFSET;
		int secondRow_x = 90 + HUD.X_OFFSET;
		int thirdRow_x = 153 + HUD.X_OFFSET;
		
		int y_offset = 75;
		//System.out.println(mouse_x + " " + mouse_y);

		if (mouse_x >= firstRow_x && mouse_x <= firstRow_x + 60)
		{
			// NECK
			if (mouse_y >= y_offset + 10 && mouse_y <= y_offset + 67)
			{
				fromInvWhere = 1;
				return getInv().getNeck();
			}
			// WEAPON
			else if (mouse_y >= y_offset + 70 && mouse_y <= y_offset + 127)
			{
				fromInvWhere = 4;
				return getInv().getWeapon();
			}
			// RING
			else if (mouse_y >= y_offset + 130 && mouse_y <= y_offset + 190)
			{
				fromInvWhere = 7;
				return getInv().getRing();
			}
		}
		else if (mouse_x >= secondRow_x && mouse_x <= secondRow_x+60)
		{

			// HELM
			if (mouse_y >= y_offset  && mouse_y <= y_offset + 55)
			{
				fromInvWhere = 2;
				return getInv().getHelmet();
			}
			// ARMOR
			else if (mouse_y >= y_offset + 56 && mouse_y <= y_offset + 111)
			{
				fromInvWhere = 5;
				return getInv().getChest();
			}
			// PANTS
			else if (mouse_y >= y_offset + 112 && mouse_y <= y_offset + 167)
			{
				fromInvWhere = 8;
				return getInv().getPants();
			}
			// BOOTS				228								272
			else if (mouse_y >= y_offset + 168 && mouse_y <= y_offset + 220)
			{
				fromInvWhere = 10;
				return getInv().getBoots();
			}
		}
		else if (mouse_x >= thirdRow_x && mouse_x <= thirdRow_x+60)
		{
			// BACKPACK
			if (mouse_y >= y_offset + 10 && mouse_y <= y_offset + 67)
			{
				fromInvWhere = 3;
				return getInv().getContainer();
			}
			// OFFHAND
			else if (mouse_y >= y_offset + 70 && mouse_y <= y_offset + 127)
			{
				fromInvWhere = 6;
				return getInv().getOff_hand();
			}
			// MISC
			else if (mouse_y >= y_offset + 130 && mouse_y <= y_offset + 190)
			{
				fromInvWhere = 9;
				return getInv().getMisc();
			}
		}
		fromInvWhere = -1;
		return null;
	}
	
	
	private Item held_from_cont = null;
	private int item_slot = -1;
	private int cont_num = -1;
	boolean invalid_choice = false;
	
	public void check_item_ContToWorld(Map land, ItemHandler ih) 
	{
		if (held_from_inv == null)
		{
			Input input = gc.getInput();
			
			//System.out.println(isOnCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()));
			
			if ((!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !isOnWorld(input.getAbsoluteMouseX(), input.getAbsoluteMouseY())))
			{
				held_from_cont = null;
				
			}
				
			if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
			{
				invalid_choice = false;
			}
			
			if ((input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && held_from_cont==null && isOnCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))&&(!invalid_choice))
	    	{
	    		//System.out.println("ON CONT");
				held_from_cont = getItemFromCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
	    		if (held_from_cont!=null)
	    			System.out.println(held_from_cont.getName());
	    		else
	    			invalid_choice = true;
	    	}
			
			//System.out.println((held_from_cont!=null) + " " + (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) + " " + (isOnWorld(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))));
			if (held_from_cont!=null && !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && isOnWorld(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))
			{
				//System.out.println("DONE!");
				Position pos = getPos(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
	    		land.tileAt(pos).setItem(held_from_cont);
	    		ih.add_item(held_from_cont);
	    		held_from_cont.setPos(pos);
	    		contH.removeItem(cont_num, item_slot);
	    		
	    		held_from_cont = null;
			}
		}
	}

	private boolean isOnCont(int mouse_x, int mouse_y) 
	{
		//System.out.println("LIMIT: " + (HUD.BODY_HEIGHT + HUD.BARS_HEIGHT + 100) );
		if ((mouse_x >= HUD.X_OFFSET))
			if ((mouse_y > 290))
				return true;
		
		return false;
		
	}
	
	private Item getItemFromCont(int mouse_x, int mouse_y)
	{
		//System.out.println(mouse_x + "  "+ mouse_y);
		cont_num = (mouse_y - Container.Y_OFFSET)/Container.CONT_HEIGHT;
		
		//System.out.println( (Container.X_OFFSET + 5) + " " + (Container.X_OFFSET + 52));
		if ((mouse_y >= Container.Y_OFFSET + (cont_num * Container.CONT_HEIGHT)) && (mouse_y <= Container.Y_OFFSET + (cont_num * Container.CONT_HEIGHT) +55))
		{
			if ((mouse_x >= Container.X_OFFSET + 5)&&(mouse_x <= Container.X_OFFSET + 56))
				item_slot = 0;
			else if ((mouse_x >= Container.X_OFFSET + 57)&&(mouse_x <= Container.X_OFFSET + 108))
				item_slot = 1;
			else if ((mouse_x >= Container.X_OFFSET + 109)&&(mouse_x <= Container.X_OFFSET + 160))
				item_slot = 2;
			else if ((mouse_x >= Container.X_OFFSET + 161)&&(mouse_x <= Container.X_OFFSET + 212))
				item_slot = 3;
		}
		else if ((mouse_y >= Container.Y_OFFSET + (cont_num * Container.CONT_HEIGHT) + 55) && (mouse_y <= Container.Y_OFFSET + (cont_num * Container.CONT_HEIGHT) +110))
		{
			if ((mouse_x >= Container.X_OFFSET + 5)&&(mouse_x <= Container.X_OFFSET + 56))
				item_slot = 4;
			else if ((mouse_x >= Container.X_OFFSET + 57)&&(mouse_x <= Container.X_OFFSET + 108))
				item_slot = 5;
			else if ((mouse_x >= Container.X_OFFSET + 109)&&(mouse_x <= Container.X_OFFSET + 160))
				item_slot = 6;
			else if ((mouse_x >= Container.X_OFFSET + 161)&&(mouse_x <= Container.X_OFFSET + 212))
				item_slot = 7;
		}

		System.out.println( cont_num + " " + item_slot);
		Item item = contH.getItem(cont_num, item_slot);
		return item;
	}
	
	// SETTERS AND GETTERS
    
	public Animation getAnim() {
		return anim;
	}

	public void setAnim(Animation anim) {
		this.anim = anim;
	}

	public Inv getInv() {
		return inv;
	}

	public void setInv(Inv inv) {
		this.inv = inv;
	}
	
	
	public Item getTryUse() {
		return tryUse;
	}

	public void setTryUse(Item tryUse) {
		this.tryUse = tryUse;
	}

	Item held_from_world_to_inv;
	Item swap_to_world;
	boolean w2i_invalid_choice = false;
	public void check_item_WorldToInv(Map land, ItemHandler ih) 
	{
		Input input = gc.getInput();
		
		if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !isOnInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))
		{
			held_from_world_to_inv = null;
		}
			
		if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
		{
			w2i_invalid_choice = false;
		}
		
		if ((input.isKeyDown(Input.KEY_LCONTROL)||input.isKeyDown(Input.KEY_RCONTROL))&&(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && held_from_world_to_inv==null && isOnWorld(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))&&!w2i_invalid_choice)
    	{
    		//System.out.println("ON WORLD TO INV!");
			held_from_world_to_inv = land.tileAt(getPos(input.getAbsoluteMouseX(), input.getAbsoluteMouseY())).getItem();
    		if (held_from_world_to_inv!=null)
    			System.out.println(held_from_world_to_inv.getName());
    		else
    			w2i_invalid_choice = true;
    	}
		//System.out.println(held_from_world_to_inv);
		//System.out.println((held_from_world_to_inv!=null) + " " + (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ) + " " + (isOnInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY())));
		if (held_from_world_to_inv!=null && !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && isOnInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))
		{
			if (held_from_world_to_inv.getPos().near(getPos(), 1))
			{
				swap_to_world = getItemFromInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
	    		//System.out.println(pos.toString());
	    		boolean stop = false;
	    		
	    		switch (fromInvWhere)
	    		{
	    		case -1:
	    			stop = true;
	    			break;
	    		case 1:
	    			if (held_from_world_to_inv.getType() == Item.NECK)
	    				getInv().setNeck(held_from_world_to_inv);
	    			else
	    				stop = true;
	    			break;
	    		case 2:
	    			if (held_from_world_to_inv.getType() == Item.HELM)
	    				getInv().setHelmet(held_from_world_to_inv);
	    			else
	    				stop = true;
	    			break;
	    		case 3:
	    			if (held_from_world_to_inv.getType() == Item.CONTAINER)
	    				getInv().setContainer((Container)held_from_world_to_inv);
	    			else
	    				stop = true;
	    			break;
	    		case 4:
	    			if (held_from_world_to_inv.getType() == Item.WEAPON)
	    				getInv().setWeapon(held_from_world_to_inv);
	    			else
	    				stop = true;
	    			break;
	    		case 5:
	    			if (held_from_world_to_inv.getType() == Item.CHEST)
	    				getInv().setChest(held_from_world_to_inv);
	    			else
	    				stop = true;
	    			break;
	    		case 6:
	    			if (held_from_world_to_inv.getType() == Item.OFF_HAND)
	    				getInv().setOff_hand(held_from_world_to_inv);
	    			else
	    				stop = true;
	    			break;
	    		case 7:
	    			if (held_from_world_to_inv.getType() == Item.RING)
	    				getInv().setRing(held_from_world_to_inv);
	    			else
	    				stop = true;
	    			break;
	    		case 8:
	    			if (held_from_world_to_inv.getType() == Item.PANTS)
	    				getInv().setPants(held_from_world_to_inv);
	    			else
	    				stop = true;
	    			break;
	    		case 9:
	    				getInv().setMisc(held_from_world_to_inv);
	    			break;
	    		case 10:
	    			if (held_from_world_to_inv.getType() == Item.BOOTS)
	    				getInv().setBoots(held_from_world_to_inv);
	    			else
	    				stop = true;
	    			break;
	    		}
	    		
	    		if (!stop)
	    		{
		    		land.tileAt(held_from_world_to_inv.getPos()).setItem(swap_to_world);
		    		land.tileAt(held_from_world_to_inv.getPos()).removeItem(held_from_world_to_inv);
		    		Position oldPosSwap = held_from_world_to_inv.getPos();
		    		
		    		held_from_world_to_inv.setPos(getPos());
		    		
		    		if (swap_to_world!= null)
		    		{
		    			swap_to_world.setPos(oldPosSwap);
		    			ih.add_item(swap_to_world);
		    		}
					ih.remove_item(held_from_world_to_inv);
	    		}
			}

			held_from_world_to_inv = null;
		}
	}

	private Item held_from_world_to_cont = null;
	boolean w2c_invalid_choice = false;
	
	public void check_item_WorldToCont(Map land, ItemHandler ih)
	{
		Input input = gc.getInput();
		
		//System.out.println(isOnCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()));
		
		if ((!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !isOnCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY())))
		{
			held_from_world_to_cont = null;
			
		}
			
		if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
		{
			w2c_invalid_choice = false;
		}
		
		if ((input.isKeyDown(Input.KEY_LCONTROL)||input.isKeyDown(Input.KEY_RCONTROL))&&(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && held_from_world_to_cont==null && isOnWorld(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))&&(!w2c_invalid_choice))
    	{
    		//System.out.println("ON CONT");
			held_from_world_to_cont = land.tileAt(getPos(input.getAbsoluteMouseX(), input.getAbsoluteMouseY())).getItem();
    		if (held_from_world_to_cont!=null)
    			System.out.println(held_from_world_to_cont.getName());
    		else
    			w2c_invalid_choice = true;
    	}
		
		//System.out.println((held_from_world_to_cont!=null) + " " + (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) + " " + (isOnCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))));
		if (held_from_world_to_cont!=null && !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && isOnCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))
		{
			getItemFromCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
			Container temp = contH.getContainer(cont_num);
			
			if (held_from_world_to_cont.getPos().near(getPos(), 1))
			{
				//System.out.println(cont_num);
				//System.out.println(temp);
				if (temp !=null )
				{
					if (!temp.isFull())
					{
						boolean check = temp.add_to_container(held_from_world_to_cont);
						if (check)
						{
							land.tileAt(held_from_world_to_cont.getPos()).removeItem(held_from_world_to_cont);
							ih.remove_item(held_from_world_to_cont);
						}
						
						
					}
				}
			}
			
			held_from_world_to_cont = null;
		}
	}
	
	private Item held_from_inv_to_cont = null;
	boolean i2c_invalid_choice = false;
	
	public void check_item_InvToCont() 
	{
		Input input = gc.getInput();
		
		//System.out.println(isOnCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()));
		
		if ((!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !isOnCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY())))
		{
			held_from_inv_to_cont = null;
			
		}
			
		if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
		{
			i2c_invalid_choice = false;
		}
		
		if ((input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && held_from_inv_to_cont==null && isOnInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))&&(!i2c_invalid_choice))
    	{
    		//System.out.println("ON CONT");
			held_from_inv_to_cont = getItemFromInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
    		if (held_from_inv_to_cont!=null)
    			System.out.println(held_from_inv_to_cont.getName());
    		else
    			i2c_invalid_choice = true;
    	}
		
		//System.out.println((held_from_cont!=null) + " " + (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) + " " + (isOnWorld(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))));
		if (held_from_inv_to_cont!=null && !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && isOnCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))
		{
			getItemFromCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
			Container temp = contH.getContainer(cont_num);
			//System.out.println(cont_num);
			//System.out.println(temp);
			if (temp !=null )
			{
				if (!temp.isFull())
				{
					boolean check = temp.add_to_container(held_from_inv_to_cont);
					if (check)
					{
			    		switch (fromInvWhere)
			    		{
			    		case 1:
			    			getInv().setNeck(null);
			    			break;
			    		case 2:
			    			getInv().setHelmet(null);
			    			break;
			    		case 3:
			    			getInv().setContainer(null);
			    			break;
			    		case 4:
			    			getInv().setWeapon(null);
			    			break;
			    		case 5:
			    			getInv().setChest(null);
			    			break;
			    		case 6:
			    			getInv().setOff_hand(null);
			    			break;
			    		case 7:
			    			getInv().setRing(null);
			    			break;
			    		case 8:
			    			getInv().setPants(null);
			    			break;
			    		case 9:
			    			getInv().setMisc(null);
			    			break;
			    		case 10:
			    			getInv().setBoots(null);
			    			break;
			    		}
					}
				}
			}
			
			held_from_inv_to_cont = null;
		}
	}

	private Item held_from_cnt_to_inv = null;
	boolean c2i_invalid_choice = false;
	Item swap_to_cont;
	public void check_item_ContToInt() 
	{
		Input input = gc.getInput();
		
		if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !isOnInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))
		{
			held_from_cnt_to_inv = null;
		}
			
		if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
		{
			c2i_invalid_choice = false;
		}
		
		if ((input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && held_from_cnt_to_inv==null && isOnCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))&&!c2i_invalid_choice)
    	{
    		System.out.println("OMG ON WORLD TO INV!");
			held_from_cnt_to_inv = getItemFromCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
    		if (held_from_cnt_to_inv!=null)
    			System.out.println(held_from_cnt_to_inv.getName());
    		else
    			c2i_invalid_choice = true;
    	}
		//System.out.println(held_from_cnt_to_inv);
		//System.out.println((held_from_cnt_to_inv!=null) + " " + (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ) + " " + (isOnInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY())));
		if (held_from_cnt_to_inv!=null && !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && isOnInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))
		{

			swap_to_cont = getItemFromInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
    		//System.out.println(pos.toString());
    		boolean stop = false;
    		
    		Item oldItem = null;
    		switch (fromInvWhere)
    		{
	    		case -1:
	    			stop = true;
	    			break;
	    		case 1:
	    			if (held_from_cnt_to_inv.getType() == Item.NECK)
	    			{
	    				oldItem = getInv().getNeck();
	    				getInv().setNeck(held_from_cnt_to_inv);
	    			}
	    			else
	    				stop = true;
	    			break;
	    		case 2:
	    			if (held_from_cnt_to_inv.getType() == Item.HELM)
	    			{
	    				oldItem = getInv().getHelmet();
	    				getInv().setHelmet(held_from_cnt_to_inv);
	    			}
	    			
	    			else
	    				stop = true;
	    			break;
	    		case 3:
	    			if (held_from_cnt_to_inv.getType() == Item.CONTAINER)
	    			{
	    				oldItem = null;
	    				stop = !getInv().getContainer().add_to_container(held_from_cnt_to_inv);
	    			}
	    			else
	    				stop = true;
	    			break;
	    		case 4:
	    			if (held_from_cnt_to_inv.getType() == Item.WEAPON)
	    			{
	    				oldItem = getInv().getWeapon();
	    				getInv().setWeapon(held_from_cnt_to_inv);
	    			}
	    			else
	    				stop = true;
	    			break;
	    		case 5:
	    			if (held_from_cnt_to_inv.getType() == Item.CHEST)
	    			{
	    				oldItem = getInv().getChest();
	    				getInv().setChest(held_from_cnt_to_inv);
	    			}
	    	
	    			else
	    				stop = true;
	    			break;
	    		case 6:	    			
	    			if (held_from_cnt_to_inv.getType() == Item.OFF_HAND)
	    			{
	    				oldItem = getInv().getOff_hand();
	    				getInv().setOff_hand(held_from_cnt_to_inv);
	    			}
	    			
	    			else
	    				stop = true;
	    			break;
	    		case 7:	    			
	    			if (held_from_cnt_to_inv.getType() == Item.RING)
	    			{
	    				oldItem = getInv().getRing();
	    				getInv().setRing(held_from_cnt_to_inv);
	    			}
	    			
	    			else
	    				stop = true;
	    			break;
	    		case 8:
	    			if (held_from_cnt_to_inv.getType() == Item.PANTS)
	    			{
	    				oldItem = getInv().getPants();
	    				getInv().setPants(held_from_cnt_to_inv);
	    			}

	    			else
	    				stop = true;
	    			break;
	    		case 9:
    				oldItem = getInv().getMisc();
    				if (oldItem.getType() != Item.CONTAINER)
    					getInv().setMisc(held_from_cnt_to_inv);
    				else
    				{
    					oldItem = null;
	    				stop = !((Container)getInv().getMisc()).add_to_container(held_from_cnt_to_inv);
    				}
	    			break;
	    		case 10:
	    			if (held_from_cnt_to_inv.getType() == Item.BOOTS)
	    			{
	    				oldItem = getInv().getBoots();
	    				getInv().setBoots(held_from_cnt_to_inv);
	    			}
	    			
	    			else
	    				stop = true;
	    			break;
    		}
    		
    		if (!stop)
    		{	
    			//getItemFromCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
    			contH.removeItem(cont_num, item_slot);
    			
	    		if (oldItem!=null)
	    		{
	    			stop = contH.getContainer(cont_num).add_to_container(oldItem);	
	    		}
    		}
			
    		held_from_cnt_to_inv = null;
		}
	}

	private Item held_from_cnt_to_cnt = null;
	boolean c2c_invalid_choice = false;
	public void check_item_ContToCont() 
	{
		Input input = gc.getInput();
		
		if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !isOnCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))
		{
			held_from_cnt_to_cnt = null;
		}
			
		if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
		{
			c2c_invalid_choice = false;
		}
		if (held_from_world_to_cont==null)
		{
			if ((input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && held_from_cnt_to_cnt==null && held_from_inv == null && isOnCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))&&!c2c_invalid_choice)
	    	{
	    		held_from_cnt_to_cnt = getItemFromCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
	    		if (held_from_cnt_to_cnt!=null)
	    			System.out.println(held_from_cnt_to_cnt.getName());
	    		else
	    			c2c_invalid_choice = true;
	    	}
			
			//System.out.println((held_from_cont!=null) + " " + (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) + " " + (isOnWorld(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))));
			if (held_from_cnt_to_cnt!=null && !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && isOnCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))
			{
				System.out.println("OMG HAPPEN");
				Container from = contH.getContainer(cont_num);
				int oldNumSlot = item_slot;
				getItemFromCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
				Container to = contH.getContainer(cont_num);
				//System.out.println(cont_num);
				//System.out.println(temp);
				if (to !=null )
				{
					if (!to.isFull())
					{
						boolean check = to.add_to_container(held_from_cnt_to_cnt);
						if (check)
						{
							from.remove_from_container(oldNumSlot);
						}
					}
				}
				
				held_from_cnt_to_cnt = null;
			}
		}
    }
	
	private Item held_from_inv_to_inv = null;
	boolean i2i_invalid_choice = false;
	int whereFromInvOld = -1;
	public void check_item_InvToInv() 
	{
		Input input = gc.getInput();
		
		//System.out.println(isOnCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()));
		
		if ((!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !isOnInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY())))
		{
			held_from_inv_to_inv = null;
			
		}
			
		if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
		{
			i2i_invalid_choice = false;
		}
		
		if ((input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && held_from_inv_to_inv==null && isOnInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))&&(!i2i_invalid_choice))
    	{
    		//System.out.println("ON CONT");
			held_from_inv_to_inv = getItemFromInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
			whereFromInvOld = fromInvWhere;
    		if (held_from_inv_to_inv!=null)
    			System.out.println(held_from_inv_to_inv.getName());
    		else
    			i2i_invalid_choice = true;
    	}
		
		//System.out.println((held_from_inv_to_inv!=null) + " " + (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ) + " " + (isOnInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY())));
		if (held_from_inv_to_inv!=null && !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && isOnInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))
		{
			System.out.println("HEE");
			swap_to_cont = getItemFromInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
    		//System.out.println(pos.toString());

    		Item oldItem = null;
    		switch (fromInvWhere)
    		{
	    		case 1:
	    			if (held_from_inv_to_inv.getType() == Item.NECK)
	    			{
	    				oldItem = getInv().getNeck();
	    				if (oldItem == null)
	    				{
	    					getInv().setNeck(held_from_inv_to_inv);
	    					removeFromInv(whereFromInvOld);
	    				}
	    			}

	    			break;
	    		case 2:
	    			if (held_from_inv_to_inv.getType() == Item.HELM)
	    			{
	    				oldItem = getInv().getHelmet();
	    				if (oldItem == null)
	    				{
	    					getInv().setHelmet(held_from_inv_to_inv);
	    					removeFromInv(whereFromInvOld);
	    				}

	    			}
	    			
	    			break;
	    		case 3:
	    			if (held_from_inv_to_inv.getType() == Item.CONTAINER)
	    			{
	    				oldItem = getInv().getContainer();
	    				if (oldItem == null)
	    				{
	    					getInv().setContainer((Container)held_from_inv_to_inv);
	    					removeFromInv(whereFromInvOld);
	    				}
	    			}

	    			break;
	    		case 4:
	    			if (held_from_inv_to_inv.getType() == Item.WEAPON)
	    			{
	    				oldItem = getInv().getWeapon();
    					if (oldItem == null)
    					{
    						getInv().setWeapon(held_from_inv_to_inv);
	    					removeFromInv(whereFromInvOld);
	    				}
	    			}
	    			
	    			break;
	    		case 5:
	    			if (held_from_inv_to_inv.getType() == Item.CHEST)
	    			{
	    				oldItem = getInv().getChest();
	    				if (oldItem == null)
	    				{
	    					getInv().setChest(held_from_inv_to_inv);
	    					removeFromInv(whereFromInvOld);
	    				}
	    			}

	    			break;
	    		case 6:	    			
	    			if (held_from_inv_to_inv.getType() == Item.OFF_HAND)
	    			{
	    				oldItem = getInv().getOff_hand();
	    				if (oldItem == null)
	    				{
	    					getInv().setOff_hand(held_from_inv_to_inv);
	    					removeFromInv(whereFromInvOld);
	    				}
	    			}

	    			break;
	    		case 7:	    			
	    			if (held_from_inv_to_inv.getType() == Item.RING)
	    			{
	    				oldItem = getInv().getRing();
	    				if (oldItem == null)
	    				{
	    					getInv().setRing(held_from_inv_to_inv);
	    					removeFromInv(whereFromInvOld);
	    				}
	    			}
	    		
	    			break;
	    			
	    		case 8:
	    			if (held_from_inv_to_inv.getType() == Item.PANTS)
	    			{
	    				oldItem = getInv().getPants();
	    				if (oldItem == null)
	    				{
	    					getInv().setPants(held_from_inv_to_inv);
	    					removeFromInv(whereFromInvOld);
	    				}
	    			}

	    			break;
	    		case 9:
    				oldItem = getInv().getMisc();
    				if (oldItem == null)
    				{
    					getInv().setMisc(held_from_inv_to_inv);
    					removeFromInv(whereFromInvOld);
    				}
    				
	    			break;
	    		case 10:
	    			if (held_from_inv_to_inv.getType() == Item.BOOTS)
	    			{
	    				oldItem = getInv().getBoots();
	    				if (oldItem == null)
	    				{
	    					getInv().setBoots(held_from_inv_to_inv);
	    					removeFromInv(whereFromInvOld);
	    				}
	    			}
	    			break;
	    			
    		}
			
    		held_from_inv_to_inv = null;
		}
	}
	
	private void removeFromInv(int whereFromInvOld2) 
	{
		switch(whereFromInvOld2)
		{
			case 1:
				getInv().setNeck(null);
				break;
			case 2:
				getInv().setHelmet(null);
				break;
			case 3:
				getInv().setContainer(null);
				break;
			case 4:
				getInv().setWeapon(null);
				break;
			case 5:
				getInv().setChest(null);
				break;
			case 6:
				getInv().setOff_hand(null);
				break;
			case 7:
				getInv().setRing(null);
				break;
			case 8:
				getInv().setPants(null);
				break;
			case 9:
				getInv().setMisc(null);
				break;
			case 10:
				getInv().setBoots(null);
				break;
		}
	}

	// This draws the dragged item next to the mouse, centered.
	public void draw_drag_item()
	{
		Item dragged = actual_held;
		if (dragged != null)
		{
			Input input = gc.getInput();
		
			if (getPos().near(dragged.getPos(), 1))
				dragged.getPic().draw(input.getAbsoluteMouseX()-25, input.getAbsoluteMouseY()-25);
		}
	}	

	private Item actual_held = null;		
	Position source_world_pos = null;
	int source_inv = -1;
	int source_cont = -1;
	int source_item_slot = -1;
	
	public void check_itemMove(Map world, ItemHandler ih)
	{
		Input input = gc.getInput();
		boolean mouseDown = input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
		boolean ctrlDown = input.isKeyDown(Input.KEY_LCONTROL) || input.isKeyDown(Input.KEY_RCONTROL);
		// when mouse down, get the item from one of two places - inv or cont.
		// when mouse down + ctrl, try get item from world.
	

		
		// Get the actual_held and remember where you got it.
		if ((actual_held == null)&&(mouseDown))
		{
			// for the world item ctrl has to be down
			if (isOnWorld(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()) && ctrlDown)
			{
				actual_held = getItemFromWorld(input.getAbsoluteMouseX(), input.getAbsoluteMouseY(), world);
				if (actual_held != null)
					source_world_pos = actual_held.getPos();
			}
			else if (isOnInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))
			{
				actual_held = getItemFromInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
				if (actual_held != null)
					source_inv = fromInvWhere;	
			}
			else if (isOnCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))
			{
				actual_held = getItemFromCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
				if (actual_held != null)
				{
					source_cont = cont_num;
					source_item_slot = item_slot;
				}
			}
		}
		
		// when mouse off and item actual_held is not null, check to see where. Try move item there.
		if ((actual_held != null)&&(!mouseDown))
		{
			// if going to world
			if (isOnWorld(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))
			{
				going_to_world(world, ih);
				
				// moved, reset all
				source_world_pos = null;
				source_inv = -1;
				source_cont = -1;
				source_item_slot = -1;
				actual_held = null;
			}
			
			// if going to inv
			else if (isOnInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))
			{
				going_to_inv(world,ih);
				
				// moved, reset all
				source_world_pos = null;
				source_inv = -1;
				source_cont = -1;
				source_item_slot = -1;
				actual_held = null;
			}
			// if going to cont
			else if (isOnCont(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()))
			{
				going_to_cnt(world, ih);
				// moved, reset all
				source_world_pos = null;
				source_inv = -1;
				source_cont = -1;
				source_item_slot = -1;
				actual_held = null;
			}
		}
	}																							
			
	
	private void going_to_cnt(Map world, ItemHandler ih) 
	{
		// TODO Auto-generated method stub
		
		// if was from world
		if (source_world_pos != null)
		{
			
		}
		// if was from inv
		else if (source_inv != -1)
		{
			
		}
		// if was from cont
		else if ((source_cont!=-1)&& (source_item_slot!=-1))
		{
			
		}
	}

	private void going_to_world(Map world, ItemHandler ih)
	{
		Input input = gc.getInput();
		
		// world to world
		if (source_world_pos != null)
		{
			Position pos = getPos(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
    		
			// set the item at the new position and remove it from the old position in the world
    		boolean check = world.tileAt(pos).setItem(actual_held);
    		if (check)
    		{
    			world.tileAt(actual_held.getPos()).removeItem(actual_held);
    			
    			// update item position
    			actual_held.setPos(pos);
    			actual_held = null;
    		}
		}
		
		// inv to world
		else if (source_inv != -1)
		{
			// get target position
			Position target_pos = getPos(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
			
			// try to add item to world
    		boolean check = world.tileAt(target_pos).setItem(actual_held);
    		
    		// if successful, update item location and ih and remove from inv.
    		if (check)
    		{
	    		ih.add_item(actual_held);
	    		actual_held.setPos(target_pos);
	    		remove_from_inv(source_inv);
    		}

    		actual_held = null;
		}

		// if was from cont
		else if ((source_cont!=-1)&& (source_item_slot!=-1))
		{
			// put item to the world
			Position target_pos = getPos(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
    		world.tileAt(target_pos).setItem(actual_held);
    		ih.add_item(actual_held);
    		actual_held.setPos(target_pos);
    		
    		// remove it from the container
    		contH.removeItem(cont_num, item_slot);
    		
    		actual_held = null;
		}
	}
	
	private void going_to_inv(Map world, ItemHandler ih)
	{
		Input input = gc.getInput();
		
		// world to inv
		if (source_world_pos != null)
		{
			// get item that was in inv and the target_inv_slot
			Item swap_to_world = getItemFromInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
			int target_inv_slot = fromInvWhere;
			
    		boolean stop = try_put_inv(actual_held, target_inv_slot);

    		// if the target item is a container, don't swap it.
    		if (swap_to_world != null)
				if (swap_to_world.getType() == Item.CONTAINER)
				{
					swap_to_world = null;
				}	    
			
    		if (!stop)
    		{
	    		// update item to be on player position	
    			actual_held.setPos(getPos());
    								
				// remove the old item
    			ih.remove_item(actual_held);
				world.tileAt(source_world_pos).removeItem(actual_held);
				
    			// if need to swap items
	    		if (swap_to_world!= null)
	    		{
	    			swap_to_world.setPos(source_world_pos);
	    			ih.add_item(swap_to_world);
	    			world.tileAt(source_world_pos).setItem(swap_to_world);
	    		}
				
				
    		}
    		
    		actual_held = null;
		}

		// inv to inv
		else if (source_inv != -1)
		{
			// get the item at the location we're trying to move actual_held
			Item swap_inv_to_inv = getItemFromInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
			int target_inv = fromInvWhere;

			// try to put the actual held into that inv slot. If it's not possible, stop. If possible, stop = false.
	    	boolean stop = try_put_inv(actual_held, target_inv);
	    	
	    	// if it was possible, check what's up with the other item
	    	if (!stop)
	    	{
	    		// remove actual_held from it's old position
	    		remove_from_inv(source_inv);
	    		
	    		// if there was an item to swap
	    		if (swap_inv_to_inv != null)
	    		{
	    			// swap only if it's not a container
	    			if (swap_inv_to_inv.getType() != Item.CONTAINER)
	    			{
		    			// check if you can swap?

			    		boolean swap_stop = try_put_inv(swap_inv_to_inv, source_inv);

			    		// if you can't, put the item_held back to it's old position.
			    		// Also, put the swap item back to swap place.
			    		if (swap_stop)
			    		{
				    		remove_from_inv(source_inv);
				    		remove_from_inv(target_inv);
				    		
			    			try_put_inv(actual_held, source_inv);
			    			try_put_inv(swap_inv_to_inv, target_inv);
			    		}
	    			}
	    		}
	    	}
			
	    	actual_held = null;
		}
		
		// cont to inv
		else if ((source_cont!=-1)&& (source_item_slot!=-1))
		{
			// get the item at the location we're trying to move actual_held (this will be put in container
			Item swap_inv_to_cnt = getItemFromInv(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
			int target_inv = fromInvWhere;

			// try to put the actual held into that inv slot. If it's not possible, stop. If possible, stop = false.
	    	boolean stop = try_put_inv(actual_held, target_inv);
	    	
	    	// if it was possible, check what's up with the other item
	    	if (!stop)
	    	{
	    		// remove actual_held from it's old container
	    		contH.getContainer(source_cont).remove_from_container(source_item_slot);
	    		
	    		// if there was an item to swap
	    		if (swap_inv_to_cnt != null)
	    		{
	    			// swap only if it's not a container
	    			if (swap_inv_to_cnt.getType() != Item.CONTAINER)
	    			{
	    				contH.getContainer(source_cont).add_to_container(swap_inv_to_cnt);
	    			}
	    		}
	    	}
			
	    	actual_held = null;			
		}
	}

	private void remove_from_inv(int source_inv2) 
	{
		switch (source_inv2)
		{
		case 1:
			getInv().setNeck(null);
			break;
		case 2:
			getInv().setHelmet(null);
			break;
		case 3:
			getInv().setContainer(null);
			break;
		case 4:
			getInv().setWeapon(null);
			break;
		case 5:
			getInv().setChest(null);
			break;
		case 6:
			getInv().setOff_hand(null);
			break;
		case 7:
			getInv().setRing(null);
			break;
		case 8:
			getInv().setPants(null);
			break;
		case 9:
			getInv().setMisc(null);
			break;
		case 10:
			getInv().setBoots(null);
			break;
		}
	}

	// This method tries to put equip the item or put it into the container/misc container. 
	// It will return false if it failed to do so or true if the item "moved" successfully.
	private boolean try_put_inv(Item actual_held, int goingToInv)
	{
		boolean stop = false;
		
		switch (goingToInv)
		{
		case -1:
			stop = true;
			break;
		case 1:
			if (actual_held.getType() == Item.NECK)
				getInv().setNeck(actual_held);
			else
				stop = true;
			break;
		case 2:
			if (actual_held.getType() == Item.HELM)
				getInv().setHelmet(actual_held);
			else
				stop = true;
			break;
		case 3:
			// if there's no container, equip one.
			// if there is, try and put the item in the container
			if (getInv().getContainer() == null)
			{
				if (actual_held.getType() == Item.CONTAINER)
					getInv().setContainer((Container)actual_held);
				else
					stop = true;
			}
			else
			{
				stop = ! getInv().getContainer().add_to_container(actual_held);
			}
			break;
		case 4:
			if (actual_held.getType() == Item.WEAPON)
				getInv().setWeapon(actual_held);
			else
				stop = true;
			break;
		case 5:
			if (actual_held.getType() == Item.CHEST)
				getInv().setChest(actual_held);
			else
				stop = true;
			break;
		case 6:
			if (actual_held.getType() == Item.OFF_HAND)
				getInv().setOff_hand(actual_held);
			else
				stop = true;
			break;
		case 7:
			if (actual_held.getType() == Item.RING)
				getInv().setRing(actual_held);
			else
				stop = true;
			break;
		case 8:
			if (actual_held.getType() == Item.PANTS)
				getInv().setPants(actual_held);
			else
				stop = true;
			break;
		case 9:
			// if misc is empty, put the new item there
			// else if not a container, put new item
			// if it is a container, try put the item in
			if (getInv().getMisc() == null)
				getInv().setMisc(actual_held);
				
			else if (getInv().getMisc().getType() != Item.CONTAINER)
				getInv().setMisc(actual_held);
			
			else
				stop = ! ((Container)getInv().getMisc()).add_to_container(actual_held);
			break;
		case 10:
			if (actual_held.getType() == Item.BOOTS)
				getInv().setBoots(actual_held);
			else
				stop = true;
			break;
		}
		
		return stop;
	}
}
