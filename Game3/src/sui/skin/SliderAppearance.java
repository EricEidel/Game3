/*
 * SliderAppearance.java
 *
 * Created on November 15, 2007, 5:54 PM
 */

package sui.skin;

import sui.Button;
import sui.ScrollConstants;
import sui.Slider;

/**
 *
 * @author Matt
 */
public interface SliderAppearance extends ComponentAppearance, ScrollConstants {
    
    /**
     * This is the knob or thumb button whic appears on the slider.
     */
    public Button createThumbButton(Slider slider); 
}
