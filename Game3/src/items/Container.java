package items;

import game_3_core.Position;

import java.util.ArrayList;

import org.newdawn.slick.Image;

import GUI.HUD;
import GUI.Map;
import creatures.Player;

public class Container extends UniqueItem
{
	public static final int X_OFFSET = 2*Map.X_OFFSET + 15*Map.SIZE_OF_TILE+30;
	public static final int Y_OFFSET = HUD.HIEGHT + 70;
	
	public static final int CONT_HEIGHT = 120;
	Position pos;
	

	private boolean open;
	private int capacity;
	private ArrayList<Item> items;

	private Image cont_pic;
	
	private long ID;
	static int lastID = 0;
	
	private Container parent;
	private ArrayList<Long> chidlren_id = new ArrayList<Long>();

	public Container(Position pos, int capacity) 
	{
		super(pos);
		setUsable(true);
		setType(Item.CONTAINER);
		this.capacity = capacity;
		items = new ArrayList<Item>();
		setID(lastID);
		Container.lastID++;
		parent = null;
		setPos(pos);
		
		try
		{
			cont_pic = (new Image("Pics/8_slot_cont.gif"));
		}
		catch (Exception e)
		{
			System.out.println("Couldn't load picture - container!");
			e.printStackTrace();
		}
	}

	/* Add an item to the container if it isn't at full capacity.
	 * Returns false if it was full, item not added.
	 */
	public boolean add_to_container(Item item)
	{
		// make sure you can fit the item in
		if (items.size()+1<=this.capacity)
		{
			// if it's a container, deal with ID stuff
			if (item.getType() == Item.CONTAINER)
			{	// cast
				Container item_cont = (Container) item;
	
				
				// if the id of the "to" container is the same as the id of the container moved, don't move.
				if (getID() == (item_cont.getID()))
				{
					System.out.println("You can't put a container inside itself...");
					return false;
				} 
				// if the item moved  ID is in  "to" container in it's parent list
				// this means that the container we are moving is a parent (or parent's parent etc) of the "to" container.
				else if (item_cont.chidlren_id.contains(getID())) 
				{
					System.out.println("You can't put a container into something that is in that same container... (mind blown)");
					return false;
				}

				item_cont.setParent(this);
			}
			
			if (item.getType() == Item.CONTAINER)
				((Container)item).setPos(pos);
			else
				item.setPos(pos);
			
			items.add(item);
			return true;
		}
		return false;
	}

	public void remove_from_container(int index)
	{		
		Item item = items.remove(index);
		if (item.getType() == Item.CONTAINER)
		{
			Container removed = (Container) item;
			chidlren_id.remove(removed.getID());
			chidlren_id.removeAll(removed.chidlren_id);
			if (removed!=null)
				removed.setParent(null);
		}
	}
	
	
	@Override
	public void use(Player player) 
	{
		//System.out.println("Container used! Open set to " + !isOpen());
		if (isOpen())
			close();
		else
		{
			open();
			player.contH.add(this);
		}

	}
	


	public void drawCont(int i) 
	{
		int x = X_OFFSET;
		int y = Y_OFFSET + i * CONT_HEIGHT;
		
		try
		{
		cont_pic.draw(x, y);
		}
		
		catch (Exception e)
		{
			System.out.println("Couldn't load empty container pic!");
		}
		
		x += 5;
		y += 5;
		int count = 0;
		
		for (Item item: items)
		{
			count++;
			item.getPic().draw(x,y);
			x+=55;
			if (count==4)
			{
				y+=55;
				x = X_OFFSET + 5;
				count = 0;
			}
		}
	}
	
	// Container utilities
	public int size()
	{
		return items.size();
	}
	
	public boolean isFull()
	{
		return items.size() == getCapacity();
			 
	}
	
	public int spaceLeft()
	{
		return getCapacity() - items.size();
	}
	
	// GETTERS AND SETTERS
	public int getCapacity() 
	{
		return capacity;
	}

	public void setCapacity(int capacity) 
	{
		this.capacity = capacity;
	}
	
	public ArrayList<Item> getItems() 
	{
		return items;
	}

	public void setItems(ArrayList<Item> items) 
	{
		this.items = items;
	}

	public boolean isOpen() 
	{
		return open;
	}
	
	public void open()
	{
		this.open = true;
	}

	public void close()
	{
		this.open = false;
	}

	public Item get_item(int item_slot) 
	{
		if (item_slot >= items.size())
				return null;
		
		return items.get(item_slot);	
	}

	
	public long getID() 
	{
		return ID;
	}

	public void setID(long iD) 
	{
		ID = iD;
	}
	
	public boolean equals(Container cont)
	{
		return getID() == cont.getID();
	}
	
	
	public Container getParent() {
		return parent;
	}

	// make the parent of this container be parent. Add this container to the parent container children list.
	public void setParent(Container parent) 
	{
		this.parent = parent;
		if (parent!=null)
		{
			parent.chidlren_id.add(this.getID());
			parent.chidlren_id.addAll(this.chidlren_id);
		}
	}
	
	@Override
	public Position getPos() 
	{
		return this.pos;
	}

	@Override
	public void setPos(Position pos) 
	{
		this.pos = pos;
		if (!items.isEmpty())
		{
			for (Item i: items)
			{
				if (i.getType() == Item.CONTAINER)
					((Container)i).setPos(pos);
				else
					i.setPos(pos);
			}
		}
	}
}
