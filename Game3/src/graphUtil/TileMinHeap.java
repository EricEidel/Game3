package graphUtil;


public class TileMinHeap 
{
	private HeapElement[] heap;
	private int size;
	
	public TileMinHeap(int length)
	{
		heap = new HeapElement[length];
		size = 0;
	}
	
	public int getSize() {return size;}
	
	// parent = floor(i-1/2)
	// left child = 2i + 1
	// right child = 2i + 2
	
	// t = tile, dist = priority
	public void insert(TileNode t, double dist)
	{
		HeapElement he = new HeapElement(t, dist); // create new element
		heap[size] = he;	// add new element at last spot
		size++;
		
		int j = size - 1;
		while (j > 0 && heap[j].getKey()<heap[(int) Math.floor((j-1)/2)].getKey()) // restore heap to be min heap after insert
		{
			HeapElement temp = heap[j];
			heap[j] = heap[(int) Math.floor((j-1)/2)];
			heap[(int) Math.floor((j-1)/2)] = temp;
			j = (int) Math.floor((j-1)/2);
		}
	}
	
	// returns lowest priority station or null if queue is empty
	public HeapElement deleteMin()
	{
		HeapElement min = null;
		if (size > 0)
		{
			// get min and remove it from heap
			min = heap[0];
			heap[0] = heap[size-1];
			size--;
			
			int j = 0 ;
			while (j < size)
			{
				int left = 2*j +1;
				int right = 2*j+2;
				int smallest = j;
				if (left < size && heap[left].getKey() < heap[smallest].getKey())
					smallest = left;
				if (left < size && heap[right].getKey() < heap[smallest].getKey())
					smallest = right;
				if (smallest!=j)
				{
					// swap
					HeapElement temp = heap[j];
					heap[j] = heap[smallest];
					heap[smallest] = temp;
					j = smallest;
				}
				else
					j = size;
			}
		}
		return min;
	}
	
	// if the station isn't in the stack already, don't update
	public void updatePriority(TileNode t, double priority)
	{
		int index = -1;
		for (int i=0; i<size; i++)
		{
			if (heap[i].getValue().equals(t))
				index = i;
		}
		
		if (index != -1)
		{	// set the new priority
			heap[index].setKey(priority);
			// restore min property
			int j = index;
			while (j > 0 && heap[j].getKey()<heap[(int) Math.floor((j-1)/2)].getKey()) // restore heap to be min heap after insert
			{
				HeapElement temp = heap[j];
				heap[j] = heap[(int) Math.floor((j-1)/2)];
				heap[(int) Math.floor((j-1)/2)] = temp;
				j = (int) Math.floor((j-1)/2);
			}
		}
	}
	
	public String toString()
	{
		String retStr = "";
		for (int i=0; i<size; i++)
			retStr += "["+ heap[i].getValue().getPos().toString() +","+ heap[i].getKey() +"]";
		
		return retStr;
	}
}
