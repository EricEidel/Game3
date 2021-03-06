package game_3_core;
//package slick.path2glory.SimpleGame;
  
import items.Item;

import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import sui.Display;
import GUI.Map;
import GUI.MyChat;
import creatures.Player;
import creatures.Rat;
import creatures.Wolf;
  
public class SimpleGame extends BasicGame
{
	int mouse_wheel_moved = 0;
	int mouse_wheel_before = 0;
    static private Display display;
	public static boolean debug = false;
	// input control
	public static Position start_player_pos = new Position(Player.X_PLAYER_START, Player.Y_PLAYER_START);
	
	private Map land = null;
	private Player player = null;
	private Image bg = null;
	
	private CreatureHandler ch;
	private ItemHandler ih;
	
	static public GameContainer gc;
	public static Random rand = new Random();
	static public MyChat chat;
	
	Wolf wolf;
	Item sword;
	 
    public SimpleGame()
    {
        super("OMG new game!!1");
    }
  
    @Override
    public void init(GameContainer gc) throws SlickException 
    {
    	 display = new Display(gc);
    	 SimpleGame.gc = gc;
    	 gc.setTargetFrameRate(60); 
    	 gc.getInput().enableKeyRepeat();
    	 
    	 
    	 // Background
    	 bg = new Image("Pics/Background.jpg");
    	 // create new world
    	 land = new Map("Maps/map2.txt");  
    	 
    	 // create new player
    	 player = new Player(start_player_pos , land);
    	 ih = new ItemHandler("Maps/map2.txt", player);
    	 ch = new CreatureHandler("Maps/map2.txt", player);
    	
    	 //Position pos_item = new Position(15,10);
    	 //sword = new Sword(pos_item);
    	 //ih.add_item(sword); 
    	 chat = new MyChat(player, gc.getGraphics());

    	 // TODO create an "over-ride" file to change quantities of stackables on load and content of containers on load for IH onload.
    	 // TODO Use coin to break , have a popup coming saying wtf, how much to break?
         // TODO when attack command comes, if no creature at that spot, scan nearby squares for cretures that WERE on that spot. 
    	 // TODO Items re-appear fully when scrolling onto the screen. They also disapear right away. Doesn't look that good.
    	 
    }
    
    @Override
    public void update(GameContainer gc, int delta)  throws SlickException     
    {
    	// Check if mouse wheel happened.
    	if (mouse_wheel_before == mouse_wheel_moved)
    		mouse_wheel_moved = 0;
    	mouse_wheel_before = mouse_wheel_moved;
    	
    	// check if any of the creatures should go active
    	ch.checkActive();
    	// Move active creatuers
    	ch.move(land, delta);
    	ch.updateBattleText(delta, display);
    	//player interactions with the world:
  
    	// player input checks
    	player.check_mouse_click(land);
        player.check_attack_command(land);
        
        // check all GUI item movements and use
        player.check_itemMove(land, ih);
        player.check_item_use(delta);
  
        // check if player tried to "see" anything.
        player.check_see(land, delta);
        // update methods for other components
    	player.update(land, delta);
    	ch.checkDead(land, ih, display);
    	
    	display.update(gc, delta);
    	chat.update(delta, mouse_wheel_moved);
    	
    	// update timer for destroying items by some tiles
    	land.updateDestoryItem(gc, delta);
    }

    public void mouseWheelMoved(int moved)
    {
    	mouse_wheel_moved = moved;
    }
    
    public void render(GameContainer gc, Graphics g) throws SlickException 
    {    	 
    	bg.draw();
    	if (player.getMoving() != 5 && !(player.getPos().equals(player.getOldPos())))
    	{
   	 		land.drawMove(player);
   	 		land.drawMoveDestroyItem(player);
    	}
    	else
    	{
    		land.draw(player);
    		land.drawDestroyItem(player);
    	}
    		
   	 	if (debug)
   	 		land.drawCoordinate(player, g);
   	 	
   	 	
   	 	
		ih.draw(g);
		ch.draw(g);
		
   	 	if (player.getMoving() != 5 && !(player.getPos().equals(player.getOldPos())))
   	 		player.drawMoving();
   	 	else
   	 		player.draw();
   	 	
   	 	player.contH.draw();
   	 	
   	 	display.render(gc, g);
   	 	
   	 	player.draw_drag_item();
   	 	
   	 	chat.drawSeeMessage();
   	 	chat.drawSaid();
    }
    

    public static void main(String[] args) throws SlickException
    {
         AppGameContainer app = new AppGameContainer(new SimpleGame());
  
         app.setDisplayMode(1100, 800, false);
         app.start();
    }
    
    static public GameContainer getGC()
    {
    	return gc;
    }
    
	public static Display getDisplay() {
		return display;
	}

	public void setDisplay(Display display) {
		SimpleGame.display = display;
	}
}