/*
 * FrameAppearance.java
 *
 * Created on November 12, 2007, 2:25 PM
 */

package sui.skin;

import sui.Button;
import sui.Frame;

/**
 *
 * @author davedes
 */
public interface FrameAppearance extends ComponentAppearance {
    //TODO: change to createCloseButton(), createTitleBar() etc
    public ComponentAppearance getCloseButtonAppearance(Button button);
    public ComponentAppearance getTitleBarAppearance(Frame.TitleBar titleBar);
    public ComponentAppearance getResizerAppearance(Frame.Resizer resizer);
}
