/*
 * AbstractComponentAppearance.java
 *
 * Created on November 8, 2007, 4:50 PM
 */

package sui.skin;

import sui.Component;
import sui.Skin;
import sui.Theme;

/**
 *
 * @author davedes
 */
public abstract class AbstractComponentAppearance implements ComponentAppearance {
    
    public boolean contains(Component comp, float x, float y) {
        return comp.inside(x, y);
    }
    
    public void install(Component comp, Skin skin, Theme theme) {    
    }
    
    public void uninstall(Component comp, Skin skin, Theme theme) {   
    }
}
