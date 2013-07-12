/*
 * SimpleContainerAppearance.java
 *
 * Created on November 7, 2007, 8:17 PM
 */

package sui.skin.simple;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

import sui.Component;
import sui.Skin;
import sui.Theme;
import sui.skin.SkinUtil;

/**
 *
 * @author davedes
 */
public class SimpleContainerAppearance extends SimpleComponentAppearance {
    
    public void render(GUIContext ctx, Graphics g, Component comp, Skin skin, Theme theme) {
        SkinUtil.renderComponentBase(g, comp);
    }
}
