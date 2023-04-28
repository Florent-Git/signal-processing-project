/*
 * SourisClicEvent.java
 *
 * Created on 10 juillet 2007, 16:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cimage.observers.events;

import java.util.*;

/**
 *
 * @author HP_Propri�taire
 */
public class UnClicEvent extends EventObject 
{
    private int x,y;
    
    /** Creates a new instance of SourisClicEvent */
    public UnClicEvent(Object source,int x,int y) 
    {
        super(source);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
