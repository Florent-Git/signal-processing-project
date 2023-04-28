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
public class DeuxClicsEvent extends EventObject 
{
    private int x1,x2,y1,y2;
    
    /** Creates a new instance of SourisClicEvent */
    public DeuxClicsEvent(Object source,int x1,int y1,int x2,int y2) 
    {
        super(source);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
    
    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
    }
    
}
