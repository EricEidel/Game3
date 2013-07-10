package Tests;

import game_3_core.Position;
import graphUtil.TileMinHeap;
import graphUtil.TileNode;

public class test
{
	 public static void main(String[] args)
	 {
		 TileNode t1 = new TileNode(new Position(1,1));
		 TileNode t2 = new TileNode(new Position(2,2));
		 TileNode t3 = new TileNode(new Position(3,3));
		 TileNode t4 = new TileNode(new Position(4,4));
		 
		 TileMinHeap tmh = new TileMinHeap(15);
		 
		 System.out.println(tmh.toString());
		 tmh.insert(t1, 1);
		 System.out.println(tmh.toString());
		 tmh.insert(t2, 1.2);
		 System.out.println(tmh.toString());
		 tmh.insert(t3, 1);
		 System.out.println(tmh.toString());
		 tmh.insert(t4, 1.2);
		 System.out.println(tmh.toString());
		 tmh.updatePriority(t2, 0.5);
		 System.out.println(tmh.toString());
		 
		 TileNode t5 = tmh.deleteMin().getValue();
		 System.out.println(tmh.toString());
		 System.out.println(t5.getPos().toString());
		 t5 = tmh.deleteMin().getValue();
		 System.out.println(tmh.toString());
		 System.out.println(t5.getPos().toString());
	 }
}