/*
 * SimpleContainerAppearance.java
 *
 * Created on November 7, 2007, 8:17 PM
 */

package sui.skin.simple;

import sui.ScrollBar;
import sui.ScrollPane;
import sui.skin.ScrollPaneAppearance;


/**
 *
 * @author davedes
 */
public class SimpleScrollPaneAppearance extends SimpleContainerAppearance implements ScrollPaneAppearance {
    
    public ScrollBar createScrollBar(ScrollPane pane, int orientation) {
        ScrollBar bar = new ScrollBar(orientation);
        bar.setSize(ScrollPane.CORNER_SIZE, ScrollPane.CORNER_SIZE);
        return bar;
    }
}
