/*
 * HoverButtonTest.java
 *
 * Created on January 2, 2008, 2:17 PM
 */

package sui.test;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 *
 * @author davedes
 */
public class HoverButtonTest extends BasicGame {
    
    public static void main(String[] args) throws Exception {
        AppGameContainer app = new AppGameContainer(new HoverButtonTest());
        app.setDisplayMode(800,600,false);
        app.start();
    }
    
    /** Creates a new instance of HoverButtonTest */
    public HoverButtonTest() {
        super("HoverButtonTest");
    }
    
    public void init(GameContainer container) throws SlickException {
        
    }
    
    public void update(GameContainer container, int delta) throws SlickException {
        
        if (container.getInput().isKeyPressed(Input.KEY_ESCAPE))
            container.exit();
    }
    
    public void render(GameContainer container, Graphics g) throws SlickException {
        
    }
}
