package game_3_core;
//package slick.path2glory.SimpleGame;
  
import items.Item;

import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import sui.Display;
import sui.Label;
import sui.Popup;
import GUI.Map;
import GUI.MyChat;
import creatures.Player;
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
    	 ih = new ItemHandler(player);
    	 ch = new CreatureHandler(player);
    	 
    	 wolf = new Wolf(new Position(15, 10), land); 
    	 Wolf wolf2 = new Wolf(new Position(16, 10), land);
    	 ch.add_creature(wolf);
    	 ch.add_creature(wolf2);
    	
    	 //Position pos_item = new Position(15,10);
    	 //sword = new Sword(pos_item);
    	 //ih.add_item(sword); 
    	 chat = new MyChat(player, gc.getGraphics());

    	 // TODO Change background map to work with new tilesets
    	 // TODO Create load creatures and load items from file? <- Need to figure out a good way to do this.
         // TODO Once that's done, look into creating obsticles.
    	 // TODO Use coin to break , have a popup coming saying wtf, how much to break?
         // TODO when attack command comes, if no creature at that spot, scan nearby squares for cretures that WERE on that spot. 
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
   	 		land.drawMove(player);
    	else
    		land.draw(player);
    		
   	 	if (debug)
   	 		land.drawCoordinate(player, g);
   	 	
   	 	land.drawDestroyItem(player);
   	 	
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