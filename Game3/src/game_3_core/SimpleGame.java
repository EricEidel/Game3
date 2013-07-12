package game_3_core;
//package slick.path2glory.SimpleGame;
  
import items.Item;

import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import sui.Display;
import GUI.Map;
import GUI.MyChat;
import creatures.Player;
import creatures.Wolf;
  
public class SimpleGame extends BasicGame
{
    static private Display display;
	public static boolean debug = true;
	// input control
	public static Position start_player_pos = new Position(Player.X_PLAYER_START, Player.Y_PLAYER_START);
	
	private Map land = null;
	private Player player = null;
	private Image bg = null;
	
	private creatureHandler ch;
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
    	 ch = new creatureHandler(player);
    	 
    	 wolf = new Wolf(new Position(15, 10), land); 
    	 Wolf wolf2 = new Wolf(new Position(16, 10), land);
    	 ch.add_creature(wolf);
    	 ch.add_creature(wolf2);
    	
    	 //Position pos_item = new Position(15,10);
    	 //sword = new Sword(pos_item);
    	 //ih.add_item(sword); 
    	 chat = new MyChat(player);

    	
    	 
    
         
    	 
    }
    
    @Override
    public void update(GameContainer gc, int delta)  throws SlickException     
    {
    	ch.checkActive();
    	ch.move(land, delta);
    	// check if any of the creatures should go active
    	
    	
    	// player input checks
    	player.check_mouse_click(land);
        player.check_attack_command(land);
        
        // check all GUI item movements and use
        player.check_itemMove(land, ih);
        player.check_item_use(delta);
        
    	player.update(land, delta);
    	ch.checkDead(land, ih);
    	
    	display.update(gc, delta);
    	chat.update();
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
   	 	
   	 	ch.draw(g);	// all the creatures that are next to the player get drawn
		ih.draw(g);
		
   	 	if (player.getMoving() != 5 && !(player.getPos().equals(player.getOldPos())))
   	 		player.drawMoving();
   	 	else
   	 		player.draw();
   	 	
   	 	player.contH.draw();
   	 	player.draw_drag_item();
   	 	
   	 	//chat.render(g, gc);
   	 	display.render(gc, g);
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