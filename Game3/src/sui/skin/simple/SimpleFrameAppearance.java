/*
 * SimpleFrameAppearance.java
 *
 * Created on November 7, 2007, 8:58 PM
 */

package sui.skin.simple;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.opengl.SlickCallable;

import sui.Button;
import sui.Component;
import sui.Frame;
import sui.Label;
import sui.Skin;
import sui.Sui;
import sui.Theme;
import sui.skin.ComponentAppearance;
import sui.skin.FrameAppearance;
import sui.skin.SkinUtil;

/**
 *
 * @author davedes
 */
public class SimpleFrameAppearance extends SimpleContainerAppearance implements FrameAppearance {
        
    private static GradientFill grad = new GradientFill(0f,0f,Color.white,0f,0f,Color.white);
    
    private ComponentAppearance resizerAppearance = new ResizerAppearance();
    
    public void render(GUIContext ctx, Graphics g, Component comp, Skin skin, Theme theme) {
        Color old = g.getColor();
        
        //borders
        if (comp.isBorderRendered()) {
            Frame win = (Frame)comp;
            Color light = theme.getSecondaryBorder1();
            Color dark = theme.getSecondaryBorder1();

            Rectangle rect = win.getAbsoluteBounds();
            //TODO: this is a hack, fix it
            //HACK: fix window title bar (removed) hack
            if (!win.getTitleBar().isVisible() || !win.containsChild(win.getTitleBar())) {
                float h = win.getTitleBar().getHeight();
                rect.setY(rect.getY()+h-1);
                rect.setHeight(rect.getHeight()-h+1);
            }

            float mid = rect.getWidth()/2f;

            grad.setStartColor(light);
            grad.setEndColor(dark);
            grad.setStart(-mid, 0);
            grad.setEnd(mid, 0);
            g.draw(rect, grad);
        }
    }

    public ComponentAppearance getCloseButtonAppearance(Button closeButton) {
        return new CloseButtonAppearance(closeButton);
    }

    public ComponentAppearance getTitleBarAppearance(Frame.TitleBar titleBar) {
        return new TitleBarAppearance(titleBar);
    }

    public ComponentAppearance getResizerAppearance(Frame.Resizer resizer) {
        return resizerAppearance;
    }
    
    protected class CloseButtonAppearance extends SimpleButtonAppearance {
        
        public CloseButtonAppearance(Button button) {
            super(button);
        }
        
        protected RoundedRectangle createRoundedBounds() {
            return new RoundedRectangle(0f,0f,0f,0f,3f,50);
        }

        public void install(Component comp, Skin skin, Theme theme) {
            super.install(comp, skin, theme);
            Button btn = (Button)comp;
            
            if (skin instanceof SimpleSkin) {
                Image img = ((SimpleSkin)skin).getCloseButtonImage();
                if (SkinUtil.installImage(btn, img)) {
                    btn.pack();
                }
            }
        }
    }
    
    protected class ResizerAppearance extends SimpleLabelAppearance {
            
        public void install(Component comp, Skin skin, Theme theme) {
            super.install(comp, skin, theme);
            if (skin instanceof SimpleSkin) {
                comp.addMouseListener(((SimpleSkin)skin).getResizeCursorListener());
            }
        }

        public void uninstall(Component comp, Skin skin, Theme theme) {
            super.uninstall(comp, skin, theme);
            if (skin instanceof SimpleSkin) {
                comp.removeMouseListener(((SimpleSkin)skin).getResizeCursorListener());
            }
        }
        
        public void render(GUIContext ctx, Graphics g, Component comp, Skin skin, Theme theme) {
            Frame win = ((Frame.Resizer)comp).getWindow();
            if (!win.isResizable())
                return;

            if (((Label)comp).getImage()==null) {
                SlickCallable.enterSafeBlock();
                Color t = Sui.getTheme().getSecondaryBorder1();

                //bind texture & color before entering gl
                t.bind();

                float x = comp.getAbsoluteX()-2 , y = comp.getAbsoluteY()-2;
                float w = comp.getWidth(), h = comp.getHeight();

                //begin drawing the triangle
                GL11.glBegin(GL11.GL_TRIANGLES);
                    GL11.glVertex3f(x+w, y, 0);
                    GL11.glVertex3f(x+w, y+h, 0);
                    GL11.glVertex3f(x, y+h, 0);
                GL11.glEnd();
                SlickCallable.leaveSafeBlock();
            } else {
                super.render(ctx, g, comp, skin, theme);
            }
        }
    }
    
    protected class TitleBarAppearance extends SimpleLabelAppearance {

        private GradientFill grad = new GradientFill(0f,0f,Color.white,0f,0f,Color.white);

        private Frame.TitleBar bar;
        
        public TitleBarAppearance(Frame.TitleBar bar) {
            this.bar = bar;
        }
        
        public void render(GUIContext ctx, Graphics g, Component comp, Skin skin, Theme theme) {
            Rectangle rect = comp.getAbsoluteBounds();

            Color old = g.getColor();
            Frame.TitleBar t = (Frame.TitleBar)comp;
            
            float x1=t.getAbsoluteX(), y1=t.getAbsoluteY();
            float width=t.getWidth(), height=t.getHeight();

            //TODO: fix rectangle + 1

            float mid = width/2.0f;

            Color start, end;

            boolean active = ((Frame)t.getParent()).isActive();

            if (active) {
                start = theme.getActiveTitleBar1();
                end = theme.getActiveTitleBar2();
            } else {
                start = theme.getTitleBar1();
                end = theme.getTitleBar2();
            }

            grad.setStartColor(start);
            grad.setEndColor(end);
            grad.setStart(-mid, 0);
            grad.setEnd(mid, 0);
            g.fill(rect, grad);

            if (t.isBorderRendered()) {
                //borders
                Color light = theme.getSecondaryBorder1();
                Color dark = theme.getSecondaryBorder1();

                grad.setStartColor(light);
                grad.setEndColor(dark);
                grad.setStart(-mid, 0);
                grad.setEnd(mid, 0);
                g.draw(rect, grad);
            }
            
            //g.setColor(old);
            
            SkinUtil.renderLabelBase(g, t);
        }
    }
}
