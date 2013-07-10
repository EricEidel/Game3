package graphUtil;

public class HeapElement 
{
	private TileNode value;
	private double key;
	
	public HeapElement(TileNode t, double key)
	{
		setValue(t);
		this.setKey(key);
	}

	public double getKey() 
	{
		return key;
	}

	public void setKey(double key) 
	{
		this.key = key;
	}

	public TileNode getValue() 
	{
		return value;
	}

	public void setValue(TileNode value) 
	{
		this.value = value;
	}
		
}
