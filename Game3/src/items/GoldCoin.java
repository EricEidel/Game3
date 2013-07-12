package items;

import game_3_core.Position;

import org.newdawn.slick.Image;

import creatures.Player;

public class GoldCoin extends Coin
{
	private int amount;
	private int worth;

	static private Image one_coin;
	static private Image two_coins;
	static private Image three_coins;
	static private Image four_coins;
	static private Image five_to_ten_coins;
	static private Image eleven_to_twenty_coins;
	static private Image twenty_up_coins;
	static private Image hundred_coins;
	
	{
		try
		{
			one_coin = new Image("Pics/Coins/one_coin.png");
			two_coins = new Image("Pics/Coins/two_coins.png");
			three_coins = new Image("Pics/Coins/three_coins.png");
			four_coins = new Image("Pics/Coins/four_coins.png");
			five_to_ten_coins = new Image("Pics/Coins/five_to_ten_coins.png");
			eleven_to_twenty_coins = new Image("Pics/Coins/eleven_to_twenty_coins.png");
			twenty_up_coins = new Image("Pics/Coins/twenty_up_coins.png");
			hundred_coins = new Image("Pics/Coins/hundred_coins.png");
		}
		catch (Exception e)
		{
			System.out.println("Could not init gold coin picture!");
		}
	}
	public GoldCoin(Position pos) 
	{
		super(pos);
		this.amount = 1;
		this.worth = 1;
		setPicture();
	}



	public GoldCoin(Position pos, int amount) 
	{
		super(pos);
		this.amount = amount;
		this.worth = 1;
		setPicture();
	}
	
	public GoldCoin(GoldCoin target, GoldCoin isMoved)
	{
		super(target.getPos());
		this.amount = target.getAmount() + isMoved.getAmount();
		setPicture();
	}
	
	private void setPicture() 
	{
		if (amount == 1)
			setPic(one_coin.copy());
		else if (amount == 2)
			setPic(two_coins.copy());
		else if (amount == 3)
			setPic(three_coins.copy());
		else if (amount == 4)	
			setPic(four_coins.copy());
		else if (amount > 5 && amount < 10)
			setPic(five_to_ten_coins.copy());
		else if (amount > 10 && amount < 21)
			setPic(eleven_to_twenty_coins.copy());
		else if (amount > 20 && amount < 100)
			setPic(twenty_up_coins.copy());
		else
			setPic(hundred_coins.copy());
	}
	
	// ================== Getters and setters from this point on =================
	@Override
	public int getWorth() 
	{
		return this.worth;
	}

	@Override
	public void setWorth(int worth) 
	{
		this.worth = worth;
	}

	@Override
	public void setAmount(int amount) 
	{
		this.amount = amount;
	}

	@Override
	public int getAmount() 
	{
		return this.amount;
	}

	@Override
	public void use(Player player) 
	{
		// TODO break coins into sub groups : 5 coins by click should change into whatever player wants.
	}

}
