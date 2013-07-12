/*
 * SimpleLabelAppearance.java
 *
 * Created on November 7, 2007, 8:25 PM
 */

package sui.skin.simple;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

import sui.Component;
import sui.Label;
import sui.Skin;
import sui.Theme;
import sui.skin.SkinUtil;

/**
 *
 * @author davedes
 */
public class SimpleLabelAppearance extends SimpleContainerAppearance {
    
    public void render(GUIContext ctx, Graphics g, Component comp, Skin skin, Theme theme) {
        super.render(ctx, g, comp, skin, theme);
        SkinUtil.renderLabelBase(g, (Label)comp);
    }
}
