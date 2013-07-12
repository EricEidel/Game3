/*
 * TextComponentAppearance.java
 *
 * Created on December 29, 2007, 9:52 PM
 */

package sui.skin;

import sui.Point;
import sui.TextComponent;

/**
 *
 * @author davedes
 */
public interface TextComponentAppearance extends ComponentAppearance {
    
    public int viewToModel(TextComponent comp, float x, float y);
    public Point modelToView(TextComponent comp, int pos);
}
