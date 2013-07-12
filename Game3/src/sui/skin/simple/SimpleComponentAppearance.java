/*
 * SimpleComponentAppearance.java
 *
 * Created on November 7, 2007, 8:18 PM
 */

package sui.skin.simple;

import org.newdawn.slick.gui.GUIContext;

import sui.Component;
import sui.Skin;
import sui.Theme;
import sui.skin.AbstractComponentAppearance;
import sui.skin.SkinUtil;

/**
 * A basic appearance that plugs into a component.
 * @author davedes
 */
public abstract class SimpleComponentAppearance extends AbstractComponentAppearance {
        
    public void update(GUIContext ctx, int delta, Component comp, Skin skin, Theme theme) {
        //do nothing
    }
    
    public void install(Component comp, Skin skin, Theme theme) {
        SkinUtil.installFont(comp, ((SimpleSkin)skin).getFont());        
        SkinUtil.installColors(comp, theme.getBackground(), theme.getForeground());
    }
    
    public void uninstall(Component comp, Skin skin, Theme theme) {   
    }
}
