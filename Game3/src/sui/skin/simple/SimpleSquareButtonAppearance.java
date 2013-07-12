/*
 * SimpleSquareButtonAppearance.java
 *
 * Created on November 8, 2007, 6:00 PM
 */

package sui.skin.simple;

import org.newdawn.slick.geom.RoundedRectangle;

import sui.Button;

/**
 *
 * @author davedes
 */
public class SimpleSquareButtonAppearance extends SimpleButtonAppearance {
    
    public SimpleSquareButtonAppearance(Button button) {
        super(button);
    }
    
    protected RoundedRectangle createRoundedBounds() {
        return null; //stores null
    }    
}
