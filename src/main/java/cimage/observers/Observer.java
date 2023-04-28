/*
 * Observer.java
 *
 * Created on 8 juillet 2007, 11:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cimage.observers;

import cimage.*;

/**
 *
 * @author HP_Propri�taire
 */
public interface Observer 
{
    public void   setCImage(CImage ci);
    public CImage getCImage();
    public void   update();
}
