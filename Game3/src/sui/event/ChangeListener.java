/*
 * ChangeListener.java
 *
 * Created on November 25, 2007, 6:28 PM
 */
package sui.event;

/**
 *
 * @author davedes
 */
public interface ChangeListener extends Listener {
    
    public void stateChanged(ChangeEvent e);
}
