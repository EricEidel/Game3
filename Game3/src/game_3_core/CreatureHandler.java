package game_3_core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import sui.Display;
import sui.Label;
import tiles.Tile;
import GUI.Map;
import creatures.Creature;
import creatures.Player;



public class CreatureHandler 
{
	static final int WOLF = 1;
	static final int RAT = 2;
	static String seperator = ",";
	
	private ArrayList<Creature> creatures;
	private Player player;
	
	private ArrayList<LabelTracker> dead_labels;
	
	public CreatureHandler(String file_name, Player player)
	{
		
		this.player = player;
		dead_labels = new ArrayList<LabelTracker>();
		creatures = new ArrayList<Creature>();
		load_from_txt(file_name);
	}
	
	private void load_from_txt(String file_name) 
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
			int length = Integer.parseInt(line.split("=")[1]);
			
			while (!line.contains("height"))
			{
				line = br.readLine();
			}
			int height = Integer.parseInt(line.split("=")[1]);
			
			
			while (!line.contains("data="))
			{
				line = br.readLine();
			}
			line = br.readLine();
			while (!line.contains("data="))
			{
				line = br.readLine();
			} // Get to the collision section of the map file.
			
			line = br.readLine();
			int column = 0;
			Creature temp_creature;
			while (!line.equals(""))
			{
				String[] one_line = line.split(seperator);
				
				for (int i=0; i<one_line.length-1; i++)
				{
					temp_creature = (CreatureFactory.makeNewCreature(Integer.parseInt(one_line[i]), new Position(i, column), player.getLand()));
					if (temp_creature != null)
						creatures.add(temp_creature);
				}
				column++;
				line = br.readLine();
			}
			
			br.close();
			fr.close();
			
		}
		catch (Exception e)
		{
			System.out.println("Couldn't load Creature Handler.");
			e.printStackTrace();
		}
	}

	public void draw(Graphics g)
	{	
		for (Creature c: creatures)
		{
			if (player.getPos().isDrawableRange(c.getPos()))
			{				
				if (player.getMoving() != Creature.no_move && c.getMoving()!=Creature.no_move)
				{
					int x = c.getPos().getX() - player.getPos().getX();
					int y = c.getPos().getY() - player.getPos().getY();
					
					c.drawMoving(player, x, y, g);
				}
				else if (player.getMoving() != Creature.no_move)
				{
					int x = c.getPos().getX() - player.getPos().getX();
					int y = c.getPos().getY() - player.getPos().getY();
					
					c.drawMovingNoCreatureMov(player, x, y, g);
				}
				else if (player.getMoving() == Creature.no_move && c.getMoving()!= Creature.no_move)
				{
					int x = c.getPos().getX() - player.getPos().getX();
					int y = c.getPos().getY() - player.getPos().getY();
					
					c.drawMovingNoPlayerMov(x, y, g);
				}
				else
				{
					int x = c.getPos().getX() - player.getPos().getX();
					int y = c.getPos().getY() - player.getPos().getY();
					
					c.draw(Player.getPlayerXCenter() + x, Player.getPlayerYCenter() + y, g);
				}
			}
		}
	}

	public void checkDead(Map land, ItemHandler ih, Display display)
	{
		ArrayList<Creature> dead_c = new ArrayList<Creature>();
		for (Creature c: creatures)
		{
			//System.out.println(c.getPos() + " " + c.isDead());
			if (c.isDead())
			{
				player.setTarget(null);
				land.kill(c, ih);
				dead_c.add(c);
			}
		}
		
		for (Creature c: dead_c)
		{
			creatures.remove(c);
			dead_labels.add(new LabelTracker(c, c.getDamageTaken(), c.getDeltaDamage()));
		}
	}

	public void checkActive() 
	{
		for (Creature c: creatures)
		{		
			if (player.getPos().isActiveRange(c.getPos()))
			{
				c.setActive(true);
			}
			else
			{
				c.setActive(false);
			}
		}
	}
	
	public void add_creature(Creature creature)
	{
		creatures.add(creature);
	}
	
	public void move(Map land, int delta)
	{
		for (Creature c: creatures)
		{
			int move_time = c.getSpeed();
			c.setInputDelta(c.getInputDelta()+delta);

			if (c.isActive() && (c.getInputDelta() > move_time))
			{
			        c.move(land);
		    		c.setInputDelta(0);
			}
		}
	}

	public void updateBattleText(int delta, Display display) 
	{		
		for (Creature c: creatures)
		{
			if (c.isActive())
			{
				int deltaDamage = c.getDeltaDamage();
				deltaDamage -= delta;
				c.setDeltaDamage(deltaDamage);

				if (deltaDamage > 0)
				{
					drawDamageBT(c , delta, display);
				}
				else
				{
					c.getDamageTaken().setText("");
				}
			
			}
		}
		
		ArrayList<LabelTracker> to_remove = new ArrayList<LabelTracker>();
		
		for (LabelTracker lt: dead_labels)
		{
			lt.delta -= delta;
			if (lt.delta <= 0)
			{
				lt.label.setText("");
				to_remove.add(lt);
			}
			else
			{
				drawDamageBT(lt.c , delta, display);
			}
		}
		
		for (LabelTracker lt: to_remove)
		{
			dead_labels.remove(lt);
			display.remove(lt.label);
		}
	}


	private void drawDamageBT(Creature c, int delta, Display display) 
	{		
		Label DamageTaken = c.getDamageTaken();
		c.animateBT();
		DamageTaken.setVisible(true);
		DamageTaken.pack();
		display.add(DamageTaken);
	}
}
