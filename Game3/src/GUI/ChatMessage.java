package GUI;

import java.util.ArrayList;

public class ChatMessage 
{
	   ArrayList<String> textToRender; //Text spliced and ready.
	   String name;

	   public ChatMessage(String text, String name)
	   {
		   this.name = name;
		   spliceText(text);
	   }

	private void spliceText(String text) 
	{
		int mark = 0;
		
		textToRender = new ArrayList<String>();
		int size = text.length()/50;
		for (int x = 0; x < text.length(); x++)
		{
			if (x > mark+50)
			{
				textToRender.add(text.substring(mark, x));
				mark = x;
			}
			else
			{
				if (mark+50 > text.length())
					{
					textToRender.add(text.substring(mark, text.length()));
					}
			}
		}
	}
}
