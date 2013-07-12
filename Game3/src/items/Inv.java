package items;

import game_3_core.Position;
import game_3_core.SimpleGame;

import org.newdawn.slick.Image;

import GUI.HUD;

public class Inv 
{
	static public int MELEE = 1;
	static public int RANGED = 2;
	
	private int weapon_type;
	
	private Container container;
	private Item helmet;
	private Item neck;
	private Item weapon;
	private Item chest;
	private Item off_hand;
	private Item pants;
	private Item boots;
	private Item ring;
	private Item misc;
	
	private Image blank;

	public Inv()
	{
		
		container = new Dead_wolf(SimpleGame.start_player_pos);
		helmet = new Sword(SimpleGame.start_player_pos);
		neck = new Sword(SimpleGame.start_player_pos);
		weapon = new Sword(SimpleGame.start_player_pos);
		chest = new Dead_wolf(SimpleGame.start_player_pos);
		off_hand = new Sword(SimpleGame.start_player_pos);
		pants = new LeatherPants(SimpleGame.start_player_pos);
		boots = new Dead_wolf(SimpleGame.start_player_pos);
		ring = new Sword(SimpleGame.start_player_pos);
		misc = new GoldCoin(SimpleGame.start_player_pos, 1);
		
		weapon_type = MELEE;
		
		try
		{
			blank = new Image("Pics/paper_doll/Blank.jpg");
		}
		catch (Exception e)
		{
			System.out.println("Could not load image - Blank from container");
		}
	}
	
	public void draw()
	{
		int x=HUD.X_OFFSET;
		int y=HUD.Y_OFFSET + HUD.BARS_HEIGHT;
		
		if (container!=null)
		{
			blank.draw(x+153, y+15);
			container.getPic().draw(x+153, y+15);
		}
		if (helmet!=null)
		{
			blank.draw(x+92, y+3);
			helmet.getPic().draw(x+92, y+3);
		}
		
		if (neck!=null)
		{
			blank.draw(x+25, y+15);
			neck.getPic().draw(x+25, y+15);
		}
		
		if (weapon != null)
		{
			blank.draw(x+25, y+75);
			weapon.getPic().draw(x+25, y+75);
		}
			
		if (chest != null)
		{
			blank.draw(x+92, y+60);
			chest.getPic().draw(x+92, y+60);
		}
			
		
		if (off_hand!=null)
		{
			blank.draw(x+153, y+75);
			off_hand.getPic().draw(x+153, y+75);
		}
			
		
		if (pants != null)
		{
			blank.draw(x+92, y+115);
			pants.getPic().draw(x+92, y+115);
		}

		
		if (boots != null)
		{
			blank.draw(x+92, y+169);
			boots.getPic().draw(x+92, y+169);
		}

		
		if (ring != null)
		{
			blank.draw(x+25, y+135);
			ring.getPic().draw(x+25, y+135);
		}

		
		if (misc != null)
		{
			blank.draw(x+153, y+135);
			misc.getPic().draw(x+153, y+135);
		}

	}
	
	// GETTERS AND SETTERS
	
	
	public int getWeapon_type() 
	{
		return weapon_type;
	}

	public void setWeapon_type(int weapon_type) 
	{
		this.weapon_type = weapon_type;
	}

	public Item getMisc() {
		return misc;
	}

	public void setMisc(Item misc) 
	{
		
		
		this.misc = misc;
		
	}
	
	public Container getContainer() 
	{
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public Item getHelmet() {
		return helmet;
	}

	public void setHelmet(Item helmet) {
		this.helmet = helmet;
	}

	public Item getNeck() {
		return neck;
	}

	public void setNeck(Item neck) {
		this.neck = neck;
	}

	public Item getWeapon() {
		return weapon;
	}

	public void setWeapon(Item weapon) {
		this.weapon = weapon;
	}

	public Item getChest() {
		return chest;
	}

	public void setChest(Item chest) {
		this.chest = chest;
	}

	public Item getOff_hand() {
		return off_hand;
	}

	public void setOff_hand(Item off_hand) {
		this.off_hand = off_hand;
	}

	public Item getPants() {
		return pants;
	}

	public void setPants(Item pants) {
		this.pants = pants;
	}

	public Item getBoots() {
		return boots;
	}

	public void setBoots(Item boots) {
		this.boots = boots;
	}

	public Item getRing() {
		return ring;
	}

	public void setRing(Item ring) {
		this.ring = ring;
	}

	// set all items to be on player's position (keep containers on player within open range at all times
	public void setAllPos(Position pos) 
	{
		if (getBoots() != null)
			getBoots().setPos(pos);
		if (getChest() != null)
			getChest().setPos(pos);
		if (getContainer() != null)
			getContainer().setPos(pos);
		if (getHelmet() != null)
			getHelmet().setPos(pos);
		if (getMisc() != null)
			getMisc().setPos(pos);
		if (getNeck() != null)
			getNeck().setPos(pos);
		if (getOff_hand() != null)
			getOff_hand().setPos(pos);
		if (getPants() != null)
			getPants().setPos(pos);
		if (getRing() != null)
			getRing().setPos(pos);
		if (getWeapon() != null)
			getWeapon().setPos(pos);
	}
}
